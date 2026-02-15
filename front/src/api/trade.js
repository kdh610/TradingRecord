import { localAxios } from "@/util/axios-common";


function saveTrade(param, success, fail) {
  localAxios()
    .post("/trades", JSON.stringify(param))
    .then(success)
    .catch(fail);
}

export { saveTrade };