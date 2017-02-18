/*
* Real-time coding
* (c) 2017 DolphinBox
* Built during the Fraser Hacks Hackathon
*/
//Node client for testing.
var socket = require('socket.io-client')('http://localhost:80');
socket.on('connect', function () { // TIP: you can avoid listening on `connect` and listen on events directly too!
    socket.emit('helloWorld', 'connect', function (data) {
      console.log(data); // data will be 'woot'
    });
    socket.emit('gitSync');
    socket.emit('fileUpdate', 'usernameTest something');
  });