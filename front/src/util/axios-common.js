import axios from 'axios';


function localAxios() {
  const instance = axios.create({
    baseURL: 'http://localhost:8080',
    headers: {

      'Content-Type': 'application/json'

    },
  });
  instance.defaults.headers.post['Content-Type'] = 'application/json';
  instance.defaults.headers.put['Content-Type'] = 'application/json';
  instance.defaults.headers.delete['Content-Type'] = 'application/json';
  return instance;
}

export {localAxios};