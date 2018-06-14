import React, { Component } from "react";
import Moment from 'react-moment';
import Error from './../../components/error/error';
import MarkTaskAsFinished from "./MarkTaskAsFinished";
import CreateReport from "../reports/CreateReport";
import Reports from "../reports/Reports"
import { Link } from "react-router-dom";
import MediumButton from './../../components/button/mediumButton';
import FetchTaskButton from '../tasks/FetchTaskButton'
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import AuthService from './../loginPage/AuthService';
import AccordionMenu from '../../components/accordianMenuTasks/AccordionMenuTasks.jsx'
import { handleTaskHeaders } from '../../components/utils/handleList'
import { updateAllUsers, updateEmail, updateCollaborators, updateDirector, updateAdministrator } from './../../actions/UserActions';



class ProjectTasks extends Component {
    constructor(props) {
        super(props);
        this.match;
        this.state = {
            users: [],
            externalData: null
        };
        this.AuthService = new AuthService()
    }

    //TODO: Add sort by ascending or descending order to these tables


    renderUsers() {

        switch (this.props.filter) {
            case ("all"):
                return (<AccordionMenuUsers list={this.props.allUsers} />);
            case ("email"):
                return (<AccordionMenuUsers list={this.props.emailUsers} />);
            case ("collaborators"):
                return (<AccordionMenuUsers list={this.props.allCollaborators} />);
            case ("directors"):
                return (<AccordionMenuUsers list={this.props.allDirector} />);
            case ("administrators"):
                return (<AccordionMenuUsers list={this.props.allAdministrator} />);
        }
    }


    render() {

        if (this.state.message != null) {
            return (<Error message={this.state.message} />)
        }
        else {
            return (
                <div>
                    <FetchTaskButton/>
                    {this.renderUsers()}
                </div>
            )
        }
    }
}

const mapStateToProps = state => { return ({ filter: state.UserReducer.filterType, allAdministrator: state.user.allAdministrator, allDirector: state.user.allDirector, allCollaborators: state.user.allCollaborators, emailUsers: state.user.emailUsers, allUsers: state.user.allUsers }) }

export default connect(mapStateToProps, null)(User);

