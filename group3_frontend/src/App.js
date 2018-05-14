import React, { Component } from "react";
import "./App.css";
import NavBar from "./components/navBar/NavBar";
import SideBar from "./components/sideBar/SideBar";
import { Grid, Jumbotron, Button } from "react-bootstrap";
import { Route, Switch } from "react-router-dom";
import ProjectsPage from "./pages/projects/ProjectsPage";
import TasksPage from "./pages/tasks/TasksPage";
import MarkTaskAsFinished from "./pages/tasks/MarkTaskAsFinished";
import UsersPage from "./pages/users/UsersPage";
import LoginPage from "./pages/loginPage/LoginPage";
import SignUpPage from "./pages/signUpPage/SignUpPage";
import firstPage from "./pages/firstPage/firstPage";
import Footer from "./components/footer/footer";
import ProjectCostCalculation from "./pages/Cost/ProjectCostCalculation";
import ProjectCost from "./pages/Cost/ProjectCost";

class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isVisible: false
        };
    }

    toogleMenu = () => {
        this.setState({
            isVisible: !this.state.isVisible
        });
    };

    pages = () => (
        <div className="row">
            <SideBar isVisible={this.state.isVisible} />

            <div className="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
                <Switch>
                    <Route exact path="/" component={firstPage} />
                    <Route path="/projects" component={ProjectsPage} />
                    <Route path="/tasks" component={TasksPage} />
                    <Route path="/marktaskfinished" component={MarkTaskAsFinished} />
                    <Route path="/users" component={UsersPage} />
                    <Route path="/selectprojectcostcalculation" component={ProjectCostCalculation}/>
                    <Route path="/projectcost" component={ProjectCost} />
                </Switch>
            </div>
        </div>
    );

    render() {
        return (
            <div>
                <NavBar toogleMenu={this.toogleMenu} />
                <div className="container-fluid">
                    <Switch>
                        <Route exact path="/login" component={LoginPage} />
                        <Route exact path="/signup" component={SignUpPage} />
                        <Route component={this.pages} />
                    </Switch>
                </div>
                <Footer />
            </div>
        );
    }
}

export default App;
