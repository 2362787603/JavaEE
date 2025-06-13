<template>
    <div class="whole-component">
        <p class="title">热门吧</p>
            <div v-for="(image, index) in displayImages" :key="index" class="image-line">
                <div v-for="(showimage, index2) in image" :key="index2" class="image-row">
                    <div class="hotshow" @click="handleInTo(index,index2)">
                        <el-image
                            :src="`http://localhost:8080/image/downloadById/`+allForumList[index * 5 + index2].imageId" 
                            :alt="`Image ${index + 1}`" 
                            class="scaled-image"
                            fit="cover"
                        />
                        <div class="hotnameshow">
                            <p>{{ allForumList[index * 5 + index2].name }}</p>
                            <div class="comment">
                                <span class="comment-icon">
                                <i class="fa-regular fa-comment"></i>
                                </span>
                                <p>{{ allForumList[index * 5 + index2].followCount }}</p>
                            </div>
                            <div class="follow">
                                <span class="userfollow" @click="handleLike">
                                <i class="fa-regular fa-heart"></i>
                                </span>
                                <p>{{ allForumList[index * 5 + index2].postCount }}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
    </div>
</template>

<script setup>
import { computed,onMounted,ref,defineProps} from 'vue';
import {useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

let allForumList=ref([])

const props = defineProps({
  userId: {
    type:String,
    default:"1"
  }
});

// Limit to maximum 3 images
const displayImages = computed(() => {
  let imagesize = allForumList.value.length;
  let arraylenth = Math.min(3,allForumList.value.length / 5);
  let images = []
  for(let i = 0;i < arraylenth;i ++ ){
    let imageRow = [];
    for(let j = i * 5;j < Math.min(imagesize,(i + 1) * 5);j ++ ) imageRow.push('PostImage.png');
    images.push(imageRow);
  } 
  console.log(images)
  return images;
});


const handleInTo = (index,index2) => {
  router.push({
        path:'/Post',
        query: {
            userId: props.userId,
            forumId: allForumList.value[index * 5 + index2].id
    }})
}

onMounted(async ()=>{
  console.log('HotPost Page Start')
  const { data, status } = await axios.get(
    'http://localhost:8080/forum/getAllForum', 
    {
      validateStatus: () => true
    })
  if(status != 200 ){
    console.log(data.message)
  }
  else{
    console.log(data.allForums)
    allForumList.value=data.allForums
    console.log(allForumList.value[0].name)
  }
})

</script>

<style scoped>
.whole-component {
  width: 900px;
  margin: 0 auto;
  padding: 20px;
  padding-top:5px;
  border-radius: 8px; /* 添加圆角效果 */
  background-color: rgb(249, 248, 248);
}

.title {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 10px;
  margin-top:8px;
}

.image-line {
  display: flex;
  flex-wrap: wrap;
  margin: -10px; /* Negative margin to offset the padding on image-row */
}

.image-row {
  position: relative;
  width: 180px; /* 16:9 aspect ratio with 100px height */
  height: 90px;
  overflow: hidden;
  border-radius: 4px;
  margin:2px; /* 5 images per row = 20% width */
  margin-bottom: 5px;
  padding: 2px;
  box-sizing: border-box;
  background-color: white;
}

.hotshow {
  display: flex;
  flex-direction: row;
  height: 100%;
  border-radius: 2px;
  overflow: hidden;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s;
  background-color: white;
}

.hotshow:hover {
  transform: translateY(-5px);
}

/*.scaled-image {
  width: 130px;
  height: 100%; 
  object-fit: cover;
}*/
.scaled-image {
  margin-top: 10px;
  width: 70px;
  height: 70px;
  object-fit: cover;
  border-radius: 8px; /* 添加圆角效果 */
}

.hotnameshow {
  width: 45%;
  padding: 5px;
  margin-top: 5px;
  background-color: white;
}

.hotnameshow p {
  font-weight: normal;
  margin: 0;
  margin-left:3px;
  font-size: 16px;
  text-align: center;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.comment p {
  margin-left: 10px;
  color: #7b7c7c; /* 蓝色 */
  font-size: 14px;
  text-decoration: none;
}

.comment {
  margin-top: 5px;
  margin-left:5px;
  display: flex;
  flex-direction: row;
}

.comment-icon {
  color: #7b7c7c; /* 灰色 */
  font-size: 18px; /* 图标大小 */
  margin-top: -3px;
  margin-left:5px;
}

.follow {
  margin-left:18%;
  display: flex;
  flex-direction: row;
}

.follow p {
  margin:0;
  margin-left:10px;
  color: #7b7c7c;
  font-size: 14px;
  text-decoration: none;
}

.userfollow {
  color: #808080; /* 灰色 */
  font-size: 18px; /* 图标大小 */
  margin-top: -3px;
  margin-left: -4px;
}

/* Responsive design for smaller screens */
@media screen and (max-width: 1200px) {
  .image-row {
    width: 25%; /* 4 images per row */
  }
}

@media screen and (max-width: 992px) {
  .image-row {
    width: 33.333%; /* 3 images per row */
  }
}

@media screen and (max-width: 768px) {
  .image-row {
    width: 50%; /* 2 images per row */
  }
}

@media screen and (max-width: 480px) {
  .image-row {
    width: 100%; /* 1 image per row */
  }
}
</style>