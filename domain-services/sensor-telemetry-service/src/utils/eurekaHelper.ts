import { Eureka } from 'eureka-js-client';

export const registerWithEureka = (appName: string, port: number) => {
  const client = new Eureka({
    instance: {
      app: appName,
      hostName: process.env.EUREKA_INSTANCE_HOSTNAME || 'localhost',
      ipAddr: process.env.EUREKA_INSTANCE_IP || '127.0.0.1',
      port: {
        '$': port,
        '@enabled': true,
      },
      vipAddress: appName,
      dataCenterInfo: {
        '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
        name: 'MyOwn',
      },
    },
    eureka: {
      host: process.env.EUREKA_HOST || 'localhost',
      port: parseInt(process.env.EUREKA_PORT || '8761'),
      servicePath: '/eureka/apps/',
    },
  });

  client.start((error: Error | undefined) => {
    if (error) {
      console.error('Failed to register with Eureka:', error);
    } else {
      console.log(`Successfully registered ${appName} with Eureka.`);
    }
  });
};
