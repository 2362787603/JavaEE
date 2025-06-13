<template>
    <div class="wholeComponent">
        <div class="home_scrollable-content" ref="scrollContainer">
            <HotSearch :user-id="userId"/>
            <div class="titlePart">
                <el-image
                    :src="getImageUrl(showimage1)" 
                    :alt="`板块图标`" 
                    class="backImage"
                    fit="cover"
                />
                <p class="postName">{{ postname }}</p>
                <el-button class="followButton" @click="ChangeFollow">{{ nowFollow }}</el-button>
                <div class="followPart">
                    <p class="partTitle">关注：</p>
                    <p class="followNum">{{ followNum }}</p>
                    <p class="partPost">帖子：</p>
                    <p class="postNum">{{ postNum }}</p>
                </div>
                <el-input 
                        type="text" 
                        class="search-input" 
                        placeholder="搜索..." 
                        v-model="searchtxt"
                />
                <el-button class="search-button" @click="searchAll">搜索</el-button>
            </div>
            <div class="choosePart">
                <el-button class="searchpartbutton" :class="{'force-hover': isNowHot }" @click="ChangeNowMod()">所有内容</el-button>
                <el-button class="searchpartbutton" :class="{'force-hover': isNowOnly }" @click="ChangeNowMod()">只看吧主</el-button>
                <el-button class="searchpartbutton" :class="{'force-hover': isNowNew }" @click="isNowNew = !isNowNew">时间倒序</el-button>
                <el-button class="searchpartbutton"  @click="gotoMainPage">返回主页</el-button>
            </div>
            <div class="contentPart">
                <innerPost class="myInnerPost" :post="post" :userId="userId" :forumId="forumId" :isNowNew="isNowNew" :is-master-only="isNowOnly"/>
                <div class="rightMessage" ref="rightMessage">
                    <div class="MyMessage">
                        <MyPostHot class="myHot" :getUserId="userId"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import {ref, onMounted, onUnmounted, onBeforeMount,computed,reactive} from 'vue'
import HotSearch from '@/components/HotSearch.vue';
import innerPost from '@/components/innerPost.vue';
import MyPostHot from '@/components/MyPostHot.vue';
import { useRoute ,useRouter} from 'vue-router'
import axios from 'axios'

const route = useRoute()
const router = useRouter()

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

const postId = computed(() => {
  const raw = route.query.postId
  if (!raw) return ''               // 没有就返回空字符串
  return raw
})

let post=ref(null)

const scrollContainer = ref(null);
const rightMessage = ref(null);
let nowFollow = ref('取消关注');
let postNum = ref(514);
let followNum = ref(114);
let showimage1 = ref('PostImage.png');
let postname = ref('飧筱刅吧');
let isNowHot = ref(true);
let isNowOnly = ref(false);
let isNowNew = ref(false);
let searchtxt = ref('')

