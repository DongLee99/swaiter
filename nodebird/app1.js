const cluster = require('cluster');
    const http = require('http');
    const numCPUs = require('os').cpus().length;
    const express = require('express');
    const cookieParser = require('cookie-parser');
    const morgan = require('morgan');
    const path = require('path');
    const session = require('express-session');
    const flash = require('connect-flash');
    const passport = require('passport');
    require('dotenv').config();
    if (cluster.isMaster) {
      console.log(`마스터 프로세스 아이디: ${process.pid}`);
      for(let i = 0; i <numCPUs; i += 1){
        cluster.fork();
      }
      cluster.on('exit', (worker, code, signal) => {
        console.log(`${worker.process.pid}번의 워커가 종료되었습니다.`);
        cluster.fork();
      });
    }else {
        console.log(`${process.pid}번 워커 실행`);
        const pageRouter = require('./routes/page');
        const authRouter = require('./routes/auth');
        const postRouter = require('./routes/post');
        const userRouter = require('./routes/user');
        const backRouter = require('./routes/back');
        const { sequelize } = require('./models');
        const passportConfig = require('./passport');

        const app = express();

        
        sequelize.sync();
        passportConfig(passport);
        app.use('/static', express.static('photo'));

        app.set('views', path.join(__dirname, 'views'));
        app.set('view engine', 'pug');
        app.set('port', process.env.PORT||9090);

        app.use(morgan('dev'));
        app.use(express.static(path.join(__dirname, 'public')));
        app.use('/img', express.static(path.join(__dirname, 'uploads')));
        app.use(express.json());
        app.use(express.urlencoded({extended: false}));
        app.use(cookieParser(process.env.COOKIE_SECRET));
        app.use(session({
            resave: false,
            saveUninitialized: false,
            secret: process.env.COOKIE_SECRET,
            cookie: {
                httpOnly: true,
                secure: false,
            },
        }));

        app.use(flash());
        app.use(passport.initialize());
        app.use(passport.session());
        app.use('/', pageRouter);
        /*app.get('/',function(req,res){
          console.log(`${process.pid}번 워커 실행`);
        })*/
        app.use('/auth', authRouter);
        app.use('/post', postRouter);
        app.use('/back', backRouter);

        app.use((req, res, next) => {
            const err = new Error('Not Found');
            err.status = 404,
            next(err);
        });

        app.use((err, req,res,next) => {
            res.locals.message = err.message;
            res.locals.error = req.app.get('env') === 'development' ? err : {};
            res.status(err.status || 500);
            res.render('error');
        });

        app.listen(app.get('port'), () => {
            console.log(app.get('port'), '번 포트에서 대기중');
        });
      
    }


    