<template>
    <div class="wholeComponent">
        <div class="home_scrollable-content">
            <HotSearch :userId="userId"/>
            <div class="titlePart">
                <div class="backPage">
                    <el-image
                        :src="getImageUrl(showimage1)" 
                        :alt="`Image ${index + 1}`" 
                        class="backImage"
                        fit="cover"
                    />
                </div>
                <div class="title">
                    <div class="photopicture">
                        <el-image
                        :src="`http://localhost:8080/image/downloadById/`+forumimageId" 
                            :alt="`Image ${index + 1}`" 
                            class="homeImage"
                            fit="cover"
                        />
                    </div>
                    <div class="introduce">
                        <div class="followPart">
                            <p class="postName">{{ postname }}</p>
                            <el-button class="followButton" @click="ChangeFollow">{{ nowFollow }}</el-button>
                            <div class="followPart">
                                <p class="partTitle">关注：</p>
                                <p class="followNum">{{ followNum }}</p>
                                <p class="partPost">帖子：</p>
                                <p class="postNum">{{ postNum }}</p>
                            </div>
                        </div>
                        <div class="information">
                            <p>{{ introduction }}</p>
                        </div>
                    </div>
                </div>
                <div class="choosePart">
                    <el-button class="hotButton" :class="{'force-hover': isNowHot }" @click="ChangeNowMod()">当前热门</el-button>
                    <el-button class="hotButton" :class="{'force-hover': isNowNew }" @click="ChangeNowMod()">最新内容</el-button>
                    <el-button class="hotButton"  @click="gotoPost()">我要发贴</el-button>
                    <el-button class="hotButton"  @click="gotoMainPage">返回主页</el-button>
                    <el-input 
                        type="text" 
                        class="search-input" 
                        v-model="searchfilter"
                        placeholder="搜索..." 
                    />
                    <el-button class="search-button" @click="searchAll">搜索</el-button>
                </div>
            </div>
            <div class="contentPart">
                <div v-if="isNowHot === true" class="postlist">
                    <div v-for="i in range(0,postList.length)" :key="i" class="innerPost">
                        <inner-home-post class="mypost" :post="hotPostList[i]" :userId="userId" :forumId="forumId"/>
                    </div>
                </div>
                <div v-if="isNowNew === true" class="postlist">
                    <div v-for="i in range(0,postList.length)" :key="i" class="innerPost">
                        <inner-home-post class="mypost" :post="newPostList[i]" :userId="userId" :forumId="forumId"/>
                    </div>
                </div>
                <div class="rightMessage">
                    <div class="MyMessage">
                        <MyPostHot :getUserId="userId"/>
                    </div>
                    <div class="postMessage">
                        <p>本吧信息</p>
                        <div class="masterMessage">
                            <el-image
                                :src="getImageUrl(masterImage)" 
                                :alt="`Image ${index + 1}`" 
                                class="masterImage"
                                fit="cover"
                            />
                            <div class="masterInformation">
                                <p>吧主</p>
                                <p class="masterName">{{ masterName}}</p>
                                <el-button class="masterButton" @click="gotoOtherOage">查看ta的主页</el-button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import HotSearch from '@/components/HotSearch.vue';
