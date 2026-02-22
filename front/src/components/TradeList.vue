<script setup>
import { useTradeStore } from '@/stores/trade';
import { storeToRefs } from 'pinia'; 
import { onMounted } from 'vue';
import dayjs from 'dayjs';

const tradeStore = useTradeStore();
const { trades } = storeToRefs(tradeStore);
const selectedDate = ref(new Date());


onMounted(() => {
  tradeStore.searchTradeAction({
    "stkNm": "",
    "tradingType": "",
    "isWin": "",
    "isStupid": "",
    "start": "",
    "end": ""
  });
});


</script>

<template>
    <div>
        <div v-for="trade in trades" :key="trade.id">
            {{ trade }}
        </div>
    </div>
</template>

<style scoped>

</style>