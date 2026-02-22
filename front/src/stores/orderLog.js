import { defineStore } from "pinia";
import { ref } from 'vue';
import { saveOrderLog, selectOrderLogs } from "@/api/orderLog";

export const useOrderLogStore = defineStore("orderLog",() => {
    const orderLogs = ref([])
    const selectedOrderIndex = ref(null);

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
                orderLogs.value = response.data.data;
            },
            (error) => {
                console.error("Error retrieving order logs:", error);
            }
        )
    }

    function selectOrderIndexAction(index) {
        selectedOrderIndex.value = index;
    }

    return { orderLogs, selectedOrderIndex, saveOrderLogAction, selectOrderLogsAction, selectOrderIndexAction }
})