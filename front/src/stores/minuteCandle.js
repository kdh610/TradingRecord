import { defineStore } from "pinia";
import { ref } from 'vue';
import { getMinuteCandle } from "@/api/minuteCandle";

export const useMinuteCandleStore = defineStore("minuteCandle",() => {
    const minuteCandles = ref([])

    function getMinuteCandleAction(param) {
        getMinuteCandle(
            param,
            (response) => {
                console.log("Minute candles retrieved:", response.data);
                minuteCandles.value = response.data.data;
                console.log("minuteCandles updated:", minuteCandles.value);
            },
            (error) => {
                console.error("Error retrieving minute candles:", error);
            }
        )
    }


    return { minuteCandles, getMinuteCandleAction }

})