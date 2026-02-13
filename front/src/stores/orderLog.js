import { defineStore } from "pinia";
import { ref } from 'vue';
import { saveOrderLog, selectOrderLogs } from "@/api/orderLog";

export const useOrderLogStore = defineStore("orderLog",() => {
    const orderLogs = ref([])

    function saveOrderLogAction(param) {
        saveOrderLog(
            param,
            (response) => {
                console.log("Order log saved:", response.data);
            },
            (error) => {
                console.error("Error saving order log:", error);
            }
        )
    }

    function selectOrderLogsAction(param){
        selectOrderLogs(
            param,
            (response) =>{
                console.log("Order logs retrieved:", response.data);
                orderLogs.value = response.data;
            },
            (error) => {
                console.error("Error retrieving order logs:", error);
            }
        )
    }

    return { orderLogs, saveOrderLogAction, selectOrderLogsAction }

})