import innerHomePost from '@/components/innerHomePost.vue';
import MyPostHot from '@/components/MyPostHot.vue';
import {ref,computed,onMounted,reactive, onBeforeMount} from 'vue'
import { useRoute,useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const route = useRoute()
const userId = computed(() => {
  const raw = route.query.userId
  if (!raw) return ''               // 没有就返回空字符串
  return raw
})
const forumId = computed(() => {
  const raw = route.query.forumId
  if (!raw) return ''               // 没有就返回空字符串
  return raw
})

const showimage1=ref('BackGround.png')



let postname=ref('飧筱刅吧')
let nowFollow=ref('取消关注')
let postNum=ref(514)
let followNum=ref(114)
let introduction=ref('多平台的怪物猎人粉丝和谐讨论贴吧')
let isNowHot=ref(true)
let isNowNew=ref(false)
let postList=ref([])
let searchfilter=ref('')

const hotPostList = computed(() => {
    if (!postList.value || !Array.isArray(postList.value)) {
        return []
    }

    return postList.value
        .filter(post => post.content && post.content.includes(''))  // 只保留包含"444"的帖子
        .sort((a, b) => b.likeNumber - a.likeNumber) 
    
})

// 按创建时间降序排序的列表（最新的在前）
const newPostList = computed(() => {
    if (!postList.value || !Array.isArray(postList.value)) {
        return []
    }

    return postList.value
        .filter(post => post.content && post.content.includes(''))  // 只保留包含"444"的帖子
        .sort((a, b) => new Date(b.createTime) - new Date(a.createTime)) 
    
})

let masterImage=ref('head.png')
let masterName=ref('吴安邦是蠢猪')
let masterId=ref(1)
let forumimageId=ref(1)
const searchAll = () => {
  router.push({
    path:'/Search',
    query: {
      userId: userId.value,
      txt: searchfilter.value
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

const ChangeNowMod = () => {
    if(isNowHot.value == true){
        isNowHot.value = false;isNowNew.value = true;
    }
    else{
        isNowHot.value = true;isNowNew.value = false;
    }
}

const gotoPost = () => {
    router.push({
        path:'/createPost',
        query: {
            userId: userId.value,
            forumId: forumId.value
    }})
}

const gotoOtherOage = () => {
    router.push({
        path:'/OthersHomePage',
        query: {
            userId: masterId.value,
    }})
}
 
const gotoMainPage = () => {
    router.push({
        path:'/MainPage',
        query: {
            loginData: userId.value,
    }})
}

const ChangeFollow = async () => {
    if(nowFollow.value === '取消关注'){
        nowFollow.value = '关注'

        let deleteForm = reactive({
            userId: userId.value,
            forumId: Number(forumId.value)
        })
        const{data:userdata,status:userstatus} = await axios.delete(
            'http://localhost:8080/forum/follow', 
            {
                data:deleteForm,
                validateStatus: () => true
            })
        if(userstatus != 200) console.log(userdata)

        followNum.value --;
    }
    else{
        nowFollow.value = '取消关注'

        let deleteForm = reactive({
            userId: userId.value,
            forumId: Number(forumId.value)
        })
        const{data:userdata,status:userstatus} = await axios.post(
            'http://localhost:8080/forum/follow', deleteForm,
            {
            validateStatus: () => true
            })
        if(userstatus != 200) console.log(userdata)

        followNum.value ++;
    }
}

const range = (start, end) => {
    return Array.from({length: end - start}, (_, index) => start + index);
}

onBeforeMount(async () => {
    const {data:postdata,status:poststatus} = await axios.get(
        'http://localhost:8080/post/forum/' + forumId.value,
        {
            validateStatus: () => true
        }
    )

    if(poststatus == 200){
        postList.value=postdata.posts
    }
})

onMounted(async ()=>{
    console.log('Post View Start')
    const { data, status } = await axios.get(
        'http://localhost:8080/forum/search/'+forumId.value, 
        {
        validateStatus: () => true
        })

    if(status == 200 ){
        forumimageId.value=data.forum.imageId
        postname.value=data.forum.name
        postNum.value=data.forum.postCount == null?0:data.forum.postCount
        followNum.value=data.forum.followCount == null?0:data.forum.followCount
        introduction.value=data.forum.introduction
        masterId.value=data.forum.userID
        let createUserId = ref(data.forum.userID)

        const{data:userdata,status:userstatus} = await axios.get(
            'http://localhost:8080/user/'+createUserId.value, 
            {
            validateStatus: () => true
            })
        if(userstatus == 200){
            masterName.value=userdata.user.username
        }
    }

    const{data:followdata,status:followstatus} = await axios.get(
    'http://localhost:8080/forum/isUserFollow', 
    {
    params: {
        userId: userId.value,
        forum_id: forumId.value
    },
    validateStatus: () => true
    })
    if(followstatus == 200){
        nowFollow.value = (followdata.isFollowed == true?'取消关注':'关注')
    }
})

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
    margin:0;
    background-color: rgb(238, 239, 240);
}

.hotButton.force-hover {
  background-color: #ecf5ff !important;
  color: #409eff !important;
  border-color: #c6e2ff !important;
}

.search-input {
    height: 30px;
    width: 200px;
    margin-top: 10px;
    margin-left: 250px;
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