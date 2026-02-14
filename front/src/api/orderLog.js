import { localAxios } from "@/util/axios-common";


function saveOrderLog(param, success, fail) {
  localAxios()
    .post("/stock-company/order-logs", JSON.stringify(param))
    .then(success)
    .catch(fail);
}

function selectOrderLogs(param, success, fail) {
    localAxios()
      .get(`/order-logs`,{ params: param })
      .then(success)
      .catch(fail);
}

export { saveOrderLog, selectOrderLogs };