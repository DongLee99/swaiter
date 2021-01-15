const {PythonShell} = require('python-shell');

var options = {
   mode : 'text',
   pythonPath:'',
   pythonOptions:['-u'],
   scriptPath: '',
   args : ['매운 음식 추천해줘']
};

PythonShell.run('test2.py', options, function(err,results){
   if (err) throw err;

   console.log('results: %j', results.toString());

});