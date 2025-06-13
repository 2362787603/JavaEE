<template>
  <div class="picture-container">
    <div v-for="(image, index) in displayImages" :key="index" class="image-wrapper">
      <el-image
      :src="image" 
      :alt="`Image ${index + 1}`" 
      class="scaled-image"
      fit="cover"
      />
    </div>
  </div>
</template>

<script setup>
import { computed, defineProps, ref, watch, onBeforeMount } from 'vue';
import axios from 'axios';
// Define props
const props = defineProps({
  postId:{
    type:Number,
    default:() => 1
  },
  images: {
    type: Array,
    default: () => []
  }
});

let fileList=ref([])

const displayImages = computed(() => {
  console.log('Backend images:', fileList.value.slice(0,3));
  return fileList.value.slice(0,3);
});

const waitForPost = () => {
  return new Promise((resolve) => {
    if (props.postId && props.postId !== -1) {
      resolve(props.postId);
      return;
    }
    
    const unwatch = watch(
      () => props.postId,
      (newPost) => {
        if (newPost && newPost !== -1) {
          unwatch();
          resolve(newPost);
        }
      }
    );
  });
}

onBeforeMount(async () => {
    await waitForPost();
    fileList.value = await getPostImage(props.postId)
});

const getPostImage = async () => {
  
    let imageIdList = ref([]);
    let fileList=ref([])
    
    // 获取帖子对应的图片映射
    const { data: replydata, status: replystatus } = await axios.get(
      `http://localhost:8080/postImageMapping/getByPostId/${props.postId}`, 
      {
        validateStatus: () => true
      }
    );
    
    if (replystatus === 200) {
      imageIdList.value = replydata.mappings || [];
      console.log('Image mappings:', imageIdList.value);
    }
    else console.log("get Image Fail!!")

    // 获取每个图片的实际数据
    for (const imagerow of imageIdList.value) {
      try {
        const { data: imageBlob, status: imagestatus } = await axios.get(
          `http://localhost:8080/image/downloadById/${imagerow.imageId}`,
          {
            responseType: 'blob', // 重要：设置响应类型为 blob
            validateStatus: () => true
          }
        );
        
        if (imagestatus === 200 && imageBlob) {
          // 创建 Object URL 用于显示图片
          const imageUrl = URL.createObjectURL(imageBlob);
          fileList.value.push(imageUrl);
        }
      } catch (error) {
        console.error(`Error loading image ${imagerow.imageId}:`, error);
      }
    }
    return fileList.value
}
</script>

<style scoped>
.picture-container {
  display: flex;
  flex-direction: row;
  gap: 12px;
  align-items: center;
  margin-top:10px;
  margin-bottom: 10px;
  width: 100%;
}

.image-wrapper {
  position: relative;
  width: 40%; /* 16:9 aspect ratio with 100px height */
  height: 120px;
  overflow: hidden;
  border-radius: 4px;
  margin:2px;
}

.scaled-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.scaled-image:hover {
  transform: scale(1.05);
}
</style>