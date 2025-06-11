import axios from '../axios';


export const exportReport = () => {
    return axios.get('/api/download-pdf', {responseType: 'blob'} );
};