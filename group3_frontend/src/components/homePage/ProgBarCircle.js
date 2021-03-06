import React, { Component } from 'react';
import { Circle } from 'rc-progress';
import './Homepage.css';

class ProgBarCircle extends Component {
    constructor(props) {
        super(props);
        this.state = {
            percent: 0,
            color: '#25c4a9',
            trailWidth: 5,
            trailColor: '#aaa',
            strokeLinecap: 'square',
            strokeWidth: '5',
            gapDegree: '190',
            gapPosition: 'bottom'
        };
    }

    componentDidMount() {
        this.increase();
    }

    increase = () => {
        const percent = this.state.percent + 1;
        if (percent >= this.props.limit) {
            clearTimeout(this.tm);
            return;
        }
        this.setState({
            percent
        });
        this.tm = setTimeout(this.increase, 10);
    };

    render() {
        return (
            <div className="CircleGraph">
                <Circle
                    percent={this.state.percent}
                    gapPosition={this.state.gapPosition}
                    gapDegree={this.state.gapDegree}
                    strokeWidth={this.state.strokeWidth}
                    trailColor={this.state.trailColor}
                    trailWidth={this.state.trailWidth}
                    strokeLinecap={this.state.strokeLinecap}
                    strokeColor={this.state.color}
                />
            </div>
        );
    }
}
export default ProgBarCircle;
