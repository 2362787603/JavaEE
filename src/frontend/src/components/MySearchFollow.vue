<template>
    <div class="wholeComponent">
        <div class="titleLine">
            <div v-for="(showimage, index) in props.getimages" :key="index" class="image-row">
                <el-image
                    :src="getImageUrl(showimage)" 
                    :alt="`Image ${index + 1}`" 
                    class="scaled-image"
                    fit="cover"
                />
                <div class="content">
                    <h3>{{ getnames[index] }}</h3>
                    <div class="information">
                        <p class="postnum">📝 发帖总数：&nbsp; {{ getpostnum[index] }}</p>
                        <p class="follownum">➕ 关注总数：&nbsp; {{ follownum[index] }} </p>
                    </div>
                </div>
                <el-button class="cancelFollow"  @click="changeFollow(index)">{{ buttonContent[index] }}</el-button>
            </div>
        </div>
    </div>
</template>

<script setup>

import { defineProps,ref} from 'vue';

// Define props
const props = defineProps({
  SearchForum: {
    type:Array,
    default: null
  },
  getimages: {
    type: Array,
    default: () => ['BackGround.png','LoginBackGroud.png','LoginTestFinal.png','BackGround.png','LoginBackGroud.png','LoginTestFinal.png','BackGround.png','LoginBackGroud.png','LoginTestFinal.png','BackGround.png','LoginBackGroud.png','LoginTestFinal.png']
  },
  getnames: {
    type:Array,
    default: ()=> ['飧筱刅吧','LoginBackGroud.png','LoginTestFinal.png','BackGround.png','LoginBackGroud.png','LoginTestFinal.png','BackGround.png','LoginBackGroud.png','LoginTestFinal.png','BackGround.png','LoginBackGroud.png','LoginTestFinal.png']
  },
  getpostnum: {
    type:Array,
    default: ()=>[1,1,4,5,1,4,114,514,19,19,81,0]
  },
  getfollownum: {
    type:Array,
    default: ()=>[234,324,5,23,56,78,22,34,2,3,4,567,8888]
  }
});

let getimages=ref([])
let getnames=ref([])
let getpostnum=ref([])
let follownum=ref([])
console.log(props)
for(const forum of props.SearchForum){
  getimages.value.push('PostImage.png')
  getnames.value.push(forum.name)
  getpostnum.value.push(forum.post_count)
  follownum.value.push(forum.follow_count)
}

let buttonContent=ref([])
for(let i = 0;i < props.SearchForum.length;i ++ ){
    buttonContent.value.push('取消关注');
}

const getImageUrl = (imageName) => {
  try {
    return require(`../assets/${imageName}`);
  } catch (error) {
    console.error('Error loading image:', error);
    return ''; // Return empty string or a placeholder image URL
  }
}

const changeFollow = (index) =>{
    if(buttonContent.value[index] == '取消关注'){
        follownum.value[index] --;
        buttonContent.value[index] = '关注';
    }
    else if(buttonContent.value[index] == '关注'){
        follownum.value[index] ++;
        buttonContent.value[index] = '取消关注';
    }
}

</script>

<style scoped>

.wholeComponent{
    background-color: rgb(234, 233, 233);
    height: 100%;
}

.postnum {
  padding: 0;
  margin: 0;
}

.follownum {
  padding: 0;
  margin: 0;
}

.scaled-image {
  margin-top: 10px;
  width: 70px;
  height: 70px;
  object-fit: cover;
  border-radius: 8px; /* 添加圆角效果 */
}

.image-row {
    margin-top:20px;
    margin-left:20px;
    margin-right: 20px;
    padding: 10px;
    display: flex;
    flex-direction: row;
    height:100px;
    background-color:rgb(254, 254, 254);
    border-radius: 1ch;
}


.titleLine {
    height: 100%;
    margin-top:20px;
    border-radius: 0;
}

.content {
    border-radius: 0;
    background-color:rgb(246, 245, 245);
    padding: 5px;
    margin-left: 10px;
    width: 85%;
}

.content h3{
    margin-top:10px;
    margin-left:5px;
}

.information {
    display: flex;
    flex-direction: row;
}

.information p{
    font-size: 16px;
    margin-top: 0px;
    margin-left:10px;
}

.follownum {
    margin-left: 30px !important;
}

.cancelFollow {
    margin-left:-150px;
    margin-top:28px;
    width: 120px;
    height: 45px;
}

</style>