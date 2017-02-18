/*
* Real-time coding
* (c) 2017 DolphinBox
* Built during the Fraser Hacks Hackathon
*/
console.log('Starting GitRTC backend server...');
var io = require('socket.io')(80);

io.on('connection', function (socket) {
  socket.on('helloWorld', function (data, fn) {
    console.log(data)
    fn('helloClient');
  });
});