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
import MarkdownIt from 'markdown-it'; // 추가

const md = new MarkdownIt({
  breaks: true, // 줄바꿈 반영
  linkify: true // URL 자동 링크
});

const renderMarkdown = (text) => {
  if (!text) return '';
  return md.render(text);
};


const dateStore = useDateStore();
const {setDate, formatDate } = dateStore;
const { selectedDate} = storeToRefs(dateStore);

const minuteCandleStore = useMinuteCandleStore();
const {  getMinuteCandleAction } = minuteCandleStore;
const { minuteCandles } = storeToRefs(minuteCandleStore);

const tradeDiaryStore = useTradeDiaryStore();
const { saveTradeDiaryAction, saveMarketTrendAction, saveOverallReviewAction} = tradeDiaryStore;
const { tradeDiary: tradeDiary } = storeToRefs(tradeDiaryStore);


const orderLogStore = useOrderLogStore();
const {selectOrderLogsAction} = orderLogStore;
const { orderLogs:orderLogs } = storeToRefs(orderLogStore);

const tradeStore = useTradeStore();
const { searchTradeAction } = tradeStore;
const { trades } = storeToRefs(tradeStore);
const marketTrend = ref('');  

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


const isTrendExpanded = ref(false); // 시황 아코디언 상태

const toggleTrend = () => {
  isTrendExpanded.value = !isTrendExpanded.value;
};
function handleSaveMarketTrend(){
  saveMarketTrendAction(marketTrend.value, tradeDiary.value.id);
  tradeDiary.value.marketTrend = marketTrend.value;
}

const isOverallReviewExpanded = ref(false); // 총평 아코디언 상태
const toggleOverallReview = () => {
  isOverallReviewExpanded.value = !isOverallReviewExpanded.value;
};
function handleSaveOverallReview(){
  console.log("Saving overall review for date:", formatDate(tradeDiary.value.tradeDay));
  tradeDiary.value.overallReview =saveOverallReviewAction(formatDate(tradeDiary.value.tradeDay));
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
    
  <div class="trend-accordion-header" @click="toggleTrend">
    <div class="header-left">
      <span class="ai-icon">🌍</span>
      <span class="label">당일 시황 기록</span>
      <span v-if="tradeDiary.marketTrend" class="status-dot"></span> </div>
    <button class="expand-btn">
      <span class="arrow" :class="{ 'is-active': isTrendExpanded }">▼</span>
    </button>
  </div>

  <transition name="expand">
    <div v-if="isTrendExpanded" class="trend-content-wrapper">
      <div class="trend-inner">
        <div v-if="tradeDiary.marketTrend" class="trend-display">
          <p class="trend-text">{{ tradeDiary.marketTrend }}</p>
          <button class="edit-mini-btn" @click="prepareEditTrend">수정하기</button>
        </div>

        <div v-else class="trend-input">
          <textarea 
            v-model="marketTrend" 
            placeholder="오늘 시장의 주요 테마나 지수 흐름을 기록하세요."
            class="styled-textarea"></textarea>
          <div class="btn-group">
            <button class="save-btn" @click="handleSaveMarketTrend">시황 저장</button>
          </div>
        </div>
      </div>
    </div>
  </transition>


 <div class="trend-accordion-header" @click="toggleOverallReview">
    <div class="header-left">
      <span class="ai-icon">🌍</span>
      <span class="label">당일 매매 총평</span>
      <span v-if="tradeDiary.overallReview" class="status-dot"></span> </div>
    <button class="expand-btn">
      <span class="arrow" :class="{ 'is-active': isOverallReviewExpanded }">▼</span>
    </button>
  </div>

  <transition name="expand">
    <div v-if="isOverallReviewExpanded" class="trend-content-wrapper">
      <div class="trend-inner">
        <div v-if="tradeDiary.overallReview" class="trend-display">
          <div class="markdown-body" v-html="renderMarkdown(tradeDiary.overallReview)"></div>
        
        </div>

        <div v-else class="trend-input">
          <div class="btn-group">
            <button class="save-btn" @click="handleSaveOverallReview">총평 저장</button>
          </div>
        </div>
      </div>
    </div>
  </transition>



  <hr class="divider" />


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

.market-trend-section {
  margin-bottom: 20px;
}

/* 시황 출력 박스 */
.trend-display-box {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 15px;
  border-left: 4px solid #4a90e2; /* 시황은 파란색 포인트 */
}

.trend-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.trend-text {
  font-size: 0.95rem;
  line-height: 1.6;
  color: #333;
  white-space: pre-wrap; /* 줄바꿈 유지 */
  margin: 0;
}

.edit-btn {
  background: transparent;
  border: 1px solid #ddd;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 0.75rem;
  cursor: pointer;
}

.edit-btn:hover {
  background: #eee;
}

/* 입력창 스타일 */
.styled-textarea {
  width: 100%;
  height: 80px;
  border-radius: 8px;
  border: 1px solid #ddd;
  padding: 10px;
  resize: none;
  font-family: inherit;
}

.trend-accordion-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 15px;
  background: #f8faff;
  cursor: pointer;
  border-radius: 8px 8px 0 0;
  transition: background 0.2s;
}

.trend-accordion-header:hover {
  background: #edf2ff;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 8px;
}

.status-dot {
  width: 6px;
  height: 6px;
  background-color: #4a90e2;
  border-radius: 50%;
}

/* 아코디언 내부 내용 */
.trend-content-wrapper {
  overflow: hidden; /* transition을 위해 필수 */
  background: #f8faff;
  border-bottom: 1px solid #eee;
}

.trend-inner {
  padding: 0 15px 15px 15px;
}

.trend-display {
  position: relative;
}

.trend-text {
  font-size: 0.9rem;
  color: #444;
  line-height: 1.6;
  white-space: pre-wrap;
  background: white;
  padding: 12px;
  border-radius: 6px;
  border: 1px solid #eef;
}

.edit-mini-btn {
  margin-top: 8px;
  font-size: 0.75rem;
  color: #888;
  background: none;
  border: none;
  text-decoration: underline;
  cursor: pointer;
}

/* 화살표 애니메이션 (이미 기존에 있다면 재사용 가능) */
.arrow {
  display: inline-block;
  transition: transform 0.3s ease;
}
.arrow.is-active {
  transform: rotate(180deg);
}

/* 입력창 및 저장 버튼 */
.btn-group {
  display: flex;
  justify-content: flex-end;
  margin-top: 8px;
}

.save-btn {
  background: #4a90e2;
  color: white;
  border: none;
  padding: 6px 15px;
  border-radius: 4px;
  cursor: pointer;
}

:deep(.markdown-body) {
  font-size: 0.9rem;
  line-height: 1.7;
  color: #2d3436;
}

:deep(.markdown-body strong) {
  color: #d63031; /* 강조(별표)는 빨간색으로 - 뼈 때리는 느낌 */
  background: #fff5f5;
  padding: 0 2px;
}

:deep(.markdown-body ul) {
  padding-left: 20px;
  margin: 10px 0;
}

:deep(.markdown-body li) {
  margin-bottom: 5px;
}

:deep(.markdown-body p) {
  margin-bottom: 10px;
}
</style>