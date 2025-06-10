<template>
    <div class = "background">
        <div class = "test">
            <TestLogin />
        </div>
    </div>
</template>
  
<script setup>
  import TestLogin from '../components/TestLogin.vue'
</script>
  
<style scoped>
.background {
  /* 设置背景图片 */
  background-image: url('../assets/BackGround.png');
  background-size: cover; /* 覆盖整个区域 */
  background-position: center; /* 居中显示 */
  background-repeat: no-repeat;
  
  /* 全屏设置 */
  width: 100vw; /* 100%视口宽度 */
  height: 100vh; /* 100%视口高度 */
  position: fixed; /* 固定位置,确保全屏 */
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  
  /* 处理内容布局 */
  display: flex;
  justify-content: flex-end; /* 将内容放在右侧 */
}

.test {
  /* 使用相对定位而非固定margin */
  padding-right: 35%; /* 距离右侧边缘的距离 */
  padding-top: 10%;
}
</style>


