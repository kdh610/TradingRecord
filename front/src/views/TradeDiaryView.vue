<script setup>
import TradeDiaryCard from '@/components/TradeDiaryCard.vue';
import OrderLogList from '@/components/orderLogList.vue';
import MinuteChart from '@/components/MinuteChart.vue';
import TradeList from '@/components/TradeList.vue';
import TradeRecord from '@/components/TradeRecord.vue';

</script>

<template>
  <div class="dashboard-container">
    <h1 class="page-title">📈 매매일지</h1>
    
    <section class="stats-summary-section">
      <TradeRecord />
    </section>
    
    <header class="dashboard-header">
      <section class="left-panel">
        <TradeDiaryCard/>
      </section>

      <section class="right-panel">
        <div class="list-section">
          <TradeList />
        </div>
      </section>

    </header>

    <main class="dashboard-main">
           
        <div class="chart-section">
          <MinuteChart />
        </div>
        <div class="chart-section">
          <OrderLogList />
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

.page-title {
  margin-bottom: 24px; /* 제목과 전적 카드 사이 간격 */
  font-size: 2rem;
  font-weight: 800;
  color: #1e293b;
  text-align: left; /* 제목은 왼쪽 정렬이 더 깔끔할 수 있어요 */
}

.stats-summary-section {
  margin-bottom: 40px; /* 전적 카드와 하단 메인 컨텐츠 사이 간격 */
  width: 100%;
}

/* 기존 스타일 유지하면서 레이아웃 최적화 */
.dashboard-header {
  display: flex;
  gap: 30px;
  justify-content: space-between; /* 양 끝으로 벌리기 */
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