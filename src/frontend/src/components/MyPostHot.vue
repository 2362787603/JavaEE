<template>
    <div class="LeftHotCottent">
        <p>个人资料</p>
        <div class="LeftHotImage">
            <el-image
                :src="getImageUrl(showimage)" 
                :alt="`Image ${index + 1}`" 
                class="scaled-image"
                fit="cover"
            />
            <div class="LeftHotIntro">
                <div class="littlePerson">
                    <div class="userPhoto">
                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <circle cx="12" cy="8" r="5" fill="#CCCCCC"/>
                            <path d="M3 22C3 17.58 6.58 14 11 14H13C17.42 14 21 17.58 21 22" stroke="#CCCCCC" stroke-width="2" stroke-linecap="round"/>
                        </svg>
                    </div>
                    <div class="userName">
                        <p>{{ username }}</p>
                    </div>       
                </div>
                <router-link to="/Test/Test">
                    <el-button class="personHome">个人主页</el-button>
                </router-link>
            </div>
        </div>

        <p class="followP">关注的吧</p>
        <div class="myfollow" v-if="clickBotton === false">
            <div v-for="(Names, index) in displayName" :key="index" class="follow-line">
                <div v-for="(Name, index2) in Names" :key="index2" class="follow-row">
                    <div class="follow-row1" @click="gotoPost(index,index2)">
                        <p>{{Name}}</p>
                    </div>
                </div>
            </div>
            <div v-if="hasmorefollow === true">
                <el-button class="lookMore" @click="showmoreimage">查看更多</el-button>
            </div>
        </div>
        <div class="myfollow" v-if="clickBotton === true">
            <div v-for="(Names, index) in displayName" :key="index" class="follow-line">
                <div v-for="(Name, index2) in Names" :key="index2" class="follow-row">
                    <div class="follow-row1" @click="gotoPost(index,index2)">
                        <p>{{Name}}</p>
                    </div>
                </div>
            </div>
            <div v-if="clickBotton === true">
                <el-button class="lookMore" @click="showlessimage">收起</el-button>
            </div>
        </div>

        <p class="guessp">猜你喜欢</p>
        <div class="bottonclass">
            <el-button class="guessbotton2" @click="createForum">创建论坛</el-button>
            <el-button class="guessbotton">我的点赞</el-button>
            <el-button class="guessbotton">我的评论</el-button>
        </div>
    </div>
</template>

<script setup>

import {ref,defineProps,computed,onMounted} from 'vue';
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()
let clickBotton=ref(false)
const showimage=ref('head.png');
let username = ref(null)
const hasmorefollow = computed(() => {return props.getnames.length > 8;})
let forumNames=ref([])
let forumList=ref([])

const getImageUrl = (imageName) => {
  try {
    return require(`../assets/${imageName}`);
  } catch (error) {
    console.error('Error loading image:', error);
    return '';
  }
}

const props = defineProps({
  getUserId: {
    type: String ,
    default: "1"
  },
  getnames: {
    type:Array,
    default: ()=> ['飧筱刅飧筱刅飧筱刅吧','威吧','终锅衽剾吧','夯箜苜🗡吧','康鸭琲虢吧','低于校花吧','闽柯吧','婼☞吧','勇除铊飞吧']
  }
});

// Limit to maximum 3 images
const displayName = computed(() => {
  let namesize = forumNames.value.length;
  let arraylenth = clickBotton.value == false?Math.min(4,namesize / 2):Math.min(6,namesize / 2);
  let mynames = []
  for(let i = 0;i < arraylenth;i ++ ){
    let nameRow = [];
    for(let j = i * 2;j < Math.min(namesize,(i + 1) * 2);j ++ ) nameRow.push(forumNames.value[j]);
    mynames.push(nameRow);
  } 
  console.log(mynames);
  return mynames;
});

const showmoreimage = () =>{
    clickBotton.value = true;
    console.log(clickBotton);
}

const showlessimage = () =>{
    clickBotton.value = false;
}

const createForum = () => {
  router.push({
    path:'/createForum',
    query: {
      userId: props.getUserId
  }})
}

const gotoPost = async (index,index2) =>{
  await router.push({
      path: '/post',
      query: {
        userId: props.getUserId,
        forumId: forumList.value[index*2 + index2].id
      }
    })
    
    // 跳转成功后刷新页面
    window.location.reload()
}

