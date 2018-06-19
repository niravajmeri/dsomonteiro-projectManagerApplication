import React, { Component } from 'react';
import {
    FormGroup,
    ControlLabel,
    Button,
    Checkbox
} from 'react-bootstrap';
import AuthService from "../loginPage/AuthService";
import {toastr} from "react-redux-toastr";

class SelectCalculationMethods extends Component {
    constructor(props) {
        super(props);
        this.state = {
            selectedMethods: this.props.project.projectAvaliableCalculationMethods.split(','),
        }

        this.handleChange = this.handleChange.bind(this);
        this.AuthService = new AuthService();
    }

    validateForm() {
        return this.state.selectedMethods.length;
    }

    validateArray(calculationMethod) {
        return calculationMethod === "CI" || calculationMethod === "CF" || calculationMethod === "CM"
    }

    handleChange(event) {
        var selectedMethods = this.state.selectedMethods;

        if(selectedMethods.includes(event.target.value)) {
            var index = selectedMethods.indexOf(event.target.value)
            selectedMethods.splice(index, 1);
        } else {
            selectedMethods.push(event.target.value);
        }
        this.setState({
            selectedMethods: selectedMethods
        })
    }

    handleSubmit = event => {
        event.preventDefault();


        const selectedMethods = this.state.selectedMethods;

        selectedMethods.filter(this.validateArray);

        const projectDTO = {
            availableCalculationMethods: selectedMethods.join(','),
            calculationMethod: selectedMethods[0]
        }

        console.log(projectDTO);

        this.AuthService.fetch(`/projects/${this.props.project.projectId}`, {
            body: JSON.stringify(projectDTO),
            method: 'patch'
        })
            .then(responseData => {
                toastr.success('Available Calculation Methods Changed!');
                setTimeout(function() {
                    window.location.href = '/activeprojects';
                }, 1000);
            })
            .catch(err => {
                toastr.error('An error occurred!');
            });

    }

    render() {

        return (<div>
            <p>This is a test</p>
            <p>"Project ID: " {this.props.project.projectId}</p>
            <p>"Project Cost Calculation Methods: "  {this.props.project.projectAvaliableCalculationMethods}</p>


            <form onSubmit>

                <FormGroup controlId="selectedMethods">
                    <ControlLabel className="formTitle"><b>Available Calculation Methods</b></ControlLabel>

                    <Checkbox value="CI" checked={this.state.selectedMethods.includes("CI")} onChange={this.handleChange}>
                        Cost Initial
                    </Checkbox>
                    <Checkbox value="CF" checked={this.state.selectedMethods.includes("CF")} onChange={this.handleChange}>
                        Cost Final
                    </Checkbox>{' '}
                    <Checkbox value="CM" checked={this.state.selectedMethods.includes("CM")} onChange={this.handleChange}>
                        Cost Average
                    </Checkbox>

                </FormGroup>


                <Button block className="btn btn-primary" disabled={!this.validateForm()} type="submit" >
                    Update
                </Button>
            </form>
        </div>);
    }
}
export default SelectCalculationMethods;