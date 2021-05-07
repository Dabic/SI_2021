import React, {Component} from 'react';
import styles from './ClassIcon.module.css';

interface ClassIconProps {
    setNodeState: (state: string) => void;
    objClass: string
}
class ClassIcon extends Component<ClassIconProps, {}> {
    render() {
        let classes = [styles.ClassIcon];
        switch (this.props.objClass) {
            case 'class':
                classes.push(styles.class);
                break;
            case 'interface':
                classes.push(styles.interface);
                break;
            case 'enum':
                classes.push(styles.enum);
                break;
            case 'actor':
                classes.push(styles.actor);
                break;
            case 'story':
                classes.push(styles.story);
                break;
        }
        return (
            <div className={classes.join(' ')} onClick={() => this.props.setNodeState(this.props.objClass)}>
                {this.props.objClass[0].toUpperCase()}
            </div>
        );
    }
}

export default ClassIcon;