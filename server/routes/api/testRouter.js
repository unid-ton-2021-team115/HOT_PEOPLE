const router = require('express').Router();
const dbConn= require(`${process.cwd()}/dbConnection`);

router.get('/', async (req, res) => {
    try {
        let sql = "select * from user"; 
        let [results] = await dbConn.query(sql, []);

        return res.status(200).json({
            code: 1,
            data: results
        });

    } catch(err) {
        console.log(err);
        return res.status(400).json({err});
    }
});

module.exports = router;