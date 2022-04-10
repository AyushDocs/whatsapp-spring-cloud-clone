/** @format */

import {EurekaClient} from 'eureka-js-client';
import {Request} from 'express';
import client from './client';
/** @format */
const map = new Map<string, number>();
export default function getServiceUrl(req: Request, serviceName: string): string | undefined {
	const instances = client.getInstancesByVipAddress(serviceName); //[profile_service]
	if (!instances.length) return undefined;
	const possibleValue = map.get(serviceName); //undefined
	let port = extractPort(instances, 0),
		index = 0;
	if (!possibleValue || possibleValue > instances.length - 1) {
		map.set(serviceName, 0);
		return `http://${instances[index].ipAddr}:${port}${req.url}`;
	}
	port = extractPort(instances, possibleValue + 1);
	map.set(serviceName, possibleValue + 1);
	return `http://${instances[index].ipAddr}:${port}${req.url}`;
}
function extractPort(instances: EurekaClient.EurekaInstanceConfig[], index: number) {
	const portObj: any = instances[index]?.port;
	const port: any = portObj?.['$'];
	return port;
}
