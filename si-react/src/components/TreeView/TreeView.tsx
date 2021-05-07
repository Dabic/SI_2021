import React, {Component} from 'react';
import styles from './TreeView.module.css';
import TreeViewItem from "./TreeViewItem/TreeViewItem";
import axios from "../axios/brokerAxios";
import {ModelDTO} from "../model/ModelDTO";
import {AxiosResponse} from "axios";
import {produce} from 'immer';
import CreateModelModal from "./CreateModelModal/CreateModelModal";

interface TreeViewState {
    items: Array<ModelDTO>;
    showModal: boolean;
}
class TreeView extends Component<{}, TreeViewState> {
    componentDidMount() {
        axios.get<Array<ModelDTO>>("/resolver/read?teamName=" + localStorage.getItem('teamName'))
            .then((res: AxiosResponse<Array<ModelDTO>>) => {
                this.setState({
                    items: res.data,
                    showModal: false
                });
            });
    }

    constructor(props: any) {
        super(props);
        this.state = {
            items: [],
            showModal: false
        }
        this.createModel = this.createModel.bind(this);
        this.closeModal = this.closeModal.bind(this);

    }

    private createModel() {
        this.setState(
            produce((draft: TreeViewState) => {
                draft.showModal = true;
            })
        );
    }

    private closeModal() {
        this.setState(
            produce((draft: TreeViewState) => {
                draft.showModal = false;
            })
        );
    }

    render() {
        return (
            <div className={styles.TreeView}>
                <div className={styles.TreeViewItemContainer}>
                    {localStorage.getItem('teamName')}
                    {this.state.items.map((el, i) => <TreeViewItem key={i} id={el.id} type={'model'} modelType={el.type} text={el.name} />)}
                </div>
                <div className={styles.TreeViewCreateModel}>
                    <button onClick={this.createModel}>Add new model</button>
                </div>
                <CreateModelModal show={this.state.showModal} handleClose={this.closeModal} />
            </div>
        );
    }
}

export default TreeView;