const getImageUrl = (imageName) => {
    try {
        return require(`../assets/${imageName}`);
    } catch (error) {
        console.error('Error loading image:', error);
        return '';
    }
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

const ChangeNowMod = () => {
    if(isNowHot.value) {
        isNowHot.value = false;
        isNowOnly.value = true;
    } else {
        isNowHot.value = true;
        isNowOnly.value = false;
    }
}

const searchAll = () => {
  router.push({
    path:'/Search',
    query: {
      userId: userId.value,
      txt: searchtxt.value
  }})
}

const gotoMainPage = () => {
    router.push({
        path:'/MainPage',
        query: {
            loginData: userId.value,
    }})
}


// 处理滚动事件
const handleScroll = () => {
    if (!scrollContainer.value || !rightMessage.value) return;
    
    const container = scrollContainer.value;
    const message = rightMessage.value;
    const contentPart = message.parentElement;
    
    // 计算内容区顶部到容器顶部的距离
    const contentTopOffset = contentPart.offsetTop;
    // 计算滚动距离
    const scrollTop = container.scrollTop;
    
    // 当滚动距离超过内容区顶部时
    if (scrollTop > contentTopOffset) {
        // 设置固定定位，使其跟随滚动
        message.style.position = 'fixed';
        message.style.top = '0px';
        message.style.right = `${window.innerWidth - contentPart.getBoundingClientRect().right}px`;
    } else {
        // 恢复相对定位
        message.style.position = 'static';
        message.style.right = 'auto';
    }
}

onBeforeMount( async () => {

    const { data:postdata, status:poststatus } = await axios.get(
    'http://localhost:8080/post/'+postId.value, 
    {
      validateStatus: () => true
    })
    if(poststatus == 200){
        post=postdata.post
    }

    console.log('Post View Start')
    const { data, status } = await axios.get(
        'http://localhost:8080/forum/search/'+forumId.value, 
        {
        validateStatus: () => true
        })

    if(status == 200 ){
        postname.value=data.forum.name
        postNum.value=data.forum.postCount == null?0:data.forum.postCount
        followNum.value=data.forum.followCount == null?0:data.forum.followCount
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

onMounted(() => {
    if (scrollContainer.value) {
        scrollContainer.value.addEventListener('scroll', handleScroll);
        // 初始调用一次以设置正确的位置
        handleScroll();
    }


});

onUnmounted(() => {
    if (scrollContainer.value) {
        scrollContainer.value.removeEventListener('scroll', handleScroll);
    }
});
</script>

<style scoped>

.wholeComponent {
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  position: relative;
  height: 100vh;
  width: 100%;
  background-color: rgb(172, 207, 241);
}

.home_scrollable-content {
  height: 100%;
  overflow-y: auto;
  overflow-x: hidden;
  padding-right: 0px;
}

.postName {
    white-space: nowrap;
    margin-top:35px;
    margin-left: 20px;
    font-size: 24px;
    font-weight: 400;
    color: rgb(75, 75, 75);
}

.partTitle {
    font-size: 15px;
    color:rgb(138, 138, 138)
}

.followNum {
    font-size: 15px;;
    margin-left: 5px;
    color:rgb(234, 159, 72)
}

.partPost {
    margin-left: 20px;
    font-size: 15px;
    color:rgb(138, 138, 138)
}

.postNum {
    font-size: 15px;;
    margin-left: 5px;
    color:rgb(234, 159, 72)
}

.backImage {
    margin-top:20px;
    margin-left: 30px;
    width: 60px !important;
    height: 60px;
    border:2px solid white;
}

.followPart {
    display: flex;
    flex-direction: row;
    margin-top:27px;
    margin-left: 25px;
}

.titlePart {
    margin-left: 330px;
    width: 1050px;
    height: 100px;
    background-color: rgb(243, 245, 248);
    display: flex;
    flex-direction: row;
    border: 0.5px solid rgb(232, 232, 232)
}

.followButton {
    margin-top:38px;
    margin-left: 25px;
    border-radius: 0;
}

.search-input {
    height: 30px;
    width: 200px;
    margin-top: 37px;
    margin-left: 100px;
    border-radius: 0;
}

.search-button {
    width: 80px;
    height: 30px;
    border-radius: 0;
    margin-top:37px;
    background-color: rgb(238, 239, 240);
}

.choosePart {
    margin-left: 330px;
    width: 1050px;
    height: 50px;
    background-color: rgb(243, 245, 248);
    display: flex;
    flex-direction: row;
    border: 0.5px solid rgb(228, 228, 228)
}

.searchpartbutton {
    width: 130px;
    height: 50px;
    background-color: rgb(238, 239, 240);
    border-radius: 0;
    margin: 0;
}

.searchpartbutton.force-hover {
  background-color: #ecf5ff !important;
  color: #409eff !important;
  border-color: #c6e2ff !important;
}

.contentPart {
    margin-left: 330px;
    width: 1050px;
    position: relative;
    display: flex;
    flex-direction: row;
}

.myInnerPost {
    min-height: auto;
    width: 835px;
}

.myHot {
    border-radius: 0;
    width: 215px;
    height: 1000px;
}

.rightMessage {
    align-self: flex-start;
    margin-left: 0px;
}

</style>    