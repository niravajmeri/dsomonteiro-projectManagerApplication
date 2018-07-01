import React, { Component } from 'react';
import AuthService from "../../pages/loginPage/AuthService";
import { Modal } from 'react-bootstrap';
import { toastr } from 'react-redux-toastr';
import './uploadButton.css'

class UploadUsersFile extends Component {
    constructor(props) {
        super(props);
        this.state = {
            show: false,
            file: null
        };
        this.AuthService = new AuthService();
    }

    handleClose() {
        this.setState({ show: false });
    }

    handleShow() {
        this.setState({ show: true });
    }


    uploadAction() {
        if (this.isFileExtensionValid()) {
            const formData = new FormData()
            formData.append('myFile', document.querySelector('input[type="file"]').files[0])
            this.AuthService.fetchWithoutContentType("users/uploadUserFile", {
                method: "POST",
                body: formData
            }).then(response => {
                if (response.status === 200) {
                    toastr.success('File uploaded successfully!')
                    this.handleClose()
                } else {
                    toastr.error('Unable to upload the file!')
                }
            }

            )
        }
    }

    getFileExtension() {
        var fileInput = document.getElementById('fileName');
        if (fileInput !== null && fileInput.files[0] !== undefined) {
            var filename = fileInput.files[0].name;
            return filename.slice((filename.lastIndexOf(".") - 1 >>> 0) + 2);
        } else {
            return null;
        }

    }

    isFileExtensionValid() {
        var extension = this.getFileExtension();
        if (extension !== null) {
            return extension.toLowerCase() === 'xml' || extension.toLowerCase() === 'csv';
        } else {
            return false;
        }
    }

    handleChange() {
        this.setState({
            file: document.getElementById('fileName')
        })
    }

    renderUploadMenu() {
        return (
            <div>
                <Modal.Header closeButton>
                    <Modal.Title>Upload Users From File</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <input type="file" id="fileName" name="fileName" autoComplete="off" accept=".xml, .csv" onChange={this.handleChange.bind(this)} />
                </Modal.Body>
                <Modal.Footer>
                    <button className="uploadButton" onClick={this.uploadAction.bind(this)} disabled={!this.isFileExtensionValid()}>Upload!</button>
                </Modal.Footer>
            </div>
        )
    }

    render() {
        return (
            <div>
                <button className="uploadButton" onClick={this.handleShow.bind(this)}>Upload Users</button>
                <Modal show={this.state.show} onHide={this.handleClose.bind(this)}>
                    {this.renderUploadMenu()}
                </Modal>
            </div>
        )
    }

}
export default UploadUsersFile;