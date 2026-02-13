<script setup>
import { ref, watch } from 'vue';
import { storeToRefs } from 'pinia';
import { useTradeDiaryStore } from '@/stores/tradeDiary';
import TradeDiaryCard from '@/components/TradeDiaryCard.vue';

import { onMounted } from 'vue';
import { DatePicker } from 'v-calendar';
import 'v-calendar/style.css';
import dayjs from 'dayjs';

const tradeDiaryStore = useTradeDiaryStore();
const { saveTradeDiaryAction } = tradeDiaryStore;
const { tradeDiary: tradeDiary } = storeToRefs(tradeDiaryStore);
const selectedDate = ref(new Date());

onMounted(  async()=> {
    await fetchTradeDiary(dayjs(selectedDate.value).format('YYYYMMDD'));
})

watch(selectedDate, (newDate) => {
    if (newDate) {
        const formattedDate = dayjs(newDate).format('YYYYMMDD');
        fetchTradeDiary(formattedDate); 
    }
});

function fetchTradeDiary(date) {
    tradeDiaryStore.selectOneTradeDiaryAction(date);
}


</script>

<template>
    <div>
        <h2>Trade Diary View</h2>
        <DatePicker v-model="selectedDate" @dayclick="onDayClick" />
        
    </div>
    <div>
        <TradeDiaryCard :tradeDiary="tradeDiary" :date="selectedDate" :isDetail="true" @saveTradeDiary="saveTradeDiaryAction" />
    </div>
</template>

<style scoped>

</style>