import { ref } from 'vue';
import { saveTradeDiary, selectOneTradeDiary, saveMarketTrend, saveOverallReview } from "@/api/tradeDiary";
import { defineStore } from "pinia";

export const useTradeDiaryStore = defineStore("tradeDiary",() => {
    const tradeDiary = ref({})


    function saveTradeDiaryAction(param) {
        saveTradeDiary(
            param,
            (response) => {
                console.log("Trade diary saved:", response.data);
                alert("매매일지 저장 완료")
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
                tradeDiary.value = response.data.data;
            },
            (error) => {
                console.error("Error retrieving trade diary:", error);
                tradeDiary.value = {};
            }
        );
    }

    const saveMarketTrendAction = async (trend, id) => {
        await saveMarketTrend(
            trend, id,
            (response) => {
                console.log("Market trend saved:", response.data);
                alert("시장 트렌드 저장 완료")
                return response.data.data;
            },
            (error) => {
                console.error("Error saving market trend:", error);
            }
        );
    }

    const saveOverallReviewAction = async (date) => {
        await saveOverallReview(
            date,
            (response) => {
                console.log("Overall review saved:", response.data);
                alert("매매 총평 저장 완료")
                return response.data.data;
            },
            (error) => {
                console.error("Error saving overall review:", error);
            }
        );
    }   



    return { tradeDiary, saveOverallReviewAction, saveTradeDiaryAction, selectOneTradeDiaryAction, saveMarketTrendAction }

})