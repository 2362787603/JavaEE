<template>
    <div class="whole-component">
        <div class="post-container">
            <div class="postpicture">
                <el-image :src="require(`../assets/${imagePath}`)" ></el-image>
            </div>
            <div class="postline">
                <div class="postman">
                    <div class="postfrom">
                        <a class="h3-style" :href="href">{{ postFromName }}</a>
                    </div>
                    <div class="postinformation">
                        <p>🕒 {{ posttime }}</p>
                        <p>👁️ {{ postfollow }}人关注了该吧</p>
                    </div>
                </div>
            </div>
            <div class="follow">
                <button class="follow-button">关注</button>
            </div>
        </div>
        <div class="content-container">
          <a :href="href" :to="postpage" @click="handleClick">### 你有没有发现,这是一个测试qq ###</a>
          <div class="content">
            <contentBlock :text="longText"/>
          </div>
          <div class="picture">
            <pictureBlock1 :images="image"/>
          </div>
        </div>
        <div class="bottonLine">
          <div class="littlePerson">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <circle cx="12" cy="8" r="5" fill="#CCCCCC"/>
              <path d="M3 22C3 17.58 6.58 14 11 14H13C17.42 14 21 17.58 21 22" stroke="#CCCCCC" stroke-width="2" stroke-linecap="round"/>
            </svg>
            <a :href="href" :to="postpage" @click="handleClick">{{ username }}</a>
          </div>
          <div class="comment">
            <span class="comment-icon">
              <i class="fa-regular fa-comment"></i>
            </span>
            <a :href="href" :to="postpage" @click="handleClick">{{ commentNumber }}条评论</a>
          </div>
          <div class="like">
            <span :class="getUserLike" @click="handleLike">
              <i class="fa-regular fa-thumbs-up"></i>
            </span>
            <p>{{ likeNumber }}人点赞</p>
          </div>
        </div>
    </div>
</template>

<script name='HomePost' setup>
import { ref,computed,defineProps} from 'vue'
import contentBlock from './contentBlock.vue'
import pictureBlock1 from './pictureBlock1.vue'
import '@fortawesome/fontawesome-free/css/all.min.css'

const imagePath=ref('LoginTestFinal.png')
const postFromName=ref('飧筱刅吧')
const posttime=ref('13:59')
const postfollow=ref(119)
const href=ref('/Test/Test')
const longText=ref(`这是一个很长的段落文本，可能会超过三行。Vue (发音为 /vjuː/，类似 view) 是一个用于构建用户界面的 JavaScript 框架。它基于标准 HTML、CSS 和 JavaScript 构建，并提供了一个声明式的、组件化的编程模型，帮助你高效地开发用户界面。无论是简单还是复杂的界面，Vue 都可以胜任。Vue 的两个核心功能：声明式渲染和响应性系统。Vue 通过扩展标准 HTML 模板语法来实现声明式渲染，允许我们根据 JavaScript 状态来描述 HTML 应该是什么样子的。当状态改变时，HTML 会自动更新。`);
const image=['BackGround.png','LoginBackGroud.png','LoginTestFinal.png']
const username=ref('我不是隼鸮牕')
const commentNumber=ref(11)
let likeNumber=ref(114)

const props = defineProps({
  HasUserLiked: {
    type: Boolean,
    default: () => false
  }
});

let isUserLike=ref(props.HasUserLiked)

const handleLike = () => {
  if(!isUserLike.value){
    likeNumber.value=likeNumber.value + 1
    isUserLike.value=true
  }
  else{
    likeNumber.value=likeNumber.value - 1
    isUserLike.value=false
  }
}

const getUserLike = computed(() =>{
  return isUserLike.value?'like-icon':"not-like-icon"
})

</script>


<style scoped>
/*
.post-container {

  padding: 12px;
  border: 1px solid #090909;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}
.postpicture {
  margin-left: 10px;
  margin-top: 10px;
  width: 50px;
  height: 50px;
}

.postpicture .el-image {
  width: 100%;
  height: 100%;
  display: block;
}

.postpicture .el-image img {
  width: 100%;
  height: 100%;
  object-fit: cover; 
}*/

