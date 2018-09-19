import React, {Fragment, PureComponent} from 'react';
import {Table} from 'semantic-ui-react';

class RealTimeTable extends PureComponent {
    render() {
        const {rows} = this.props;
        return (
            <Fragment> <h3>Live data</h3>
                <Table celled inverted>
                    <Table.Header>
                        <Table.Row key={`realtime-header`}>
                            <Table.HeaderCell key={`header-user-id`}>User Id</Table.HeaderCell>
                            <Table.HeaderCell key={`header-from`}>From</Table.HeaderCell>
                            <Table.HeaderCell key={`header-to`}>To</Table.HeaderCell>
                            <Table.HeaderCell key={`header-sell`}>Sell</Table.HeaderCell>
                            <Table.HeaderCell key={`header-buy`}>Buy</Table.HeaderCell>
                            <Table.HeaderCell key={`header-rate`}>Rate</Table.HeaderCell>
                            <Table.HeaderCell key={`header-timeplaced`}>Time Placed</Table.HeaderCell>
                            <Table.HeaderCell key={`header-country`}>Country</Table.HeaderCell>
                        </Table.Row>
                    </Table.Header>
                    <Table.Body>
                        {rows.map((row, index) => (
                            <Table.Row key={index}>
                                <Table.Cell key={`user-id-${index}`}>{row.userId}</Table.Cell>
                                <Table.Cell key={`user-from-${index}`}>{row.currencyFrom}</Table.Cell>
                                <Table.Cell key={`user-to-${index}`}>{row.currencyTo}</Table.Cell>
                                <Table.Cell key={`user-sell-${index}`}>{row.amountSell}</Table.Cell>
                                <Table.Cell key={`user-buy-${index}`}>{row.amountBuy}</Table.Cell>
                                <Table.Cell key={`user-rate-${index}`}>{row.rate}</Table.Cell>
                                <Table.Cell key={`user-timeplaced-${index}`}>{row.timePlaced}</Table.Cell>
                                <Table.Cell key={`user-country-${index}`}>{row.originatingCountry}</Table.Cell>
                            </Table.Row>))
                        }
                    </Table.Body>
                </Table>
            </Fragment>
        );
    }
}

export default RealTimeTable;
