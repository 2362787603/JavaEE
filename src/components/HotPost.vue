<template>
    <div class="whole-component">
        <p class="title">热门吧</p>
            <div v-for="(image, index) in displayImages" :key="index" class="image-line">
                <div v-for="(showimage, index2) in image" :key="index2" class="image-row">
                    <div class="hotshow" @click="handleInTo">
                        <el-image
                            :src="getImageUrl(showimage)" 
                            :alt="`Image ${index + 1}`" 
                            class="scaled-image"
                            fit="cover"
                        />
                        <div class="hotnameshow">
                            <p>{{ getnames[index * 5 + index2] }}</p>
                            <div class="comment">
                                <span class="comment-icon">
                                <i class="fa-regular fa-comment"></i>
                                </span>
                                <p>{{ commentNumber }}</p>
                            </div>
                            <div class="follow">
                                <span class="userfollow" @click="handleLike">
                                <i class="fa-regular fa-heart"></i>
                                </span>
                                <p>{{ followNumber }}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
    </div>
</template>

<script setup>
import { computed,defineProps,ref} from 'vue';

let commentNumber = ref(114)
let followNumber = ref(514)

// Define props
const props = defineProps({
  getimages: {
    type: Array,
    default: () => ['BackGround.png','LoginBackGroud.png','LoginTestFinal.png','BackGround.png','LoginBackGroud.png','LoginTestFinal.png','BackGround.png','LoginBackGroud.png','LoginTestFinal.png','BackGround.png','LoginBackGroud.png','LoginTestFinal.png','LoginTestFinal.png','BackGround.png','LoginBackGroud.png','LoginTestFinal.png','BackGround.png','LoginBackGroud.png','LoginTestFinal.png','BackGround.png','LoginBackGroud.png','LoginTestFinal.png']
  },
  getnames: {
    type:Array,
    default: ()=> ['飧筱刅吧','LoginBackGroud.png','LoginTestFinal.png','BackGround.png','LoginBackGroud.png','LoginTestFinal.png','BackGround.png','LoginBackGroud.png','LoginTestFinal.png','BackGround.png','LoginBackGroud.png','LoginTestFinal.png','LoginTestFinal.png','BackGround.png','LoginBackGroud.png','LoginTestFinal.png','BackGround.png','LoginBackGroud.png','LoginTestFinal.png','BackGround.png','LoginBackGroud.png','LoginTestFinal.png']
  }
});

// Limit to maximum 3 images
const displayImages = computed(() => {
  let imagesize = props.getimages.length;
  let arraylenth = Math.min(3,imagesize / 5);
  let images = []
  for(let i = 0;i < arraylenth;i ++ ){
    let imageRow = [];
    for(let j = i * 5;j < Math.min(imagesize,(i + 1) * 5);j ++ ) imageRow.push(props.getimages[j]);
    images.push(imageRow);
  } 
  return images;
});

const getImageUrl = (imageName) => {
  try {
    // For Vite
    //return new URL(`../assets/${imageName}`, import.meta.url).href;   
    // Alternatively, if using webpack
    return require(`../assets/${imageName}`);
  } catch (error) {
    console.error('Error loading image:', error);
    return ''; // Return empty string or a placeholder image URL
  }
}

const handleInTo = () => {
  followNumber.value ++;
}

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