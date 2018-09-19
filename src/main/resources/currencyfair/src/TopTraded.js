import React, {Fragment, PureComponent} from 'react';
import {Table} from 'semantic-ui-react';

class TopTraded extends PureComponent {
    render() {
        const {rows} = this.props;
        return (
            <Fragment><h3>Top traded</h3>
                <Table celled inverted>
                    <Table.Header>
                        <Table.Row key={`realtime-header`}>
                            <Table.HeaderCell key={`header-pair`}>Pair</Table.HeaderCell>
                            <Table.HeaderCell key={`header-value`}>Total trades</Table.HeaderCell>
                        </Table.Row>
                    </Table.Header>
                    <Table.Body>
                        {rows.map((row, index) => (
                            <Table.Row key={index}>
                                <Table.Cell key={`user-id-${index}`}>{row.pair}</Table.Cell>
                                <Table.Cell key={`user-from-${index}`}>{row.value}</Table.Cell>
                            </Table.Row>))
                        }
                    </Table.Body>
                </Table>
            </Fragment>
        );
    }
}

export default TopTraded;
