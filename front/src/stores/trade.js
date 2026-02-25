import { ref } from 'vue';
import { saveTrade, searchTrade, deleteTrade } from "@/api/trade";
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
            trades.value = response.data.data.content;
            return response; 
        },
        (error) => {
            console.error("Error searching trades:", error);
            throw error; 
        }
    );
    }

    const deleteTradeAction = async (id) => {
        await deleteTrade(
            id,
        (response) => {
            console.log("Trade deleted:", response.data);
            return response; 
        },
        (error) => {
            console.error("Error deleting trade:", error);
            throw error; 
        }   
        )
    }

    return { trade, trades, saveTradeAction, searchTradeAction, deleteTradeAction }

})