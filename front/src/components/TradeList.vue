<script setup>
import { ref,watch } from 'vue';
import { useTradeStore } from '@/stores/trade';
import { storeToRefs } from 'pinia'; 
import { useDateStore } from '@/stores/dateStore';

const tradeStore = useTradeStore();
const { searchTradeAction } = tradeStore;
const { trades } = storeToRefs(tradeStore);


const dateStore = useDateStore();
const {setDate, formatDate } = dateStore;
const { selectedDate} = storeToRefs(dateStore);


watch(selectedDate, (newDate) => {
    if (newDate) {
        expandedIds.value = [];
        const formattedDate = formatDate(newDate);
        searchTradeAction({
            "stkNm": "",
            "tradingType": "",
            "isWin": "",
            "isStupid": "",
            "start": formattedDate,
            "end": ""
        });
    
    }
});

const expandedIds = ref([]);
const toggleExpand = (id) => {
  const index = expandedIds.value.indexOf(id);
  if (index > -1) {
    expandedIds.value.splice(index, 1); // 닫기
  } else {
    expandedIds.value.push(id); // 열기
  }
};

const isExpanded = (id) => expandedIds.value.includes(id);

const formatAmount= (val) => {
  if (val === undefined || val === null) return '0';
  return val.toLocaleString();
};

</script>

<template>
  <div class="trade-list-container">
    <div 
      v-for="trade in trades" 
      :key="trade.id" 
      class="trade-card" 
      :class="trade.winLose ? 'win' : 'lose'"
    >
    
      <div class="card-main">
        <div class="status-col">
          <span class="result-text">{{ trade.winLose ? '승리' : '패배' }}</span>
          <span class="trading-type">{{ trade.tradingType }}</span>
          <div class="stupid-badge" v-if="trade.stupid">뇌동 😱</div>
        </div>

        <div class="info-col">
          <div class="stk-name">{{ trade.stkNm }}</div>
          <div class="trade-date">{{ trade.tradeDay }}</div>
        </div>

        <div class="profit-col">
          <div class="pl-amount"
          :class="trade.winLose ? 'text-red' : 'text-blue'">
            {{ trade.plAmt > 0 ? '+' : '' }}{{ formatAmount(trade.plAmt) }}원
          </div>
          <div class="profit-rate">{{ trade.prftRt }}%</div>
        </div>

        <div class="action-col">
          <button @click="toggleExpand(trade.id)" class="expand-btn">
            <span class="arrow" :class="{ 'is-active': isExpanded(trade.id) }">▼</span>
          </button>
        </div>
      </div>

      <transition name="expand">
        <div v-if="isExpanded(trade.id)" class="card-detail">
          <div class="detail-content">
            <div class="comment-box" v-if="trade.comment">
              <span class="label">💬 한줄평:</span> {{ trade.comment }}
            </div>
            <div class="review-box">
              <span class="label">📝 상세 복기:</span>
              <p class="review-text">{{ trade.review || '작성된 복기가 없습니다.' }}</p>
            </div>
          </div>
        </div>
      </transition>
    </div>
  </div>
</template>

<style scoped>
.trade-list-container {
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 10px;
}

/* 카드 기본 스타일 */
.trade-card {
  border-radius: 4px;
  border-left: 6px solid #ccc;
  overflow: hidden;
  background-color: #fff;
  transition: transform 0.1s;
}

/* 승리 (RED) 테마 */
.trade-card.win {
  background-color: #fff1f1; /* 매우 연한 빨강 배경 */
  border-left-color: #d63031; /* 강한 빨강 사이드 바 */
}
.trade-card.win .result-text { color: #d63031; }
.trade-card.win .expand-btn { background-color: #f8d7da; }

/* 패배 (BLUE) 테마 */
.trade-card.lose {
  background-color: #f1f7ff; /* 매우 연한 파랑 배경 */
  border-left-color: #0984e3; /* 강한 파랑 사이드 바 */
}
.trade-card.lose .result-text { color: #0984e3; }
.trade-card.lose .expand-btn { background-color: #cfe2ff; }

/* 내부 레이아웃 */
.card-main {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  min-height: 80px;
}

.status-col { width: 100px; display: flex; flex-direction: column; }
.result-text { font-weight: 800; font-size: 0.85rem; }
.trading-type { font-weight: bold;font-size: 0.95rem; color: #666; }
.stupid-badge { font-size: 0.7rem; color: #fff; background: #e84118; border-radius: 10px; padding: 2px 6px; width: fit-content; margin-top: 4px; }

.info-col { flex: 1; padding-left: 20px; }
.stk-name { font-size: 1.1rem; font-weight: bold; color: #2c3e50; }
.trade-date { font-weight: bold; font-size: 0.95rem; color: #95a5a6; }

.profit-col { width: 150px; text-align: right; padding-right: 20px; }
.pl-amount { font-weight: bold; font-size: 1rem; }
.profit-rate {font-weight: bold; font-size: 0.95rem; color: #7f8c8d; }

/* 확장 버튼 애니메이션 */
.action-col { width: 40px; }
.expand-btn {
  width: 30px;
  height: 30px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.arrow { display: inline-block; transition: transform 0.3s; font-size: 0.7rem; }
.arrow.is-active { transform: rotate(180deg); }

/* 상세 영역 스타일 */
.card-detail {
  background-color: rgba(255, 255, 255, 0.5);
  border-top: 1px solid rgba(0, 0, 0, 0.05);
}
.detail-content { padding: 15px 20px; font-size: 0.9rem; line-height: 1.6; }
.label { font-weight: bold; color: #555; margin-right: 8px; }
.review-text { white-space: pre-wrap; margin-top: 8px; color: #2d3436; }

/* Vue transition */
.expand-enter-active, .expand-leave-active { transition: all 0.3s ease-in-out; max-height: 400px; }
.expand-enter-from, .expand-leave-to { opacity: 0; max-height: 0; }

.text-red { color: #d63031 !important; }
.text-blue { color: #0984e3 !important; }
</style>