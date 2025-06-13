<template>
    <Teleport to="body">
            <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
            <div class="modal-content">
                <button class="close-btn" @click="showModal = false">×</button>
                <!-- 这里放置你要显示的组件 -->
                <ChangeMessage :initialUsername="userName" :initialPhone="userPhone" />
            </div>
            </div>
    </Teleport>
    <div class="wholeHomePage">
        <div class="home_scrollable-content">
            <HotSearch/>
            <!-- 使用 Teleport 将弹窗内容传送到 body 元素下 -->
            <div class="HomePageTitle">
                <div class="leftpart"></div>
                <div class="middlepart">
                    <div class="titlepart">
                        <div class="photopicture">
                            <el-image
                                :src="getImageUrl(showimage)" 
                                :alt="`Image ${index + 1}`" 
                                class="homeImage"
                                fit="cover"
                            />
                        </div>
                        <div class="introduce">
                            <p>{{ username }}</p>
                            <div class="information">
                                <p>今天是ta在论坛的:&nbsp;{{ usertime }}天&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 发帖:  {{ userposttime }}</p>
                            </div>
                        </div>
                    </div>
                    <div class="choosepart">
                        <div v-for="(name, index) in buttonList" :key="index" class="image-line">
                            <el-button class="myButton" :class="{'force-hover': ishoveredList[index] }" @click="changeNowPage(index)">{{ name }}</el-button>
                        </div>
                    </div>
                    <div class="followPart">
                        <div v-if="nowPage === 'MyHomePost'">
                            <OtherHomePost :userId="userId"/>
                        </div>
                        <div v-if="nowPage === 'MyHomeFollow'">
                            <OthersHomeFollow :userId="userId"/>
                        </div>
                        <div v-if="nowPage === 'MyHomeLike'">
                            <MyHomeLike :userId="userId"/>
                        </div>
                        <div v-if="nowPage === 'MyHomeComment'">
                            <div v-for="i in range(0,commentList.length)" :key="i" class="myLike">
                                <myComment :userId="userId" :comment="commentList[i]"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="rightpart"></div>
            </div>
        </div>
    </div>
    
</template>

<script setup>
    // 脚本部分保持不变
    import { ref} from 'vue'
    import HotSearch from '@/components/HotSearch.vue';
    import ChangeMessage from '@/components/ChangeMessage.vue';
    import OthersHomeFollow from '@/components/OthersHomeFollow.vue';
    import MyHomeLike from '@/components/MyHomeLike.vue';
    import myComment from '@/components/myComment.vue';
    import OtherHomePost from '@/components/OtherHomePost.vue';

    import {computed, onBeforeMount} from 'vue'
    import { useRoute } from 'vue-router'
    import axios from 'axios'

    const route = useRoute()
    const userId = computed(() => {
      const raw = route.query.userId
      if (!raw) return ''               // 没有就返回空字符串
      return raw
    })

    const username=ref('论坛用户_UNKLJSDKUF');
    const usertime=ref('1');
    const userposttime=ref('1')
    const showimage=ref('head.png')
    const buttonList=ref(['📝ta的论坛','💖ta的点赞','➕关注的贴','🗨️ta的评论']);
    let ishoveredList=ref([true,false,false,false,false]);
    let showModal=ref(false);
    let userName=ref('论坛用户_114514');
    let userPhone=ref('15684926543');
    let AlluserPost=ref([])
    let commentList=ref([])
    let nowPage=ref('MyHomePost');

    const getImageUrl = (imageName) => {
        try {
            return require(`../assets/${imageName}`);
        } catch (error) {
            console.error('Error loading image:', error);
            return '';
        }
    }

    function changeNowPage(index){
        for(let nowindex = 0;nowindex < ishoveredList.value.length;nowindex ++ ) ishoveredList.value[nowindex] = false;
        ishoveredList.value[index] = true;
        if(index == 0) nowPage.value='MyHomePost';
        else if(index == 2) nowPage.value="MyHomeFollow";
        else if(index == 1) nowPage.value="MyHomeLike";
        else if(index == 3) nowPage.value="MyHomeComment";
    }

    const range = (start, end) => {
        return Array.from({length: end - start}, (_, index) => start + index);
    }

    onBeforeMount(async ()=>{

      console.log("Before Mounted Start!!")
      const { data:userdata, status:userstatus } = await axios.get(
          'http://localhost:8080/user/'+userId.value, 
          {
              validateStatus: () => true
          }
      )
      if(userstatus == 200 ){
          username.value=userdata.user.username
          userPhone.value=userId.value
      }

      const { data:postdata, status:poststatus } = await axios.get(
      'http://localhost:8080/post/user/' + userId.value,
      {
        validateStatus: () => true
      })
      if(poststatus == 200){
        AlluserPost.value=postdata.posts
        userposttime.value=AlluserPost.value.length
      }

      const { data:commentdata, status:commentstatus } = await axios.get(
      'http://localhost:8080/comment/user/' + userId.value,
      {
        validateStatus: () => true
      })
      if(commentstatus == 200){
        console.log("99999999999999999999999999999999")
        console.log(commentdata)
        commentList.value=commentdata.comments
      }
    })
