<script setup>
import { ref, watch } from 'vue';
import { storeToRefs } from 'pinia';
import { useTradeDiaryStore } from '@/stores/tradeDiary';
import { useOrderLogStore } from '@/stores/orderLog';
import TradeDiaryCard from '@/components/TradeDiaryCard.vue';
import OrderLogList from '@/components/OrderLogList.vue';

import { onMounted } from 'vue';
import { DatePicker } from 'v-calendar';
import 'v-calendar/style.css';
import dayjs from 'dayjs';

const tradeDiaryStore = useTradeDiaryStore();
const { saveTradeDiaryAction } = tradeDiaryStore;
const { tradeDiary: tradeDiary } = storeToRefs(tradeDiaryStore);
const selectedDate = ref(new Date());

const orderLogStore = useOrderLogStore();
const { orderLogs:orderLogs } = storeToRefs(orderLogStore);

onMounted(  async()=> {
    await fetchTradeDiary(dayjs(selectedDate.value).format('YYYYMMDD'));
})

watch(selectedDate, (newDate) => {
    if (newDate) {
        const formattedDate = dayjs(newDate).format('YYYYMMDD');
        fetchTradeDiary(formattedDate); 
        orderLogs.value = []; 
    }
});


function fetchTradeDiary(date) {
    tradeDiaryStore.selectOneTradeDiaryAction(date);
}


</script>

<template>
  <div class="dashboard-container">
    <header class="dashboard-header">
      <h2>📈 Trading Record Dashboard</h2>
      <div class="calendar-wrapper">
        <DatePicker v-model="selectedDate" />
      </div>
    </header>

    <main class="dashboard-main">
      <section class="left-panel">
        <TradeDiaryCard 
          :tradeDiary="tradeDiary" 
          :date="selectedDate" 
          :isDetail="true" 
          @saveTradeDiary="saveTradeDiaryAction" 
        />
      </section>

      <section class="right-panel">
        <OrderLogList />
      </section>
    </main>
  </div>
</template>

<style scoped>
.dashboard-container {
  max-width: 1600px; /* 전체 폭 제한 */
  margin: 0 auto;    /* 화면 중앙 정렬 */
  padding: 40px 20px;
  background-color: #f8fafc;
  min-height: 100vh;
}

.dashboard-header {
  text-align: center;
  margin-bottom: 40px;
}

.calendar-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.dashboard-main {
  display: flex;         /* 가로 배치 */
  gap: 30px;            /* 좌우 간격 */
  justify-content: center; /* 중앙 모음 */
  align-items: flex-start;
}

.left-panel, .right-panel {
  flex: 1;              /* 5:5 비율로 동일하게 분할 */
  min-width: 650px;     /* 글자 꺾임 방지를 위한 최소폭 */
}

/* 화면이 작아지면 위아래로 쌓기 */
@media (max-width: 1400px) {
  .dashboard-main {
    flex-direction: column;
    align-items: center;
  }
  .left-panel, .right-panel {
    width: 100%;
    min-width: unset;
    max-width: 900px;
  }
}
</style>