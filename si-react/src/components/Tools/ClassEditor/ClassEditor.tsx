import React, {useEffect, useState} from 'react';
import {NodeField} from "../../model/NodeField";
// @ts-ignore
import cellEditFactory from 'react-bootstrap-table2-editor';
// @ts-ignore
import {Type} from 'react-bootstrap-table2-editor';
// @ts-ignore
import BootstrapTable from 'react-bootstrap-table-next';

import styles from './ClassEditor.module.css';

const ClassEditor = (props: any) => {

    const [data, setData] = useState([]);
    const [selected, setSelected] = useState([]);
    const [columns, setColumns] = useState([{
        dataField: 'text',
        text: 'Name'
    }, {
        dataField: 'info',
        text: 'Type',
        editor: {
            type: Type.SELECT,
            options: [{
                value: 'number',
                label: 'number'
            }, {
                value: 'string',
                label: 'string'
            }]
        }

    }, {
        dataField: 'type',
        text: 'Encapsulation',
        editor: {
            type: Type.SELECT,
            options: [{
                value: 'public',
                label: 'public'
            }, {
                value: 'private',
                label: 'private'
            }, {
                value: 'protected',
                label: 'protected'
            }]
        }
    }])
    useEffect(() => {
        if (props.fields)
            setData(props.fields.map((el: any) => Object.assign({}, el)));
    }, [props.fields])

    useEffect(() => {
        if (props.nodeArray) {
            let newTypeCombo = props.nodeArray.map((el: any) => createTypeOption(el.text));
            let uniqueCombo: any[] = [];
            uniqueCombo.push(createTypeOption('int'));
            uniqueCombo.push(createTypeOption('string'));
            newTypeCombo.forEach((el: any) => {
                if (uniqueCombo.filter(uel => {
                    return uel.value === el.value
                }).length === 0) {
                    uniqueCombo.push(el);
                }
            })
            let oldState = [...columns];
            oldState.forEach(el => {
                if (el.dataField === 'info' && el.editor) {
                    el.editor.options = uniqueCombo;
                }
            });
            setColumns(oldState);
        }
    }, [props.nodeArray])

    const createTypeOption = (name: string) => {
        return {
            value: name,
            label: name
        }
    }

    const addNewRow = () => {
        const newData = [...data];
        // @ts-ignore
        newData.push(new NodeField(props.fieldCount, 'temp', 'string', 'private'));
        props.incrementFieldCount();
        setData(newData);
        props.updateFieldsForSelected(newData.map((el: any) => new NodeField(el.nodeField, el.text, el.info, el.type)))
    }

    const deleteRow = () => {
        if (selected.length === 0) {
            return
        }
        let newData = [...data];
        // @ts-ignore
        newData = newData.filter(el => el.nodeField !== selected[0])
        props.decrementFieldCount();
        setData(newData);
        setSelected([])
        props.updateFieldsForSelected(newData.map((el: any) => new NodeField(el.nodeField, el.text, el.info, el.type)))
    }

    const afterSave = () => {
        props.updateFieldsForSelected(data.map((el: any) => new NodeField(el.nodeField, el.text, el.info, el.type)))
    }

    const selectRow = {
        mode: 'radio',
        selected: selected,
        onSelect: (row: any, isSelect: boolean) => {
            if (isSelect) {
                // @ts-ignore
                setSelected([row.nodeField]);
            }
        }
    }
    return (
        <div className={styles.ClassEditor}>
            <BootstrapTable keyField='nodeField' data={data} columns={columns} cellEdit={cellEditFactory({
                mode: 'click',
                blurToSave: true,
                afterSaveCell: () => afterSave()
            })} selectRow={selectRow}/>
            <div className={styles.Buttons}>
                <button className={styles.Add} onClick={addNewRow}>Add</button>
                <button className={styles.Delete} onClick={deleteRow}>Delete</button>
            </div>
        </div>
    );
};

export default ClassEditor;