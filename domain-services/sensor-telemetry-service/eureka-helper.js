import { Eureka } from "eureka-js-client";

const client = new Eureka({
    instance: {
        app: "sensor-service",
        hostName: "localhost",
        ipAddr: "127.0.0.1",
        port: {
            "$": process.env.PORT || 8082,
            "@enabled": "true"
        },
        vipAddress: 'sensor-service',
        dataCenterInfo: {
            '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
            name: 'MyOwn',
        },
    },
    eureka: {
        host: 'localhost',
        port: 8761,
        servicePath: '/eureka/apps/'
    }
});

export const registerWithEureka = () => {
    client.start((error) => {
        if (error) {
            console.error('Failed to register with Eureka:', error);
        } else {
            console.log('Successfully registered with Eureka.');
        }
    });
};