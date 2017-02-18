/*
* Real-time coding
* (c) 2017 DolphinBox
* Built during the Fraser Hacks Hackathon
*/
//Main backend server!
console.log('Starting GitRTC backend server...');
var io = require('socket.io')(80);
var fs = require('fs');

var name = {}; 
var fileCacheDir = {};


io.on('connection', function (socket) {

  socket.on('helloWorld', function (data, fn) {
    console.log(data)
    console.log(socket.id)
    fn('helloClient');
    
  });
  socket.on('hello', function (data, fn) {
    var split = data.split(" ");
    name[socket.id] = split[0];
    console.log(data)
    console.log(socket.id)
    fn('Authed');
    
  });
  socket.on('changedir', function (data, fn) {
    fileCacheDir[socket.id] = data;
    fn('OK');
    socket.broadcast.send('changedir', data);
  });
  socket.on('changefile', function (data, fn) {
    socket.broadcast.send('changefile', data);
    fs.writeFile("C:\\GitRTC\\test\\" + fileCacheDir[socket.id], data, function(err) {
    if(err) {
        return console.log(err);
    }

    console.log("The file was saved!");
    }); 
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
  socket.on('cursor', function (data, fn) {
    var split = data.split(" ");
    socket.broadcast.send('cursor', name[socket.id] + " " + split[0] + " " + split[1] + " " + split[2]);
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