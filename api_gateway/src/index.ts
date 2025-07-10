/** @format */

import express from 'express';
import {Server as HttpServer} from 'http';
import {Server as SocketIOServer} from 'socket.io';
import {fileMiddleWare} from './middleware/file';
import {handleImageRequests, proxyRequest} from './service';
import {setupSocketEvents} from './eureka/socket-events'
import cookieParser from 'cookie-parser';
import { corsMiddleware } from './middleware/corsMiddleware';
import httpLogger from './middleware/httpLogger';

const port = process.env.PORT ? parseInt(process.env.PORT) : 8078;
const app = express();
app.use(corsMiddleware);
app.use(httpLogger);
app.use(cookieParser());
app.use(fileMiddleWare);
app.use(express.json());
app.use(express.urlencoded({extended: true}));

app.use('/api/v1/images', handleImageRequests);
app.use('/api/v1/images/:path', handleImageRequests);

app.use('/api/v1/users',(req, res) => proxyRequest(req, res, 'PROFILE-SERVICE'));
app.use('/api/v1/users/:path',(req, res) => proxyRequest(req, res, 'PROFILE-SERVICE'));

app.use('/api/v1/messages',(req, res) => proxyRequest(req, res, 'MESSAGE-SERVICE'));
app.use('/api/v1/messages/:path',(req, res) => proxyRequest(req, res, 'MESSAGE-SERVICE'));

import logger from './utils/logger';

const httpServer = new HttpServer(app);
const socketIoServer = new SocketIOServer(httpServer, {
    cors:{
        origin:'*',
        methods:'*'
    }
});
setupSocketEvents(socketIoServer);
httpServer.listen(port, () => logger.info(`server is running on port ${port}`));
