#currencyfair

Implementation of a system that receives a payload using http and pushes the data in real time to a FE.

Server implemented using [Vert.x](https://vertx.io) and the basic frontend is built on [React](https://reactjs.org).

## Dependencies

 * Java 8
 * Maven
 * Node >= 6
 * NPM
 * [Create React App](https://github.com/facebook/create-react-app)
 
## Building

 - Clone the project
 - Navigate to {project root}/src/main/resources/currencyfair
 - Run `$ npm install` to install node dependencies for the frontend application
 - Navigate to the {project root}
 - Run `$ mvn clean package`
 
 This produces a `fat jar` with all the dependencies packaged to run the application.
 
## Running

For deployment simplicity the FE app is served by the Vert.x application, so we only need to run the created jar

`java -jar target/exercise-fat.jar`
