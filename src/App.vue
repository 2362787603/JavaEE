
<template>
  <el-container class="container">
    <!-- 左侧导航栏 -->
    <!--<el-aside class="aside">
      <Sidebar/>
    </el-aside>-->
    <el-main class="main">
      <RouterView/>
    </el-main>
  </el-container>
  <!-- <HomeView /> -->
</template>

<script setup>
import {RouterView} from 'vue-router'
//import Sidebar from './components/Sidebar.vue';
</script>


<style scoped>
.container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
}

.aside {
  width: 64px;
  margin: 0;
  padding: 0;
  z-index: 15;
}
.main {
  padding: 0;
}
</style>
