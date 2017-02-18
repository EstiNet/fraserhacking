/*
* Real-time coding
* (c) 2017 DolphinBox
* Built during the Fraser Hacks Hackathon
*/
console.log('Starting GitRTC backend server...');
var io = require('socket.io')(80);
var name = {}; 

io.on('connection', function (socket) {

  socket.on('helloWorld', function (data, fn) {
    console.log(data)
    console.log(socket.id)
    fn('helloClient');
    
  });
  socket.on('gitSync', function (data, fn) {
    //Sync with git!
    var spawn = require('child_process').spawn,
    ls    = spawn('cmd.exe', ['/c', 'git.bat']);

    ls.stdout.on('data', function (data) {
    console.log('GIT: ' + data);
    });

    ls.stderr.on('data', function (data) {
    console.log('GIT ERROR: ' + data);
    });

    ls.on('exit', function (code) {
    console.log('child process exited with code ' + code);
    });
    
  });
  socket.on('fileUpdate', function(data, fn){
    console.log("---------------------------------------------");
    var splitData = data.split(" ");
    console.log(splitData[0]);
    console.log(splitData[1]);
  });

});
setInterval(function(){
    var spawn = require('child_process').spawn,
    ls    = spawn('cmd.exe', ['/c', 'git.bat']);

    ls.stdout.on('data', function (data) {
    console.log('GIT: ' + data);
    });

    ls.stderr.on('data', function (data) {
    console.log('GIT ERROR: ' + data);
    });

    ls.on('exit', function (code) {
    console.log('child process exited with code ' + code);
    });
}, 10000);