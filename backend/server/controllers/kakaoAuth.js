const checkLogin = (req, res, next) => {
    if (req.isAuthenticated() && req.user) return next();
    return res.status(401).json({
        status: "NOT LOGIN"
    });
};

module.exports = {
    checkLogin
}