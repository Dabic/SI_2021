import React from 'react';
import RqmToolbarItem from "./RqmToolbarItem/RqmToolbarItem";
import styles from './RqmToolbar.module.css';

const RqmToolbar = (props) => {
    return (
        <div className={styles.RqmToolbar}>
            <RqmToolbarItem action={props.addRow} type={'add-row'}/>
            <RqmToolbarItem action={props.deleteRow} type={'remove-row'}/>
            <RqmToolbarItem action={props.indentRight} type={'indent-right'}/>
            <RqmToolbarItem type={'indent-left'}/>
            <RqmToolbarItem action={props.save} type={'save'}/>
        </div>
    );
};

export default RqmToolbar;