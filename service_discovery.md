# Service Discovery

For this chapter, please checkout the `v3` tag: `git checkout v3`.

NOTE: This chapter is included so make the transition to PCF's service discovery service easier.

One of the problems with distributed systems is keeping track of all available running instances of a particular service so other services can communicate with it. As an app scales, it will bring up new nodes or destroy existing nodes.

To solve this problem, we introduce Eureka, a tool from Netflix that allows services to register themselves with a central registrar. Other applications can then ask the central register how to reach a specific service, instead of having to keep and update that configuration in the services themselves.

In this commit we added `org.springframework.boot:spring-boot-starter-actuator` and `org.springframework.cloud:spring-cloud-starter-eureka` as dependencies to the billing and subscription applications. We also annotated both applications with the `@EnableDiscoveryClient` annotation. Finally, we created the `platform/serviceDiscovery` directory to hold our actual Eureka instance, which will be deployed as it's own application.