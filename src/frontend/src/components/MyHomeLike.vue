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
            <div v-for="i in range(0,postlikeList.length)" :key="i" class="myLike">
                <HomePost HasUserLiked=true :userId="props.userId" :post="postsData[i]"/>
            </div>
        </div>
        <div v-if="nowChoose === 'CommentLike'">
            <div v-for="i in range(0,commentlikeList.length)" :key="i" class="myLikeComment">
                <LoveComment :userId="props.userId" :comment="commentData[i]"/>
            </div>
        </div>
    </div>
</template>


<script setup>
import { ref, computed,defineProps,watch,onBeforeMount, onMounted} from 'vue';
import HomePost from './HomePost.vue';
import LoveComment from './LoveComment.vue';
import axios from 'axios'

let nowChoose = ref('PostLike')
let allLikeList=ref([])
let commentData=ref([])
let postsData = ref([])
let postlikeList = computed(() => {
    return allLikeList.value.filter(item => item.postId !== 0 && item.commentId === 0)
})

let commentlikeList = computed(() => {
    return allLikeList.value.filter(item => item.commentId !== 0 && item.postId === 0)
})

const props = defineProps({
userId: {
    type: String ,
    default: "1"
  }
});

console.log(props)

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

const loadAllPosts = async () => {
    const posts = []
    for (let i = 0; i < postlikeList.value.length; i++) {
        const post = await searchPost(i)
        posts.push(post)
    }
    postsData.value = posts
}

const searchPost = async (index) => {
    const { data:commentdata, status:commentstatus } = await axios.get(
    'http://localhost:8080/post/' + postlikeList.value[index].postId,
    {
      validateStatus: () => true
    })
    console.log(commentstatus)
    return commentdata.post
}

const loadAllComment = async () => {
    const posts = []
    for (let i = 0; i < commentlikeList.value.length; i++) {
        const post = await searchComment(i)
        posts.push(post)
    }
    commentData.value = posts
}

const searchComment = async(i) => {
    console.log(i)
    const { data:commentdata, status:commentstatus } = await axios.get(
    'http://localhost:8080/comment/' + commentlikeList.value[i].commentId,
    {
      validateStatus: () => true
    })
    console.log(commentstatus)
    return commentdata.comment
}

const waitForPost = () => {
  return new Promise((resolve) => {
    if (props.userId && props.userId !== "0") {
      resolve(props.userId)
      return
    }
    
    const unwatch = watch(
      () => props.userId,
      (newPost) => {
        if (newPost && newPost !== "0") {
          unwatch()
          resolve(newPost)
        }
      }
    )
  })
}

onBeforeMount( async () => {

    await waitForPost()
    const data = {
        userID:String(props.userId)
    }
    const { data:createdata, status:createstatus } = await axios.post(
      'http://localhost:8080/like/getAllUserLike',data,
      {
        validateStatus: () => true
      })
      if(createstatus == 200){
        console.log(props.userId)
        allLikeList.value=createdata.likeList
    }
})

onMounted(() => {
    loadAllPosts()
    loadAllComment()
})
// 监听postlikeList变化，重新加载数据
watch(postlikeList, () => {
    loadAllPosts()
    loadAllComment()
})

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