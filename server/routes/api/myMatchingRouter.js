const router = require('express').Router();
const dbConn= require(`${process.cwd()}/dbConnection`);
const moment = require('moment');

router.get('/:status', async (req, res) => {
    try {
        if(!req.user) 
            return res.status(401).json({
                status: "NOT LOGIN"
            });

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