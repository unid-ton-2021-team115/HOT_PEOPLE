const router = require('express').Router();
const passport = require('passport')
const axios = require('axios');

router.get('/kakao/login', passport.authenticate('kakao'));

router.get('/kakao/callback', passport.authenticate('kakao', {
    successRedirect: `https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=${process.env.KAKAO_REST_API_KEY}&redirect_uri=${process.env.KAKAO_LOGOUT_REDIRECT_URI}&prompt=login`,
    failureRedirect: '/auth/kakao'
}),(req,res)=>{
    res.redirect('/');
});

router.get('/kakao/check', async (req, res) => {
    try {
        if(req.isAuthenticated())
            return res.status(200).json(req.user);
        else
            return res.status(200).json({msg: "not logined"});
    } catch (err) {
        console.log(err)
    }
});
router.get('/kakao/logout', async (req, res) => {
    try {
        await axios.post('https://kapi.kakao.com/v1/user/logout', null, {headers: {Authorization: `Bearer ${req.user?.accessToken}`}});
        req.logout();
        req.session.destroy();
        return res.status(200).json({
            status: "OK",
        });
    } catch (err) {
        return res.status(401).json({
            status: "LOGOUT_FAIL",
        });
    }
});

router.get('/kakao/token', passport.authenticate('kakao-token'), function (req, res) {
    if (req.user) {
        console.log(req.user);
        return res.status(200).json({
            status: "OK",
            data: {
                ...req.user
            }
        });
    } else {
        return res.status(401).json({
            status: "LOGIN_FAIL",
        });
    }
});

module.exports = router;