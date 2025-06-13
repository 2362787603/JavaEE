<template>
    <div class="wholeComponent">
        <div class="titleLine">
            <div v-for="(forum, index) in allPost" :key="index" class="image-row">
                <el-image
                    :src="`http://localhost:8080/image/downloadById/`+forum.imageId" 
                    :alt="`Image ${index + 1}`" 
                    class="scaled-image"
                    fit="cover"
                />
                <div class="content">
                    <h3 @click="gotoPost(index)">{{  forum.name  }}</h3>
                    <div class="information">
                        <p class="postnum">📝 发帖总数：&nbsp; {{ forum.postCount }}</p>
                        <p class="follownum">➕ 关注总数：&nbsp; {{ forum.followCount }} </p>
                    </div>
                </div>
                <el-button class="cancelFollow"  @click="changeFollow(index)">{{ buttonContent[index] }}</el-button>
            </div>
        </div>
    </div>
</template>

<script setup>

import { defineProps,ref,reactive} from 'vue';
import { onBeforeMount,watch} from 'vue';
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()

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


let buttonContent=ref([])
let allPost=ref([])
for(let i = 0;i < props.getnames.length;i ++ ){
    buttonContent.value.push('取消关注');
}



const changeFollow = async (index) =>{
    if(buttonContent.value[index] == '取消关注'){
        buttonContent.value[index] = '关注';
        let deleteForm = reactive({
            userId: props.userId,
            forumId: Number(allPost.value[index].id)
        })
        const{data:userdata,status:userstatus} = await axios.delete(
            'http://localhost:8080/forum/follow', 
            {
                data:deleteForm,
                validateStatus: () => true
            })
        if(userstatus != 200) console.log(userdata)
        allPost.value[index].followCount --;
    }
    else if(buttonContent.value[index] == '关注'){
      buttonContent.value[index] = '取消关注';
      let deleteForm = reactive({
            userId: props.userId,
            forumId: Number(allPost.value[index].id)
        })
        const{data:userdata,status:userstatus} = await axios.post(
            'http://localhost:8080/forum/follow', deleteForm,
            {
            validateStatus: () => true
            })
        if(userstatus != 200) console.log(userdata)

        allPost.value[index].followCount ++;
    }
}

const gotoPost = (index) => {
    router.push({
        path:'/Post',
        query: {
            userId: props.userId,
            forumId: allPost.value[index].id
    }})
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
    const { data:createdata, status:createstatus } = await axios.get(
      'http://localhost:8080/forum/getAllUserFollow',
      {
        params:{
          userId:props.userId
        },
        validateStatus: () => true
      })
      if(createstatus == 200){
        allPost.value=createdata.followForums
        console.log(createdata.followForums)
      }

})

</script>

<style scoped>

.wholeComponent{
    background-color: rgb(234, 233, 233);
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

.cancelFollow {
    margin-left:-200px;
    margin-top:28px;
    width: 120px;
    height: 45px;
}

</style>