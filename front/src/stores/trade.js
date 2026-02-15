import { ref } from 'vue';
import { saveTrade } from "@/api/trade";
import { defineStore } from "pinia";

export const useTradeStore = defineStore("trade",() => {
    const trade = ref({})


    function saveTradeAction(param) {
        saveTrade(
            param,
            (response) => {
                console.log("Trade  saved:", response.data);
            },
            (error) => {
                console.error("Error saving trade :", error);
            }
        );
    }

    return { trade, saveTradeAction }

})