<template>
  <div class="picture-container">
    <div v-for="(image, index) in displayImages" :key="index" class="image-wrapper">
      <el-image
      :src="getImageUrl(image)" 
      :alt="`Image ${index + 1}`" 
      class="scaled-image"
      fit="cover"
      />
    </div>
  </div>
</template>

<script setup>
import { computed,defineProps} from 'vue';

// Define props
const props = defineProps({
  images: {
    type: Array,
    default: () => ['TestPicture.jpg','TestPicture.jpg']
  }
});

// Limit to maximum 3 images
const displayImages = computed(() => {
  console.log(props.images.slice(0,3));
  return props.images.slice(0, 3);
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
</script>

<style scoped>
.picture-container {
  gap: 12px;
  align-items: center;
  margin-top:10px;
  margin-bottom: 10px;
}

.image-wrapper {
  position: relative;
  width: 584px; /* 16:9 aspect ratio with 100px height */
  height: 328.5px;
  overflow: hidden;
  border-radius: 0px;
  margin:2px;
}

.scaled-image {
  width: 100%;
  height: 100%;
  border-radius: 0;
  margin-top: 15px;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.scaled-image:hover {
  transform: scale(1.05);
}
</style>