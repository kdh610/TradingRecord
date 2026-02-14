<script setup>
import { useOrderLogStore } from '@/stores/orderLog';
import { storeToRefs } from 'pinia';

const orderLogStore = useOrderLogStore();
const { orderLogs } = storeToRefs(orderLogStore);

const formatAmount = (val) => {
  if (!val) return '0';
  return Math.floor(val).toLocaleString();
};

</script>

<template>
  <div class="table-container" v-if="orderLogs && orderLogs.length > 0">
    <table class="financial-table">
      <thead>
        <tr>
          <th>체결시간</th>
          <th>종목명</th>
          <th>구분</th>
          <th class="text-right">체결단가</th>
          <th class="text-right">체결수량</th>

        </tr>
      </thead>
      <tbody>
        <tr v-for="(log, index) in orderLogs" :key="index" class="hover-row">
          <td>{{ log.cntrTm }}</td>
          <td class="stk-name">{{ log.stkNm }}</td>
          <td>
            <span :class="['badge', log.ioTpNm === '매수' ? 'bg-red' : 'bg-blue']">
              {{ log.ioTpNm }}
            </span>
          </td>
          <td class="text-right">{{ formatAmount(log.cntrUv) }}</td>
          <td class="text-right">{{ log.cntrQty }}</td>
        </tr>
      </tbody>
    </table>
  </div>
    <div v-else class="empty-card">
        
    데이터를 불러오는 중이거나 기록이 없습니다.

  </div>
</template>

<style scoped>
    .table-container {
    width: 100%;
    overflow-x: auto; /* 내용이 넘치면 가로 스크롤 */
    background: #ffffff;
    border-radius: 8px;
    border: 1px solid #e9ecef;
    }

    .financial-table {
    width: 100%;
    border-collapse: collapse; /* 테두리 겹침 방지 */
    font-size: 14px;
    color: #333;
    }

    /* 헤더 스타일 */
    .financial-table th {
    background-color: #f8f9fa;
    color: #6c757d;
    font-weight: 600;
    padding: 12px 15px;
    border-bottom: 2px solid #dee2e6;
    text-align: left;
    }

    /* 셀 스타일 */
    .financial-table td {
    padding: 12px 15px;
    border-bottom: 1px solid #f1f3f5;
    vertical-align: middle;
    }

    /* 종목명 강조 */
    .stk-name {
    font-weight: 700;
    color: #212529;
    }

    /* 마우스 호버 효과 */
    .hover-row:hover {
    background-color: #f8f9fa;
    cursor: pointer;
    }

    /* 숫자 우측 정렬 (금융 데이터 필수) */
    .text-right {
    text-align: right;
    }

    /* 수익/손실 컬러 클래스 (한국 기준) */
    .text-red { color: #d63031 !important; font-weight: 600; }
    .text-blue { color: #0984e3 !important; font-weight: 600; }
    .text-gray { color: #adb5bd; }

    /* 매수/매도 뱃지 스타일 */
    .badge {
    padding: 4px 8px;
    border-radius: 4px;
    font-size: 11px;
    color: white;
    font-weight: bold;
    }
    .bg-red { background-color: #ff7675; }  /* 매수 */
    .bg-blue { background-color: #74b9ff; } /* 매도 */

    /* 데이터 없을 때 */
    .empty-msg {
    padding: 40px;
    text-align: center;
    color: #adb5bd;
    }

    .empty-card { text-align: center; padding: 60px; background: white; border-radius: 12px; color: #94a3b8; border: 2px dashed #e2e8f0; }
</style>