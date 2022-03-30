/** @format */

import axios from 'axios';
import {Proxy} from 'axios-express-proxy';
import {Request, Response} from 'express';
import FormData from 'form-data';
import getServiceUrl from '../eureka/path';

const createFormForFileUpload = (req: Request) => {
	const validImage = validateFile(req);
	if (!validImage) return undefined;
	const form = new FormData();
	form.append('image', validImage.data, validImage.name);
	return form;
};

const handleMultipartRequest = async (req: Request, res: Response, serviceName: string) => {
	const serverPath = getServiceUrl(req, serviceName);
	if (!serverPath) return res.status(502).send('SERVICE UNREACHABLE');
	const form = createFormForFileUpload(req);
	if (!form) return res.status(400).end();
	const response = await axios.post(serverPath, form, {
		headers: form.getHeaders(),
	});
	return res.json({data: response.data});
};
export const handleImageRequests = (req: Request, res: Response) => {
	const isMultiPartMessage = req.headers['content-type'] !== 'application/json';
	if (isMultiPartMessage) return handleMultipartRequest(req, res, 'IMAGE-SERVICE');
	return proxyRequest(req, res, 'IMAGE-SERVICE');
};
export const proxyRequest = async (req: Request, res: Response, serviceName: string) => {
	const serverPath = getServiceUrl(req, serviceName);
	if (!serverPath) return res.status(502).send('SERVICE UNREACHABLE');
	return Proxy(serverPath, req, res)
		.then(result => result)
		.catch(err => res.status(500).send(err.message));
};
const validateFile = (req: Request) => {
	if (!req.files) return undefined;
	const imageFile = req.files.image;
	if (Array.isArray(imageFile)) return undefined;
	return imageFile;
};
