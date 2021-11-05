const router = require('express').Router();

router.use('/google_map', require('./api/googleMapRouter'));
router.use('/hot_place', require('./api/hotPlaceRouter'));
router.use('/place', require('./api/placeRouter'));

module.exports = router;