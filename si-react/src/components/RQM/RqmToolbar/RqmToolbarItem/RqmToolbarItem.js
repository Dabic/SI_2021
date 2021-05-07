import React, {useEffect, useState} from 'react';
import styles from './RqmToolbarItem.module.css';
import indentRight from '../../../../assets/indentRight.png';
import indentLeft from '../../../../assets/indentLeft.png';
import addRow from '../../../../assets/addRow.png';
import removeRow from '../../../../assets/removeRow.png';
import save from '../../../../assets/save.png';
import close from '../../../../assets/close.png';

const RqmToolbarItem = (props) => {

    let classes = [styles.RqmToolbarItem];
    const [image, setImage] = useState();
    useEffect(() => {

        switch (props.type) {
            case 'add-row':
                setImage((<img width='20' height='20' src={addRow} alt=""/>));
                break;
            case 'indent-left':
                setImage((<img width='20' height='20' src={indentLeft} alt=""/>));
                break;
            case 'indent-right':
                setImage((<img width='20' height='20' src={indentRight} alt=""/>));
                break;
            case 'remove-row':
                setImage((<img width='20' height='20' src={removeRow} alt=""/>));
                break;
            case 'save':
                setImage((<img width='20' height='20' src={save} alt=""/>));
                break;
            case 'delete':
                setImage((<img width='20' height='20' src={close} alt=""/>));
                break;
            default:
                break
        }
    }, [])
    return (
        <div onClick={props.action} className={classes.join(' ')}>
            {image}
        </div>
    );
};

export default RqmToolbarItem;