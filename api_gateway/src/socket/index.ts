/** @format */

import * as SocketIO from 'socket.io';

export default function handleIo (io: SocketIO.Server){
      io.on('connection',(socket)=>{
            console.log('a user connected');
            socket.on('disconnect',()=>{
                  console.log('user disconnected');
            })
      })
}
