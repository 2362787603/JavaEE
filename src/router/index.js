import {createRouter, createWebHistory} from 'vue-router'
import LoginView from '@/views/LoginView.vue'
import TestView from '@/views/TestView.vue'
import MainPageView from '@/views/MainPage.vue'
import HomePage from '@/views/HomePage.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'LoginView',
      component: LoginView,
    },
    {
      path: '/Test',
      name: 'TestView',
      component: TestView,
    },
    {
      path: '/MainPage',
      name: 'MainPage',
      component: MainPageView,
    },
    {
      path: '/HomePage',
      name: 'HomePage',
      component: HomePage,
    }
  ],
})

export default router