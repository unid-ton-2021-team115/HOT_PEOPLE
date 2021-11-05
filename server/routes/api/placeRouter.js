const router = require('express').Router();
const dbConn= require(`${process.cwd()}/dbConnection`);

router.get('/search', async (req, res) => {
    try {
        let sql = 'select * from place where'; 
        for(key in req.query) sql += ` ${key} = ? AND`;
        sql = sql.substr(0, sql.length - 3);
        let [results] = await dbConn.query(sql, Object.values(req.query));

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

router.get('/:id', async (req, res) => {
    try {
        let sql = 'select * from place where id = ?'; 
        let [results] = await dbConn.query(sql, [req.params.id]);

        return res.status(200).json({
            status: "OK",
            data: results[0]
        });

    } catch(err) {
        return res.status(400).json({
            status: "NOT FOUND"
        });
    }
});

module.exports = router;