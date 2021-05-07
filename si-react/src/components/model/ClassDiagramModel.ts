import {DNode} from "./DNode";
import {DLink} from "./DLink";

export class ClassDiagramModel {
    constructor(public id: number | null, public name: string | null, public dnodes: Array<DNode>, public dlinks: Array<DLink>, public teamName: string | null, public modelType?: string | null) {
    }
}