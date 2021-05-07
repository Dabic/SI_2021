import axios from "axios";

export default axios.create({
    baseURL: 'http://localhost:8080/api/broker',
    headers: {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem('token')
    }
})