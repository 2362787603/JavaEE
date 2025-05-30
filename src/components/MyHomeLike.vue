<template>
    <div class="wholeComponent">
        <div class="chooseLike">
            <el-button 
                class="choosebutton" 
                :class="{'force-hover': isPostLike }"
                @click="selectPostLike">
                帖子点赞
            </el-button>
            <el-button 
                class="choosebutton" 
                :class="{'force-hover': isCommentLike }"
                @click="selectCommentLike">
                评论点赞
            </el-button>
        </div>
        <div v-if="nowChoose === 'PostLike'">
            <div v-for="i in range(0,10)" :key="i" class="myLike">
                <HomePost HasUserLiked=true />
            </div>
        </div>
        <div v-if="nowChoose === 'CommentLike'">
            <div v-for="i in range(0,10)" :key="i" class="myLikeComment">
                <LoveComment />
            </div>
        </div>
    </div>
</template>


<script setup>
import { ref, computed } from 'vue';
import HomePost from './HomePost.vue';
import LoveComment from './LoveComment.vue';

let nowChoose = ref('PostLike')

// 计算属性来判断当前选择状态
const isPostLike = computed(() => nowChoose.value === 'PostLike')
const isCommentLike = computed(() => nowChoose.value === 'CommentLike')

// 切换选择的方法
const selectPostLike = () => {
    nowChoose.value = 'PostLike'
}

const selectCommentLike = () => {
    nowChoose.value = 'CommentLike'
}

const range = (start, end) => {
    return Array.from({length: end - start}, (_, index) => start + index);
}

</script>


<style scoped>
.wholeComponent {
    width: 100%;
}

.chooseLike {
    margin-left: 120px;
    margin-top: 10px;
    display: flex;
    flex-direction: row;
    gap: 0;
}

.choosebutton {
    width: 380px;
    height: 50px;
    border-radius: 0;
    margin: 0;
    transition: all 0.3s ease; /* 添加过渡效果 */
}

.myLike {
    margin-top: 10px;
    margin-left: 120px;
    width: 140%;
}

.myLikeComment{
    margin-left:110px;
    margin-top:15px;
    width:805px;
}

.choosebutton.force-hover {
    background-color: #ecf5ff !important;
    color: #409eff !important;
    border-color: #c6e2ff !important;
}
</style>