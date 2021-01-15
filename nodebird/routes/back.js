const express = require('express');
const multer = require('multer');
const path = require('path');
const fs = require('fs');
const { Post, Hashtag, User, Image } = require('../models');
const { isLoggedIn } = require('./middlewares');
const router = express.Router();
router.get('/', async (req, res, next) => {
    console.log(`${process.pid}번 워커 실행`);
    const {spawn} = require('child_process')
    /**
     * Run python myscript, pass in `-u` to not buffer console output
     * @return {ChildProcess}
    */
    async function runScript(){
    return spawn('python', [
        "-u",
        path.join(__dirname, 'test2.py'),
        "--foo", "매운 음식 추천해줘",
    ]);
    }
    const subprocess = await runScript()
    // print output of script
    subprocess.stdout.on('data', (data) => {
        console.log(`error:${data}`);
    });
    subprocess.stderr.on('data', (data) => {
    console.log(`error:${data}`);
    });
    subprocess.stderr.on('close', () => {
        runScript();
        console.log('closed');
    });


  });
module.exports = router;