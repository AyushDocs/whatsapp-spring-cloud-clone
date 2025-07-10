/** @format */

import {EurekaClient} from 'eureka-js-client';
import {Request} from 'express';
import client from './client';
import logger from '../utils/logger';

const map = new Map<string, number>();

export default function getServiceUrl(req: Request, serviceName: string): string | undefined {
	const instances = client.getInstancesByVipAddress(serviceName);
	if (!instances.length) {
		logger.error(`No instances found for service: ${serviceName}`);
		return undefined;
	}
	const possibleValue = map.get(serviceName);
	let port = extractPort(instances, 0),
		index = 0;
	if (!possibleValue || possibleValue > instances.length - 1) {
		map.set(serviceName, 0);
		logger.info(`Routing to service: ${serviceName} at ${instances[index].ipAddr}:${port}${req.url}`);
		return `http://${instances[index].ipAddr}:${port}${req.url}`;
	}
	port = extractPort(instances, possibleValue + 1);
	map.set(serviceName, possibleValue + 1);
	logger.info(`Routing to service: ${serviceName} at ${instances[index].ipAddr}:${port}${req.url}`);
	return `http://${instances[index].ipAddr}:${port}${req.url}`;
}

function extractPort(instances: EurekaClient.EurekaInstanceConfig[], index: number) {
	const portObj: any = instances[index]?.port;
	const port: any = portObj?.['$'];
	return port;
}
