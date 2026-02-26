<script setup>
import { onMounted, computed, ref,watch } from 'vue';
import { useTradeStore } from '@/stores/trade';
import { storeToRefs } from 'pinia'; 
import TradeStatsCard from '@/components/TradeStatCard.vue';

const tradeStore = useTradeStore();
const { searchTradeAction, getAllTradesAction } = tradeStore;
const { allTrades} = storeToRefs(tradeStore);


onMounted(async () => {
  await getAllTrades();
  console.log("All trades on mounted:", allTrades.value);
});

function getAllTrades() {
  getAllTradesAction(
    (response) => {
      console.log("All trades retrieved:", response.data);
    },
    (error) => {
      console.error("Failed to retrieve all trades:", error);
    }   
);}

const breakTradesList = computed(() =>{
    return (allTrades.value || []).filter(t => t.tradingType === '돌파');
});
const pullbackTradesList = computed(() =>{
    return (allTrades.value || []).filter(t => t.tradingType.includes("눌림"));
});
const swingTradesList = computed(() =>{
    return (allTrades.value || []).filter(t => t.tradingType.includes("스윙"));
});
const endTimeTradesList = computed(() =>{
    return (allTrades.value || []).filter(t => t.tradingType.includes("종베"));
});
const pairTradesList = computed(() =>{
    return (allTrades.value || []).filter(t => t.tradingType.includes("짝"));
});
    




</script>

<template>
    <div class="stats-page">
        <h2 class="page-title">매매 통계 대시보드</h2>
        
        <div class="stats-grid-container">
        <TradeStatsCard tradeType="📊 전체 매매 요약" :trades="allTrades" class="featured-card" />
        <TradeStatsCard tradeType="🚀 돌파 매매" :trades="breakTradesList" />
        <TradeStatsCard tradeType="📉 눌림 매매" :trades="pullbackTradesList" />
        <TradeStatsCard tradeType="📈 스윙 매매" :trades="swingTradesList" />
        <TradeStatsCard tradeType="⏰ 종베 매매" :trades="endTimeTradesList" />
        <TradeStatsCard tradeType="🧷 짝 매매" :trades="pairTradesList" />
        </div>
    </div>
</template>
<style scoped>
.stats-page { padding: 20px; background: #f8fafc; min-height: 30vh; }
.page-title { margin-bottom: 20px; font-weight: 800; }

.stats-grid-container {
  display: flex;
  flex-wrap: wrap; /* 화면이 좁아지면 아래로 내려가도록 */
  gap: 16px;
  justify-content: flex-start;
}

/* 전체 요약 카드는 좀 더 강조하고 싶을 때 */
.featured-card {
  border: 2px solid #6c5ce7;
  background: #fdfcff;
}

/* 모바일 대응: 화면이 아주 작아지면 꽉 차게 */
@media (max-width: 600px) {
  .stats-card {
    flex: 1 1 100%;
  }
}
</style>