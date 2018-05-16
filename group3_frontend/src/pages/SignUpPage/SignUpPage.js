import React, { Component } from "react";
import "./SignUpPage.css";
import {
    Button,
    FormGroup,
    FormControl,
    ControlLabel,
    Checkbox,
    Alert
} from "react-bootstrap";
import { NavLink } from "react-router-dom";

const SMSVALIDATION = "smsValidation";
const EMAILVALIDATION = "emailValidation";

class signUpPage extends Component {
    constructor(props) {
        super(props);
        this.state = {
            name: "",
            email: "",
            userFunction: "",
            phone: "",
            password: "",
            securityQuestion: "",
            securityAnswer: "",
            street: "",
            zipCode: "",
            city: "",
            district: "",
            country: "",
            idNumber: "",
            verificationCode: "",
            hideWrongCode: "hide-code",
            signupStep: 1,
            aceptTerms: false
        };
    }

    validateForm() {
        return (
            this.state.name.length > 0 &&
            this.state.email.length > 0 &&
            this.state.userFunction.length > 0 &&
            this.state.phone.length > 0 &&
            this.state.password.length > 0 &&
            this.state.securityQuestion.length > 0 &&
            this.state.securityAnswer.length > 0 &&
            this.state.street.length > 0 &&
            this.state.zipCode.length > 0 &&
            this.state.city.length > 0 &&
            this.state.district.length > 0 &&
            this.state.country.length > 0 &&
            this.state.idNumber.length > 0 &&
            this.state.aceptTerms
        );
    }

    handleChange = event => {
        this.setState({
            [event.target.id]: event.target.value
        });
    };

    handleChangeCheckboxTerms = event => {
        this.setState({
            aceptTerms: event.target.checked
        });
    };

    handleSubmit = event => {
        event.preventDefault();

        const {
            name,
            email,
            userFunction,
            phone,
            password,
            securityQuestion,
            securityAnswer,
            street,
            zipCode,
            city,
            district,
            country,
            idNumber
        } = this.state;

        const signUpData = {
            name,
            email,
            function: userFunction,
            phone,
            password,
            question: securityQuestion,
            answer: securityAnswer,
            street,
            zipCode,
            city,
            district,
            country,
            idNumber
        };

        fetch("/account/register", {
            body: JSON.stringify(signUpData),
            headers: {
                "content-type": "application/json"
            },
            method: "POST"
        })
            .then(response => response.json())
            .then(jsonResponse => {
                jsonResponse.forEach(element => {
                    if (
                        element.rel === SMSVALIDATION ||
                        element.rel === EMAILVALIDATION
                    ) {
                        this.setState({
                            [element.rel]: element.href
                        });
                    }
                });
                this.setState({
                    signupStep: 2
                });
            });
    };

    stepOneForm = () => (
        <div className="SignUp">
            <form onSubmit={this.handleSubmit}>
                <FormGroup controlId="name">
                    <ControlLabel>Name</ControlLabel>
                    <FormControl
                        autoFocus
                        type="text"
                        value={this.state.name}
                        onChange={this.handleChange}
                    />
                </FormGroup>

                <FormGroup controlId="email">
                    <ControlLabel>Email</ControlLabel>
                    <FormControl
                        autoFocus
                        type="email"
                        value={this.state.email}
                        onChange={this.handleChange}
                    />
                </FormGroup>

                <FormGroup controlId="userFunction">
                    <ControlLabel>Function</ControlLabel>
                    <FormControl
                        autoFocus
                        type="text"
                        value={this.state.userFunction}
                        onChange={this.handleChange}
                    />
                </FormGroup>

                <FormGroup controlId="phone">
                    <ControlLabel>Phone</ControlLabel>
                    <FormControl
                        autoFocus
                        type="text"
                        value={this.state.phone}
                        onChange={this.handleChange}
                    />
                </FormGroup>

                <FormGroup controlId="street">
                    <ControlLabel>Street</ControlLabel>
                    <FormControl
                        autoFocus
                        type="text"
                        value={this.state.street}
                        onChange={this.handleChange}
                    />
                </FormGroup>

                <FormGroup controlId="zipCode">
                    <ControlLabel>Zip Code</ControlLabel>
                    <FormControl
                        autoFocus
                        type="text"
                        value={this.state.zipCode}
                        onChange={this.handleChange}
                    />
                </FormGroup>

                <FormGroup controlId="city">
                    <ControlLabel>City</ControlLabel>
                    <FormControl
                        autoFocus
                        type="text"
                        value={this.state.city}
                        onChange={this.handleChange}
                    />
                </FormGroup>

                <FormGroup controlId="district">
                    <ControlLabel>District</ControlLabel>
                    <FormControl
                        autoFocus
                        type="text"
                        value={this.state.district}
                        onChange={this.handleChange}
                    />
                </FormGroup>

                <FormGroup controlId="country">
                    <ControlLabel>Country</ControlLabel>
                    <FormControl
                        autoFocus
                        type="text"
                        value={this.state.country}
                        onChange={this.handleChange}
                    />
                </FormGroup>

                <FormGroup controlId="password">
                    <ControlLabel>Password</ControlLabel>
                    <FormControl
                        value={this.state.password}
                        onChange={this.handleChange}
                        type="password"
                    />
                </FormGroup>

                <FormGroup controlId="securityQuestion">
                    <ControlLabel>Security Question</ControlLabel>
                    <FormControl
                        value={this.state.securityQuestion}
                        onChange={this.handleChange}
                        componentClass="select"
                        placeholder="select"
                    >
                        <option value="" disabled selected>
                            Select your option
                        </option>
                        <option value="pet">
                            What is the name of your first pet?
                        </option>
                        <option value="school">
                            What elementary school did you attend?
                        </option>
                        <option value="honeymoon">
                            Where did you go for your honeymoon?
                        </option>
                    </FormControl>
                </FormGroup>

                <FormGroup controlId="securityAnswer">
                    <ControlLabel>Security Answer</ControlLabel>
                    <FormControl
                        autoFocus
                        type="text"
                        value={this.state.securityAnswer}
                        onChange={this.handleChange}
                    />
                </FormGroup>

                <FormGroup controlId="idNumber">
                    <ControlLabel>ID Number</ControlLabel>
                    <FormControl
                        autoFocus
                        type="text"
                        value={this.state.idNumber}
                        onChange={this.handleChange}
                    />
                </FormGroup>

                <div className="termsAndConditions">
                    By using this application, you agree to be bound by, and to
                    comply with these Terms and Conditions. To proceed with
                    registration you must accept access conditions.
                </div>

                <Checkbox
                    className="termsCheckbox"
                    onChange={this.handleChangeCheckboxTerms}
                >
                    I agree to the Terms and Conditions.
                </Checkbox>

                <Button
                    block
                    disabled={!this.validateForm() || this.codeCheckbox}
                    type="submit"
                >
                    Sign Up
                </Button>
            </form>
        </div>
    );