</script>

<style scoped>
.wholeHomePage {
  background-image: url('../assets/PhotoTest.png');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  position: relative;
  height: 100vh; /* 保持视口高度 */
  width: 100%;
  overflow: hidden; /* 这个属性保持不变，因为我们希望在内部容器滚动 */
  background-color: rgb(234, 233, 233);
}

.home_scrollable-content {
  height: 100%;
  overflow-y: auto; /* 保持垂直方向滚动 */
  overflow-x: hidden;
  padding-right: 0px;
}

/* 滚动条样式保持不变 */
.home_scrollable-content::-webkit-scrollbar {
  width: 8px;
}

.home_scrollable-content::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

.home_scrollable-content::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 4px;
}

.home_scrollable-content::-webkit-scrollbar-thumb:hover {
  background: #555;
}

.HomePageTitle {
  width: 100%;
  min-height: 100%; /* 修改为最小高度，确保可以随内容扩展 */
  max-width: 100%;
  display: flex;
  flex-direction: row;
}

.leftpart {
  background-color: rgba(22, 99, 186, 0.5);
  color: white;
  padding: 20px;
  border-radius: 0px;
  width: 300px;
}

.rightpart {
  background-color: rgba(22, 99, 186, 0.5);
  color: white;
  padding: 20px;
  border-radius: 0px;
  width: 300px;
}

.middlepart {
  background-color: rgb(246, 245, 245);
  margin-top: 250px;
  width: 1010px;
  /* 修改: 移除固定高度，允许内容撑开高度 */
  display: flex;
  flex-direction: column;
  /* 确保内容可以滚动 */
  min-height: calc(100vh - 250px); /* 减去顶部margin的高度 */
}

.titlepart {
  display: flex;
  flex-direction: row;
  flex-shrink: 0; /* 防止标题部分被压缩 */
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
  margin-top:20px;
  margin-left: 20px;
}

.introduce p {
  font-weight: 400 !important;
  font-size: 22px !important;
  margin-top: 0px;
}

.information {
  margin-top: -10px;
}

.information p {
  font-weight: 100 !important;
  font-size: 14px !important;
  white-space: pre-wrap !important;
  color: #555;
}

.changeMessage {
  margin-top: -70px;
  border-radius: 0;
  border: solid 1px rgb(101, 159, 246);
  color: rgb(101, 159, 246);
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

.choosepart {
  margin-top: 10px;
  display: flex;
  flex-direction: row;
  width: 1000px;
  padding: 0px;
  background-color: rgb(239, 237, 237);
  border-radius: 0;
  height: 50px;
  border: solid 0.5px rgb(219, 220, 220);
  flex-shrink: 0; /* 防止选择部分被压缩 */
}

.followPart {
  padding: 0px;
  background-color: rgb(239, 237, 237);
  /* 修改: 允许内容撑开高度并使用弹性布局 */
  flex: 1;
  overflow-y: auto; /* 如果内容过多，允许在此处滚动 */
  display: flex;
  flex-direction: column;
}

.image-line {
  width: 130px;
}

.myButton {
  height: 50px;
  width: 130px;
  border-radius: 0;
  border: solid 0.5px rgb(219, 220, 220);
  background-color: rgb(239, 237, 237);
}

.myButton.force-hover {
  background-color: #ecf5ff !important;
  color: #409eff !important;
  border-color: #c6e2ff !important;
}

.myLike {
  margin-left:50px;
  margin-top:20px;
}
</style>