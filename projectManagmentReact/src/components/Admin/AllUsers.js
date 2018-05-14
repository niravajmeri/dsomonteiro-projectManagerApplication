import React, { Component } from 'react';
import './AllUsers.css';

class AllUsers extends Component {

    constructor(props) {
        super(props);
        this.state = {
            users: []
        }
    }

      componentDidMount() {
        this.loadUsersFromServer();
      }
      
      // Load users from database
      loadUsersFromServer() {
          fetch('users/allUsers',{ method: 'get'}) 
          .then((response) => response.json()) 
          .then((responseData) => { 
              this.setState({ 
                  users: responseData, 
              }); 
          });     
      }


    renderUsers(){
        return this.state.users.map((userItem) =>{
            return(
                <div>
                <p>{userItem.userID}</p>
                <p>{userItem.name}</p>
                <p>{userItem.email}</p>
                <p>{userItem.function}</p>
                <p>{userItem.userProfile}</p>
                <hr/>
                </div>
            )
        })
    }

    render() {
        return (
            <div className="App">
                <p className="App-intro">
                    Here follows:
                </p>                
                <h3>List of All Users</h3>   
                {this.renderUsers()}                        
            </div>
        );
    }
}

export default AllUsers;