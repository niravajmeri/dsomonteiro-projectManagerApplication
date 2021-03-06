import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import AuthService from './../loginPage/AuthService';
import Error from './../../components/error/error';
import MediumButton from '../../components/button/mediumButton.jsx';
import Moment from 'react-moment';

class ProjectDetails extends Component {
    constructor(props) {
        super(props);
        this.state = {
            project: {},
            message: ''
        };
        this.AuthService = new AuthService();
    }

    componentDidMount() {
        this.AuthService.fetch(
            `/projects/${this.props.match.params.projectID}`,
            {
                method: 'get'
            }
        ).then(responseData => {
            this.setState({
                project: responseData,
                projectManager: responseData.projectManager.name,
                message: responseData.message
            });
        });
    }


    //Some of these parameters will be transfered to the accordion
    renderProject() {
        var projectWithDetails = this.state.project;
        var projectManagerX = this.state.projectManager;
        return (
            <tr className="line">
                <td>
                    <th>{projectWithDetails.projectId}</th>
                </td>
                <td>{projectWithDetails.projectStatusName}</td>
                <td>{projectWithDetails.name}</td>
                <td>{projectWithDetails.description}</td>
                <td>{projectManagerX}</td>
                <td>{projectWithDetails.effortUnit}</td>
                <td>{projectWithDetails.budget}</td>
                <td>
                <Moment format="YYYY/MM/DD">
                            {projectWithDetails.startdate}
                </Moment>
                </td>
                <td>
                <Moment format="YYYY/MM/DD">
                            {projectWithDetails.finishdate}
                </Moment>
                </td>
                <td>{projectWithDetails.calculationMethod}</td>
                <td>{projectWithDetails.availableCalculationMethods}</td>
            </tr>
        );
    }

    
    render() {
        if (this.state.message === '') {
            return <Error message={this.state.message} />;
        } else {
            return (
                <div className=" table-striped">
                    <h3>
                        <b>Project Details</b>
                    </h3>
                    <table className="table table-hover">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Status</th>
                                <th>Name</th>
                                <th>Description</th>
                                <th>Project Manager</th>
                                <th>Effort Unit</th>
                                <th>Budget</th>
                                <th>Start Date</th>
                                <th>Finish Date</th>
                                <th>Calculation Method</th>
                                <th>Available Calculation Methods</th>
                            </tr>
                        </thead>
                        <tbody>{this.renderProject()}</tbody>
                    </table>
                    <p />
                    <Link
                        to={'/projectcost/' + this.state.project.projectId}
                        activeClassName="active"
                    >
                        <MediumButton text="Calculate Project Cost" />
                    </Link>
                    &nbsp;
                    <Link
                        to={
                            '/selectprojectcostcalculation/' +
                            this.state.project.projectId
                        }
                        activeClassName="active"
                    >
                        <MediumButton text="Change Calculation Method" />
                    </Link>
                    &nbsp;
                </div>
            );
        }
    }
}

export default ProjectDetails;
