/** @format */

import axios from 'axios';
import express from 'express';
import FormData from 'form-data';
import Fetch, {Headers, Response} from 'node-fetch';
import getServiceUrl from '../eureka/path';

const createFormForFileUpload = (req: express.Request) => {
	const validImage = validateFile(req);
	if (!validImage) return undefined;
	const form = new FormData();
	form.append('image', validImage.data, validImage.name);
	return form;
};

const handleMultipartRequest = async (req: express.Request, res: express.Response, serviceName: string) => {
	const serverPath = getServiceUrl(req, serviceName);
	if (!serverPath) return res.status(502).send('SERVICE UNREACHABLE');
	const form = createFormForFileUpload(req);
	if (!form) return res.status(400).end();
	const response = await axios.post(serverPath, form, {
		headers: form.getHeaders(),
	});
	return res.json({data: response.data});
};
export const handleImageRequests = (req: express.Request, res: express.Response) => {
	const isMultiPartMessage = req.headers['content-type'] !== 'application/json';
	if (isMultiPartMessage) return handleMultipartRequest(req, res, 'IMAGE-SERVICE');
	return proxyRequest(req, res, 'IMAGE-SERVICE');
};
export const proxyRequest = async (request: express.Request, response: express.Response, serviceName: string) => {
	const serverPath: string | undefined = getServiceUrl(request, serviceName);
	if (!serverPath) return response.status(502).send('SERVICE UNREACHABLE');
	const headers = getHeadersFromRequest(request);
	const res: Response = await Fetch(serverPath, {
		method: request.method,
		headers,
		body: JSON.stringify(request.body)
	});
	const body=await res.text()
	addHeadersToResponse(response,res.headers);
	return response.status(res.status).send(body).end();
};
const validateFile = (req: express.Request) => {
	if (!req.files) return undefined;
	const imageFile = req.files.image;
	if (Array.isArray(imageFile)) return undefined;
	return imageFile;
};
function addHeadersToResponse(response: express.Response,requestHeaders:Headers) {
	const keys=Array.from(requestHeaders.keys());
	const values=Array.from(requestHeaders.values());
	for (let i = 0; i < keys.length; i++) {
		response.setHeader(keys[i],values[i]);
	}
}

function getHeadersFromRequest(request:express.Request) {
	const headers=new Headers();
	const requestHeaders = request.rawHeaders;
	for (let i = 0; i < requestHeaders.length; i += 2) {
		headers.append(requestHeaders[i], requestHeaders[i + 1]);
	}
	return headers;
}

