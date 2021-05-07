import {NodeField} from "./NodeField";
import * as go from 'gojs';

export class DNode implements go.ObjectData{
    public id: number | undefined;
    public key: number;
    public type: string;
    public text: string;
    public loc: string;
    public color: string;
    public fields: Array<NodeField>

    constructor(key: number, type: string, loc: string, text: string) {
        this.key = key;
        this.type = type;
        this.text = text;
        this.loc = loc;
        if (this.type === 'class') {
            this.color = 'lightblue';
        } else if (this.type === 'interface') {
            this.color = '#ffe268';
        } else if (this.type === 'enum') {
            this.color = 'salmon';
        } else if (this.type === 'Actor') {
            this.color = 'black';
        } else {
            this.color = '#ffe268';
        }
        this.fields = [];
    }
}