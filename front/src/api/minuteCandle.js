import { localAxios } from "@/util/axios-common";


function getMinuteCandle(param, success, fail) {
  localAxios()
    .post("/stock-company/minute-candles", JSON.stringify(param))
    .then(success)
    .catch(fail);
}


export { getMinuteCandle };