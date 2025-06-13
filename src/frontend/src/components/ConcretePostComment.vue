<template>
    <el-image
        :src="getImageUrl(masterImage)" 
        :alt="`Image ${index + 1}`" 
        class="commentImage"
        fit="cover"
    />
    <div class="replyRightPart">
        <p class="comment">
                <span  class="MyreplyName" @click="handleClick">{{ replyName }}</span>:{{ replyComment }}
        </p>
        <div class="CommentbuttonLine">
            <div class="likePartComment">
                <span :class="getUserLike" @click="handleLike">
                    <i class="fa-regular fa-thumbs-up"></i>
                </span>
                <p class="MyLikeNumber">&nbsp;{{ nowLikeNumber }}</p>
            </div>
            <p class="myTime">🕒 {{ publishTime }}</p>
            <p class="myComment" @click="changeCommentReply()">{{ nowCommentList}}</p>
        </div>
        <div v-if="nowReplyButton === true">
            <CommentInput class="CommentReply" :isReplyReply="isReplyReply" :replyName="replyName" :isReplyPost="replyPost" :commentId="props.commentId" :userId="props.userId" :forumId="props.forumId"/>
        </div>
    </div>
</template>

<script setup>
import {ref,computed,reactive} from 'vue'
import CommentInput from './CommentInput.vue'
import { defineProps, onBeforeMount,watchEffect,watch} from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
const router = useRouter()
const props = defineProps({
  comment: {
    type:Object,
    default:null
  },
  userId:{
    type:[String,Number],
    default:1
  },
  forumId:{
    type:[String,Number],
    default:1
  },
  commentId:{
    type:[String,Number],
    default:1
  }
});


let replyName=ref('myName')
let masterImage=ref('head.png')
let isUserLike=ref(false)
let nowReplyButton=ref(false)
let nowCommentList=ref('回复')

let nowLikeNumber = ref(0)
let publishTime = ref('')
let replyComment = ref('')
const replyPost=ref(false)
const isReplyReply=ref(true)

// 2. 使用watchEffect监听props变化
watchEffect(() => {
  if (props.comment) {
    console.log(props.comment)
    nowLikeNumber.value = props.comment.likeNumber || 0
    publishTime.value = props.comment.createTime?.split('T')[0] || ''
    replyComment.value = props.comment.commentContent || ''
  }
})

const changeCommentReply = () => {
    // 确保索引有效
    nowReplyButton.value = !nowReplyButton.value;
    // 修复：使用一致的数组名称
    if(nowReplyButton.value) {
        nowCommentList.value = '收起';
    } else {
        nowCommentList.value= '回复';
    }
}


const handleClick = () => {
    // 处理点击事件
    router.push({
        path:'/OthersHomePage',
        query: {
            userId: props.comment.userID,
    }})
}

const getImageUrl = (imageName) => {
    try {
        return require(`../assets/${imageName}`);
    } catch (error) {
        console.error('Error loading image:', error);
        return '';
    }
}


const handleLike = async () => {
  if(!isUserLike.value){
    nowLikeNumber.value=nowLikeNumber.value + 1
    
    const likeData = reactive({
      ID: props.comment.id,
      userID: props.userId
    });

    const { data, status } = await axios.post(
      'http://localhost:8080/comment/like', likeData,
      {
      validateStatus: () => true
      })
    if(status == 200) console.log(data)

    isUserLike.value=true
  }
  else{
    nowLikeNumber.value=nowLikeNumber.value - 1

    const likeData = reactive({
      ID: props.comment.id,
      userID: props.userId
    });

    const { data, status } = await axios.post(
      'http://localhost:8080/comment/cancelLike', likeData,
      {
      validateStatus: () => true
      })
    if(status == 200) console.log(data)
    isUserLike.value=false
  }
}

const getUserLike = computed(() =>{
  return isUserLike.value?'like-icon':"not-like-icon"
})

const waitForPost = () => {
  return new Promise((resolve) => {
    if (props.comment && props.comment !== null) {
      resolve(props.comment)
      return
    }
    
    const unwatch = watch(
      () => props.comment,
      (newPost) => {
        if (newPost && newPost !== null) {
          unwatch()
          resolve(newPost)
        }
      }
    )
  })
}

