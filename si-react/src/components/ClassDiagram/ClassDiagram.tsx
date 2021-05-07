import * as go from 'gojs';
import {produce} from 'immer';
import * as React from 'react';

import {ClassDiagramWrapper} from './ClassDiagramWrapper/ClassDiagramWrapper';

import {DNode} from "../model/DNode";
import {DLink} from "../model/DLink";
import Toolbar from "../Tools/Toolbar/Toolbar";
import {NodeField} from "../model/NodeField";
import axios from "../axios/brokerAxios";
import {ClassDiagramModel} from "../model/ClassDiagramModel";
import {AxiosResponse} from "axios";
import ExceptionHandler from "../ExceptionHandler/ExceptionHandler";
// @ts-ignore
import {withRouter} from 'react-router-dom';
import {ErrorModel} from "../model/ErrorModel";

interface ClassDiagramState {
    nodeDataArray: Array<go.ObjectData>;
    linkDataArray: Array<go.ObjectData>;
    modelData: go.ObjectData;
    selectedData: go.ObjectData | null;
    skipsDiagramUpdate: boolean;
    linkState: string;
    nodeState: string;
    fieldCount: number;
    showError: boolean;
    errorMsg: string;
    errorStatus: string;
}

interface Props {
    history: any;
}

class ClassDiagram extends React.Component<Props, ClassDiagramState> {
    // Maps to store key -> arr index for quick lookups
    private mapNodeKeyIdx: Map<go.Key, number>;
    private mapLinkKeyIdx: Map<go.Key, number>;
    private listen: any;

    componentDidMount() {
        this.fetchData();
        // @ts-ignore
        this.listen = this.props.history.listen((location, action) => {
            if (location && location.pathname.split("/")[2] === 'uml') {
                this.fetchData();
            }
        });
    }

    componentWillUnmount() {
        this.listen();
    }

    constructor(props: Props) {
        super(props);
        this.state = {
            nodeDataArray: [],
            linkDataArray: [],
            modelData: {
                canRelink: true
            },
            selectedData: null,
            skipsDiagramUpdate: false,
            linkState: 'OpenTriangle',
            nodeState: 'class',
            fieldCount: 0,
            showError: false,
            errorMsg: '',
            errorStatus: ''
        };
        // init maps
        this.mapNodeKeyIdx = new Map<go.Key, number>();
        this.mapLinkKeyIdx = new Map<go.Key, number>();
        this.refreshNodeIndex(this.state.nodeDataArray);
        this.refreshLinkIndex(this.state.linkDataArray);
        // bind handler methods
        this.handleDiagramEvent = this.handleDiagramEvent.bind(this);
        this.handleModelChange = this.handleModelChange.bind(this);
        this.setNodeState = this.setNodeState.bind(this);
        this.setLinkState = this.setLinkState.bind(this);
        this.updateFieldsForSelected = this.updateFieldsForSelected.bind(this);
        this.incrementFieldCount = this.incrementFieldCount.bind(this);
        this.decrementFieldCount = this.decrementFieldCount.bind(this);
        this.saveModel = this.saveModel.bind(this);
        this.handleOpenError = this.handleOpenError.bind(this);
        this.handleCloseError = this.handleCloseError.bind(this);
        this.fetchData = this.fetchData.bind(this);
    }

    private fetchData() {
        axios.get<ClassDiagramModel>(`/${localStorage.getItem('modelType')}/read?id=${localStorage.getItem('modelId')}`).then((res: AxiosResponse<ClassDiagramModel>) => {
            this.setState(produce((draft: ClassDiagramState) => {
                this.refreshNodeIndex(res.data.dnodes);
                this.refreshLinkIndex(res.data.dlinks);
                let fieldCount = 0;
                draft.nodeDataArray = [...res.data.dnodes.map((dnode: DNode) => {
                    let node = new DNode(dnode.key, dnode.type, dnode.loc, dnode.text);
                    node.id = dnode.id;
                    node.fields = dnode.fields.map(field => {
                        fieldCount++;
                        let nf = new NodeField(field.nodeField, field.text, field.info, field.type);
                        nf.id = field.id;
                        return nf;
                    });
                    return node;
                })];
                draft.linkDataArray = [...res.data.dlinks];
                draft.skipsDiagramUpdate = false;
                draft.fieldCount = fieldCount;
            }));
        }).catch(err => console.log(err))
    }
    /**
     * Update map of node keys to their index in the array.
     */
    private refreshNodeIndex(nodeArr: Array<go.ObjectData>) {
        this.mapNodeKeyIdx.clear();
        nodeArr.forEach((n: go.ObjectData, idx: number) => {
            this.mapNodeKeyIdx.set(n.key, idx);
        });
    }

