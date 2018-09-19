import React, {Component} from 'react';
import EventBus from 'vertx3-eventbus-client'
import './App.css';
import RealTimeTable from './RealTimeTable.js'
import TopTraded from './TopTraded.js'
import {Grid} from 'semantic-ui-react';

class App extends Component {
    state = {
        liveRows: [],
        topTraded: [],
    };

    render() {
        const {liveRows} = this.state;
        const {topTraded} = this.state;
        return (
            <div className="App">
                <header className="App-header">
                    <h1 className="App-title">currency fair</h1>
                </header>
                <div className="App-body">
                    <Grid celled>
                        <Grid.Column width={13} key='1'>
                            <RealTimeTable rows={liveRows}/>
                        </Grid.Column>
                        <Grid.Column width={3} key='2'>
                            <TopTraded rows={topTraded}/>
                        </Grid.Column>
                    </Grid>
                </div>
            </div>
        );
    }

    componentDidMount() {
        let eb = new EventBus('http://localhost:8080/eventbus');

        eb.onopen = () => {
            // set a handler to receive a message
            eb.registerHandler('realtime.messages', (error, message) => {
                this.setState((prevState) => {
                    // only keep the last 10 entries
                    let newRows = [];
                    if (prevState.liveRows.length === 10) {
                        const liveRows = [...prevState.liveRows];
                        liveRows.pop();
                        liveRows.unshift(message.body);
                        newRows = liveRows;
                    } else {
                        newRows = [message.body, ...prevState.liveRows];
                    }

                    return {liveRows: newRows};
                })
            });

            eb.registerHandler('traded.pairs.message', (error, message) => {
                this.setState({topTraded: message.body});
            });
        }
    }
}

export default App;
