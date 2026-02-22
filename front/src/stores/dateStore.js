import { defineStore } from 'pinia';
import { ref } from 'vue';
import dayjs from 'dayjs';

export const useDateStore = defineStore('date', () => {
  // 전역에서 공유되는 반응형 변수
  const selectedDate = ref(new Date());

  // 상태를 변경하는 인터페이스(Action)
  function setDate(newDate) {
    selectedDate.value = newDate;
  }

  function formatDate(date) {
    return dayjs(date).format("YYYYMMDD");
  }

  return { selectedDate, setDate, formatDate };
});