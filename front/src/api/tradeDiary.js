import { localAxios } from "@/util/axios-common";


function saveTradeDiary(param, success, fail) {
  localAxios()
    .post("/tradeDiary", JSON.stringify(param))
    .then(success)
    .catch(fail);
}

function selectOneTradeDiary(date, success, fail) {
    localAxios()
      .get(`/trade-diaries/${date}`)
      .then(success)
      .catch(fail);
}

export { saveTradeDiary, selectOneTradeDiary };