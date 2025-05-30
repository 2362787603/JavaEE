<template>
    <div class = "background">
        <div class = "test">
            <myComment/>
            <ChangeMessage/>
            <HotSearch/>
            <RightHot />
            <LeftHot  />
            <AIemojinput />
            <HotPost />
        </div>
    </div>
</template>
  
<script setup>
  //import HomePost from '@/components/HomePost.vue';
  import ChangeMessage from '@/components/ChangeMessage.vue';
  import AIemojinput from '@/components/AIemojinput.vue';
  import HotPost from '@/components/HotPost.vue';
  import LeftHot from '@/components/LeftHot.vue';
  import RightHot from '@/components/RightHot.vue';
  import HotSearch from '@/components/HotSearch.vue';
  import myComment from '@/components/myComment.vue';
</script>
  
<style scoped>

.test {
  /* 使用相对定位而非固定margin */
  padding-right: 10%; /* 距离右侧边缘的距离 */
  padding-top: 5%;
  width: 100%;
}

.meteor-demo {
  height: 100%;
  width: 100%;
}
</style>


