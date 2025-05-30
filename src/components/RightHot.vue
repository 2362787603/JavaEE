<template>
    <div class="RightHotComponent">
        <div class="part">
            <p>当前热门分区</p>
            <div class="part-line" v-for="(name, index) in partnames" :key="index">
                <div class="mylineclass">
                    <p v-if="index < 3" class="hotindextop">{{ index + 1 }}</p>
                    <p v-if="index >= 3" class="hotindex">{{ index + 1 }}</p>
                    <el-button class="hotname">{{ name }}</el-button>
                    <div class="myhotnumber">
                        <p v-if="index < 3" class="hot1">热度: {{ displayhots[index] }}</p>
                        <p v-if="index >= 3" class="hot2">热度: {{ displayhots[index] }}</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import {defineProps,ref} from 'vue';


// Define props
const props = defineProps({
  displaynames: {
    type:Array,
    default: ()=> ['高考','考研','读博','游戏','二齿缘','凫鳢麂','生活','美食','教育']
  },
  displayhots: {
    type:Array,
    default: ()=>[114,514,1919,810,114514,1919810,1,1,4]
  }
});

let partnames=ref(props.displaynames)


</script>

<style scoped>
.RightHotComponent {
  background-color:rgb(249, 248, 248);
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  width: 255px;
  margin-bottom: 20px;
  height: 100%;
}

.part p{
    font-size: 16px;
    color:rgb(80, 79, 79);
    font-weight: bold;
}

.mylineclass {
  display: flex;
  flex-direction: row;
}

.hotindextop {
    font-size: 20px;
    font-weight: bold;
    color:rgb(244, 151, 100) !important;
    font-style: italic;
}

.hotindex {
    font-size: 20px;
    font-weight: bold;
    color:rgb(107, 135, 236) !important;
    font-style: italic;
}

.hotname {
    margin-top: 10px;
    margin-left: 15px;
    width: 90px;
    font-size: 14px;
    border:none;
    font-weight: normal !important;
    white-space: nowrap; /* 防止文本换行 */
    overflow: hidden; /* 隐藏超出部分 */
    text-overflow: ellipsis; /* 使用省略号表示被截断的文本 */
    text-align: left !important;
    color: #6f6f6f !important;
}


.hot1{
    font-weight: bold !important;
    margin-left:20px !important;
    width: 100px;
    white-space: nowrap; /* 防止文本换行 */
    overflow: hidden; /* 隐藏超出部分 */
    text-overflow: ellipsis; /* 使用省略号表示被截断的文本 */
    color:rgb(244, 151, 100) !important;
}

.hot2{
    font-weight: bold !important;;
    margin-left:20px !important;
    width: 100px;
    white-space: nowrap; /* 防止文本换行 */
    overflow: hidden; /* 隐藏超出部分 */
    text-overflow: ellipsis; /* 使用省略号表示被截断的文本 */
    color:rgb(107, 135, 236) !important;
}
</style>