<template>
    <div class="wholeComponent"> 
        <div class="commentTitle">
            <a :href="href" :to="postpage" @click="handleClick">🗨️ {{ postTitle }} </a>
            <h3>{{ forumName }}</h3>
            <p>{{ time }} &nbsp; 💖{{ myLikeNum }}</p>
        </div>
        <div v-if="props.hasPicture === true" class="pictureClass">
            <pictureBlock2 :images="props.pictureRoute"/>
        </div>
        <div class="comment">
          <div class="content">
            <div v-if="isReply === true" class="ReplyShow">
                <p>回复</p>
                <a :href="href" :to="postpage" @click="handleClick">{{ props.ReplyName }} </a>
            </div>
            <contentBlock :text="longText"/>
          </div>
        </div>
    </div>
</template>

<script setup>

import { defineProps,ref} from 'vue';
import contentBlock from './contentBlock.vue';
import pictureBlock2 from './pictureBlock2.vue';
const props = defineProps({
  getPostTitle: {
    type: String,
    default: () => "你有没有发现,这是一个测试的标题啊"
  },
  getForumName: {
    type:String,
    default: ()=> "飧筱刅吧"
  },
  getCommentTime: {
    type:String,
    default: ()=>"2025-05-06"
  },
  getMyLike: {
    type:Int32Array,
    default: ()=>14
  },
  hasPicture: {
    type:Boolean,
    default: ()=> true
  },
  pictureRoute: {
    type:Array,
    default: ()=>['BackGround.png','LoginBackGroud.png','LoginTestFinal.png']
  },
  isReply :{
    type:Boolean,
    default: ()=>true
  },
  ReplyName: {
    type:String,
    default: ()=> '飧筱刅'
  },
  ReplyId: {
    type:Int32Array,
    default: ()=>1
  }
});

let postTitle = ref(props.getPostTitle)
let forumName = ref(props.getForumName)
let time = ref(props.getCommentTime)
let myLikeNum = ref(props.getMyLike)
let longText=ref(`😄😄😄😄😄😄😄😄😄物语了，这是一个很长的段落文本，可能会超过三行。Vue (发音为 /vjuː/，类似 view) 是一个用于构建用户界面的 JavaScript 框架。它基于标准 HTML、CSS 和 JavaScript 构建，并提供了一个声明式的、组件化的编程模型，帮助你高效地开发用户界面。无论是简单还是复杂的界面，Vue 都可以胜任。Vue 的两个核心功能：声明式渲染和响应性系统。Vue 通过扩展标准 HTML 模板语法来实现声明式渲染，允许我们根据 JavaScript 状态来描述 HTML 应该是什么样子的。当状态改变时，HTML 会自动更新。`);

const href=ref('/Test/Test')

</script>

<style scoped>

.wholeComponent {
    background-color: rgb(250, 249, 249);
    border-radius: 4px;
    width: 900px;
}

.commentTitle {
    margin-top: 10px;
    display: flex;
    flex-direction: row;
}

.commentTitle a{
    text-decoration: none; 
    font-size: 18px;
    margin-left:20px;
    margin-top:20px;
    color:rgb(52, 146, 218)
}

.commentTitle h3{
    margin-top:20px;
    margin-left:20px;
}

.commentTitle p {
    margin-top:24px;
    margin-left:280px;
    color:rgb(150, 151, 151)
}

.content {
  margin-top: -10px;
  margin-left: 20px;
  padding-bottom: 20px;
}
.pictureClass {
  margin-top: -10px;
  margin-left: 20px;
  padding-bottom: 5px;
}

.ReplyShow {
    display: flex;
    flex-direction: row;
    margin-bottom: -20px;
}

.ReplyShow a{
    text-decoration: none; 
    font-size: 18px;
    margin-left:10px;
    margin-top:10px;
    color:rgb(52, 146, 218)
}

.ReplyShow p{
    font-size: 18px;
    margin-top:10px;
}

</style>
