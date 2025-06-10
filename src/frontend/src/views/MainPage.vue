<template>
  <div class="page-container">
    <div class="scrollable-content">
      <div class="search-container">
        <img class="main-image" src="../assets/MainPagePhoto.png" alt="Main Page Photo">
        <div class="title">
          <p>摆渡<span class="ship">⛵</span>论坛</p>
        </div>
        <div class="search-input-container">
          <input 
            type="text" 
            class="search-input" 
            v-model="searchText"
            placeholder="搜索..." 
          />
          <button class="search-button" @click="searchAll">搜索</button>
        </div>
      </div>
      <div class="underpart">
        <div class="leftpart">
          <LeftHot class="mylefthot" :getUserId="loginData"/>
        </div>
        <div class="middlepart">
          <HotPost class="hotpost"/>
          <div class="guessyoulike">
            <p>最新动态</p>
          </div>
          <div class="mycontent">
            <div class="pagepost">
              <!-- 循环渲染 HomePost 组件并传递帖子数据，监听更新点赞事件 -->
              <HomePost 
                v-for="(post, index) in userFollowedForumsPosts" 
                :key="index" 
                :post="post" 
                class="page"
                @update-like="updatePostLike"
              />
            </div>
            <div class="rightHot">
              <RightHot />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed,ref, onMounted } from 'vue';
