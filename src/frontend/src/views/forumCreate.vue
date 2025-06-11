<template>
    <div class="wholeComponent">
        <div class="home_scrollable-content" ref="scrollContainer">
            <HotSearch :user-id="userId"/>
            <div class="createForm"> <!-- 修正类名拼写错误 -->
                <div class="form-header">
                    <h1 class="myTitle">创建新论坛</h1>
                    <p class="subtitle">创建一个属于你的社区，开始与志同道合的人交流吧！</p>
                </div>
                <CreateForum class="myForum"/>
            </div>
        </div>
    </div>
</template>

<script setup>

import CreateForum from '@/components/CreateForum.vue';
import HotSearch from '@/components/HotSearch.vue';
import { useRoute } from 'vue-router'
import { computed} from 'vue'

const route = useRoute()
const userId = computed(() => {
  const raw = route.query.userId
  if (!raw) return ''               // 没有就返回空字符串
  return raw
})

</script>

<style scoped>
.wholeComponent {
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  position: relative;
  height: 100vh;
  width: 100%;
  background-color: rgb(172, 207, 241);
}

.createForm { /* 修正类名拼写错误 */
    background-color: rgb(252, 252, 252);
    width: 1000px;
    margin-left: 330px;
    padding-top: 1px; /* 防止边距折叠 */
    margin-top:20px;
}

.home_scrollable-content {
  height: 100%;
  overflow-y: auto;
  overflow-x: hidden;
  padding-right: 0px;
}

.form-container {
    max-width: 800px;
    width: 100%;
}

.form-header {
    text-align: center;
    margin: 20px 40px; /* 调整边距，使用上下左右内边距 */
}

.myTitle {
    font-size: 28px;
    margin: 20px 0; /* 可以自由调节上下边距 */
    color: #303133;
}

.subtitle {
    color: #606266;
    font-size: 16px;
    margin-bottom: 15px;
}
</style>    