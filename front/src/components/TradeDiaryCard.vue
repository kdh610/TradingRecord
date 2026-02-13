<script setup>
import { computed } from 'vue';
import { useTradeDiaryStore } from '@/stores/tradeDiary';
import dayjs from 'dayjs';

const tradeDiaryStore = useTradeDiaryStore();
const { saveTradeDiaryAction } = tradeDiaryStore;


// props 정의: 부모(View)로부터 받은 데이터
const props = defineProps({
  tradeDiary: {
    type: Object,
    required: true,
    default: () => ({})
  },
  isDetail: {
    type: Boolean,
    default: false
  },
  date: {
    type: Date,
    required: false
  }
});


function saveTradeDiary() {
  const param = {
    "base_dt": dayjs(props.date).format('YYYYMMDD'),
    "ottks_tp": "1",
    "ch_crd_tp": "0"
  };
  console.log(param.base_dt);
  saveTradeDiaryAction(param);
}

// 금액 포맷 함수 (3자리마다 콤마)
const formatAmount = (val) => {
  if (!val) return '0';
  return Math.floor(val).toLocaleString();
};

// 수익금에 따른 텍스트 색상 결정 (수익은 빨강, 손실은 파랑 - 한국 기준)
const plColor = computed(() => {
  const plAmt = props.tradeDiary?.rlztPl || 0;
  return plAmt > 0 ? 'text-red' : plAmt < 0 ? 'text-blue' : 'text-gray';
});
</script>

<template>
  <div class="diary-card" v-if="tradeDiary?.tradeDay">
    <div class="card-header">
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

    <div v-if="isDetail && tradeDiary.todayTradeItemList?.length" class="detail-section">
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
          <tr v-for="(item, index) in tradeDiary.todayTradeItemList" :key="index">
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
  <div v-else class="no-data">
    {{ date ? date.toLocaleDateString() : '날짜 없음' }}
    데이터를 불러오는 중이거나 기록이 없습니다.
    <button @click="saveTradeDiary(param)">샘플 데이터 저장</button>
  </div>
</template>

<style scoped>
.diary-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-bottom: 20px;
  border: 1px solid #e1e8ed;
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

.no-data {
  text-align: center;
  padding: 50px;
  color: #999;
}
</style>