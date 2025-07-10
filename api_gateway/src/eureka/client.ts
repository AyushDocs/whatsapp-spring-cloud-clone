/** @format */
import {Eureka} from 'eureka-js-client';
import logger from '../utils/logger'
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
            requestRetryDelay:10000,
	},
});
client.start(error => {
    if (error) {
        logger.error('Eureka registration failed:', error);
    } else {
        logger.info('API Gateway registered with Eureka');
    }
});
export default client;