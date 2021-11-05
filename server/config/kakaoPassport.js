const passport = require('passport') 
const KakaoStrategy = require('passport-kakao').Strategy 
const dbConn= require(`${process.cwd()}/dbConnection`);

module.exports = () => {
    passport.use('kakao', new KakaoStrategy({ 
        clientID: process.env.KAKAO_REST_API_KEY, 
        callbackURL: '/auth/kakao/callback',
    }, async (accessToken, refreshToken, profile, done) => { 
        console.log(profile._json);

        // 기존에 가입된 유저인지 확인
        let sql = 'select * from user_kakao where id = ?';
        let [user_db] = await dbConn.query(sql, [profile._json.id]);

        if(!user_db.length) { // 가입이 안 되어있으면 db에 추가
            sql = 'insert into user_kakao value (?, ?, ?, ?, ?, ?, ?)';
            await dbConn.query(sql, [profile._json.id, profile._json.properties.nickname, profile._json.properties.profile_image, 
                profile._json.kakao_account.gender, null, profile._json.kakao_account.age_range, 0]);

            sql = 'select * from user_kakao where id = ?';
            [user_db] = await dbConn.query(sql, [profile._json.id]);
        }

        let user = {...user_db[0], accessToken};
        return done(null, user);
    }));

    passport.serializeUser(function (user, done) {
        done(null, user);
    });
    passport.deserializeUser(function (obj, done) {
        done(null, obj);
    });
}