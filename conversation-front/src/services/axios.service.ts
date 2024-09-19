import axios from "axios"

export const Api = axios.create({
    baseURL: "http://localhost:8080",
    headers:{
        Accept: "*",
        "Content-Type": "application/json"
    }

})