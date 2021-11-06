const router = require('express').Router();

router.use('/google_map', require('./api/googleMapRouter'));
router.use('/hot_place', require('./api/hotPlaceRouter'));
router.use('/place', require('./api/placeRouter'));
router.use('/matching', require('./api/matchingRouter'));
router.use('/my_matching', require('./api/myMatchingRouter'));
router.use('/matching_join', require('./api/matchingJoinRouter'));

module.exports = router;