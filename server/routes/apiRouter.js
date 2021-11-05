const router = require('express').Router();

router.use('/google_map', require('./api/googleMapRouter'));

module.exports = router;