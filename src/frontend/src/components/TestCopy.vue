<template>

    <div class="main-container">
      <!-- 关闭按钮 -->
      <el-button
          class="close-button"
      >
      <el-icon size="35px"><Close /></el-icon>
      </el-button>
      <div class="message-or-phone">
        <el-tabs v-model="activeName" size="large">
          <el-tab-pane label="短信登录"  name="message"/>
          <el-tab-pane label="账户登录"  name="account"/>
        </el-tabs>
      </div>
      <el-form
          :model="form"
          label-width="auto"
          class="login-form"
          :rules="rules"
      >
        <div v-if="activeName === 'message'" class="message-div">
          <el-form-item prop="email">
            <el-input v-model="form.email" placeholder="请输入邮箱" class="my-input" />
          </el-form-item>
  
          <div class="flex-verification" >
            <el-form-item class="my-form-item-message-verification" prop="verification">
              <el-input v-model="form.verification" placeholder="请输入验证码" class="my-input-message-verification" />
            </el-form-item >
            <el-button type="primary" class="verification-button" :disabled="isSending || countdown > 0">发送验证码</el-button>
          </div>
        </div>
        <div v-else>
          <el-form-item prop="username">
            <el-input v-model="form.userID" placeholder="用户ID" class="my-input"/>
          </el-form-item>
  
          <el-form-item prop="password">
            <el-input v-model="form.password" placeholder="请输入密码" class="my-input"/>
          </el-form-item>
        </div>
  
        <el-form-item>
          <el-button type="primary"  class="login-button" :disabled="!isFormValid">登录</el-button>
        </el-form-item>
        <div class="checkbox">
          <el-checkbox value="privacy" name="type">
            <el-text>已阅读并同意</el-text>
            <a href="your-user-agreement-url" target="_blank" class="privacy-color">用户协议</a>
            <el-text>和</el-text>
            <a href="your-privacy-policy-url" target="_blank" class="privacy-color">隐私政策</a>
          </el-checkbox>
          <br>
          <el-checkbox value="privacy" name="type">
            <el-text>保持登录状态</el-text>
          </el-checkbox>
        </div>
        <div class="forgetPwd-register">
          <a href="http://localhost:5173/forgetPwd" target="_blank" class="privacy-color">忘记密码</a>
          |
          <a href="http://localhost:5173/register" target="_blank" class="privacy-color">注册</a>
        </div>
        <div class="other-login-methods">
          —
          <el-text style="color: grey;">其他登录方式</el-text>
        </div>
      </el-form>
    </div>
  </template>
  
  <script setup>
  
  import { ref } from 'vue';
  const activeName = ref('message');
  
  const isSending = ref(false);
  const countdown = ref(0);
  // do not use same name with ref
  const form = ref({
    verification: '',
    username: '',
    userID: '',
    phone: '',
    email: '',
    password: '',
    rePassword: '',
    region: '',
    date1: '',
    date2: '',
    delivery: false,
    type: [],
    resource: '',
    desc: '',
  })
  </script>

  <style scoped>
  .upper-picture{
    margin-top: -2.5vh;
    margin-left: -2.7vw;
    width: 150%;
    height: 10vh;
    object-fit: cover; /* 保持图片比例，并裁剪超出部分 */
    margin-bottom: 2vh;
  }
  .main-container{
  
  
  
    position: relative;
    padding-left : 50px;
    padding-right: 50px;
  
  
    background-color: rgb(255, 255, 255);
    border: 2px solid rgba(201, 222, 231, 0.57);
  
    /* 设置圆角：上边两个角为圆角，下面两个角为直角 */
    border-top-left-radius: 40px; /* 左上角圆角 */
    border-top-right-radius: 40px; /* 右上角圆角 */
    margin-left: 22vw;
    bottom: 20vh;
    width: 560px;
    height: 70vh;
    overflow: hidden; /* 隐藏超出容器的部分 */
  
  }
  
  .checkbox{
    width: 10vw;
  }
  .login-button{
    margin-left: 0vw;
    width: 1000px;
    height: 5vh;
    border-radius: 10px;
  }
  .privacy-color{
    color:rgb(0, 149, 255);
  }
  .forgetPwd-register{
    margin-left: 440px;
    width: 15vw;
    margin-bottom: 10vh;
  
  }
  .my-input  {
    font-size: 12px;
    width: 100%;
    padding: 0px;
    height: 6vh;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);  /* 水平偏移，垂直偏移，模糊半径，阴影颜色 */
    transition: box-shadow 0.3s ease;  /* 添加过渡效果，使阴影更平滑 */
    border-radius: 15px;
    border-color: #112170;  /* 聚焦时改变边框颜色 */
    box-shadow: 0 2px 8px rgba(15, 67, 119, 0.3);  /* 聚焦时改变阴影颜色 */
    display: flex;
  }
  .my-input-message-verification{
    font-size: 12px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);  /* 水平偏移，垂直偏移，模糊半径，阴影颜色 */
    border-radius: 15px;
    border-color: #112170;  /* 聚焦时改变边框颜色 */
    box-shadow: 0 2px 8px rgba(15, 67, 119, 0.3);  /* 聚焦时改变阴影颜色 */
    height: 55px;
    padding-left: 3px;
    bottom: 14px;
  }
  .my-input.el-input__wrapper{
    background: none;
    box-shadow: none;
    border:none;
  }
  .my-input-message-verification.el-input__wrapper{
    background: none;
    box-shadow: none;
    border:none;
  }
  .my-form-item-message-verification{
    left: 0px;
    width: 400px;
    height: 55px;
    padding-right: 17px;
  
  }
  
  .other-login-methods{
    margin-top: -5vh;
    margin-left: 230px;
  }
  .weixin-icon{
    width: 50px;
    height: 50px;
    margin-left: -180px;
    margin-bottom: -60px;
    transition: filter 0.5s ease; /* 平滑过渡 */
  }
  .beihang-icon{
    width: 55px;
    height: 57px;
    margin-left: 58px;
    margin-bottom: -68px;
    transition: filter 0.5s ease; /* 平滑过渡 */
  
  }
  .zhongxin-icon{
    width: 48px;
    height: 50px;
    margin-left: 55px;
    margin-bottom: -65px;
    transition: filter 0.5s ease; /* 平滑过渡 */
  
  }
  .zhongxin-icon:hover,
  .weixin-icon:hover,
  .beihang-icon:hover {
    cursor: pointer;
    filter: invert(1) brightness(1.1) contrast(1.5);
  }
  .verification-button{
  
    left: 30px;
    height: 57px;
    width: 140px;
    margin-bottom: 10px;
    border-radius: 10px;
    padding-left: 20px;
  
  }
  
  .message-or-phone{
    margin-top: 10px;
    margin-bottom: 20px;
  }
  .message-div{
    padding-bottom: -10vh;
    margin-bottom: -6vh;
    transition: opacity 2s;
  }
  /* 关闭按钮样式 */
  .close-button {
    position: absolute;
    top: 10px;
    right: 10px;
    font-size: 16px;
    color: #efe9e9;
    background-color: transparent;
    border: none;
    cursor: pointer;
    z-index: 99199;
  }
  
  .close-button:hover {
    color: #494646;
  }
  
  .flex-verification{
    display: flex;
    margin-bottom: 30px;
    height: 100px;
    width:860px;
  }
  
  
  </style>