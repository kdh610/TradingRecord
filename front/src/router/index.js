import { createRouter, createWebHistory } from 'vue-router'
import TradeDiaryView from '../views/TradeDiaryView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: TradeDiaryView,
    },

  ],
})

export default router
