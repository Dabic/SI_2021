import React from 'react';
import styles from './Toolbar.module.css';
import {DPalette} from "../Palette/DPalette";
import ClassEditor from "../ClassEditor/ClassEditor";

const Toolbar = (props) => {
    let content;

    if (props.type === 'use-case') {
        content = (
            <div className={styles.Toolbar}>
                <div className={styles.ModelName}>
                    {localStorage.getItem('modelName')}
                </div>
                <DPalette
                    setLinkState={props.setLinkState}
                    setNodeState={props.setNodeState}
                    type={props.type}
                    saveModel={props.saveModel}/>
            </div>
        )
    } else {
        content = (
            <div className={styles.Toolbar}>
                <div className={styles.ModelName}>
                    {localStorage.getItem('modelName')}
                </div>
                <DPalette
                    setLinkState={props.setLinkState}
                    setNodeState={props.setNodeState}
                    type={props.type}
                    saveModel={props.saveModel}/>
                <ClassEditor nodeArray={props.nodeArray}
                             updateFieldsForSelected={props.updateFieldsForSelected}
                             fields={props.fields}
                             fieldCount={props.fieldCount}
                             incrementFieldCount={props.incrementFieldCount}
                             decrementFieldCount={props.decrementFieldCount}/>
            </div>
        )
    }
    return (
        <div>
            {content}
        </div>
    );
};

export default Toolbar;