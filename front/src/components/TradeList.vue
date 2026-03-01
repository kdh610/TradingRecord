<script setup>
import { ref,watch } from 'vue';
import { useTradeStore } from '@/stores/trade';
import { storeToRefs } from 'pinia'; 
import { useDateStore } from '@/stores/dateStore';
import Menu from 'primevue/menu'
import Button from 'primevue/button';
import MarkdownIt from 'markdown-it'; // 추가

const md = new MarkdownIt({
  breaks: true, // 줄바꿈 반영
  linkify: true // URL 자동 링크
});

const renderMarkdown = (text) => {
  if (!text) return '';
  return md.render(text);
};

const tradeStore = useTradeStore();
const { searchTradeAction, deleteTradeAction, saveCommentAction } = tradeStore;
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

const menu = ref(null); // 🟢 템플릿의 ref="menu"와 연결됨
const selectedTradeId = ref(null); // 어떤 카드의 메뉴가 눌렸는지 저장

const items = ref([
    {
        label: 'Options',
        items: [
            {
                label: '삭제하기',
                icon: 'pi pi-trash',
                command: async () => {
                    await deleteTradeAction(selectedTradeId.value);
                    
                    await tradeStore.searchTradeAction({
                      "stkNm": "",
                      "tradingType": "",
                      "isWin": "",
                      "isStupid": "",
                      "start": formatDate(selectedDate.value),
                      "end": ""
                    });
                }
            }
        ]
    }
]);
const toggle = (event, id) => {
    selectedTradeId.value = id; // 현재 클릭된 trade.id 저장
    menu.value.toggle(event);   // 해당 버튼 위치에 메뉴 띄우기
};

async function saveComment(trade){
    console.log("trade:", trade.id, trade.stkNm, formatDate(trade.tradeDay));
    const response = await saveCommentAction({
      "id": trade.id,
      "stkNm": trade.stkNm,
      "tradeType": trade.tradingType,
      "plAmt": trade.plAmt,
      "stupid": trade.stupid,
      "review": trade.review,
      "trade_day": formatDate(trade.tradeDay),      
    });

    await tradeStore.searchTradeAction({
      "stkNm": "",
      "tradingType": "",
      "isWin": "",
      "isStupid": "",
      "start": formatDate(selectedDate.value),
      "end": ""
    });
    console.log("저장된 한줄평:", response); // 응답 확인용 로그
    trade.comment = response; // 응답에서 업데이트된 comment 가져와서 trade 객체에 반영

}


</script>

<template>
  <div class="trade-list-container">
    <div 
      v-for="trade in trades" 
      :key="trade.id" 
      class="trade-card" 
      :class="trade.winLose ? 'win' : 'lose'"
    >

    <div class="more-menu-container">

        <Button type="button" icon="pi pi-ellipsis-v" @click="toggle($event, trade.id)" aria-haspopup="true" aria-controls="overlay_menu" class="p-button-text p-button-secondary kebab-btn"/>

    </div>
    
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
            

            <div class="ai-report-box" v-if="trade.comment">
              <div class="ai-header">
                <span class="ai-icon">🤖</span>
                <span class="label">AI 코치 분석 결과</span>
              </div>
              <div class="markdown-body" v-html="renderMarkdown(trade.comment)"></div>
            </div>

            <div class="comment-box" v-else>
              <span class="label">💬 AI 분석:</span> 아직 분석된 내용이 없습니다.
              <button class="ai-btn" @click="saveComment(trade)">AI 팩폭 리포트 생성 🤖</button>
            </div>
            
            <div class="review-box">
              <span class="label">📝 상세 복기:</span>
              <p class="review-text">{{ trade.review || '작성된 복기가 없습니다.' }}</p>
            </div>
          </div>
        </div>
      </transition>
    </div>
    <Menu ref="menu" id="overlay_menu" :model="items" :popup="true" />
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
  position: relative; 
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

/* 케밥 메뉴 컨테이너 위치 잡기 */
.more-menu-container {
  position: absolute;
  top: 8px;
  right: 8px;
  z-index: 2; /* 카드 내용보다 위에 위치 */
}

/* 케밥 버튼 커스텀 */
:deep(.kebab-btn) {
  width: 28px !important;
  height: 28px !important;
  padding: 0 !important;
  min-width: auto !important;
  color: #94a3b8 !important; /* 기본 색상: 연한 그레이 */
  border-radius: 50% !important;
  transition: all 0.2s ease;
}

/* 마우스 호버 시 배경과 색상 변경 */
:deep(.kebab-btn:hover) {
  background: rgba(0, 0, 0, 0.05) !important;
  color: #475569 !important;
}

/* 클릭 시 생기는 링(Focus) 제거 (선택 사항) */
:deep(.kebab-btn:focus) {
  box-shadow: none !important;
}

/* --- PrimeVue Menu(드롭다운) 스타일링 --- */
/* v-for 밖의 Menu 컴포넌트를 이쁘게 깎아봅시다 */
:deep(.p-menu) {
  min-width: 120px;
  border: 1px solid #e2e8f0;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  padding: 4px;
}

:deep(.p-menuitem-link) {
  padding: 8px 12px !important;
  border-radius: 4px;
}

/* 삭제 버튼(빨간색 강조) */
:deep(.p-menuitem:last-child .p-menuitem-link .p-menuitem-text),
:deep(.p-menuitem:last-child .p-menuitem-link .p-menuitem-icon) {
  color: #ef4444 !important; /* 삭제 버튼은 레드 계열로 강조 */
}

:deep(.p-menuitem-link:hover) {
  background: #f8fafc !important;
}

.ai-report-box {
  background-color: #fcfcfc;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 15px;
  box-shadow: inset 0 2px 4px rgba(0,0,0,0.02);
}

.ai-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  border-bottom: 2px solid #6c5ce7;
  padding-bottom: 5px;
}

.ai-icon { font-size: 1.2rem; }

/* 🟢 마크다운 내부 요소 스타일링 */
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

/* AI 생성 버튼 */
.ai-btn {
  background: #6c5ce7;
  color: white;
  border: none;
  padding: 5px 12px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.8rem;
  margin-left: 10px;
}
</style>