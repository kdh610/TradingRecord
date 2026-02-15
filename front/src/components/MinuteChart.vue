<script setup>
import { onMounted, onUnmounted, watch, ref} from 'vue';
import { useMinuteCandleStore } from '@/stores/minuteCandle';
import { storeToRefs } from 'pinia';
import { createChart, CandlestickSeries, createSeriesMarkers, ColorType } from 'lightweight-charts';
import { useOrderLogStore } from '@/stores/orderLog';
import { useTradeStore } from '@/stores/trade';
import dayjs from 'dayjs';
import { DatePicker } from 'primevue';

const orderLogStore = useOrderLogStore();
const {selectOrderLogsAction} = orderLogStore;
const { orderLogs } = storeToRefs(orderLogStore);

const minuteCandleStore = useMinuteCandleStore();
const {  getMinuteCandleAction } = minuteCandleStore;
const { minuteCandles } = storeToRefs(minuteCandleStore);

const tradeStore = useTradeStore();
const {saveTradeAction} = tradeStore;
const { trades } = storeToRefs(tradeStore);

// 차트에서 기간조회를 위한 날짜 범위
const rangeDates = ref([]);

// 매매를 저장하기 위한 변수들
const tradeType = ref('돌파'); // 기본값
const isStupid = ref(false);   // 기본값 (N)
const startOrderLog = ref(null);
const endOrderLog = ref(null);
const selectedLogs = ref([]);

function fetchRangeDateCandle(){
    const param={
      "stkNm": orderLogs.value[0].stkNm,
      "start": dayjs(rangeDates.value[0]).format('YYYYMMDD'),
      "end": dayjs(rangeDates.value[1]).format('YYYYMMDD')
    }
    selectOrderLogsAction(param);


    let stkCd = orderLogs.value[0]?.stkCd;
    if( orderLogs.value[0].stkCd.startsWith("*")){
      stkCd = orderLogs.value[0].stkCd.substring(1);
    }

    const minuteParam = {
    "stk_cd": stkCd+"_AL",
    "tic_scope": "1",
    "upd_stkpc_tp": "1",
    "base_dt": dayjs(rangeDates.value[1]).format('YYYYMMDD'),
    "start": dayjs(rangeDates.value[0]).format('YYYYMMDD')
  }
  getMinuteCandleAction(minuteParam);

}


// 차트 객체들을 담을 변수
const chartContainer = ref(null);
let chart = null;
let candleSeries = null;
let resizeObserver = null;

//스토어의 데이터를 Lightweight Charts가 요구하는 OHLC 포맷으로 변환합니다. 
const transformData = (input) => {
  if (!input) return [];
  let items =  (input.stk_min_pole_chart_qry || [])

  if (!Array.isArray(items)) {
    console.error("차트 데이터 변환 실패: 배열이 아닙니다.", input);
    return [];
  }
  return items.map(item => {
    try {
      const rawTime = item.cntr_tm;
      const timestamp = new Date(
        rawTime.replace(/(\d{4})(\d{2})(\d{2})(\d{2})(\d{2})(\d{2})/, '$1-$2-$3T$4:$5:$6')
      ).getTime() / 1000;

      const parsePrice = (val) => parseFloat(String(val).replace(/[+-]/g, ''));

      return {
        time: timestamp, // 한국 시간 보정
        open: parsePrice(item.open_pric),
        high: parsePrice(item.high_pric),
        low: parsePrice(item.low_pric),
        close: parsePrice(item.cur_prc),
      };
    } catch (e) {
      return null;
    }
  })
  .filter(item => item !== null && !isNaN(item.time))
  .sort((a, b) => a.time - b.time);
};


