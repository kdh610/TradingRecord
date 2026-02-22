import { ref } from 'vue';
import { saveTrade, searchTrade } from "@/api/trade";
import { defineStore } from "pinia";

export const useTradeStore = defineStore("trade",() => {
    const trade = ref({})
    const trades = ref([])


    const saveTradeAction = async (param) => {
        await saveTrade(
            param,
        (response) => {
            console.log("Trade saved:", response.data);
            return response; 
        },
        (error) => {
            console.error("Error saving trade:", error);
            throw error; 
        }
    );
    }

    const searchTradeAction = async (param) => {
        await searchTrade(
            param,
        (response) => {
            console.log("Trade search results:", response.data);
            trades.value = response.data.data;
            console.log("trades updated:", trades.value);
            return response; 
        },
        (error) => {
            console.error("Error searching trades:", error);
            throw error; 
        }
    );
    }

    return { trade, trades, saveTradeAction, searchTradeAction }

})