import React, {Component} from 'react';
import styles from './Palette.module.css';
import ClassIcon from "../ClassEditor/ClassIcon/ClassIcon";

interface PaletteProps {
    setLinkState: (state: string) => void;
    setNodeState: (state: string) => void;
    type: string;
    saveModel: any;

}
export class DPalette extends Component<PaletteProps, {}> {

    content: any;

    constructor(props: PaletteProps) {
        super(props);
        if (this.props.type === 'class') {
            this.content = (
                <div className={styles.Palette}>
                    <div className={styles.Shapes}>
                        <ClassIcon setNodeState={() => this.props.setNodeState('class')} objClass={'class'} />
                        <ClassIcon setNodeState={() => this.props.setNodeState('interface')} objClass={'interface'} />
                        <ClassIcon setNodeState={() => this.props.setNodeState('enum')} objClass={'enum'} />
                    </div>
                    <button onClick={() => this.props.setLinkState('Triangle')}>generalisation</button>
                    <button onClick={() => this.props.setLinkState('OpenTriangle')}>implementation</button>
                    <button onClick={() => this.props.setLinkState('OpenTriangleLine')}>realisation</button>
                    <div className={styles.Separator} />
                    <button className={styles.Save} onClick={props.saveModel}>save</button>
                </div>
            )
        } else {
            this.content = (
                <div className={styles.Palette}>
                    <div className={styles.Shapes}>
                        <ClassIcon setNodeState={() => this.props.setNodeState('Actor')} objClass={'actor'} />
                        <ClassIcon setNodeState={() => this.props.setNodeState('Ellipse')} objClass={'story'} />
                    </div>
                    <button onClick={() => this.props.setLinkState('Triangle')}>generalisation</button>
                    <button onClick={() => this.props.setLinkState('OpenTriangle')}>include</button>
                    <button onClick={() => this.props.setLinkState('OpenTriangleLine')}>extend</button>
                    <button onClick={() => this.props.setLinkState('')}>relation</button>
                    <div className={styles.Separator} />
                    <button className={styles.Save} onClick={props.saveModel}>save</button>
                </div>
            )
        }
    }

    public render() {
        return this.content;
    }
}