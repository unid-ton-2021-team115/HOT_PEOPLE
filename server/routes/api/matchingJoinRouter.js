const router = require('express').Router();
const dbConn= require(`${process.cwd()}/dbConnection`);
const kakaoAuth = require(`${process.cwd()}/controllers/kakaoAuth`);
const moment = require('moment');

router.post('/', kakaoAuth.checkLogin, async (req, res) => {
    try {
        let sql = 'insert into matching_join value (null, ?, ?, ?)'; 
        let [result] = await dbConn.query(sql, [req.body.matching_id, req.user.id, 'waiting']);

        return res.status(201).json({
            status: "OK",
            insertId: result.insertId,
            data: req.body,
        });

    } catch(err) {
        console.error(err);
        return res.status(400).json({
            status: "INSERTION ERROR"
        });
    }
});

router.get('/search', async (req, res) => {
    try {
        let sql = 'select * from matching_id where'; 
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
        let sql = 'select * from matching_id where id = ?'; 
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