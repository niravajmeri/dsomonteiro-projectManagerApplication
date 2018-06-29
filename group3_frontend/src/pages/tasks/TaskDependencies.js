import React, { Component } from 'react';
import {connect} from "react-redux";
import AccordionMenu from '../../components/accordianMenuTasks/AccordionMenuTasks.jsx';
import LoadingComponent from './../../components/loading/LoadingComponent';
import {bindActionCreators} from "redux";
import {getAllTaskDependencies} from "../../actions/projectTasksActions";
import Error from "../../components/error/error";
import AddDependency from "./AddDependency";
import AuthService from "../loginPage/AuthService";
import RemoveDependency from "./RemoveDependency";


class TaskDependencies extends Component {
    constructor(props) {
        super(props);

        this.state = {
            projectManager: false
        }


        this.authService = new AuthService();
    }

    // after mounting the component, an action is dispatched to fetch all dependencies of the chosen task
    // as well as confirmation of the logged in user's permissions in the project
    componentDidMount() {
        this.props.getAllTaskDependencies(this.props.match.params.projectID, this.props.match.params.taskID);

        this.isProjectManager();

    }

    // this method fetches the selected task and compares its project manager against the logged in user
    isProjectManager() {
        this.authService.fetch(`/projects/${this.props.match.params.projectID}/tasks/${this.props.match.params.taskID}`,
            { method: 'GET' }
        ).then(response => {
            console.log(response);
            this.setState({
                task: response,
                projectManager: response.project.projectManager.email === this.authService.getProfile().sub
            });

        }).catch(error => {
            console.log(error);
            this.setState({
                projectManager: false
            });

        });
    }

    // when the logged in user is the project manager, this method renders both buttons to add and remove dependency
    getManagerOptions() {
        if(this.state.projectManager) {
            return (
            <div align="right">
                <AddDependency projectID={this.props.match.params.projectID} taskID={this.props.match.params.taskID} />
                {' '}
                <RemoveDependency projectID={this.props.match.params.projectID} taskID={this.props.match.params.taskID} />
            </div>);
        } else {
            return <div align="right"></div>;
        }
    }

    renderTasks = () => {
        if (this.props.tasksLoading) {
            return <LoadingComponent />;
        } else if (this.props.error) {
            return <Error message={this.props.error} />;
        } else if (this.props.tasks != null) {
            return <AccordionMenu list={this.props.tasks} />;
        }

    };

    render() {

        console.log(this.state.projectManager);

        return (
            <div>
                {this.getManagerOptions()}

                {this.renderTasks()}
            </div>
        );
    }

}
const mapStateToProps = state => {
    return {
        tasks: state.tasks.tasksDependencies,
        tasksLoading: state.tasks.itemIsLoading,
        error: state.tasks.error
    };
};
const mapDispatchToProps = dispatch =>
    bindActionCreators(
        { getAllTaskDependencies }, dispatch
    );
export default connect(
    mapStateToProps,
    mapDispatchToProps
)(TaskDependencies);

