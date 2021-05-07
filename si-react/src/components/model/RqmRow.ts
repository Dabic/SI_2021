export class RqmRow {
    constructor(public rqmId: string, public header: string, public description: string, public type: string, public priority: number, public risk: string, public status: string, public id?: number, ) {
    }
}