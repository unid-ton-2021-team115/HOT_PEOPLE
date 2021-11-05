const router = require('express').Router();
const passport = require('passport');
const dbConn = require(`${process.cwd()}/dbConnection`);

router.get('/', async (req, res) => {
    console.log(req.user);
    res.render(`index`);
});

module.exports = router;