    handleSignUpValidation = event => {
        const validationType = event.target.id;

        if (
            validationType === SMSVALIDATION ||
            validationType === EMAILVALIDATION
        ) {
            fetch(this.state[validationType])
                .then(response => {
                    return response.json();
                })
                .then(jsonResponse => {
                    this.setState({
                        signupStep: 3,
                        verificationUrl: jsonResponse.href
                    });
                });
        }
    };

    stepTwoValidation = (title, i) => (
        <div>
            <div className="chooseValidationMethodText">
                {" "}
                To continue with sign up process, you will receive a validation
                code by SMS or email. Choose the method:
            </div>
            <Button
                bsStyle="primary"
                id="smsValidation"
                onClick={this.handleSignUpValidation}
            >
                SMS
            </Button>
            <Button
                bsStyle="primary"
                id="emailValidation"
                onClick={this.handleSignUpValidation}
            >
                Email
            </Button>
        </div>
    );

    handleCodeSubmit = event => {
        event.preventDefault();
        const verificationCode = this.state.verificationCode;

        fetch(this.state.verificationUrl, {
            body: JSON.stringify({ codeToCheck: verificationCode }),
            headers: {
                "content-type": "application/json"
            },
            method: "POST"
        })
            .then(response => response.json())
            .then(jsonResponse => {
                this.setState({
                    signupStep: 4
                });
            })
            .catch(error => {
                this.setState({
                    hideWrongCode: ""
                });
                console.log("Verification code is incorrect. Try again");
            });
    };

    validateCodeForm() {
        return this.state.verificationCode.length > 0;
    }

    stepThreeForm = () => (
        <div className="validationCodeForm">
            <form onSubmit={this.handleCodeSubmit}>
                <FormGroup controlId="verificationCode">
                    <ControlLabel>Verification code</ControlLabel>
                    <FormControl
                        autoFocus
                        type="text"
                        value={this.state.verificationCode}
                        onChange={this.handleChange}
                    />
                </FormGroup>
                <Alert bsStyle="danger" className={this.state.hideWrongCode}>
                    <strong>Invalid validation code!</strong> Try again.
                </Alert>
                <Button block disabled={!this.validateCodeForm} type="submit">
                    Send validation code
                </Button>
            </form>
        </div>
    );

    stepFourConfirmation = () => (
        <div>
            <div>Registration completed successfully!</div>
            <div className="LogIn">
                <NavLink className="btn btn-primary btn-lg" to="/login">
                    Log In
                </NavLink>
            </div>
        </div>
    );

    render() {
        let formStp = this.stepOneForm();
        if (this.state.signupStep === 2) {
            formStp = this.stepTwoValidation();
        } else if (this.state.signupStep === 3) {
            formStp = this.stepThreeForm();
        } else if (this.state.signupStep === 4) {
            formStp = this.stepFourConfirmation();
        }
        return <div>{formStp}</div>;
    }
}

export default signUpPage;