import axios from 'axios';
import HomePost from '@/components/HomePost.vue';
import HotPost from '@/components/HotPost.vue';
import LeftHot from '@/components/LeftHot.vue';
import RightHot from '@/components/RightHot.vue';
import {  useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter();
const loginData = computed(() => {
  const raw = route.query.loginData;
  if (!raw) return '';
  return raw;
});

let userInformation = ref(null);
let searchText = ref(null);
// 存储用户关注论坛的帖子数据
const userFollowedForumsPosts = ref([]); 
const loading = ref(true);
const error = ref(null);

const searchAll = () => {
  router.push({
    path: '/Search',
    query: {
      userId: loginData.value,
      txt: searchText.value
    }
  });
};

// 获取用户关注论坛的帖子数据
const fetchUserFollowedForumsPosts = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/forum/getAllPostsOfUserFollowedForums?userId=${loginData.value}`);
    if (response.data.success) {
      userFollowedForumsPosts.value = response.data.posts;
    } else {
      error.value = '获取用户关注论坛的帖子失败: ' + response.data.message;
    }
  } catch (err) {
    error.value = '请求出错: ' + err.message;
  } finally {
    loading.value = false;
  }
};

onMounted(async () => {
  console.log('Main Page Start');
  const { data, status } = await axios.get(
    'http://localhost:8080/user/' + loginData.value, 
    {
      validateStatus: () => true
    }
  );
  if (status !== 200) {
    console.log(data.message);
  } else {
    console.log("success!!");
  }
  userInformation.value = data;
  await fetchUserFollowedForumsPosts(); 
});

const updatePostLike = async (postId, newLikeNumber) => {
  try {
    // 向后端发送点赞请求，假设后端接口为 /like
    const response = await axios.post('http://localhost:8080/post/like', {
      postID: postId
    });

    if (response.data.success) {
      // 若后端响应成功，更新本地数据
      const postIndex = userFollowedForumsPosts.value.findIndex(post => post.id === postId);
      if (postIndex !== -1) {
        userFollowedForumsPosts.value[postIndex].likeNumber = newLikeNumber;
      }
    } else {
      console.error('点赞失败:', response.data.message);
    }
  } catch (err) {
    console.error('点赞请求出错:', err.message);
  }
};
</script>

<style scoped>
.page-container {
  position: relative;
  height: 100vh; /* 使用视口高度让容器充满整个屏幕 */
  width: 100%;
  overflow: hidden; /* 防止外部出现滚动条 */
  background-color:rgb(234, 233, 233)
}

.scrollable-content {
  height: 100%;
  overflow-y: auto; /* 仅在垂直方向允许滚动 */
  padding-right: 5px; /* 为滚动条预留一些空间 */
}

/* 自定义滚动条样式（可选） */
.scrollable-content::-webkit-scrollbar {
  width: 8px; /* 滚动条宽度 */
}

.scrollable-content::-webkit-scrollbar-track {
  background: #f1f1f1; /* 滚动条轨道背景 */
  border-radius: 4px;
}

.scrollable-content::-webkit-scrollbar-thumb {
  background: #888; /* 滚动条滑块颜色 */
  border-radius: 4px;
}

.scrollable-content::-webkit-scrollbar-thumb:hover {
  background: #555; /* 悬停时滑块颜色 */
}

.search-container {
  position: relative;
  width: 100%;
  overflow: hidden;
}

.main-image {
  width: 100%;
  height: 330px;
  display: block;
  object-fit: cover;
}

.title {
  position: absolute;
  top: 32%;
  left: 57%;
  transform: translate(-50%, -50%);
  width: 120%;
  max-width: 500px;
  display: flex;
}

.title p{
  color:rgb(218, 218, 218);
  font-size: 50px;
  font-family: "Microsoft YaHei", "PingFang SC", "Hiragino Sans GB", "Noto Sans SC", "Source Han Sans CN", sans-serif;
  font-weight: bold;
  /*font-style: italic;*/
}

.ship{
    color:rgb(218, 218, 218);
}

.search-input-container {
  position: absolute;
  top: 57%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 120%;
  max-width: 650px;
  display: flex;
}

.search-input {
  flex: 1;
  padding: 12px 20px;
  font-size: 16px;
  border: none;
  border-radius: 4px 0 0 4px;
  box-shadow: 0 2px 5px rgba(241, 241, 241, 0.2);
}

.guessyoulike {
  position: sticky; /* 修改为sticky定位 */
  top: -2px; /* 设置粘性定位的顶部距离 */
  flex: 1;
  z-index: 1000; /* 较高的z-index值 */
  padding: 12px 20px;
  font-size: 16px;
  border: none;
  border-radius: 4px 0 0 4px;
  background-color: white;
  box-shadow: 0 2px 5px rgba(241, 241, 241, 0.2);
  margin: 12px;
  height: 25px;
  width: 900px;
}

.guessyoulike p{
    font-size: 16px;
    text-decoration: underline 2.5px solid; 
    text-underline-offset: 14px;
    color:#4285f4;
    margin-top:4px;
    margin-left:8px;
}

.search-input:focus {
  border-color: #4a90e2; /* 蓝色边框 */
  outline: 2px solid #4a90e2; /* 添加内边框 */
  outline-offset: -2px; /* 负值使outline向内偏移，形成内边框效果 */
}

.search-button {
  padding: 12px 24px;
  background-color: #4285f4;
  color: white;
  font-size: 16px;
  border: none;
  border-radius: 0 4px 4px 0;
  cursor: pointer;
}

.search-button:hover {
  background-color: #357ae8;
}

.page {
  width: 630px;
  margin-top: 10px;
  margin-bottom: 10px;
  margin-left:12px;
}

.hotpost{
  margin-top: 15px;
  margin-left:12px;
}

.underpart {
  display: flex;
  flex-direction: row;
}

.leftpart {
    margin-top: 15px;
    margin-left:14%;
    position: sticky; /* 修改为sticky定位 */
    top: 10px; /* 设置粘性定位的顶部距离 */
    height: 1200px;
    align-self: flex-start; 
}

.mycontent {
  display: flex;
  flex-direction: row;
}

.rightHot {
    margin-top: 12px;
    margin-left: 20px;
    position: sticky; /* 修改为sticky定位 */
    top: 60px; /* 设置粘性定位的顶部距离 */
    align-self: flex-start; 
}
</style>
