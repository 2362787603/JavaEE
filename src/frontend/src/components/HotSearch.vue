<template>
    <div class="wholeHotSearch">
        <div class="Hottitle">
          <p>摆渡<span class="Hotship">⛵</span>论坛</p>
        </div>
        <div class="search-input-container">
            <el-input 
            type="text" 
            class="search-input" 
            placeholder="搜索..." 
            />
            <el-button class="search-button">论坛搜索</el-button>
        </div>
    </div>
</template>

<script setup>


</script>

<style scoped>
.wholeHotSearch {
  width: 100%;
  height: 100px;
  margin: 0 auto;
  padding: 20px;
  padding-top:5px;
  border-radius: 0px; /* 添加圆角效果 */
  background-color: white;
  display: flex;
  flex-direction: row;
}

.Hottitle {
  margin-top: 10px;
  margin-left: 380px;
  display: flex;
}

.Hottitle p{
  color:rgb(105, 176, 230);
  font-size: 30px;
  font-family: "Microsoft YaHei", "PingFang SC", "Hiragino Sans GB", "Noto Sans SC", "Source Han Sans CN", sans-serif;
  font-weight: bold;
  /*font-style: italic;*/
}

.Hotship{
    color:rgb(218, 218, 218);
}

.search-input-container {
  margin-top: 40px;
  height:45px;
  margin-left:20px;
  display: flex;
}

.search-input {
  flex: 1;
  width: 500px;
  font-size: 16px;
  border-radius: 0;
  /*border:solid 1px rgb(141, 140, 140);*/
}

:deep(.search-input .el-input__wrapper) {
  border-color: rgb(141, 140, 140);
  transition: border-color 0.3s ease;
}

:deep(.search-input .el-input__wrapper.is-focus) {
  border-color: #4141fd !important;
  box-shadow: 1px #4141fd inset !important;
}

.search-button {
    width:120px;
    color:rgb(237, 236, 236);
    font-size: 18px;
    font-weight: 100;
    background-color: rgb(69, 133, 245);
    border:solid 2px rgb(69, 133, 245);
    height:45px;
    border-radius: 0;
}

:deep(.search-button:hover) {
  background-color: #405ef4;
  border:solid 2px #405ef4;
  color:rgb(237, 236, 236);
  transition: background-color 0.3s ease;
}
</style>