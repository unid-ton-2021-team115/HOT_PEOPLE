const router = require('express').Router();
const dbConn= require(`${process.cwd()}/dbConnection`);
const kakaoAuth = require(`${process.cwd()}/controllers/kakaoAuth`);

router.get('/:status', kakaoAuth.checkLogin, async (req, res) => {
    try {
        let sql = 'select * from matching where host_id = ? and status = ?'; 
        let [results] = await dbConn.query(sql, [req.user.id, req.params.status]);

        return res.status(200).json({
            status: "OK",
            data: results 
        });

    } catch(err) {
        return res.status(400).json({
            status: "NOT FOUND"
        });
    }
});

module.exports = router;