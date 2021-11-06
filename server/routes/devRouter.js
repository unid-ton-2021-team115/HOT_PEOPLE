const router = require('express').Router();
const passport = require('passport');
const dbConn = require(`${process.cwd()}/dbConnection`);
//const hotPlaceUpdate = require(`${process.cwd()}/middleware/hotPlaceUpdate`);

router.get('/', async (req, res) => {
    console.log(req.user);
    res.render(`index`);
});

router.get('/update_hot_place', async (req, res) => {
    //hotPlaceUpdate();
    res.redirect('/dev');
});

module.exports = router;