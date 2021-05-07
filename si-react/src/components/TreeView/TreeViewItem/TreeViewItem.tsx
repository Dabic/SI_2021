import React, {Component} from 'react';
import styles from './TreeViewItem.module.css';
// @ts-ignore
import {Link} from 'react-router-dom';

interface TreeViewItemProps {
    type: string;
    text: string;
    modelType: string;
    id: number;
}
class TreeViewItem extends Component<TreeViewItemProps, {}> {
    classes = [styles.TreeViewItem];

    constructor(props: TreeViewItemProps) {
        super(props);
        this.setStateForModel = this.setStateForModel.bind(this);
    }

    private setStateForModel() {
        localStorage.setItem('modelType', this.props.modelType);
        localStorage.setItem('modelId', this.props.id.toString());
        localStorage.setItem('modelName', this.props.text);

    }
    render() {
        if (this.props.type === 'team') {
            this.classes.push(styles.team);
        } else {
            this.classes.push(styles.model);
        }
        return (
            <Link to={`/dashboard/${this.props.modelType}/${this.props.id}`} onClick={this.setStateForModel} className={this.classes.join(' ')}>
                - {this.props.text}
            </Link>
        );
    }
}

export default TreeViewItem;