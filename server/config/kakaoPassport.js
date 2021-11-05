const passport = require('passport') 
const KakaoStrategy = require('passport-kakao').Strategy 

module.exports = () => {
    passport.use('kakao', new KakaoStrategy({ 
        clientID: process.env.KAKAO_REST_API_KEY, 
        callbackURL: '/auth/kakao/callback',
    }, async (accessToken, refreshToken, profile, done) => { 
        console.log(`accessToken : ${accessToken}`)
        console.log(`사용자 profile: ${JSON.stringify(profile._json)}`)
        let user = {
            profile: profile._json,
            accessToken: accessToken
        }
        return done(null, user);
    }));

    passport.serializeUser(function (user, done) {
        console.log(`user : ${user.profile.id}`);
        done(null, user);
    })
    passport.deserializeUser(function (obj, done) {
        console.log(`obj : ${obj}`);
        done(null, obj);
    })
}


