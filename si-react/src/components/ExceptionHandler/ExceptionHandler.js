import React from 'react';
import {Button, Modal} from "react-bootstrap";

const ExceptionHandler = (props) => {
    return (
        <Modal show={props.show} onHide={props.handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>{props.header}</Modal.Title>
            </Modal.Header>
            <Modal.Body>{props.text}</Modal.Body>
            <Modal.Footer>
                <Button variant="primary" onClick={props.handleClose}>
                    OK
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default ExceptionHandler;