.whole-component {
  /*border: 1px solid #090909;*/
  border: 2px solid rgb(249, 248, 248);
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  width: 55%;
  background-color:rgb(249, 248, 248)
}
.post-container {
  display: flex;
  /*align-items: center;*/
  padding: 12px;
  padding-bottom: 6px;
  /*width: 50%;*/
  /*border: 1px solid #090909;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);*/
}

.postpicture {
  margin-left: 8px;
  margin-top: 8px;
  width: 50px;
  height: 50px;
  /*border-radius: 50%;*/
  overflow: hidden; /* 确保内容不会溢出圆形区域 */
}

.postpicture .el-image {
  width: 100%;
  height: 100%;
  display: block;
  border-radius: 50%; /* 确保图片元素也是圆形 */
}

.postpicture .el-image img {
  width: 100%;
  height: 100%;
  object-fit: cover; /* 保持图片比例，裁剪多余部分 */
  border-radius: 50%; /* 确保图片内容也是圆形 */
}

.postfrom {
  margin-left: 18px;
  margin-top: 10px;
}

.postman {
    display: flex;
    flex-direction: column; /* 纵向排列 */
}

.postfrom .h3-style {
    margin: 0 0 0px 0; /* 减少底部间距 */
    font-size: 16px;
    display: block;
    font-size: 1.17em;
    font-weight: bold;
    color: black;
    text-decoration: none; /* 移除下划线 */
}

.postfrom .h3-style:hover {
    text-decoration: underline; 
}

.postinformation {
    display: flex;
    margin-left:10px;
}

.postinformation p {
    margin: 2px 0; /* 减小段落间的间距 */
    padding-left:5px;
    font-size: 14px;
    color: #666;
}

.follow {
    margin-left: auto; /* 将按钮推到右侧 */
}

.follow-button {
  background-color: transparent;
  border: 2px solid #409eff; /* 蓝色边框 */
  color: #409eff; /* 蓝色文字 */
  padding: 6px 12px;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 15px;
  margin-right: 10px;
}

.follow-button:hover {
  background-color: rgba(64, 158, 255, 0.1); /* 轻微的背景色 */
  border-color: #66b1ff;
  color: #66b1ff;
}

.follow-button:active {
  transform: scale(0.98);
}

.content-container {
    padding-left:5%;
    display: flex;
    flex-direction: column;
}

.content-container a {
    color:#0659ac;
    transition: color 0.3s ease;
    text-decoration: none;
}

.content-container a:hover{
    text-decoration: underline; 
}

.content {
  margin-top: 15px;
}

.littlePerson {
  display: flex;
  flex-direction: row;
}

.littlePerson a {
  margin:0;
  margin-left:10px;
  color: #7b7c7c; /* 蓝色 */
  font-size: 14px;
  text-decoration: none;
}

.littlePerson a:hover{
  text-decoration: underline; 
}

.comment {
  margin-left:18%;
  display: flex;
  flex-direction: row;
}

.comment a {
  margin:0;
  margin-left:10px;
  color: #7b7c7c; /* 蓝色 */
  font-size: 14px;
  text-decoration: none;
}

.comment a:hover{
  text-decoration: underline; 
}

.comment-icon {
  color: #7b7c7c; /* 灰色 */
  font-size: 18px; /* 图标大小 */
  margin-top: -3px;
  margin-left: 20px;
}

.like {
  margin-left:18%;
  display: flex;
  flex-direction: row;
}

.like p {
  margin:0;
  margin-left:10px;
  color: #7b7c7c;
  font-size: 14px;
  text-decoration: none;
}

.bottonLine {
  margin-left: 5%;
  margin-bottom: 10px;
  display: flex;
  flex-direction: row;
}

.not-like-icon {
  color: #808080; /* 灰色 */
  font-size: 18px; /* 图标大小 */
  margin-top: -3px;
  margin-left: 20px;
}

.not-like-icon :hover{
  color: #fb7a10; /* 灰色 */
}

.like-icon {
  color: #fb7a10; /* 灰色 */
  font-size: 18px; /* 图标大小 */
  margin-top: -3px;
  margin-left: 20px;
}
</style>

