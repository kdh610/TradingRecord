import { ref } from 'vue';
import { saveTrade, searchTrade, deleteTrade, saveComment, getAllTrades } from "@/api/trade";
import { defineStore } from "pinia";

export const useTradeStore = defineStore("trade",() => {
    const trade = ref({})
    const trades = ref([])
    const allTrades = ref([])
    const breakTrades = ref([])
    const pullbackTrades = ref([])
    const endTimeTrades = ref([])
    const swingTrades = ref([])
    const pairTrades = ref([])
    

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

    const saveCommentAction = async (param) => {
        await saveComment(
            param,
        (response) => {
            console.log("Comment saved:", response.data);
            return response.data.data; 
        },
        (error) => {
            console.error("Error saving comment:", error);
            throw error; 
        }
    );
    }

    const getAllTradesAction = async () => {
        await getAllTrades(
        (response) => {
            console.log("All trades retrieved:", response.data);
            allTrades.value = response.data.data;
            return response; 
        },
        (error) => {
            console.error("Error retrieving all trades:", error);
            throw error; 
        }
    );
    }

    return { trade, trades,pairTrades, allTrades, breakTrades, pullbackTrades, endTimeTrades, swingTrades, saveTradeAction, searchTradeAction, deleteTradeAction, saveCommentAction, getAllTradesAction }

})