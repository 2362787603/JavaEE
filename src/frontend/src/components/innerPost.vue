<template>
    <div class="wholeComponent">
        <div class="masterPost">
            <div class="masterContent">
                <masterPost/>
                <div v-for="i in range(0,10)" :key="i" class="myLike">
                    <PostComment />
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>

import masterPost from './masterPost.vue';
import PostComment from './PostComment.vue';

const range = (start, end) => {
    return Array.from({length: end - start}, (_, index) => start + index);
}

</script>

<style scoped>

.wholeComponent {
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  position: relative;
  width: 798px;
  min-height: auto;
  background-color: rgb(253, 253, 253);
}


.mastertitle {
    background-color: rgb(248, 248, 248);
    border-left:0.3px solid rgb(230, 230, 230);
    border-right:0.3px solid rgb(230, 230, 230);
    height: 50px;
    display: flex;
    flex-direction: row;
    align-items: center; /* 垂直居中对齐 */
}

.help {
    height: 20px;
    width: 40px;
    border-radius: 6px;
    color:white;
    background-color: rgb(117,89,255);
    margin-left: 10px;
}

.help p{
    margin-top:0;
    font-size: 14px;
    font-weight: 600;
    margin-left: 6px;
}

.titleContent {
    margin-left: 14px;
    max-width: 500px;
    color:rgb(59, 59, 59);
    white-space: nowrap; /* 防止文本换行 */
    overflow: hidden; /* 隐藏超出部分 */
    text-overflow: ellipsis; /* 超出部分显示省略号 */
}

.titleReplyButton {
    height:50px;
    width: 100px;
    border-radius: 0;
    margin-left: auto; /* 这行代码让按钮自动对齐到右侧 */
}


</style>