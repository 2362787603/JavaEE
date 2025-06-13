<template>
    <div class="wholeComponent">
      <Teleport to="body">
          <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
            <div class="modal-content">
                <button class="close-btn" @click="showModal = false">×</button>
                <!-- 这里放置你要显示的组件 -->
                <ChangePost :userId="props.userId" :forumId="nowForumId" :initialUsername="userName" :initialPhone="userPhone" @cancel="showModal = false" @update="updateData"/>
            </div>
          </div>
      </Teleport>
      <div class="titleLine">
          <div v-for="(forum, index) in allPost" :key="index" class="image-row">
              <el-image
              :src="`http://localhost:8080/image/downloadById/`+forum.imageId"  
                  :alt="`Image ${index + 1}`" 
                  class="scaled-image"
                  fit="cover"
              />
              <div class="content">
                  <h3>{{ forum.name }}</h3>
                  <div class="information">
                      <p class="postnum">📝 发帖总数：&nbsp; {{ forum.postCount }}</p>
                      <p class="follownum">➕ 关注总数：&nbsp; {{ forum.followCount }} </p>
                  </div>
                  <el-button class="changeMessage" @click="modifyMessage(index)">编辑信息</el-button>
                  <el-button class="gotoPage" @click="gotoPost(index)">进入论坛</el-button>
              </div>
              <h3 v-if="forum.postCount >= 10" class="hot">当前热门</h3>
          </div>
      </div>
    </div>
</template>

<script setup>

import { defineProps,onBeforeMount,ref,watch} from 'vue';
import ChangePost from './ChangePost.vue';
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()
let showModal=ref(false)
let nowForumId=ref(1)

// Define props
const props = defineProps({
  userId:{
    type:String,
    default:"0"
  },
  getimages: {
    type: Array,
    default: () => ['BackGround.png','LoginBackGroud.png','LoginTestFinal.png','BackGround.png','LoginBackGroud.png','LoginTestFinal.png','BackGround.png','LoginBackGroud.png','LoginTestFinal.png','BackGround.png','LoginBackGroud.png','LoginTestFinal.png']
  },
  getnames: {
    type:Array,
    default: ()=> ['飧筱刅吧','LoginBackGroud.png','LoginTestFinal.png','BackGround.png','LoginBackGroud.png','LoginTestFinal.png','BackGround.png','LoginBackGroud.png','LoginTestFinal.png','BackGround.png','LoginBackGroud.png','LoginTestFinal.png']
  },
  getpostnum: {
    type:Array,
    default: ()=>[1,1,4,5,1,4,114,514,19,19,81,0]
  },
  getfollownum: {
    type:Array,
    default: ()=>[234,324,5,23,56,78,22,34,2,3,4,567,8888]
  }
});

let allPost=ref([])


const modifyMessage = (index) => {
  nowForumId.value = allPost.value[index].id
  showModal.value = true
}

const gotoPost = (index) => {
    router.push({
        path:'/Post',
        query: {
            userId: props.userId,
            forumId: allPost.value[index].id
    }})
}

/*
const getnames=computed(()=>{
  return allPost?.map(forum => forum.name) || [];
})

const getpostnum=computed(()=>{
  return allPost?.map(forum => forum.postCount) || [];
})

const getfollownum=computed(()=>{
  return allPost?.map(forum => forum.followCount) || [];
})*/




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

const updateData = async() => {
    const { data:createdata, status:createstatus } = await axios.get(
      'http://localhost:8080/forum/getForumByUserId',
      {
        params:{
          userId:props.userId
        },
        validateStatus: () => true
      })
      if(createstatus == 200){
        allPost.value=createdata.forumList
      } 
}

onBeforeMount( async () => {

    await waitForPost()
    const { data:createdata, status:createstatus } = await axios.get(
      'http://localhost:8080/forum/getForumByUserId',
      {
        params:{
          userId:props.userId
        },
        validateStatus: () => true
      })
      if(createstatus == 200){
        allPost.value=createdata.forumList
      }

})

</script>

<style scoped>

.wholeComponent{
    background-color: rgb(234, 233, 233);
}


.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 8px;
  width: 500px;
  max-height: 90%;
  overflow: auto;
  position: relative;
}

.close-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
}

.postnum {
  padding: 0;
  margin: 0;
}

.follownum {
  padding: 0;
  margin: 0;
}

.scaled-image {
  margin-top: 10px;
  width: 70px;
  height: 70px;
  object-fit: cover;
  border-radius: 8px; /* 添加圆角效果 */
}

.image-row {
    margin-top:20px;
    margin-left:20px;
    margin-right: 20px;
    padding: 10px;
    display: flex;
    flex-direction: row;
    width: 940px;
    height:100px;
    background-color:rgb(254, 254, 254);
    border-radius: 1ch;
}

.hot{
    margin-left: -150px;
    margin-top:37px;
    white-space: nowrap; /* 防止文本换行 */
    overflow: hidden; /* 隐藏超出部分 */
    color:rgb(247, 143, 78) !important;
}

.titleLine {
    margin-top:20px;
    border-radius: 0;
}

.content {
    border-radius: 0;
    background-color:rgb(246, 245, 245);
    padding: 5px;
    margin-left: 10px;
    width: 850px;
}

.content h3{
    margin-top:10px;
    margin-left:5px;
}

.information {
    display: flex;
    flex-direction: row;
}

.information p{
    font-size: 16px;
    margin-top: 0px;
    margin-left:10px;
}

.follownum {
    margin-left: 30px !important;
}

.changeMessage {
    margin-left: 400px;
    margin-top:-85px;
    height: 40px;
    width: 100px;
}

.gotoPage{
    margin-left: 20px;
    margin-top:-85px;
    height: 40px;
    width: 100px;
}

</style>