/*
* Real-time coding
* (c) 2017 DolphinBox
* Built during the Fraser Hacks Hackathon
*/
var io = require('socket.io')(8080);
console.log('Starting GitRTC backend server...');
io.on('connection', function (socket) {
  socket.on('helloWorld', function (name, fn) {
    fn('hai', 'hai');
  });
});
