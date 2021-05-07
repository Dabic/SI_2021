import {finished} from "stream";
import * as go from 'gojs';
export class NodeField implements go.ObjectData{
    public id: number | undefined;
    public text: string;
    public info: string;
    public type: string;
    public figure: string;
    public color: string;
    public nodeField: number;

    constructor(nodeField: number, text: string, info: string, type: string) {
        this.nodeField = nodeField;
        this.text = text;
        this.info = info;
        this.type = type;

        if (this.type === 'public') {
            this.figure = 'Ellipse';
            this.color = '#FF0000'
        } else if (this.type === 'private') {
            this.figure = 'Ellipse';
            this.color = '#0000FF'
        } else {
            this.figure = 'Ellipse';
            this.color = 'green'
        }
    }


}