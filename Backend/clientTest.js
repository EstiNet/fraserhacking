/*
* Real-time coding
* (c) 2017 DolphinBox
* Built during the Fraser Hacks Hackathon
*/
//Node client for testing.
var socket = require('socket.io-client')('http://localhost:8080');
socket.on('connect', function(){
  socket.emit('helloWorld');
});
socket.on('event', function(data){});
socket.on('disconnect', function(){});