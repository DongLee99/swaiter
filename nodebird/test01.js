    /*var express = require('express');
    var router = express.Router();
    let {PythonShell} = require('python-shell')
    let options = {
        mode: 'text',
        pythonPath: '/Library/Frameworks/Python.framework/Versions/3.7/bin/python3',
        pythonOptions: ['-u'], // get print results in real-time
        scriptPath: './folder1/dm-portal'
      };
    PythonShell.run('python-file.py', options, function (err) {
        if (err) throw err;
        console.log('finished');
    });*/

    const cluster = require('cluster');
    const http = require('http');
    const numCPUs = require('os').cpus().length;

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
      http.createServer((req,res) => {
        res.write('<h1> Hello Node!</h1>');
        res.end('<p>Hello cluster!</p>');
        setTimeout(()=>{
          process.exit(1);
        },1000);
      }).listen(8085);
      console.log(`${process.pid}번 워커 실행`);
    }