// 차트 초기화 함수
const initChart = () => {
  if (!chartContainer.value) return;

  // 1. 차트 생성
  chart = createChart(chartContainer.value, {
    layout: { background: { type: ColorType.Solid, color: '#C0C0C0' } ,textColor: '#334155' },
    grid: { vertLines: { color: '#f1f5f9' }, horzLines: { color: '#f1f5f9' } },
    width: chartContainer.value.clientWidth,
    height: 350,
    localization: {
      locale: 'ko-KR', // 한국 지역 설정
      timeFormatter: (time) => {
        // time은 Unix Timestamp(초 단위)입니다.
        const date = new Date(time * 1000);
        const month = date.getMonth() + 1;
        const day = date.getDate();
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');

        return `${month}/${day} ${hours}:${minutes}`;
    }},
    timeScale: {
      timeVisible: true,
      tickMarkFormatter: (time, tickMarkType, locale) => {
        const date = new Date(time * 1000);
        const month = date.getMonth() + 1;
        const day = date.getDate();
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        if (tickMarkType <= 2) {
          return `${month}월 ${date.getDate()}일`;
        }
        return `${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`;
      }
    },  
  });

  function myClickHandler(param) {
    if (!param.point) {
        return;
    }
    const capturedTime = param.time;
    console.log(`Click at ${param.point.x}, ${param.point.y}. The time is ${capturedTime}.`);
    if(!startOrderLog.value){
      startOrderLog.value = capturedTime
      console.log("startOrderLog set:", startOrderLog.value);
    }
    else if(!endOrderLog.value){
      endOrderLog.value = capturedTime
      console.log("endOrderLog set:", endOrderLog.value);
    }
    if(startOrderLog.value && endOrderLog.value){
      filterLogsByRange();
      startOrderLog.value = null;
      endOrderLog.value = null;
    }
  }
  chart.subscribeClick(myClickHandler);

  resizeObserver = new ResizeObserver(entries => {
    if (entries.length === 0 || !chart) return;
    const { width } = entries[0].contentRect;
    chart.applyOptions({ width });
  });
  resizeObserver.observe(chartContainer.value);
    
  // 2. 시리즈 추가 
    candleSeries = chart.addSeries(CandlestickSeries, {
      upColor: '#ef4444',
      downColor: '#3b82f6',
      borderVisible: false,
      wickUpColor: '#ef4444',
      wickDownColor: '#3b82f6',
  });
};

function filterLogsByRange() {
  if (!startOrderLog.value || !endOrderLog.value) return;

  const start = Math.min(startOrderLog.value, endOrderLog.value);
  const end = Math.max(startOrderLog.value, endOrderLog.value);

  // orderLogs에서 시간 범위 내의 데이터 필터링
  // log.tradeTime이 Unix Timestamp(초)라고 가정합니다.
  const filtered = orderLogStore.orderLogs.filter(log => {
    const tradeDateStr = dayjs(log.tradeDay).format('YYYYMMDD');
    const year = tradeDateStr.substring(0, 4);
    const month = tradeDateStr.substring(4, 6);
    const day = tradeDateStr.substring(6, 8);

    const timeOnly = log.cntrTm.substring(0, 5) + ":00";
    const isoDateTime = `${year}-${month}-${day}T${timeOnly}`;
    const timestamp = new Date(isoDateTime).getTime() / 1000;
  
    return timestamp >= start && timestamp <= end;
  }).map(log =>{
    return log.id
  });

  selectedLogs.value = filtered;
  console.log("선택된 로그:", selectedLogs.value);
  console.log(`선택된 구간 내 로그 개수: ${filtered.length}개`);
}

function handleSaveTrade(){
  const param = {
    "stkNm": orderLogs.value[0].stkNm,
    "tradeType": tradeType.value,
    "isStupid": isStupid.value,
    "orderLogIds": selectedLogs.value
  }
  console.log("매매 저장 파라미터:", param);
  saveTradeAction(param);
}



onMounted(() => {
  initChart();
  
  if (minuteCandles.length > 0) {
    const formattedData = transformData(minuteCandles.value);
    candleSeries.setData(transformData(minuteCandles));
    chart.timeScale().fitContent();
  }
});


let myMarkers = null // 마커 상태를 관리하는 ref

const updateMarkers = () => {
  const markers = orderLogs.value.map(log => {
    // 1. 시간 변환 (로그의 시간 형식을 차트의 timestamp와 맞춤)
    const tradeDateStr = dayjs(log.tradeDay).format('YYYYMMDD');
    // console.log(`원본 체결시간: ${log.cntrTm}, 변환 전 문자열: ${tradeDateStr} ${log.cntrTm}`);
    const year = tradeDateStr.substring(0, 4);
    const month = tradeDateStr.substring(4, 6);
    const day = tradeDateStr.substring(6, 8);

    const timeOnly = log.cntrTm.substring(0, 5) + ":00";
    const isoDateTime = `${year}-${month}-${day}T${timeOnly}`;
    const timestamp = new Date(isoDateTime).getTime() / 1000;

    // console.log(`매매시각: ${log.cntrTm} -> 마커부착시각: ${isoDateTime} (TS: ${timestamp})`);
    

    const isBuy = (log.ioTpNm || "").includes('현금매수');
    const isUsed = log.isUsed
    return {
      time: timestamp,
      position: isBuy ? 'inBar' : 'inBar', // 매수는 아래에, 매도는 위에
      color: isUsed ? '#228B22' : '#000000	',     // 매매에 사용되지 않은 주문은 검정색
      shape: isBuy ? 'circle' : 'square',   // 위/아래 화살표
      text: isBuy ? 'B' : 'S',                  // 텍스트 아이콘
      size: 1,
    };
  })
  .sort((a, b) => a.time - b.time); // 시간순 정렬 필수

  myMarkers = createSeriesMarkers(candleSeries, markers);
};


/**
 * [반응형 업데이트]
 * 종목 클릭으로 스토어의 minuteCandles가 바뀌면 자동으로 실행됩니다.
 */
