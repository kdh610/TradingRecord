import { localAxios } from "@/util/axios-common";


async function saveTrade(param, success, fail) {
  await localAxios()
    .post("/trades", JSON.stringify(param))
    .then(success)
    .catch(fail);
}

async function searchTrade(param, success, fail) {
  await localAxios()
    .get("/trades/search", { params: param })
    .then(success)
    .catch(fail);
}

async function deleteTrade(id, success, fail) {
  await localAxios()
    .delete(`/trades/${id}`)
    .then(success)
    .catch(fail);
}

async function saveComment(param, success, fail) {
  await localAxios()
    .post("/trades/comments", JSON.stringify(param))
    .then(success)
    .catch(fail);
}

export { saveTrade, searchTrade, deleteTrade, saveComment };