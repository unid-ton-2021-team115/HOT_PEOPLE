const parser = require("csv-parse/lib/sync");
const fs = require("fs");
const axios = require('axios');
const dbConn = require(`${process.cwd()}/dbConnection`);
const {JSDOM} = require('jsdom');
const {window}=new JSDOM('');
const $ = require('jquery')(window);

// 문화 식당 주류 카페 쇼핑 교육 뷰티 여가

const type2kor = {
    amusement_park: '문화',
    aquarium: '문화',
    art_gallery: '문화',
    bakery: '카페',
    bar: '주류',
    book_store: '쇼핑',
    cafe: '카페',
    campground: '여가',
    church: '교육',
    clothing_store: '쇼핑',
    gym: '여가',
    hair_care: '뷰티',
    library: '교육',
    movie_theater: '문화',
    museum: '문화',
    night_club: '주류',
    park: '여가',
    restaurant: '식당',
    school: '교육',
    shoe_store: '쇼핑',
    shopping_mall: '쇼핑',
    spa: '여가',
    stadium: '여가',
    store: '쇼핑',
    supermarket: '쇼핑',
    tourist_attraction: '문화',
    university: '교육',
    zoo: '여가',
};

module.exports = async () => {
    const rtf = fs.readFileSync(`${process.cwd()}/../web-scraping/instagram_web_scraping.rtf`);
    const rawPlaces = JSON.parse(rtf.toString("utf-8"));
    console.log(rawPlaces.length);
    let sql = 'update place set is_hot = 0';
    await dbConn.query(sql, []);

    for(let rawPlace of rawPlaces) {
        let place_id = await axios.get('https://maps.googleapis.com/maps/api/place/findplacefromtext/json', {
            params : {
                input : rawPlace.place,
                inputtype : 'textquery',
                key : process.env.GOOGLE_MAP_API_KEY
            }
        });
        if(place_id.data.status != "OK") continue;
        place_id = place_id.data.candidates[0].place_id;

        // sql = 'insert ignore into place_img value (null, ?, ?)';
        // await dbConn.query(sql, [place_id, rawPlace.image]);

        sql = 'select * from place where id = ?';
        let [check] = await dbConn.query(sql, [place_id]);
        if(check.length){
            sql = 'update place set is_hot = 1 and recent_post_cnt = ? where id = ?';
            await dbConn.query(sql, [rawPlace.count, place_id]);
            continue;
        }

        let place = await axios.get('https://maps.googleapis.com/maps/api/place/details/json', {
            params : {
                fields : 'formatted_address,name,geometry,types,formatted_phone_number',
                place_id,
                language: 'ko',
                key : process.env.GOOGLE_MAP_API_KEY
            }
        });
        place = place.data.result;

        let typeNotMatched = true;
        for(type of place.types)
            if(type2kor[type]) typeNotMatched = false;
        if(typeNotMatched) continue;

        sql = 'insert into place value (?, ?, ?, ?, ?, ?, 1, ?)';
        await dbConn.query(sql, [place_id, place.name, place.formatted_address, place.geometry.location.lat, place.geometry.location.lng, place.formatted_phone_number, rawPlace.count]);

        for(type of place.types) {
            if(!type2kor[type]) continue;
            sql = 'insert ignore into place_types value (null, ?, ?)';
            await dbConn.query(sql, [place_id, type2kor[type]]);
        }
    }
}