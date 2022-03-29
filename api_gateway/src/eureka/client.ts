/** @format */
import {Eureka} from 'eureka-js-client';

const port = process.env.PORT ? parseInt(process.env.PORT) : 3001;
const client = new Eureka({
	instance: {
		app: 'API-GATEWAY',
		hostName: 'localhost',
		ipAddr: '127.0.0.1',
            statusPageUrl: `http://localhost:${port}/info`,
		port: {
			$: port,
			'@enabled': true,
		},
		vipAddress: 'http://API-GATEWAY',
		dataCenterInfo: {
			'@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
			name: 'MyOwn',
		},
	},
	eureka: {
		host: '127.0.0.1',
		port: 8761,
            servicePath: '/eureka/apps/',
            maxRetries:10,
            requestRetryDelay:2000,
	},
});
client.start(error =>console.log(error || 'user service registered'));
export default client;