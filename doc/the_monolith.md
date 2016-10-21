# The Monolith

To begin, make sure you are in the repository cloned in the introduction, and checkout the `v1` tag: `git checkout v1`.

The application will now be in it's monolithic state. We have used a component-based architecture to make it easier to identify the bounded contexts that exist in our application. This will make it easier to extract them into their own applications.

Notice the application, located under `applications/ums` is a simple application. It contains an `Application.java` which configures the Spring Boot application and some Beans, and a single Subscriptions Controller. 

The rest of the applicaiton's functionality comes from it components. The UMS application's `build.gradle` specifies these components as dependecies in it's [`build.gradle`](../applications/ums/build.gradle):

[import](../applications/ums/build.gradle)
