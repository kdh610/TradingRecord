<script setup>
import { computed } from 'vue';


const props = defineProps({
  tradeType: String,    // 예: "전체 매매", "돌파 매매"
  trades: Array     // 필터링된 매매 리스트
});


const getStats = (list) =>{
    if (!list || list.length === 0) {
        return { count: 0, win: 0, loss: 0, rate: 0, money: 0 };
    }
    const totlalTrades = list.length;
    const winTrades = list.filter(trade => trade.winLose == true).length;
    const lossTrades = totlalTrades - winTrades;
    const winRate = totlalTrades > 0 ? (winTrades / totlalTrades * 100).toFixed(2) : 0;
    const totalMoney = list.reduce((sum, t) => sum + (t.plAmt || 0), 0);
    const winMoney = list.filter(trade => trade.winLose == true).reduce((sum, t) => sum + (t.plAmt || 0), 0);
    const lossMoney = list.filter(trade => trade.winLose == false).reduce((sum, t) => sum + (t.plAmt || 0), 0);
    return {
        count: totlalTrades,
        win: winTrades,
        loss: lossTrades,
        rate: winRate,
        money: totalMoney,
        winMoney: winMoney,
        lossMoney: lossMoney
    };
}

const stats = computed(() => {
    console.log("Calculating stats for trades:", props.trades);

    return getStats(props.trades);
});

const whatIfStat = computed(() =>{
    if (!props.trades || props.trades.length === 0) {
        return { count: 0, win: 0, loss: 0, rate: 0, money: 0 };
    }
    const whatIfTrades = props.trades.filter(trade => !trade.isStupid);
    return getStats(whatIfTrades);
})


const format = (val) => Math.floor(val || 0).toLocaleString();
</script>

<template>
  <div class="stats-card">
    <div class="card-title">{{ tradeType }}</div>
    
    <div class="actual-section">
      <div class="record-row">
        {{ stats.count }}전 {{ stats.win }}승 {{ stats.loss }}패 ({{ stats.rate }}%)
      </div>
      <div class="money-row">
        총 {{ format(stats.money) }}원 : 
        <span class="text-red">+{{ format(stats.plus) }}원</span> / 
        <span class="text-blue">{{ format(stats.minus) }}원</span>
      </div>
    </div>

    <hr class="stat-divider" />

    <div class="what-if-section">
      <p class="what-if-label">뇌동이 없다면? 💡</p>
      <div class="what-if-record">
        {{ whatIfStat.count }}전 {{ whatIfStat.win }}승 {{ whatIfStat.loss }}패 ({{ whatIfStat.rate }}%)
      </div>
      <div class="what-if-money">
        총 {{ format(whatIfStat.money) }}원 : 
        <span class="text-red">+{{ format(whatIfStat.plus) }}원</span> / 
        <span class="text-blue">{{ format(whatIfStat.minus) }}원</span>
      </div>
    </div>
  </div>
</template>

<style scoped>
.stats-card {
  flex: 0 0 auto; /* 가로 스크롤이나 그리드를 위해 크기 고정 안함 */
  min-width: 280px;
  border: 1px solid #e2e8f0;
  padding: 16px;
  border-radius: 12px;
  background: white;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}

.card-title { font-weight: bold; font-size: 1rem; margin-bottom: 12px; color: #1e293b; }

/* 실제 성적 영역 */
.record-row { font-size: 1.1rem; font-weight: 800; margin-bottom: 4px; }
.money-row { font-size: 0.85rem; color: #64748b; font-weight: 600; }

.stat-divider { border: 0; border-top: 1px solid #f1f5f9; margin: 12px 0; }

/* 뇌동 제거 영역 (작게) */
.what-if-label { font-size: 0.75rem; font-weight: bold; color: #7f8c8d; margin-bottom: 4px; }
.what-if-record { font-size: 0.85rem; font-weight: 600; color: #475569; }
.what-if-money { font-size: 0.75rem; color: #94a3b8; }

.text-red { color: #ef4444 !important; }
.text-blue { color: #3b82f6 !important; }
</style>