const router = require('express').Router();
const dbConn= require(`${process.cwd()}/dbConnection`);

router.get('/', async (req, res) => {
    try {
        let sql = "select * from place where is_hot = true order by recent_post_cnt desc"; 
        let [results] = await dbConn.query(sql, []);

        for(let result of results) {
            sql = 'select * from place_types where place_id = ?'; 
            let [place_types] = await dbConn.query(sql, [result.id]);
            let types = [];
            for(let place_type of place_types)
                types.push(place_type.type)
            result.types = types;
        } 

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