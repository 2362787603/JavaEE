<template>
    <div class="wholeComponent">
        <div class="home_scrollable-content">
            <HotSearch/>
            <div class="titlePart">
                <div class="choosePart">
                    <el-button class="hotButton" :class="{'force-hover': isNowHot }" @click="ChangeNowMod()">搜索帖子</el-button>
                    <el-button class="hotButton" :class="{'force-hover': isNowNew }" @click="ChangeNowMod()">搜索论坛</el-button>
                </div>
            </div>
            <div class="contentPart">
                <div v-if="isNowHot === true" class="postlist">
                    <div v-for="i in range(0,10)" :key="i" class="innerPost">
                        <HomePost class="mypost"/>
                    </div>
                </div>
                <div v-if="isNowNew === true" class="followList">
                    <MySearchFollow class="mypost" :SearchForum="mySearchForum"/>
                </div>
                <div class="rightMessage">
                    <div class="MyMessage">
                        <MyPostHot :getUserId="userId"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import HotSearch from '@/components/HotSearch.vue';
import MyPostHot from '@/components/MyPostHot.vue';
import HomePost from '@/components/HomePost.vue';
import MySearchFollow from '@/components/MySearchFollow.vue';
import {ref,computed,reactive} from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'

const route = useRoute()
let isNowHot=ref(true)
let isNowNew=ref(false)

const userId = computed(() => {
  const raw = route.query.userId
  if (!raw) return 1               // 没有就返回空字符串
  return raw
})

const txt = computed(() => {
  const raw = route.query.txt
  if (!raw) return ''               // 没有就返回空字符串
  return raw
})

const handleForumSearch = async () => {
  try {
    let SendForumSearchForm = reactive({
      searchName:txt
    })
    console.log(SendForumSearchForm)

    const { data, status } = await axios.get(
        'http://localhost:8080/forum/getAllForumByName', 
        {
            validateStatus: () => true,
            params: {
                name: txt.value // 参数名为name，值为1
            }
        }
    )
    console.log(data)
    if(status == 200) return data.forums;
  } catch (error) {
    console.log(error)
    return null;
  }
}

let mySearchForum=ref(handleForumSearch())

const ChangeNowMod = () => {
    if(isNowHot.value == true){
        isNowHot.value = false;isNowNew.value = true;
    }
    else{
        isNowHot.value = true;isNowNew.value = false;
    }
}

const range = (start, end) => {
    return Array.from({length: end - start}, (_, index) => start + index);
}

</script>

<style scoped>

.wholeComponent {
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  position: relative;
  height: 100vh; /* 保持视口高度 */
  width: 100%;
  overflow: hidden; /* 这个属性保持不变，因为我们希望在内部容器滚动 */
  background-color: rgb(249, 248, 248);
}

.followList {
    height:100% ;
    margin-top:-15px;
    overflow-y: auto; /* 如果内容过多，允许在此处滚动 */
}

.home_scrollable-content {
  height: 100%;
  overflow-y: auto; /* 保持垂直方向滚动 */
  overflow-x: hidden;
  padding-right: 0px;
}

.backPage {
    width: 100%;
}

.backImage {
    height: 200px;
    width:  1000px;
    border-radius: 0;
}

.choosePart {
    height: 50px;
    width: 1000px;
    border-radius: 0;
    background-color: rgb(238, 239, 240);
    border: solid 1.2px rgb(219, 219, 219);
    display: flex;
    flex-direction: row;
}

.titlePart {
  margin-left:330px;
}

.contentPart {
  margin-left:330px;
  display: flex;
  flex-direction: row;
}

.homeImage {
  width: 150px;
  height: 150px;
  border-radius: 8px;
  margin-left: 20px;
  margin-top: -60px;
  border-color: white;
  border: solid 3px white;
}

.introduce {
    margin-left: -25px;
}

.title {
    width: 1000px;
    height: 115px;
    border-radius: 0;
    background-color: rgb(238, 239, 240);
    display: flex;
    flex-direction: row;
}

.postName {
    white-space: nowrap; /* 防止文本换行 */
    margin-top:15px;
    margin-left: 10px;
    font-size: 24px;
    font-weight: 400;
    columns: black;
}

.followPart {
    width: 1000px;
    display: flex;
    flex-direction: row;
}

.followButton {
    height: 40px;
    width: 100px;
    margin-top: 12px;
    margin-left: 30px;
}

.followPart {
    margin-left:50px;
    margin-top:8px;
    display: flex;
    flex-direction: row;
}

.partTitle {
    font-size: 14px;
    color:rgb(139, 139, 139)
}

.partPost {
    font-size: 14px;
    color:rgb(139, 139, 139);
    margin-left: 20px;
}

.followNum {
    font-size:14px;
    margin-left:10px;
    color:rgb(234, 159, 72)
}

.postNum {
    font-size:14px;
    margin-left:10px;
    color:rgb(234, 159, 72)
}

.information {
    margin-left: 50px;
    margin-top: -22px;
    color:rgb(140, 140, 139)
}

.hotButton {
    width: 150px;
    height: 50px;
    border-radius: 0;
    margin-left: 0;
    margin:0 !important;
    background-color: rgb(238, 239, 240);
}

.hotButton.force-hover {
  background-color: #ecf5ff !important;
  color: #409eff !important;
  border-color: #c6e2ff !important;
  margin:0;
}

.search-input {
    height: 30px;
    width: 200px;
    margin-top: 10px;
    margin-left: 400px;
    border-radius: 0;
}

.search-button {
    width: 80px;
    height: 30px;
    border-radius: 0;
    margin-top:10px;
    background-color: rgb(238, 239, 240);
}

.mypost {
    margin-top:15px;
    width: 725px;
    height:100%
}

.rightMessage {
    margin-top:15px;
    margin-left: 10px;
    width: 265px;
    position: sticky; /* 修改为sticky定位 */
    top: 0px; /* 设置粘性定位的顶部距离 */
    align-self: flex-start; 
}

.postMessage {
    margin-top:10px;
    margin-left: 0;
    background-color: rgb(238, 239, 240);
    height: 170px;
    border: 2px solid rgb(249, 248, 248);
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
    background-color: rgb(240, 240, 241)
}

.postMessage p {
    margin-left:20px;
    font-weight: 550;
    color:#878787
}

.masterInformation p{
    font-size: 16px;
    font-weight: 500;
    margin-top: 0;
    margin-left:15px;
}

.masterMessage {
    margin-left: 10px;
    margin-top:10px;
    display: flex;
    flex-direction: row;
}

.masterImage {
  width: 100px;
  height: 100px;
  border-radius: 4px;
  margin-left: 10px;
  margin-top: -5px;
  border-color: white;
  border: solid 2px white;
}

.masterName {
    margin-left:15px !important;
    white-space: nowrap !important; /* 防止文本换行 */
    overflow: hidden !important; /* 隐藏超出部分 */
    margin-top: -10px !important;
}

.masterButton {
    border-radius: 0;
    margin-left: 10px;
}

</style>