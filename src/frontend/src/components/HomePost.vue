<template>
    <div class="whole-component">
        <div class="post-container">
            <div class="postpicture">
                <el-image :src="`http://localhost:8080/image/downloadById/`+forumimageId" ></el-image>
            </div>
            <div class="postline">
                <div class="postman">
                    <div class="postfrom">
                        <a class="h3-style"  @click="EnterForum">{{ postFromName }}</a>
                    </div>
                    <div class="postinformation">
                        <p>🕒 {{ posttime }}</p>
                        <p>👁️ {{ postfollow }}人关注了该吧</p>
                    </div>
                </div>
            </div>
            <div class="follow">
                <button class="follow-button" @click="EnterForum">进入</button>
            </div>
        </div>
        <div class="content-container">
          <a  :to="postpage" @click="EnterPost">### {{ postTitle }} ###</a>
          <div class="content">
            <contentBlock :text="longText"/>
          </div>
          <div class="picture">
            <pictureBlock1 v-if="props.post" :postId="props.post.id"/>
          </div>
        </div>
        <div class="bottonLine">
          <div class="littlePerson">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <circle cx="12" cy="8" r="5" fill="#CCCCCC"/>
              <path d="M3 22C3 17.58 6.58 14 11 14H13C17.42 14 21 17.58 21 22" stroke="#CCCCCC" stroke-width="2" stroke-linecap="round"/>
            </svg>
            <a  @click="handleMaster" class="mastername">{{ mastername }}</a>
          </div>
          <div class="comment">
            <span class="comment-icon">
              <i class="fa-regular fa-comment"></i>
            </span>
            <a  :to="postpage" @click="EnterPost">{{ commentNumber }}条评论</a>
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
import { ref,computed,defineProps,watchEffect,watch,onBeforeMount,reactive} from 'vue'
import contentBlock from './contentBlock.vue'
import pictureBlock1 from './pictureBlock1.vue'
import '@fortawesome/fontawesome-free/css/all.min.css'
import axios from 'axios'
import {useRouter } from 'vue-router'

const router = useRouter()

const props = defineProps({
  post:{
    type:Object,
    default:null
  },
  userId:{
    type:[String,Number],
    default:1
  },
  HasUserLiked: {
    type: Boolean,
    default: () => false
  }
});


const postfollow=ref(119)
let mastername=ref('我不是隼鸮牕')
let commentNumber=ref(11)
let masterid=ref(1)
let postFromName=ref('飧筱刅吧')
let forumimageId=ref(1)
let likeNumber = ref(props.post != null?props.post.likeNumber:0)
let posttime = ref('')
let longText = ref('')
let postTitle = ref('')

// 2. 使用watchEffect监听props变化
watchEffect(() => {
  if (props.post) {
    
    likeNumber.value = props.post.likeNumber || 0
    posttime.value = props.post.createTime?.split('T')[0] || ''
    longText.value = props.post.content || ''
    postTitle.value = props.post.title || ''
  }
})

let isUserLike=ref(props.HasUserLiked)

const handleLike = async () => {
  if(!isUserLike.value){
    likeNumber.value=likeNumber.value + 1
    
    const likeData = reactive({
      postID: props.post.id,
      userID:props.userId
    });

    const { data, status } = await axios.post(
      'http://localhost:8080/post/like', likeData,
      {
      validateStatus: () => true
      })
    if(status == 200) console.log(data)

    isUserLike.value=true
  }
  else{
    likeNumber.value=likeNumber.value - 1

    const likeData = reactive({
      postID: props.post.id,
      userID: props.userId
    });

    const { data, status } = await axios.post(
      'http://localhost:8080/post/cancelLike', likeData,
      {
      validateStatus: () => true
      })
    if(status == 200) console.log(data)
    isUserLike.value=false
  }
}

const EnterForum = () =>{
    router.push({
        path:'/post',
        query: {
            userId: props.userId,
            forumId: props.post.forumID,
    }})
}

const handleMaster = () => {
    router.push({
        path:'/OthersHomePage',
        query: {
            userId: masterid.value,
    }})
}
const EnterPost = () =>{
    router.push({
        path:'/InnerPost',
        query: {
            userId: props.userId,
            forumId: props.post.forumID,
            postId: props.post.id
    }})
}

const getUserLike = computed(() =>{
  return isUserLike.value?'like-icon':"not-like-icon"
})

const waitForPost = () => {
  return new Promise((resolve) => {
    if (props.post && props.post !== null) {
      resolve(props.post)
      return
    }
    
    const unwatch = watch(
      () => props.post,
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
    'http://localhost:8080/user/'+ props.post.userID, 
    {
      validateStatus: () => true
    })
    if(masterstatus == 200){
      mastername.value=masterdata.user.username
      masterid.value=masterdata.user.userId
    }

    const { data:followdata, status:followstatus } = await axios.get(
    'http://localhost:8080/forum/' + props.post.forumID + '/followCount',
    {
      validateStatus: () => true
    })
    if(followstatus == 200){
      postfollow.value=followdata.followCount
    }

    const requestData = {
      postId:props.post.id
    }
    const { data:commentdata, status:commentstatus } = await axios.post(
    'http://localhost:8080/post/commentCount',requestData,
    {
      validateStatus: () => true
    })
    if(commentstatus == 200){
      commentNumber.value=commentdata.count
    }

    const { data:forumdata, status:forumstatus } = await axios.get(
    'http://localhost:8080/forum/search/' + props.post.forumID,
    {
      validateStatus: () => true
    })
    if(forumstatus == 200){
      postFromName.value=forumdata.forum.name
      forumimageId.value=forumdata.forum.imageId
    }

    const likeData = {
      userID:props.userId,
      postID:props.post.id
    }
    const { data:likedata, status:likestatus } = await axios.post(
    'http://localhost:8080/like/getUserLikePost',likeData,
    {
      validateStatus: () => true
    })
    if(likestatus == 200){
      isUserLike.value=likedata.isUserLike
      console.log(likeData)
      console.log(likedata)
    }
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

.mastername{
  width: 100px;
}
</style>

