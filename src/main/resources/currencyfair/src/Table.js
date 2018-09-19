import React, {PureComponent} from 'react';

class Table extends PureComponent {
    render() {
        const {rows} = this.props;
        return (
            <table>
                <thead>
                <tr>
                    <th>user id</th>
                </tr>
                </thead>
                <tbody>
                {rows.map((row, index) => (<tr key={index}><td>{row.userId}</td></tr>))}

                </tbody>
            </table>
        );
    }
}

export default Table;
