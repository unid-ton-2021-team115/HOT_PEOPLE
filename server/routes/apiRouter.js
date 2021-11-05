const router = require('express').Router();
const testRouter = require('./api/testRouter');

router.use('/test', testRouter);

module.exports = router;