onMounted(async ()=>{
  console.log('Post Hot Start')
  console.log(props.getUserId)
  const { data, status } = await axios.get(
    'http://localhost:8080/user/'+props.getUserId, 
    {
      validateStatus: () => true
    })
  if(status == 200) {
    console.log("Left Message Get!!");
    console.log(data.user);
    username.value=data.user.username
  }

  const {data:followdata,status:followstatus} = await axios.get(
    'http://localhost:8080/forum/getAllUserFollow', 
    {
      params: {
        userId:props.getUserId
      },
      validateStatus: () => true
    })
  if(followstatus == 200){
    forumList.value=followdata.followForums
    for(const value of followdata.followForums) forumNames.value.push(value.name)
  }
})

</script>

<style scoped>

.LeftHotCottent {
  border: 2px solid rgb(249, 248, 248);
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  background-color: rgb(240, 240, 241);
  border-radius: 8px;
  padding: 16px;
  width: 230px;
  margin-bottom: 20px;
  height: 100%;
}

.profile-title {
  font-weight: 700; /* 设置字体加粗 */
  font-size: 18px;
  margin-bottom: 16px;
  color: #333333;
}

.LeftHotImage {
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 16px;
}

.scaled-image {
  width: 80px;
  height: 80px;
  border-radius: 8px;
}

.LeftHotIntro {
  display: flex;
  flex-direction: column;
  width: 50%;
  gap: 12px;
  flex: 1;
}

.littlePerson {
  display: flex;
  flex-direction: row;
  align-items: center;
}

/*
.littlePerson p {
  margin: 0;
  margin-left: 10px;
  color: #7b7c7c;
  font-size: 14px;
  font-weight: 500; 
  text-decoration: none;
}*/

.userPhoto {
    width: 30px;
}

.personHome {
    width: 100%;
}

.userName p {
  margin: 0;
  margin-left: 10px;
  color: #7b7c7c;
  font-size: 14px;
  font-weight: 500; 
  text-decoration: none;
  white-space: nowrap; /* 防止文本换行 */
  overflow: hidden; /* 隐藏超出部分 */
  text-overflow: ellipsis; /* 使用省略号表示被截断的文本 */
  max-width: 90%; /* 确保文本不会超出父容器 */
}

.profile-button {
  align-self: flex-start;
  font-size: 12px;
  padding: 6px 12px;
  font-weight: 500; /* 设置按钮文字粗细 */
}

.followP {
    font-weight: bold;
    font-size: 14px;
    margin-top:20px;
}

.follow-line {
  display: flex;
  flex-direction: row;
  margin-left:-5px;
  width: 100%;
}

.follow-row {
  background-color:white;
  border-radius: 4px;
  border:solid 1.2px rgb(216, 216, 216);
  padding: 4px;
  box-shadow: 0 2px 8px rgba(161, 160, 160, 0.1);
  height: 35px;
  width: 90px;
  margin: 5px;
  margin-bottom: 2px;
}

.follow-row1 {
    margin-top: -5px;
    text-align: left;
    width: 100%;
}

.follow-row:hover {
    background-color:rgb(242, 244, 244);
}

.follow-row1 p {
  color: #6f6f6f;
  white-space: nowrap; /* 防止文本换行 */
  overflow: hidden; /* 隐藏超出部分 */
  text-overflow: ellipsis; /* 使用省略号表示被截断的文本 */
  max-width: 90%; /* 确保文本不会超出父容器 */
  margin-left:5px;
  margin-top:12px;
  font-size: 14px;
}

.follow-row1 p:hover{
  color: #409eff;
}

.lookMore {
    margin-top:10px;
    width: 94%;
    height: 40px;
}

.guessp {
    font-weight: bold;
    font-size: 14px;
    margin-top:30px;
}

.bottonclass {
   margin-left:-12px;
   margin-top:-10px;
}

/* 确保按钮尺寸一致 */
.guessbotton {
    width: 90%;
    height: 40px;
    margin-top:12px;
}

.guessbotton2 {
    width: 90%;
    height: 40px;
    margin-top:12px;
    margin-left: 12px;
}

</style>