<script setup>
import { ref } from 'vue';
import { storeToRefs } from 'pinia';
import { useTradeDiaryStore } from '@/stores/tradeDiary';
import TradeDiaryCard from '@/components/TradeDiaryCard.vue';

import { onMounted } from 'vue';

const tradeDiaryStore = useTradeDiaryStore();
const { saveTradeDiaryAction } = tradeDiaryStore;
const { selectOneTradeDiaryAction: tradeDiary } = storeToRefs(tradeDiaryStore);



onMounted(  async()=> {
  await fetchTradeDiary('20260206')

  if(teams.value.length>0){
    isTeamsEmpty.value=false
  }

})


function fetchTradeDiary(date) {
    tradeDiaryStore.selectOneTradeDiaryAction(date);
}





</script>

<template>
    <div>
        <h2>Trade Diary View</h2>
        <button @click="fetchTradeDiary('20260206')">Load Trade Diary for 20260206</button>
    </div>
    <div>
        <TradeDiaryCard :tradeDiary="tradeDiary" :isDetail="true" @saveTradeDiary="saveTradeDiaryAction" />
    </div>
</template>

<style scoped>

</style>