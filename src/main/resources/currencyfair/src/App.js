import React, {Component} from 'react';
import logo from './logo.svg';
import EventBus from 'vertx3-eventbus-client'
import './App.css';
import Table from './Table.js'

class App extends Component {
    state = {
        rows: []
    };
    render() {
        const {rows }= this.state;
        return (
            <div className="App">
                <header className="App-header">
                    <img src={logo} className="App-logo" alt="logo"/>
                    <h1 className="App-title">Welcome to React</h1>
                </header>
                <Table rows={rows}/>
            </div>
        );
    }

    componentDidMount() {
        let eb = new EventBus('http://localhost:8080/eventbus');

        eb.onopen =  () =>{
            // set a handler to receive a message
            eb.registerHandler('exercise.message',  (error, message) =>{
                console.log(typeof message);
                console.log(message);

                console.log(message.body);
                this.setState((prevState) => {
                    const newRows = [...prevState.rows, message.body];

                    return {rows: newRows};
                })
            });
        }
    }
}

export default App;
