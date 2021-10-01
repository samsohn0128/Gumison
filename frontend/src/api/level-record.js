import axios from 'axios';
import { BASE_URL } from "@/constant/index"

const instance = axios.create({
    baseURL: BASE_URL + '/history',
    headers: {
      'Content-type': 'application/json',
    },
    withCredentials: true,
});

function submit(recordData) {
    return instance.post('/', recordData);
}

export {
    submit,
}