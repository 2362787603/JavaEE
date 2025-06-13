<template>
  <div class="wholeComponent">
    <div class="home_scrollable-content">
      <HotSearch/>
      <div class="titlePart">
        <div class="backPage">
          <el-image
            :src="getImageUrl(showimage1)" 
            :alt="板块背景" 
            class="backImage"
            fit="cover"
          />
        </div>
        <div class="title">
          <div class="photopicture">
            <el-image
            :src="`http://localhost:8080/image/downloadById/`+forumimageId"
              :alt="板块图标" 
              class="homeImage"
              fit="cover"
            />
          </div>
          <div class="introduce">
            <div class="followPart">
              <p class="postName">{{ postname }}</p>
              <el-button class="followButton" @click="ChangeFollow">{{ nowFollow }}</el-button>
              <div class="followCountPart"> <!-- 新增包裹层 -->
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
      </div>
      <div class="contentPart">
        <div class="Mytitle">
          <p class="createMyPostTitle">创建新帖</p>
        </div>
        <PostCreateInput class="contentInput" :userId="userId" :forumId="forumId"/>
      </div>
    </div>
  </div>
</template>

<script setup>
import PostCreateInput from '@/components/PostCreateInput.vue';
import HotSearch from '@/components/HotSearch.vue';
import { ref,computed,reactive,onMounted} from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'

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

const showimage1 = ref('BackGround.png')
let forumimageId = ref(1)
let postname = ref('飧筱刅吧')
let nowFollow = ref('取消关注')
let postNum = ref(514)
let followNum = ref(114)
let introduction = ref('多平台的怪物猎人粉丝和谐讨论贴吧')

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
  height: 100vh;
  width: 100%;
  overflow: hidden;
  background-color: rgb(172, 207, 241);
}

.home_scrollable-content {
  height: 100%;
  overflow-y: auto;
  overflow-x: hidden;
  padding-right: 0px;
}

.backPage {
  width: 100%;
}

.backImage {
  height: 200px;
  width: 1000px;
  border-radius: 0;
}

.titlePart {
  margin-left: 330px;
}

.contentPart {
  width: 1000px;
  background-color: rgb(253,253,253);
  margin: 0 auto; /* 水平居中 */
  margin-top: -25px;
  margin-left: 330px;
  min-height: 400px;
  padding-top: 30px; /* 新增内边距，为标题腾出空间 */
  padding-bottom: 20px;
}

.homeImage {
  width: 150px;
  height: 150px;
  border-radius: 8px;
  margin-left: 20px;
  margin-top: -60px;
  border: 3px solid white;
}

.introduce {
  margin-left: -25px;
}

.title {
  width: 1000px;
  height: 135px;
  background-color: rgb(238, 239, 240);
  display: flex;
  flex-direction: row;
  margin-top: -5px;
}

.postName {
  white-space: nowrap;
  margin-top: 15px;
  margin-left: 10px;
  font-size: 24px;
  font-weight: 400;
  color: black; /* 修正文字颜色 */
}

.followPart {
  display: flex;
  flex-direction: row;
  align-items: center;
  margin-left: 50px;
}

.followButton {
  height: 40px;
  width: 100px;
  margin: 0 30px; /* 调整左右间距 */
}

.followCountPart {
  display: flex;
  flex-direction: row;
  align-items: center;
}

.partTitle {
  font-size: 14px;
  color: rgb(139, 139, 139);
  margin-right: 5px; /* 增加元素间距 */
}

.partPost {
  font-size: 14px;
  color: rgb(139, 139, 139);
  margin: 0 20px; /* 调整左右间距 */
}

.followNum, .postNum {
  font-size: 14px;
  color: rgb(234, 159, 72);
}

.information {
  margin-left: 50px;
  margin-top: -22px;
  color: rgb(140, 140, 139);
  flex-grow: 1; /* 自动填充剩余空间 */
}

.Mytitle {
  padding: 10px 10px; /* 新增内边距，方便调节标题位置 */
}

.createMyPostTitle {
  margin: 0; /* 清除默认边距 */
  margin-top: -15px; /* 可调节的顶部边距 */
  margin-left: 420px;
  margin-bottom: 10px;
  font-size: 26px;
  font-weight: 500;
  color: #333;
}

.postTitle {
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 10px; /* 元素间距 */
}

.search-input {
  flex-grow: 1; /* 输入框自动扩展 */
  height: 40px;
  border-radius: 4px;
  margin-top: 0px; /* 微调输入框位置 */
}

.search-button {
    width: 80px;
    height: 40px;
}

.contentInput {
    margin-left: 10px;
}
</style>