import {RqmRow} from "./RqmRow";

export class RqmDiagramModel {
    constructor(public id: number, public name: string, public teamName: string, public rqmRows: Array<RqmRow>) {
    }
}