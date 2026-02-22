<script setup>
import { ref, watch,computed } from 'vue';
import { storeToRefs } from 'pinia';
import { useTradeDiaryStore } from '@/stores/tradeDiary';
import { useOrderLogStore } from '@/stores/orderLog';
import { useDateStore } from '@/stores/dateStore';
import { useMinuteCandleStore } from '@/stores/minuteCandle';
import { useTradeStore } from '@/stores/trade';

import { DatePicker } from 'primevue';
import IftaLabel from 'primevue/iftalabel';

const dateStore = useDateStore();
const {setDate, formatDate } = dateStore;
const { selectedDate} = storeToRefs(dateStore);

const minuteCandleStore = useMinuteCandleStore();
const {  getMinuteCandleAction } = minuteCandleStore;
const { minuteCandles } = storeToRefs(minuteCandleStore);

const tradeDiaryStore = useTradeDiaryStore();
const { saveTradeDiaryAction } = tradeDiaryStore;
const { tradeDiary: tradeDiary } = storeToRefs(tradeDiaryStore);


const orderLogStore = useOrderLogStore();
const {selectOrderLogsAction} = orderLogStore;
const { orderLogs:orderLogs } = storeToRefs(orderLogStore);

const tradeStore = useTradeStore();
const { searchTradeAction } = tradeStore;
const { trades } = storeToRefs(tradeStore);


// 달력 날짜 설정
watch(selectedDate, (newDate) => {
    if (newDate) {
        const formattedDate = formatDate(newDate);
        fetchTradeDiary(formattedDate); 
        orderLogs.value = []; 
        minuteCandles.value = [];
    }
});


// 매매일지 API
function fetchTradeDiary(date) {
    tradeDiaryStore.selectOneTradeDiaryAction(date);
}

function saveTradeDiary() {
  const param = {
    "base_dt": formatDate(selectedDate.value),
    "ottks_tp": "1",
    "ch_crd_tp": "0"
  };
  saveTradeDiaryAction(param);
}

// 주문 체결 로그 & 분봉 차트 API
function selectStockAction(item){
  
  selectOrderLogsAction({
    "stkNm": item.stkNm,
    "start": formatDate(selectedDate.value),
    "end": formatDate(selectedDate.value)
  });

  getMinuteCandleAction({
    "stk_cd": item.stkCd+"_AL",
    "tic_scope": "1",
    "upd_stkpc_tp": "1",
    "base_dt": formatDate(selectedDate.value),
    "start": formatDate(selectedDate.value)
  });

  searchTradeAction({
            "stkNm": item.stkNm,
            "tradingType": "",
            "isWin": "",
            "isStupid": "",
            "start": formatDate(selectedDate.value),
            "end": ""
  });

}

// 금액 포맷 함수 (3자리마다 콤마)
const formatAmount = (val) => {
  if (!val) return '0';
  return Math.floor(val).toLocaleString();
};

// 수익금에 따른 텍스트 색상 결정 (수익은 빨강, 손실은 파랑 - 한국 기준)
const plColor = computed(() => {
  const plAmt = tradeDiary.value?.rlztPl || 0;
  return plAmt > 0 ? 'text-red' : plAmt < 0 ? 'text-blue' : 'text-gray';
});
</script>

<template>
  <div class="card-box" v-if="tradeDiary?.tradeDay">
    <div class="card-header">
      <IftaLabel>
        <DatePicker v-model="selectedDate" inputId="date" showIcon iconDisplay="input" variant="filled" />
        <label for="date">Date</label>
      </IftaLabel>
      <span class="date">{{ tradeDiary.tradeDay }} 매매 기록</span>
      <span :class="['total-pl', plColor]">
        {{ tradeDiary.rlztPl > 0 ? '+' : '' }}{{ formatAmount(tradeDiary.rlztPl) }}원 
        ({{ (tradeDiary.rlztPl / tradeDiary.totBuyAmt * 100).toFixed(2) }}%)
      </span>
    </div>

    <hr class="divider" />

    <div class="card-body">
      <div class="info-grid">
        <div class="info-item">
          <label>총 매수</label>
          <span>{{ formatAmount(tradeDiary.totBuyAmt) }}원</span>
        </div>
        <div class="info-item">
          <label>총 매도</label>
          <span>{{ formatAmount(tradeDiary.totSellAmt) }}원</span>
        </div>
        <div class="info-item">
          <label>실현손익</label>
          <span :class="plColor">{{ formatAmount(tradeDiary.rlztPl) }}원</span>
        </div>
      </div>
    </div>

    <div v-if="tradeDiary.todayTradeItemList?.length" class="detail-section">
      <p class="detail-title">종목별 상세</p>
      <table class="item-table">
        <thead>
          <tr>
            <th>종목명</th>
            <th>매수평균</th>
            <th>매도평균</th>
            <th>수량</th>
            <th>수익률</th>
            <th>손익금</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, index) in tradeDiary.todayTradeItemList" :key="index" @click="selectStockAction(item)">
            <td class="stk-name">{{ item.stkNm }}</td>
            <td>{{ formatAmount(item.buyAvgPric) }}</td>
            <td>{{ formatAmount(item.selAvgPric) }}</td>
            <td>{{ item.buyQty || item.selQty }}</td>
            <td :class="item.prftRt > 0 ? 'text-red' : 'text-blue'">
              {{ item.prftRt }}%
            </td>
            <td :class="item.plAmt > 0 ? 'text-red' : item.plAmt < 0 ? 'text-blue' : 'text-gray'">{{ formatAmount(item.plAmt) }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
  <div v-else class="empty-card">
    <IftaLabel>
      <DatePicker v-model="selectedDate" inputId="date" showIcon iconDisplay="input" variant="filled" />
      <label for="date">Date</label>
    </IftaLabel>
    데이터를 불러오는 중이거나 기록이 없습니다.
    <button @click="saveTradeDiary(param)">샘플 데이터 저장</button>
  </div>
</template>

<style scoped>
.card-box {
  background: white;
  padding: 25px;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.08);
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.date {
  font-size: 1.2rem;
  font-weight: bold;
  color: #2c3e50;
}

.total-pl {
  font-size: 1.1rem;
  font-weight: bold;
}

.divider {
  border: 0;
  height: 1px;
  background: #eee;
  margin: 15px 0;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
}

.info-item {
  display: flex;
  flex-direction: column;
}

.info-item label {
  font-size: 0.85rem;
  color: #7f8c8d;
  margin-bottom: 4px;
}

.info-item span {
  font-weight: 600;
  color: #34495e;
}

/* 상세 섹션 스타일 */
.detail-section {
  margin-top: 25px;
  background: #f9f9f9;
  padding: 15px;
  border-radius: 8px;
}

.detail-title {
  font-weight: bold;
  margin-bottom: 10px;
  color: #2c3e50;
}

.item-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 0.9rem;
}

.item-table th {
  text-align: left;
  color: #7f8c8d;
  border-bottom: 2px solid #ddd;
  padding: 8px 4px;
}

.item-table td {
  padding: 8px 4px;
  border-bottom: 1px solid #eee;
}

.stk-name {
  font-weight: bold;
  color: #2c3e50;
}

/* 공통 색상 */
.text-red { color: #d63031 !important; }
.text-blue { color: #0984e3 !important; }
.text-gray { color: #b2bec3; }

.empty-card { text-align: center; padding: 60px; background: white; border-radius: 12px; color: #94a3b8; border: 2px dashed #e2e8f0; }
</style>