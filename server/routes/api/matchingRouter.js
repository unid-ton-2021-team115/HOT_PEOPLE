const router = require('express').Router();
const dbConn= require(`${process.cwd()}/dbConnection`);
const passport = require('passport');
const moment = require('moment');

router.post('/', passport.authenticate('kakao-token'), async (req, res) => {
    try {
        let sql = "insert into matching value (null, ?, ?, ?, ?, ?, ?)"; 
        let [result] = await dbConn.query(sql, [req.user.id, req.body.place_id, moment().format('YYYY-MM-DDTHH:mm:ss'), req.body.matching_datetime, 
            req.body.description, 'wait']);

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
        let sql = 'select * from matching where'; 
        for(key in req.query) sql += ` ${key} = ? AND`;
        sql = sql.substr(0, sql.length - 3);
        let [results] = await dbConn.query(sql, Object.values(req.query));

        for(let result of results) {
            sql = 'select * from matching_join where matching_id = ?'; 
            let [joinRequests] = await dbConn.query(sql, [result.id]);

            for(let joinRequest of joinRequests) {
                sql = 'select * from user_kakao where id = ?'; 
                let [userInfo] = await dbConn.query(sql, [joinRequest.guest_id]);
                joinRequest.guest = userInfo[0];
            }
            result.joinRequests = joinRequests;
        }

        return res.status(200).json({
            status: "OK",
            data: results
        });

    } catch(err) {
        console.error(err);
        return res.status(400).json({
            status: "NOT FOUND"
        });
    }
});

router.post('/:id/:status', passport.authenticate('kakao-token'), async (req, res) => {
    try {
        let sql = 'select * from matching where id = ?'; 
        let [matching] = await dbConn.query(sql, [req.params.id]);
        matching = matching[0];

        if(matching.host_id !== req.user.id)
            return res.status(400).json({
                status: "YOU'RE NOT HOST"
            });

        sql = 'select * from matching_join where matching_id = ?'; 
        let [joinRequests] = await dbConn.query(sql, [req.params.id]);
    
        for(let joinRequest of joinRequests) {
            sql = 'update matching_join set status = ? where id = ?'; 
            let [joinRequests] = await dbConn.query(sql, [req.body.matching_join_id === joinRequest.id ? 'makeup' : 'cancel', joinRequest.id]);
        }

        sql = 'update matching set status = ? where id = ?'; 
        let [result] = await dbConn.query(sql, [req.params.status, req.params.id]);

        return res.status(201).json({
            status: "OK",
            insertId: result.insertId,
            data: req.body,
        });

    } catch(err) {
        console.error(err);
        return res.status(400).json({
            status: "NOT FOUND"
        });
    }
});

router.get('/:id', async (req, res) => {
    try {
        let sql = 'select * from matching where id = ?'; 
        let [results] = await dbConn.query(sql, [req.params.id]);

        for(let result of results) {
            sql = 'select * from matching_join where matching_id = ?'; 
            let [joinRequests] = await dbConn.query(sql, [result.id]);

            for(let joinRequest of joinRequests) {
                sql = 'select * from user_kakao where id = ?'; 
                let [userInfo] = await dbConn.query(sql, [joinRequest.guest_id]);
                joinRequest.guest = userInfo[0];
            }
            result.joinRequests = joinRequests;
        }

        return res.status(200).json({
            status: "OK",
            data: results[0]
        });

    } catch(err) {
        console.error(err);
        return res.status(400).json({
            status: "NOT FOUND"
        });
    }
});

module.exports = router;