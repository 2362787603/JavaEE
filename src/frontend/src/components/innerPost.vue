<template>
    <div class="wholeComponent">
        <div class="masterPost">
            <div class="masterContent">
                <masterPost :post="props.post" :userId="props.userId" :forumId="props.forumId"/>
                <div v-for="i in range(0,commentList.length)" :key="i" class="myLike">
                    <PostComment :userId="props.userId" :forumId="props.forumId" :comment="commentList[i]" :masterId="props.post.userID"/>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>

import masterPost from './masterPost.vue';
import PostComment from './PostComment.vue';
import '@fortawesome/fontawesome-free/css/all.min.css'
import { defineProps,onBeforeMount,ref,watch} from 'vue'
import axios from 'axios'
let commentList=ref([])

const props = defineProps({
  post: {
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
  isNowNew:{
    type:Boolean,
    default:false
  },
  isMasterOnly:{
    type:Boolean,
    default:false
  }
});

const range = (start, end) => {
    return Array.from({length: end - start}, (_, index) => start + index);
}

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

    const post = await waitForPost()
    const { data:postdata, status:poststatus } = await axios.get(
    'http://localhost:8080/comment/post/'+ post.id, 
    {
      validateStatus: () => true
    })
    if(poststatus == 200){
        commentList.value=postdata.comments
        if(props.isNowNew){
            commentList.value.sort((a, b) => {
                const timeA = new Date(a.createTime)
                const timeB = new Date(b.createTime)
                return timeB - timeA  // 最新在前
             })
        }
        if(props.isMasterOnly){
             commentList.value = commentList.value.filter(comment => 
                comment.userID === props.post.userID
            )
        }
      }
    }
)

watch(
  [() => props.isNowNew, () => props.isMasterOnly],
  async() => {
    console.log('Props变化，重新应用规则')
    const { data:postdata, status:poststatus } = await axios.get(
    'http://localhost:8080/comment/post/'+ props.post.id, 
    {
      validateStatus: () => true
    })
    if(poststatus == 200){
        commentList.value=postdata.comments
        if(props.isNowNew){
            commentList.value.sort((a, b) => {
                const timeA = new Date(a.createTime)
                const timeB = new Date(b.createTime)
                return timeB - timeA  // 最新在前
             })
        }
        if(props.isMasterOnly){
             commentList.value = commentList.value.filter(comment => 
                comment.userID === props.post.userID
            )
        }
      }
    }
)

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