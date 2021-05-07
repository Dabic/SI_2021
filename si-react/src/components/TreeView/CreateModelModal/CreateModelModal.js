import React, {useState} from 'react';
import {Button, Modal} from "react-bootstrap";
import axios from "../../axios/brokerAxios";
import {ClassDiagramModel} from "../../model/ClassDiagramModel";
const CreateModelModal = (props) => {

    const [modelData, setModelData] = useState({
        name: '',
        type: 'uml'
    });

    const handleInput = (event, type) => {
        const oldState = modelData;
        oldState[type] = event.target.value;
        setModelData(oldState);
    }

    const onCreate = () => {
        let clsDiagram = new ClassDiagramModel();
        clsDiagram.name = modelData.name;
        clsDiagram.teamName = localStorage.getItem('teamName');
        clsDiagram.modelType = modelData.type;
        clsDiagram.dlinks = [];
        clsDiagram.dnodes = [];
        console.log('halo')
        switch (modelData.type) {
            case "uml":
                axios.post('/uml/save', clsDiagram).then(res => {
                    console.log(res.data);
                    window.location.reload();
                });
                break;
            case "use-case":
                axios.post('/use-case/save', clsDiagram).then(res => {
                    console.log(res.data);
                    window.location.reload();
                });
                break;
            case "rqm":
                let rqmDiagram = {
                    name: modelData.name,
                    teamName: localStorage.getItem("teamName"),
                    rqmRows: []
                }
                axios.post('/rqm/save', rqmDiagram).then(res => {
                    console.log(res.data);
                    window.location.reload();
                });
                break;
            default:
                break;
        }
        props.handleClose();
    }
    return (
        <Modal show={props.show} onHide={props.handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>Create new model</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <div>
                    <select onInput={(event) => handleInput(event, 'type')}>
                        <option>uml</option>
                        <option>use-case</option>
                        <option>rqm</option>
                    </select>
                    <input type="text" placeholder="Model name..." onInput={(event) => handleInput(event, 'name')}/>
                </div>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={props.handleClose}>
                    Close
                </Button>
                <Button variant="primary" onClick={onCreate}>
                    Create
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default CreateModelModal;