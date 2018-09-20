# currencyfair

Implementation of a system that receives a payload using http and pushes the data in real time to a FE.

Server implemented using [Vert.x](https://vertx.io) and the basic frontend is built on [React](https://reactjs.org).

Frontend app was bootstrapped with  [Create React App](https://github.com/facebook/create-react-app)

## Dependencies

 * Java 8
 * Maven
 * Node >= 6
 * NPM

 
## Building

 - Clone the project
 - Navigate to `{project root}/src/main/resources/currencyfair`
 - Run `$ npm install` to install node dependencies for the frontend application
 - Navigate to the {project root}
 - Run `$ mvn clean package`
 
 This produces a `fat jar` with all the dependencies packaged to run the application.
 
## Running

For deployment simplicity the FE app is served by the Vert.x application, so we only need to run the created jar

`java -jar target/exercise-fat.jar`

## Development

To run the frontend locally for development: 

 - Navigate to `{project root}/src/main/resources/currencyfair`
 - Run `$ npm run start-local`
 
To run the backend for development:
 
Depending on your IDE the way to configure it may vary but the main configuration details are

 - Main class: `io.vertx.core.Launcher`
 - Program arguments: `run com.currencyfair.exercise.MainVerticle`
