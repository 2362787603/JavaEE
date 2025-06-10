import {createRouter, createWebHistory} from 'vue-router'
import LoginView from '@/views/LoginView.vue'
import TestView from '@/views/TestView.vue'
import MainPageView from '@/views/MainPage.vue'
import HomePage from '@/views/HomePage.vue'
import OtherHomePage from '@/views/OtherHomePage.vue'
import SearchView from '@/views/SearchView.vue'
import forumCreate from '@/views/forumCreate.vue'

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
    },
    {
      path: '/OthersHomePage',
      name: 'OtherHomePage',
      component: OtherHomePage,
    },
    {
      path: '/Post/:postId',
      name: 'Post',
      component: () => import('@/views/PostView.vue'),
      props: true
    },
    {
      path:'/createPost',
      name:'createPost',
      component: () => import('@/views/PostCreate.vue'),
      props:true
    },
    {
      path:'/createForum',
      name:'createForum',
      component: forumCreate,
      props:true
    },
    {
      path: '/InnerPost/:postId',
      name: 'InnerPost',
      component: () => import('@/views/ConcretePost.vue'),
      props: true
    },
    {
      path:'/Search',
      name:'Search',
      component: SearchView,
    }
  ],
})

export default router