const router = require('express').Router();
const axios = require('axios');
const dbConn= require(`${process.cwd()}/dbConnection`);

router.get('/place', async (req, res) => {
    try {
        let place_id = await axios.get('https://maps.googleapis.com/maps/api/place/findplacefromtext/json', {
            params : {
                input : req.query.name,
                inputtype : 'textquery',
                key : process.env.GOOGLE_MAP_API_KEY
            }
        });
        place_id = place_id.data.candidates[0].place_id;

        return res.status(200).json({
            status: "OK",
            data: { place_id }
        });

    } catch(err) {
        return res.status(400).json({
            status: "NOT FOUND"
        });
    }
});

router.get('/place/:id', async (req, res) => {
    try {
        let place = await axios.get('https://maps.googleapis.com/maps/api/place/details/json', {
            params : {
                fields : 'formatted_address,name,geometry,types,formatted_phone_number',
                place_id : req.params.id,
                language: 'ko',
                key : process.env.GOOGLE_MAP_API_KEY
            }
        });
        place = place.data.result;

        return res.status(200).json({
            status: "OK",
            data: { id : req.params.id, ...place }
        });

    } catch(err) {
        return res.status(400).json({
            status: "NOT FOUND"
        });
    }
});

module.exports = router;