    /**
     * Update map of link keys to their index in the array.
     */
    private refreshLinkIndex(linkArr: Array<go.ObjectData>) {
        this.mapLinkKeyIdx.clear();
        linkArr.forEach((l: go.ObjectData, idx: number) => {
            this.mapLinkKeyIdx.set(l.key, idx);
        });
    }

    public handleDiagramEvent(e: go.DiagramEvent) {
        const text = e.name;
        switch (text) {
            case 'ChangedSelection': {
                const sel = e.subject.first();
                this.setState(
                    produce((draft: ClassDiagramState) => {
                        if (sel) {
                            if (sel instanceof go.Node) {
                                const idx = this.mapNodeKeyIdx.get(sel.key);
                                if (idx !== undefined && idx >= 0) {
                                    const nd = draft.nodeDataArray[idx];
                                    draft.selectedData = nd;
                                }
                            } else if (sel instanceof go.Link) {
                                const idx = this.mapLinkKeyIdx.get(sel.key);
                                if (idx !== undefined && idx >= 0) {
                                    const ld = draft.linkDataArray[idx];
                                    draft.selectedData = ld;
                                }
                            }
                        } else {
                            draft.selectedData = null;
                        }
                    })
                );
                break;
            }
            case 'BackgroundDoubleClicked': {
                this.setState(
                    produce((draft: ClassDiagramState) => {
                        draft.nodeDataArray.push(new DNode(draft.nodeDataArray.length, draft.nodeState, '130 120', 'new shape'));
                        this.refreshNodeIndex(draft.nodeDataArray);
                        draft.skipsDiagramUpdate = false;
                    })
                );
                break;
            }
            default:
                break;
        }
    }

    public handleModelChange(obj: go.IncrementalData) {
        const insertedNodeKeys = obj.insertedNodeKeys;
        const modifiedNodeData = obj.modifiedNodeData;
        const removedNodeKeys = obj.removedNodeKeys;
        const insertedLinkKeys = obj.insertedLinkKeys;
        const modifiedLinkData = obj.modifiedLinkData;
        const removedLinkKeys = obj.removedLinkKeys;
        const modifiedModelData = obj.modelData;

        // maintain maps of modified data so insertions don't need slow lookups
        const modifiedNodeMap = new Map<go.Key, go.ObjectData>();
        const modifiedLinkMap = new Map<go.Key, go.ObjectData>();
        this.setState(
            produce((draft: ClassDiagramState) => {
                let narr = draft.nodeDataArray;
                if (modifiedNodeData) {
                    modifiedNodeData.forEach((nd: go.ObjectData) => {
                        modifiedNodeMap.set(nd.key, nd);
                        const idx = this.mapNodeKeyIdx.get(nd.key);
                        if (idx !== undefined && idx >= 0) {
                            narr[idx] = nd;
                            if (draft.selectedData && draft.selectedData.key === nd.key) {
                                draft.selectedData = nd;
                            }
                        }
                    });
                }

                if (insertedNodeKeys) {
                    insertedNodeKeys.forEach((key: go.Key) => {
                        const nd = modifiedNodeMap.get(key);
                        const idx = this.mapNodeKeyIdx.get(key);
                        if (nd && idx === undefined) {  // nodes won't be added if they already exist
                            this.mapNodeKeyIdx.set(nd.key, narr.length);
                            narr.push(nd);
                        }
                    });
                }

                if (removedNodeKeys) {
                    narr = narr.filter((nd: go.ObjectData) => {
                        if (removedNodeKeys.includes(nd.key)) {
                            return false;
                        }
                        return true;
                    });
                    draft.nodeDataArray = narr;
                    this.refreshNodeIndex(narr);
                }

                let larr = draft.linkDataArray;
                if (modifiedLinkData) {
                    modifiedLinkData.forEach((ld: go.ObjectData) => {
                        modifiedLinkMap.set(ld.key, ld);
                        const idx = this.mapLinkKeyIdx.get(ld.key);
                        if (idx !== undefined && idx >= 0) {
                            larr[idx] = ld;
                            if (draft.selectedData && draft.selectedData.key === ld.key) {
                                draft.selectedData = ld;
                            }
                        }
                    });
                }
                if (insertedLinkKeys) {
                    insertedLinkKeys.forEach((key: go.Key) => {
                        const ld = modifiedLinkMap.get(key);
                        const idx = this.mapLinkKeyIdx.get(key);
                        if (ld && idx === undefined) {  // links won't be added if they already exist
                            this.mapLinkKeyIdx.set(ld.key, larr.length);
                            larr.push(new DLink(ld.key, ld.from, ld.to, draft.linkState));
                        }
                    });
                    draft.skipsDiagramUpdate = false;
                    return
                }
                if (removedLinkKeys) {
                    larr = larr.filter((ld: go.ObjectData) => {
                        if (removedLinkKeys.includes(ld.key)) {
                            return false;
                        }
                        return true;
                    });
                    draft.linkDataArray = larr;
                    this.refreshLinkIndex(larr);
                }
                // handle model data changes, for now just replacing with the supplied object
                if (modifiedModelData) {
                    draft.modelData = modifiedModelData;
                }
                draft.skipsDiagramUpdate = true;  // the GoJS model already knows about these updates
            })
        );
    }

