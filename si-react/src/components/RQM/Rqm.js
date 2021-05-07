import React, {useEffect, useState} from 'react';
import {Type} from "react-bootstrap-table2-editor";
import cellEditFactory from 'react-bootstrap-table2-editor';
// @ts-ignore
// @ts-ignore
import BootstrapTable from 'react-bootstrap-table-next';
import styles from './Rqm.module.css';
import {RqmRow} from "../model/RqmRow";
import RqmToolbar from "./RqmToolbar/RqmToolbar";
import axios from "../axios/brokerAxios";
import {withRouter} from 'react-router-dom';

const Rqm = (props) => {

    const [data, setData] = useState([]);
    const [columns, setColumns] = useState([
        {
            dataField: 'rqmId',
            text: 'Id',
            headerStyle: (column, colIndex) => {
                return {width: '80px', textAlign: 'center'};
            },
            align: function callback(cell, row, rowIndex, colIndex) { return 'center' }
        }, {
            dataField: 'header',
            text: 'Header',
            headerStyle: (column, colIndex) => {
                return {width: '350px', textAlign: 'center'};
            }
        }, {
            dataField: 'description',
            text: 'Desc'
        }, {
            dataField: 'type',
            text: 'Type',
            headerStyle: (column, colIndex) => {
                return {width: '140px', textAlign: 'center'};
            },
            align: function callback(cell, row, rowIndex, colIndex) { return 'center' },
            editor: {
                type: Type.SELECT,
                options: [{
                    value: 'Functional',
                    label: 'Functional'
                }, {
                    value: 'Non-functional',
                    label: 'Non-functional'
                }]
            }
        }, {
            dataField: 'priority',
            text: 'Priority',
            headerStyle: (column, colIndex) => {
                return {width: '90px', textAlign: 'center'};
            },
            align: function callback(cell, row, rowIndex, colIndex) { return 'center' },
            editor: {
                type: Type.SELECT,
                options: [{
                    value: '1',
                    label: '1'
                }, {
                    value: '2',
                    label: '2'
                }, {
                    value: '3',
                    label: '3'
                }, {
                    value: '4',
                    label: '4'
                }, {
                    value: '5',
                    label: '5'
                }]
            }
        }, {
            dataField: 'risk',
            text: 'Risk',
            headerStyle: (column, colIndex) => {
                return {width: '90px', textAlign: 'center'};
            },
            align: function callback(cell, row, rowIndex, colIndex) { return 'center' },
            editor: {
                type: Type.SELECT,
                options: [{
                    value: 'L',
                    label: 'L'
                }, {
                    value: 'M',
                    label: 'M'
                }, {
                    value: 'H',
                    label: 'H'
                }]
            }
        },
        {
            dataField: 'status',
            text: 'Status',
            headerStyle: (column, colIndex) => {
                return {width: '150px', textAlign: 'center'};
            },
            align: function callback(cell, row, rowIndex, colIndex) { return 'center' },
            editor: {
                type: Type.SELECT,
                options: [{
                    value: 'Draft',
                    label: 'Draft'
                }, {
                    value: 'Defined',
                    label: 'Defined'
                }, {
                    value: 'Verified',
                    label: 'Verified'
                }, {
                    value: 'To be reviewed',
                    label: 'To be reviewed'
                }, {
                    value: 'Approved',
                    label: 'Approved'
                }]
            }
        }
    ]);
    const [selected, setSelected] = useState([]);

    useEffect(() => {
        fetchData();
    }, [])

    let listen;
    useEffect(() => {
        listen = props.history.listen((location, action) => {
            if (location && location.pathname.split("/")[2] === 'rqm') {
                fetchData();
            }
        });
        return () => {
            listen();
        }
    }, [props.history]);

    const fetchData = () => {
        axios.get(`/${localStorage.getItem('modelType')}/read?id=${localStorage.getItem('modelId')}`).then((res) => {
            setData(res.data.rqmRows);
        }).catch(err => console.log(err))
    }

    const save = () => {
        const rqmDiagram = {
            id: localStorage.getItem("modelId"),
            name: localStorage.getItem("modelName"),
            teamName: localStorage.getItem("teamName"),
            rqmRows: data
        }
        axios.post(`/${localStorage.getItem('modelType')}/save`, rqmDiagram)
            .then(res => {
                localStorage.setItem("modelId", res.data.id);
                setData(res.data.rqmRows);
                window.location.reload();
            }).catch(err => {

        });
    }

    const selectRow = {
        mode: 'radio',
        selected: selected,
        onSelect: (row, isSelect) => {
            console.log(row)
            if (isSelect) {
                setSelected([row.rqmId]);
            } else {
                setSelected([])
            }
        }
    }

    const addNewMainRow = () => {
        const newData = [...data];
        newData.push( new RqmRow(getMainRqmIdForNewRow(), '', '', 'Functional', 1, 'L', 'Draft'));
        setData(newData);
    }

    const getMainRqmIdForNewRow = () => {
        if (data.length === 0) {
            return 1;
        } else {
            const lastRow = data[data.length - 1];
            return getMainRqmIdNumber(lastRow.rqmId) + 1;
        }
    };

    const deleteRow = () => {
        const newData = data.filter(row => row.rqmId !== selected[0]);
        newData.forEach(row => {
            if (row.rqmId > selected[0]) {
                row.rqmId = row.rqmId - 1;
            }
        })
        setData(newData);
        setSelected([]);
    }
    const indentRight = () => {

    }

    const getMainRqmIdNumber = (id) => {
        console.log(id)
        const ids = id.toString().split(".");
        return parseInt(ids[ids.length - 1]);
    }

    return (
        <div className={styles.Rqm}>
            <RqmToolbar addRow={addNewMainRow} indentRight={indentRight} deleteRow={deleteRow} save={save}/>
            {/* eslint-disable-next-line react/jsx-no-undef */}
            <BootstrapTable keyField='rqmId' data={data} columns={columns} selectRow={selectRow} cellEdit={cellEditFactory({
                mode: 'click',
                blurToSave: true,
                afterSaveCell: () => {}
            })}/>
        </div>
    );
};

export default withRouter(Rqm);