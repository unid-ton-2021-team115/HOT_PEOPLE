const router = require('express').Router();
const dbConn = require(`${process.cwd()}/dbConnection`);
const passport = require('passport');

router.get('/:status', passport.authenticate('kakao-token'), async (req, res) => {
    try {
        let sql = 'select * from matching where host_id = ? and status = ?'; 
        let [results] = await dbConn.query(sql, [req.user.id, req.params.status]);

        for(let result of results) {
            sql = 'select * from matching_join where matching_id = ?'; 
            let [joinRequests] = await dbConn.query(sql, [result.id]);

            sql = 'select * from user_kakao where id = ?'; 
            let [hostInfo] = await dbConn.query(sql, [result.host_id]);
            result.host = hostInfo[0];

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

module.exports = router;