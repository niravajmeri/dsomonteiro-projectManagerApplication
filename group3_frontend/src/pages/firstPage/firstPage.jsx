import React, { Component } from "react";
import "./firstPage.css";
import { NavLink } from "react-router-dom";
import background from "./background.jpg"
import logo from "./top_banner.png";
import brand from "./brand.svg";

class firstPage extends Component {
    state = {};
    render() {
        return (
            <div>
                <div className="Bar">
                    <div className="Logo">
                        <center> <h1 className="switch">appManager</h1> </center>
                        <center> <p className="subTitle">Project Management Application</p> </center>
                    </div>

                    <div className="ButtonsStartPage">

                    <table className="tableButtom">
                        <tr className="spacer" >
                        <th className="spacer">
                        <NavLink to="/signup">
                            <button type="button" class="btn btn-primary btn-lg">
                                Create account
                        </button>
                        </NavLink>
                    </th>
                        <th className="spacer"> 
                        <NavLink className="btn btn-primary btn-lg" to="/login">
                            Log In
                    </NavLink>
                </th>
                        </tr>
                    </table>
                    </div>


                    
                   
                </div>
                <div>
                    <img src={logo} alt="logo" className="image" href="/" />
                </div>
                <img src={brand} alt="brand" className="switch" href="/" />
            </div>



        );
    }
}

export default firstPage;
