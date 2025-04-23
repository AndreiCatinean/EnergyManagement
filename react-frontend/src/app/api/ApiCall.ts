import axios from 'axios';

export const apiCall = axios.create({
    withCredentials: true,
});
