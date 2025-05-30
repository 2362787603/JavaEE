<template>
    <div class="wholeComponent">
        <div v-for="i in range(0,size)" :key="i" class="myLikeComment">
            <div class="messageShow">
                <a :href="getPostId(i)" :to="postpage" @click="handleClick">{{ props.getnames[i] }}</a>
                <p v-if="props.gettype[i] === 'Comment'" class="messageContent">评论了您的帖子</p>
                <p v-if="props.gettype[i] === 'Like'" class="messageContent">赞了您</p>
                <p v-if="props.gettype[i] === 'Reply'" class="messageContent">回复了您</p>
            </div>
            <router-link :to="getPostId(i)">
                <el-button class="detailButton"> 查看详情 </el-button>
            </router-link>
            <p v-if="props.getIsRead[i] === true" class="showUnread">!</p>
        </div>
    </div>
</template>

<script setup>

import { defineProps,ref} from 'vue';

// Define props
const props = defineProps({
  getnames: {
    type:Array,
    default: ()=> ['用户114514','用户111111','用户123456','用户111315','用户114514','用户111111','用户123456','用户111315']
  },
  gettype: {
    type:Array,
    default: ()=>['Comment','Like','Reply','Reply','Comment','Like','Reply','Reply']
  },
  getIsRead: {
    type:Array,
    default: ()=>[false,false,true,true,false,false,true,true]
  },
  getPostId: {
    type:Array,
    default: ()=> [1,1,4,5,1,1,4,5]
  }
});

let size = ref(props.getnames.length)

const range = (start, end) => {
    return Array.from({length: end - start}, (_, index) => start + index);
}

const getPostId =(index) => {
    return "/Test/" + props.getPostId[index];
}

</script>

<style scoped>

.wholeComponent{
    width: 950px;
}

.myLikeComment {
    background-color: white;
    display: flex;
    flex-direction: row;
    border-radius: 4px;
    margin-left:75px;
    margin-top:15px;
    height: 70px;
}

.messageContent {
    margin-top:25px;
    margin-left:20px;
    font-size: 16px !important;
    color: rgb(129, 129, 129) !important;
}

.messageShow {
    display: flex;
    flex-direction: row;
    width: 600px;
}

.messageShow a{
    text-decoration: none; 
    font-size: 18px;
    margin-left:50px;
    margin-top:23px;
    color:rgb(52, 146, 218)
}

.showUnread {
    font-size: 20px;
    font-weight: 800;
    color:rgb(241, 46, 46) !important;
    margin-left: 20px;
}

.detailButton {
    margin-top:15px;
    height: 40px;
    width: 100px;
}

</style>
