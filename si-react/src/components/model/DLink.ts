import * as go from 'gojs';

export class DLink implements go.ObjectData {
    public id: number | undefined;
    constructor(public key: number, public from: number, public to: number, public type: string, private label?: string) {
        if (label === null) {
            this.label = '';
        }
    }
}