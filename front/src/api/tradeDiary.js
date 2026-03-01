import { localAxios } from "@/util/axios-common";


function saveTradeDiary(param, success, fail) {
  localAxios()
    .post("/stock-company/trade-diaries", JSON.stringify(param))
    .then(success)
    .catch(fail);
}

function selectOneTradeDiary(date, success, fail) {
    localAxios()
      .get(`/trade-diaries/${date}`)
      .then(success)
      .catch(fail);
}

async function saveMarketTrend(trend,id, success, fail) {
  await localAxios()
    .post("/trades-diaries/market-trend", {trend, id})
    .then(success)
    .catch(fail);
}

async function saveOverallReview(date, success, fail) {
  await localAxios()
    .post(`/trades/comments/${date}`)
    .then(success)
    .catch(fail);
}

export { saveTradeDiary, selectOneTradeDiary, saveMarketTrend, saveOverallReview };