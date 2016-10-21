# Introduction

This tutorial will walk through the process of breaking up a monotolithic Java application to a collection of microservices, that will then be deployed to a local installation of Pivotal Cloud Foundry. This will hopefully show how to break out bounded contexts into their own applications, how to enable communication among the now distributed applications, how to deploy the microservices to a cloud environment, and finally how to simulate load and scale only the services that experience the highest load.

To follow along with this book, please clone the repository:
`https://github.com/trevorwhitney/netflix_oss_playground.git`