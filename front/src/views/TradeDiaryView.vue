<script setup>
import TradeDiaryCard from '@/components/TradeDiaryCard.vue';
import OrderLogList from '@/components/orderLogList.vue';
import MinuteChart from '@/components/MinuteChart.vue';


</script>

<template>
  <div class="dashboard-container">
     <h1>📈 매매일지</h1>
    <header class="dashboard-header">
      <section class="left-panel">
        <TradeDiaryCard/>
      </section>

      <section class="right-panel">
        <div class="list-section">
          <OrderLogList />
        </div>
      </section>

    </header>

    <main class="dashboard-main">
           
        <div class="chart-section">
          <MinuteChart />
        </div>
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
  display: flex;         /* 가로 배치 */
  gap: 30px;            /* 좌우 간격 */
  justify-content: center; /* 중앙 모음 */
  align-items: flex-start;
}

.calendar-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.dashboard-main {
    text-align: center;
  margin-bottom: 40px;
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