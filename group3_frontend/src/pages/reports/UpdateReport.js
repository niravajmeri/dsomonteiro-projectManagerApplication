import React, { Component } from "react";
import { FormGroup, FormControl, ControlLabel } from "react-bootstrap";
import AuthService from './../loginPage/AuthService';
import "./CreateReport.css";

class UpdateReport extends Component {
    constructor(props) {
        super(props);
        this.match;
        this.state = {
            projectId: "",
            taskID: "",
            reportId: "",
            reportedTime: "",
            taskCollabEmail: ""
        };
        this.AuthService = new AuthService();
    }

    validateForm() {
        return (
            this.state.reportedTime.length > 0 && this.state.email.length > 0
        );
    }

    handleChange = event => {
        this.setState({
            [event.target.id]: event.target.value
        });
    };

    handleSubmit = event => {
        event.preventDefault();
        const { reportedTime, taskCollabEmail } = this.state;

        const reportDTOData = {
            reportedTime,
            taskCollaborator: {
                projCollaborator: {
                    collaborator: {
                        email: taskCollabEmail
                    }
                }
            }
        };

        console.log(reportDTOData);

        this.AuthService.fetch(`/projects/${this.props.match.params.projectID}/tasks/${this.props.match.params.taskID}/reports/
                ${this.props.match.params.reportId}/update/`,
            {
                body: JSON.stringify(reportDTOData),
                method: "PUT"
            }
        )
            .then(function (myJson) {
                console.log(myJson);
            });
    };

    render() {
        return (
            <div className=" table-striped">
                <h3>
                    <b>Update Report</b>
                </h3>
                <form onSubmit={this.handleSubmit}>
                    <FormGroup controlId="reportId" bsSize="large">
                        <ControlLabel>Type Report ID</ControlLabel>
                        <FormControl
                            autoFocus
                            type="text"
                            value={this.state.reportId}
                            onChange={this.handleChange}
                        />
                    </FormGroup>

                    <FormGroup controlId="projectId" bsSize="large">
                        <ControlLabel>Type Project ID</ControlLabel>
                        <FormControl
                            autoFocus
                            type="text"
                            value={this.state.projectId}
                            onChange={this.handleChange}
                        />
                    </FormGroup>

                    <FormGroup controlId="taskID" bsSize="large">
                        <ControlLabel>Type Task ID</ControlLabel>
                        <FormControl
                            autoFocus
                            type="text"
                            value={this.state.taskID}
                            onChange={this.handleChange}
                        />
                    </FormGroup>

                    <FormGroup controlId="reportedTime" bsSize="large">
                        <ControlLabel>Type reported time</ControlLabel>
                        <FormControl
                            autoFocus
                            type="text"
                            value={this.state.reportedTime}
                            onChange={this.handleChange}
                        />
                    </FormGroup>

                    <FormGroup controlId="taskCollabEmail" bsSize="large">
                        <ControlLabel>
                            Type task collaborator email address
                        </ControlLabel>
                        <FormControl
                            autoFocus
                            type="text"
                            value={this.state.taskCollabEmail}
                            onChange={this.handleChange}
                        />
                    </FormGroup>

                    <button
                        className="buttonUpdate" /*onClick={this.userDetail}*/
                    >
                        Update Report
                    </button>
                </form>
            </div>
        );
    }
}

export default UpdateReport;
