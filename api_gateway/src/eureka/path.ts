/** @format */

import {EurekaClient} from 'eureka-js-client';
import {Request} from 'express';
import client from './client';
/** @format */
export default function getServiceUrl(req: Request, serviceName: string): string | undefined {
	const instances = client.getInstancesByVipAddress(serviceName);
	if (!instances.length) return undefined;
	if (!instances[0]) return undefined;
	const port: any = extractPort(instances);
	return 'http://' + instances[0].ipAddr + ':' + port + req.url;
}
function extractPort(instances: EurekaClient.EurekaInstanceConfig[]) {
	const portObj: any = instances[0]?.port;
	const port: any = portObj?.['$'];
	return port;
}
