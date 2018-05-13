import React, { Component } from "react";
import "./SideBar.css";
import { NavLink } from "react-router-dom";

class SideBar extends Component {
    constructor(props) {
        super(props);
    }

    componentWillMount() {
        const visibility = this.props.isVisible ? "" : "hide";
        this.setState({
            visibility
        });
    }

    static getDerivedStateFromProps(nextProps, prevState) {
        const visibility = nextProps.isVisible ? "" : "hide";

        if (visibility === prevState.visibility) {
            return null;
        }
        return {
            visibility
        };
    }

    render() {
        return (
            <div
                className={"col-sm-3 col-md-2 sidebar " + this.state.visibility}
            >
                <ul className="nav nav-sidebar">
                    <li>
                        <NavLink to="/projects" activeClassName="active">
                            Projects
                        </NavLink>
                    </li>
                    <li>
                        <NavLink to="/tasks" activeClassName="active">
                            Tasks
                        </NavLink>
                    </li>
                    <li>
                        <NavLink to="/users" activeClassName="active">
                            Users
                        </NavLink>
                    </li>
                    <li>
                        <NavLink to="/projectcost" activeClassName="active">
                            Project Cost
                        </NavLink>
                    </li>
                    <li>
                        <NavLink to="/selectprojectcostcalculation" activeClassName="active">
                            Select Calculation Method<br/>for Project Cost 
                        </NavLink>
                    </li>
                </ul>
            </div>
        );
    }
}

export default SideBar;