watch(minuteCandles, (newData) => {
  if (newData && candleSeries) {
    const formattedData = transformData(newData);
    candleSeries.setData(formattedData);

    setTimeout(() => {
      myMarkers && myMarkers.setMarkers([]); // 기존 마커 제거
      updateMarkers();
    }, 100);

    chart.timeScale().fitContent();
  }
}, { deep: true });

onUnmounted(() => {
  if (resizeObserver) {
    resizeObserver.disconnect();
  }
  if (chart) { chart.remove(); chart = null; }
});

</script>

<template>
  <div class="chart-card">
    <div class="chart-header">
    <h3 v-if="minuteCandles?.stk_min_pole_chart_qry?.length > 0">
      <DatePicker v-model="rangeDates" placeholder="기간선택" selectionMode="range" :manualInput="false"  />
      <button @click="fetchRangeDateCandle">기간 조회</button>
       <span class="title"> 📈 {{orderLogs[0]?.stkNm }} 1분봉 차트</span>
    </h3>
      <h3 v-else>📈  종목을 선택해 주세요</h3>
    </div>
     
    <div class="chart-wrapper">
      <div ref="chartContainer" class="chart-container"></div>
      
      <div v-if="selectedLogs.length > 0" class="trade-save-area">
          <div class="input-group">
            <span class="label">🎯 매매 유형:</span>
            <div class="chip-group">
              <button 
                v-for="type in ['돌파', '눌림', '스윙', '종베', '상따']" 
                :key="type"
                :class="{ active: tradeType === type }"
                @click="tradeType = type"
              >
                {{ type }}
              </button>
            </div>
          </div>

          <div class="input-group">
            <span class="label">🧠 뇌동 여부:</span>
            <label class="switch">
              <input type="checkbox" v-model="isStupid">
              <span class="slider"></span>
            </label>
            <span class="switch-text">{{ isStupid ? 'Y (뇌동매매 😱)' : 'N (원칙매매 😎)' }}</span>
          </div>

          <div class="action-group">
            <span class="count-info">선택된 로그: <strong>{{ selectedLogs.length }}</strong>개</span>
            <button class="save-btn" @click="handleSaveTrade">매매 저장</button>
          </div>
        </div>


      <div v-if="minuteCandles.length === 0" class="no-data-overlay">
        <p>왼쪽 목록에서 종목을 클릭하면<br>분봉 차트가 여기에 표시됩니다.</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.chart-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.05);
  border: 1px solid #f1f5f9;
}

.chart-header h3 {
  margin: 0 0 15px 0;
  font-size: 1.1rem;
  color: #1e293b;
}

.chart-wrapper {
  position: relative;
  width: 100%;
}

.chart-container {
  width: 100%;
}

.no-data-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 350px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #fcfcfc;
  border: 1px dashed #e2e8f0;
  border-radius: 8px;
  text-align: center;
  color: #94a3b8;
  font-size: 0.95rem;
  line-height: 1.6;
}

.title {
  font-size: 1.2rem;
  font-weight: bold;
  color: #2c3e50;
}


.trade-save-area {
  margin-top: 15px;
  padding: 15px;
  background: #f8fafc;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
}

.input-group {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.label {
  font-weight: bold;
  margin-right: 15px;
  color: #475569;
  min-width: 80px;
}

/* 매매유형 칩 스타일 */
.chip-group button {
  padding: 6px 14px;
  margin-right: 8px;
  border-radius: 20px;
  border: 1px solid #cbd5e1;
  background: white;
  cursor: pointer;
  transition: all 0.2s;
}

.chip-group button.active {
  background: #3b82f6;
  color: white;
  border-color: #3b82f6;
}

/* 뇌동여부 스위치 스타일 */
.switch {
  position: relative;
  display: inline-block;
  width: 50px;
  height: 24px;
}

.switch input { opacity: 0; width: 0; height: 0; }

.slider {
  position: absolute;
  cursor: pointer;
  top: 0; left: 0; right: 0; bottom: 0;
  background-color: #ccc;
  transition: .4s;
  border-radius: 34px;
}

.slider:before {
  position: absolute;
  content: "";
  height: 16px; width: 16px;
  left: 4px; bottom: 4px;
  background-color: white;
  transition: .4s;
  border-radius: 50%;
}

input:checked + .slider { background-color: #ef4444; } /* 뇌동일 땐 빨간색! */
input:checked + .slider:before { transform: translateX(26px); }

.switch-text { margin-left: 10px; font-size: 0.9rem; color: #64748b; }

.save-btn {
  background: #1e293b;
  color: white;
  padding: 10px 20px;
  border-radius: 6px;
  border: none;
  font-weight: bold;
  cursor: pointer;
}

.save-btn:hover { background: #0f172a; }
</style>