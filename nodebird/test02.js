const path = require('path')
const {spawn} = require('child_process')
/**
   * Run python myscript, pass in `-u` to not buffer console output
   * @return {ChildProcess}
*/
function runScript(){
   return spawn('python', [
      "-u",
      path.join(__dirname, 'test.py'),
     "--foo", "매운 음식 추천해줘",
   ]);
}
let kaka = 0;
let count = 0;
const subprocess = runScript()
// print output of script
subprocess.stdout.on('data', (data) => {
   console.log(typeof(data));
   console.log(`data:${data}`);
   console.log(data.toString());
   count = count + 1;
   console.log(count);
   kaka = data.toString();
   console.log(kaka)
});
subprocess.stderr.on('data', (data) => {
   console.log(`error:${data}`);
});
subprocess.stderr.on('close', () => {
});

