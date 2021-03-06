# currencyfair

Implementation of a system that receives a payload using http and pushes the data in real time to a FE.

Server implemented using [Vert.x](https://vertx.io) and the basic frontend is built on [React](https://reactjs.org).

Frontend app was bootstrapped with  [Create React App](https://github.com/facebook/create-react-app).

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
 
To run the backend locally for development:
 
Depending on your IDE the way to configure it may vary but the main configuration details are:

 - Main class: `io.vertx.core.Launcher`
 - Program arguments: `run com.currencyfair.exercise.MainVerticle`

## Project architecture / organization

The current implementation leverages the concept of verticles provided by Vert.x, there is one verticle that starts an http server and for each request publishes
a message on the event bus.

Then there is a verticle for each "feature", being pushing directly to the FE, or calculating which trading pairs are most traded. Adding new features would be a question
of adding new Verticles that would handle that specific feature.

Given the code simplicity there is no dependency injection mechanism put in place, if the code continues evolve and become more complex [Guice](https://github.com/google/guice) could be added to manage DI.

## Details

Endpoint to ingest metrics is available at
`/ingestion`

Expects/consumes: `application/json; charset=utf-8`


## Limitations

 - Current implementation doesn't persist data, so if the server is restarted all data is lost.
 - While vertx event bus supports clustering the current implementation, given that there is no shared data, would not work. This could be handled either by using a persistence 
 mechanism or a in memory distributed cache ([hazelcast](https://hazelcast.org) could be an option).
 - The application is not exposing metrics.
 - The event bus doesn't provide out of the box guaranteed delivery, only best effort, so some messages can be lost and there is no application logic to handle that.
 
 ## Stress testing
 
 On the folder named `vegeta` there is a simple test config file tha can be executed with [Vegeta](https://github.com/tsenart/vegeta).
 
 On my local enviroment (2015 Macbook Pro 15') I get the folowing test results:
 
 ```
 
 $ vegeta attack -rate=2000 -duration=30s -targets=vegeta-config | vegeta report
 Requests      [total, rate]            60000, 2000.13
 Duration      [total, attack, wait]    29.998342079s, 29.998119752s, 222.327µs
 Latencies     [mean, 50, 95, 99, max]  175.392µs, 149.371µs, 251.138µs, 495.858µs, 14.777566ms
 Bytes In      [total, mean]            0, 0.00
 Bytes Out     [total, mean]            11200000, 186.67
 Success       [ratio]                  100.00%
 Status Codes  [code:count]             200:60000
 
 ```
 
 These results were with a single UI (browser) being updated in real time.
 
## Deployment

Currently deployed on [AWS Fargate](https://aws.amazon.com/fargate/) in the Oregon region. 
