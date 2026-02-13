import { ref } from 'vue';
import { saveTradeDiary, selectOneTradeDiary } from "@/api/tradeDiary";
import { defineStore } from "pinia";

export const useTradeDiaryStore = defineStore("tradeDiary",() => {
    const tradeDiary = ref({})


    function saveTradeDiaryAction(param) {
        saveTradeDiary(
            param,
            (response) => {
                console.log("Trade diary saved:", response.data);
            },
            (error) => {
                console.error("Error saving trade diary:", error);
            }
        );
    }

    function selectOneTradeDiaryAction(date) {
        selectOneTradeDiary(
            date,
            (response) => {
                console.log("Trade diary retrieved:", response.data);
                tradeDiary.value = response.data;
            },
            (error) => {
                console.error("Error retrieving trade diary:", error);
                tradeDiary.value = {};
            }
        );
    }

    return { tradeDiary, saveTradeDiaryAction, selectOneTradeDiaryAction }

})