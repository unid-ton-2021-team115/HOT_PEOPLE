const router = require('express').Router();
const dbConn= require(`${process.cwd()}/dbConnection`);

router.get('/search', async (req, res) => {
    try {
        let sql = 'select * from place where'; 
        for(key in req.query) sql += ` ${key} = ? AND`;
        sql = sql.substr(0, sql.length - 3);
        let [results] = await dbConn.query(sql, Object.values(req.query));

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

router.get('/:id', async (req, res) => {
    try {
        let sql = 'select * from place where id = ?'; 
        let [results] = await dbConn.query(sql, [req.params.id]);

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
            data: results[0]
        });

    } catch(err) {
        return res.status(400).json({
            status: "NOT FOUND"
        });
    }
});

module.exports = router;