onBeforeMount( async () => {

    await waitForPost()
    const { data:masterdata, status:masterstatus } = await axios.get(
    'http://localhost:8080/user/'+ props.comment.userID, 
    {
      validateStatus: () => true
    })
    if(masterstatus == 200){
        replyName.value=masterdata.user.username
    }

    const likeData = {
      userID:props.userId,
      commentID:props.comment.id
    }
    const { data:likedata, status:likestatus } = await axios.post(
    'http://localhost:8080/like/getUserLikeComment',likeData,
    {
      validateStatus: () => true
    })
    if(likestatus == 200){
      isUserLike.value=likedata.isUserLike
    }

})

</script>

<style scoped>
.wholeComponent {
  width: 798px;
  background-color: rgb(250, 247, 247);
  border:0.3px solid rgb(200, 200, 200)
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

.masterPart {
    align-items: stretch; /* 添加这行，使子元素拉伸到容器高度 */
    min-height: 200px; /* 可选：设置最小高度确保有足够空间 */
    display: flex;
    flex-direction: row;
}

.masterMessage {
    width: 160px;
    background-color: rgb(250, 247, 247);
}

.homeImage {
    width: 80px;
    height: 80px;
    margin-left: 35px;
    margin-top: 20px;
    border: 2px solid white;
    border-radius: 0;
}

.attention {
    font-size: 16px;
    font-weight: 600;
    color:rgb(98, 142, 208);
    margin-left: 55px;
    margin-top: 5px;
    margin-bottom: 5px;
}

.toMaster {
    display: inline-block; /* 关键：设置为内联块级元素 */
    text-decoration: none; /* 移除下划线 */
    color:rgb(121, 171, 243);
    margin-left: 35px;
    width: 100px; /* 使用width替代max-width */
    white-space: nowrap; /* 防止文本换行 */
    overflow: hidden; /* 隐藏超出部分 */
    text-overflow: ellipsis; /* 超出部分显示省略号 */
    font-size: 14px; /* 添加字体大小 */
    line-height: 1.4; /* 添加行高 */
}

.toMaster:hover {
    color: rgb(98, 142, 208); /* 添加悬停效果 */
    text-decoration: underline;
}

.masterContent {
    padding-top: 15px;
    padding-left: 15px;
    width: 650px;
    margin:5px;
    height: 100%;
}

.contentClass {
    min-height: 100px;
    margin-top:10px;
    margin-left: 10px;
}

.pictureClass {
    margin-top:10px;
    margin-left: 10px;
}

.likePart {
  margin-left:360px;
  display: flex;
  flex-direction: row;
}

.likePartComment{
  margin-left:293px;
  display: flex;
  flex-direction: row;
}

.not-like-icon {
  color: #808080; /* 灰色 */
  font-size: 16px; /* 图标大小 */
  margin-left: 20px;
  margin-top: 12px;
}

.not-like-icon :hover{
  color: #fb7a10; /* 灰色 */
}

.like-icon {
  color: #fb7a10; /* 灰色 */
  font-size: 16px; /* 图标大小 */
  margin-left: 20px;
    margin-top: 12px;
}

.buttonLine {
    display: flex;
    flex-direction: row;
    width: 100%;
    height: 30px;
    margin-top:-15px;
    margin-bottom: 10px;
}

.CommentbuttonLine {
    display: flex;
    flex-direction: row;
    width: 100%;
    height: 30px;
    margin-top:-15px;
    margin-bottom: 10px;
}

.myTime {
    margin-left: 20px;
    font-size: 14px;
    color :rgb(167, 167, 167)
}

.MyLikeNumber {
    font-size: 14px;
    color :rgb(167, 167, 167)
}

.myComment {
    margin-left: 20px;
    font-size: 14px;
    color :rgb(120, 172, 204)
}

.myReplyComment {
    margin-top:5px;
    margin-left: 10px;
    width: 600px;
    margin-bottom: 15px;
}

.commentImage {
    width: 55px;
    height: 55px;
    margin-left: 15px;
    margin-top: 10px;
    border: 2px solid white;
    border-radius: 0;
}

.myReply {
    width: 600px;
    min-height: 100px;
    margin-left: 10px;
    background-color: rgb(247,248,250);
    border-radius: 0;
    display: flex;
    flex-direction: row;
}

.MyreplyName {
    color:rgb(69, 103, 199);
    text-decoration: none;
}

.comment {
    color:#636363;
    max-width: 500px;
    margin-left: 10px;
}

.CommentReply {
    margin-top:5px;
    margin-left: 5px;
    width: 98%;
    margin-bottom: 15px;
}
</style>