    public setNodeState(state: string) {
        this.setState(
            produce((draft: ClassDiagramState) => {
                draft.nodeState = state;
            })
        );
    };

    public setLinkState(state: string) {
        this.setState(
            produce((draft: ClassDiagramState) => {
                draft.linkState = state;
            })
        );
    };

    public updateFieldsForSelected(fields: Array<NodeField>) {
        this.setState(
            produce((draft: ClassDiagramState) => {
                if (draft.selectedData !== null) {
                    let newArr = draft.nodeDataArray.map(el => Object.assign({}, el))
                    newArr.forEach(el => {
                        // @ts-ignore
                        if (el.key === draft.selectedData.key) {
                            el.fields = fields;
                        }
                    })

                    draft.nodeDataArray = newArr;
                    draft.skipsDiagramUpdate = false;
                }

            })
        );
    }

    public incrementFieldCount() {
        this.setState(
            produce((draft: ClassDiagramState) => {
                draft.fieldCount = draft.fieldCount + 1;
            })
        );
    }

    public decrementFieldCount() {
        this.setState(
            produce((draft: ClassDiagramState) => {
                draft.fieldCount = draft.fieldCount - 1;
            })
        );
    }

    public saveModel() {
        let clsId = 0;
        if (localStorage.getItem('modelId') !== null) {
            try {
                clsId = parseInt(localStorage.getItem('modelId') as string);
            } catch (e) {
                // eslint-disable-next-line @typescript-eslint/no-unused-vars
                // @ts-ignore
                clsId = null;
            }
        }
        let cls = new ClassDiagramModel(clsId, localStorage.getItem('modelName'), this.state.nodeDataArray as Array<DNode>, this.state.linkDataArray as Array<DLink>, localStorage.getItem('teamName'));
        cls.modelType = localStorage.getItem('modelType');
        axios.post(`/${localStorage.getItem('modelType')}/save`, cls)
            .then(res => {
                localStorage.setItem('modelId', res.data.id);
                window.location.reload();
            })
            .catch(err => {
                this.handleOpenError(err.response.status, err.response.data);
            });
    }

    private handleOpenError(header: string, msg: string) {
        this.setState(
            produce((draft: ClassDiagramState) => {
                draft.showError = true;
                draft.errorStatus = header;
                draft.errorMsg = msg;
            })
        );
    }

    private handleCloseError() {
        this.setState(
            produce((draft: ClassDiagramState) => {
                draft.showError = false;
            })
        );
    }

    public render() {
        return (
            <div>
                <ClassDiagramWrapper
                    nodeDataArray={this.state.nodeDataArray}
                    linkDataArray={this.state.linkDataArray}
                    modelData={this.state.modelData}
                    skipsDiagramUpdate={this.state.skipsDiagramUpdate}
                    onDiagramEvent={this.handleDiagramEvent}
                    onModelChange={this.handleModelChange}
                />
                <Toolbar
                    setNodeState={this.setNodeState}
                    setLinkState={this.setLinkState}
                    updateFieldsForSelected={this.updateFieldsForSelected}
                    fields={this.state.selectedData !== null ? this.state.selectedData.fields : []}
                    nodeArray={this.state.nodeDataArray}
                    type={'class'}
                    fieldCount={this.state.fieldCount}
                    incrementFieldCount={this.incrementFieldCount}
                    decrementFieldCount={this.decrementFieldCount}
                    saveModel={this.saveModel}
                />
                <ExceptionHandler show={this.state.showError} header={this.state.errorStatus} text={this.state.errorMsg} handleClose={this.handleCloseError}/>
            </div>
        );
    }
}

export default withRouter(ClassDiagram);