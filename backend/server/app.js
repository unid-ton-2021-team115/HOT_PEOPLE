const express = require('express');
const app = express();
const server = require('http').createServer(app);
const path = require('path');
const morgan = require('morgan');
const cookieParser = require('cookie-parser');
const bodyParser = require('body-parser');
const session = require('express-session');
const cors = require('cors');
const passport = require('passport');
const hotPlaceUpdate = require('./middleware/hotPlaceUpdate');
var cron = require('node-cron');

require('moment-timezone');
require('moment').tz.setDefault("Asia/Seoul");;
require('dotenv').config();

app.set('port', process.env.PORT);
app.set('view engine', 'ejs');

app.use(morgan('dev'));
//app.use(express.static(path.join(__dirname, 'front-react/build')));
app.use(express.json());
app.use(cors());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser(process.env.COOKIE_SECRET));
app.use(bodyParser.urlencoded({ extended: true }) );
app.use(session({
    key: process.env.SESSION_KEY,
    secret: process.env.COOKIE_SECRET,
    saveUninitialized: true,
    resave: true,
    cookie: {
        httpOnly: false,
        secure: false
    },
}));
app.use(passport.initialize());
app.use(passport.session());

const kakaoPassportConfig = require('./config/kakaoPassport');
kakaoPassportConfig();

// 매일 정각마다 핫플레이스 업데이트
cron.schedule('00 00 00 * * 0-6', () => {
    hotPlaceUpdate();
}, {
    scheduled: true,
    timezone: 'Asia/Seoul'
});

//setting cors 
app.all('/*', (req, res, next) => {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "X-Requested-With");
    next(); 
}); 

// router
const apiRouter = require('./routes/apiRouter');
const devRouter = require('./routes/devRouter');
const authRouter = require('./routes/authRouter');

app.use('/api', apiRouter);
app.use('/dev', devRouter);
app.use('/auth', authRouter);

server.listen(app.get('port'), ()=>{
    console.log(`server port: ${app.get('port')}`)
});