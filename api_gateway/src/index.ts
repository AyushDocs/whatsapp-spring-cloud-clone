import express from 'express';
import { Server } from 'http';
import * as SocketIO from 'socket.io';
const port=process.env.PORT || 3001;

const app=express();
const server=new Server(app);

const io=new SocketIO.Server(server,{
      cors:{
            origin:'*'
      }
})

server.listen(port,()=>console.log(`server is running on port ${port}`));