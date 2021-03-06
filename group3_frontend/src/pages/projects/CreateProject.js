import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import './CreateProject.css';
import {
    FormGroup,
    FormControl,
    ControlLabel,
    Button,
    Radio
} from 'react-bootstrap';
import AuthService from './../loginPage/AuthService';
import ListPossibleProjectManagers from './ListPossibleProjectManagers';
import { toastr } from 'react-redux-toastr';


class CreateProject extends Component {
    constructor(props) {
        super(props);
        this.state = {
            name: '',
            description: '',
            projectManager: { name: 'None Selected!' },
            budget: '',
            effortUnit: 'HOURS',
            awaitingResponse: false
        };
        this.AuthService = new AuthService();
        this.selectManager = this.selectManager.bind(this);
    }

    validateForm() {
        return (
            this.state.name.length > 0 &&
            this.state.description.length > 0 &&
            this.state.budget > 0 &&
            this.state.projectManager.email != null &&
            !this.state.awaitingResponse
        );
    }

    // This method handles changes for all text boxes: Name, description and budget
    handleChange = event => {
        this.setState({
            [event.target.id]: event.target.value
        });
    };

    handleEffortSelection = event => {
        this.setState({
            effortUnit: event.target.value
        });
    };

    // this method is passed on to the child component ListPossibleProjectManagers
    selectManager(event) {
        this.setState({
            projectManager: event
        });
    }

    handleSubmit = event => {
        event.preventDefault();

        const {
            name,
            description,
            projectManager,
            budget,
            effortUnit
        } = this.state;

        const projectDTO = {
            name,
            description,
            projectManager,
            budget,
            effortUnit
        };

        this.setState({
            awaitingResponse: true
        });

        this.AuthService.fetch(`/projects/`, {
            body: JSON.stringify(projectDTO),
            method: 'POST'
        })
            .then(responseData => {
                toastr.success('Project Created Successfully!');
                setTimeout(function () {
                    window.location.href = '/activeprojects';
                }, 1000);
            })
            .catch(err => {
                toastr.error('An error occurred!');
                this.setState({
                    awaitingResponse: false
                });
            });
    };

    render() {

        return (
            <div >
                <span className="pageTitle">
                    <b>Create new project</b>
                </span>
                <div className="createProjectContainer">
                    <form className="createProjectForm" onSubmit={this.handleSubmit}>
                        <FormGroup controlId="name" bsSize="large">
                            <ControlLabel className="formTitle">Project Name</ControlLabel>
                            <FormControl
                                className="textFormProject"
                                autoFocus
                                type="text"
                                value={this.state.name}
                                onChange={this.handleChange}
                            />
                        </FormGroup>

                        <FormGroup controlId="description" bsSize="large">
                            <ControlLabel className="formTitle">Description</ControlLabel>
                            <FormControl
                                className="textFormProject"
                                autoFocus
                                type="text"
                                value={this.state.description}
                                onChange={this.handleChange}
                            />
                        </FormGroup>

                        <FormGroup controlId="budget" bsSize="large">
                            <ControlLabel className="formTitle" >Budget / €</ControlLabel>
                            <FormControl
                                className="textFormProject"
                                autoFocus
                                type="number"
                                pattern="[0-9]*"
                                inputmode="numeric"
                                value={this.state.budget}
                                onChange={this.handleChange}
                            />
                        </FormGroup>

                        <FormGroup controlId="effortUnit">
                            <ControlLabel className="formTitle"><b>Effort Unit</b></ControlLabel>
                            <Radio value="HOURS" checked={this.state.effortUnit === "HOURS"}
                                onChange={this.handleEffortSelection}>
                                Hours
                        </Radio>{' '}
                            <Radio value="PM" checked={this.state.effortUnit === "PM"}
                                onChange={this.handleEffortSelection}>
                                Person/Month
                        </Radio>
                        </FormGroup>

                        <ControlLabel className="formTitle"> Project Manager ({this.state.projectManager.name})  </ControlLabel>
                        <br />
                        <ListPossibleProjectManagers onSelect={this.selectManager} />

                        <br />

                        <button block className="projectManagerButton" disabled={!this.validateForm()} type="submit" >
                            Create Project
                    </button>

                    </form>
                </div>
            </ div>
        );
    }


}

export default CreateProject;