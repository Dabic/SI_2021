import * as go from 'gojs';
import {ReactDiagram} from 'gojs-react';
import * as React from 'react';
import {Size} from "gojs";
import './UseCaseDiagramWrapper.css'
import '../figures'

interface DiagramProps {
    nodeDataArray: Array<go.ObjectData>;
    linkDataArray: Array<go.ObjectData>;
    modelData: go.ObjectData;
    skipsDiagramUpdate: boolean;
    onDiagramEvent: (e: go.DiagramEvent) => void;
    onModelChange: (e: go.IncrementalData) => void;
}

export class UseCaseDiagramWrapper extends React.Component<DiagramProps, {}> {
    private diagramRef: React.RefObject<ReactDiagram>;

    constructor(props: DiagramProps) {
        super(props);
        this.diagramRef = React.createRef();
    }

    public componentDidMount() {
        if (!this.diagramRef.current) return;
        const diagram = this.diagramRef.current.getDiagram();
        if (diagram instanceof go.Diagram) {
            diagram.addDiagramListener('ChangedSelection', this.props.onDiagramEvent);
            diagram.addDiagramListener('BackgroundDoubleClicked', this.props.onDiagramEvent);
        }
    }

    public componentWillUnmount() {
        if (!this.diagramRef.current) return;
        const diagram = this.diagramRef.current.getDiagram();
        if (diagram instanceof go.Diagram) {
            diagram.removeDiagramListener('ChangedSelection', this.props.onDiagramEvent);
            diagram.removeDiagramListener('BackgroundDoubleClicked', this.props.onDiagramEvent);
        }
    }

    private initDiagram(): go.Diagram {
        const $ = go.GraphObject.make;
        const diagram =
            $(go.Diagram,
                {
                    'undoManager.isEnabled': true,
                    layout: $(go.ForceDirectedLayout, {arrangementSpacing : new Size(20, 20)}),
                    model: $(go.GraphLinksModel, {linkKeyProperty: 'key'})
                }
            );

        diagram.nodeTemplate =
            $(go.Node, "Auto",
                new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),
                $(go.Shape,
                    {
                        portId: "", cursor: "pointer",
                        fromLinkable: true, toLinkable: true,
                        fromLinkableDuplicates: true, toLinkableDuplicates: true,
                        fromSpot: go.Spot.AllSides, toSpot: go.Spot.AllSides, strokeWidth: 10, stroke: "transparent"},
                    new go.Binding("fill", "color"),
                    new go.Binding("figure", "type")),
                // the content consists of a header and a list of items
                $(go.Panel, "Vertical",
                    // this is the header for the whole node
                    $(go.Panel, "Auto",
                        {stretch: go.GraphObject.Horizontal},  // as wide as the whole node
                        $(go.Shape,
                            {fill: "transparent", stroke: null}),
                        $(go.TextBlock,
                            {
                                alignment: go.Spot.Center,
                                margin: 3,
                                stroke: "white",
                                textAlign: "center",
                                font: "bold 12pt sans-serif",
                                editable: true
                            },
                            new go.Binding("text").makeTwoWay())),
                    // this Panel holds a Panel for each item object in the itemArray;
                    // each item Panel is defined by the itemTemplate to be a TableRow in this Table
                )  // end Vertical Panel
            );  // end DNode

        // relinking depends on modelData
        diagram.linkTemplate =
            $(go.Link,
                $(go.Shape),
                $(go.Shape,
                    {toArrow: 'Standard'},
                    new go.Binding('toArrow', 'type')
                ),
                $(go.TextBlock,
                    new go.Binding("text", "label"))
            );

        return diagram;
    }


    public render() {
        return (
            <ReactDiagram
                ref={this.diagramRef}
                divClassName='diagram-component'
                initDiagram={this.initDiagram}
                nodeDataArray={this.props.nodeDataArray}
                linkDataArray={this.props.linkDataArray}
                modelData={this.props.modelData}
                onModelChange={this.props.onModelChange}
                skipsDiagramUpdate={this.props.skipsDiagramUpdate}
            />
        );
    }
}