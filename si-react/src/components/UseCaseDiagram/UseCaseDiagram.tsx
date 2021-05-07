import React, {Component} from 'react';
import {UseCaseDiagramWrapper} from "./UseCaseDiagramWrapper/UseCaseDiagramWrapper";
import {DNode} from "../model/DNode";
import * as go from "gojs";
import {DLink} from "../model/DLink";
import { produce } from 'immer';
import Toolbar from "../Tools/Toolbar/Toolbar";
import axios from "../axios/brokerAxios";
import {ClassDiagramModel} from "../model/ClassDiagramModel";
import {AxiosResponse} from "axios";
import {NodeField} from "../model/NodeField";
import ExceptionHandler from "../ExceptionHandler/ExceptionHandler";
// @ts-ignore
import {withRouter} from 'react-router-dom';

interface ClassDiagramState {
    nodeDataArray: Array<go.ObjectData>;
    linkDataArray: Array<go.ObjectData>;
    modelData: go.ObjectData;
    selectedData: go.ObjectData | null;
    skipsDiagramUpdate: boolean;
    linkState: string;
    nodeState: string;
    showError: boolean;
    errorMsg: string;
    errorStatus: string;
}

interface Props {
    history: any;
}

class UseCaseDiagram extends Component<Props, ClassDiagramState> {
    // Maps to store key -> arr index for quick lookups
    private mapNodeKeyIdx: Map<go.Key, number>;
    private mapLinkKeyIdx: Map<go.Key, number>;
    private listen: any;

    componentDidMount() {
        this.fetchData();
        // @ts-ignore
        this.listen = this.props.history.listen((location, action) => {
            if (location && location.pathname.split("/")[2] === 'use-case') {
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
            nodeDataArray: [
                new DNode(0, 'Ellipse', '0 0', 'Alpha'),
                new DNode(1, 'Actor', '0 0', 'Gamma'),
            ],
            linkDataArray: [
                {key: -1, from: 0, to: 1, type: 'Standard', label: 'includes'},
            ],
            modelData: {
                canRelink: true
            },
            selectedData: null,
            skipsDiagramUpdate: false,
            linkState: 'OpenTriangle',
            nodeState: 'Ellipse',
            showError: false,
            errorMsg: '',
            errorStatus: ''
        };
        this.mapNodeKeyIdx = new Map<go.Key, number>();
        this.mapLinkKeyIdx = new Map<go.Key, number>();
        this.refreshNodeIndex(this.state.nodeDataArray);
        this.refreshLinkIndex(this.state.linkDataArray);
        // bind handler methods
        this.handleDiagramEvent = this.handleDiagramEvent.bind(this);
        this.handleModelChange = this.handleModelChange.bind(this);
        this.setNodeState = this.setNodeState.bind(this);
        this.setLinkState = this.setLinkState.bind(this);
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
                draft.nodeDataArray = [...res.data.dnodes.map((dnode: DNode) => {
                    let node = new DNode(dnode.key, dnode.type, dnode.loc, dnode.text);
                    return node;
                })];
                draft.linkDataArray = [...res.data.dlinks];
                draft.skipsDiagramUpdate = false;
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

    /**
     * Handle any relevant DiagramEvents, in this case just selection changes.
     * On ChangedSelection, find the corresponding data and set the selectedData state.
     * @param e a GoJS DiagramEvent
     */
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
            default: break;
        }
    }

    /**
     * Handle GoJS model changes, which output an object of data changes via Model.toIncrementalData.
     * This method iterates over those changes and updates state to keep in sync with the GoJS model.
     * @param obj a JSON-formatted string
     */
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
                            larr.push(new DLink(ld.key, ld.from, ld.to, draft.linkState, this.resolveLinkLabel()));
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

    public resolveLinkLabel() {
        switch (this.state.linkState) {
            case 'OpenTriangle':
                return 'includes';
            case 'Standard':
                return '';
            case 'OpenTriangleLine':
                return 'extends'
            default:
                return ''
        }
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

    render() {
        return (
            <div>
                <UseCaseDiagramWrapper
                    nodeDataArray={this.state.nodeDataArray}
                    linkDataArray={this.state.linkDataArray}
                    modelData={this.state.modelData}
                    skipsDiagramUpdate={this.state.skipsDiagramUpdate}
                    onDiagramEvent={this.handleDiagramEvent}
                    onModelChange={this.handleModelChange} />
                <Toolbar
                    setNodeState={this.setNodeState}
                    setLinkState={this.setLinkState}
                    updateFieldsForSelected={() => {}}
                    fields={this.state.selectedData !== null ? this.state.selectedData.fields : []}
                    nodeArray={this.state.nodeDataArray}
                    type={'use-case'}
                    saveModel={this.saveModel}
                />
                <ExceptionHandler show={this.state.showError} header={this.state.errorStatus} text={this.state.errorMsg} handleClose={this.handleCloseError}/>
            </div>
        );
    }
}

export default withRouter(UseCaseDiagram);