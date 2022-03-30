/** @format */

import axios from 'axios';
import {Proxy} from 'axios-express-proxy';
import bodyParser from 'body-parser';
import express from 'express';
import fileUpload from 'express-fileupload';
import {Server} from 'http';
import * as SocketIO from 'socket.io';
import getServiceUrl from './eureka/path';
import {createFormForFileUpload} from './service/imageService';
import handleIo from './socket';
import corsConfig from './socket/cors.config.json';

const port = process.env.PORT ? parseInt(process.env.PORT) : 8078;
const app = express();
app.use(
	fileUpload({
		createParentPath: true,
	})
);
app.use(bodyParser.json());
app.use((_, res, next) => {
	try {
		next();
	} catch (err) {
		res.send(err);
	}
});
app.use((req, _, next) => {
	console.log(req.url);
	next();
});

app.get('/info', (_, res) => {
	res.json({status: 'UP'});
});
app.all('/api/v1/images/:path', async (req, res) => {
	const serverPath = getServiceUrl(req, 'IMAGE-SERVICE');
	if (!serverPath) return res.status(502).send('SERVICE UNREACHABLE');
	const isMultiPartMessage = req.headers['content-type'] !== 'application/json';
	if (!isMultiPartMessage) return Proxy(serverPath, req, res);
	const form = createFormForFileUpload(req);
	if (!form) return res.status(400).end();
	const response = await axios.post(serverPath, form, {
		headers: form.getHeaders(),
	});
	return res.json({data: response.data});
});
app.all('/api/v1/users/:path', async (req, res) => {
	try {
		const serverPath = getServiceUrl(req, 'PROFILE-SERVICE');
		if (!serverPath) return res.status(502).send('SERVICE UNREACHABLE');
		return await  Proxy(serverPath, req, res);
	} catch (error) {
		return res.status(500).send(error);
	}
});
app.all('/api/v1/messages/:path', async (req, res) => {
	const serverPath = getServiceUrl(req, 'MESSAGE-SERVICE');
	if (!serverPath) return res.status(502).send('SERVICE UNREACHABLE');
	return Proxy(serverPath, req, res);
});
app.all('/api/v1/messages/', async (req, res) => {
	const serverPath = getServiceUrl(req, 'MESSAGE-SERVICE');
	if (!serverPath) return res.status(502).send('SERVICE UNREACHABLE');
	return Proxy(serverPath, req, res);
});
const server = new Server(app);

const socketIoServer = new SocketIO.Server(server, corsConfig);
handleIo(socketIoServer);
server.listen(port, () => console.log(`server is running on port ${port}`));
