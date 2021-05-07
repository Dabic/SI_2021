import React, {useState} from 'react';
import styles from './Login.module.css';
import axios from "axios";
import jwt_decode from 'jwt-decode';
import { useHistory } from "react-router-dom";
const Login = () => {

    let history = useHistory();
    const [creds, setCreds] = useState({
        username: "",
        password: ""
    });
    const onLogin = () => {
        axios.post('http://localhost:8080/api/users/login', creds).then(res => {
            localStorage.setItem('token', res.headers['authorization'])
            let decodedToken = jwt_decode(res.headers['authorization'].split(" ")[1])
            localStorage.setItem('teamName', decodedToken['teamName']);
            localStorage.setItem('username', decodedToken['sub']);
            localStorage.setItem('roles', decodedToken['roles']);
            history.push('/dashboard')
        }).catch(err => {

        })
    };

    const onInput = (inputName, value) => {
        const oldState = creds;
        oldState[inputName] = value;
        setCreds(oldState);
    }

    return (
        <div className={styles.Login}>
            <div className={styles.LoginContainer}>
                <input onInput={event => onInput('username', event.target.value)} type="text" placeholder="Enter username..."/>
                <input onInput={event => onInput('password', event.target.value)} type="password" placeholder="Enter password..."/>
                <button onClick={onLogin} type="button">Login</button>
            </div>
        </div>
    );
};

export default Login;