<template>
  <div class="auth-container">
    <div class="auth-card">
      <!-- 标题和切换按钮 -->
      <div class="tabs">
        <button 
          :class="{ active: activeTab === 'login' }" 
          @click="activeTab = 'login'"
        >
          登录
        </button>
        <button 
          :class="{ active: activeTab === 'register' }" 
          @click="activeTab = 'register'"
        >
          注册
        </button>
      </div>

      <!-- 登录表单 -->
      <form v-if="activeTab === 'login'" @submit.prevent="handleLogin" class="auth-form">
        <div class="form-group">
          <label for="login-username">手机号</label> 
          <input 
            id="login-username" 
            type="text" 
            v-model="loginForm.username" 
            placeholder="请输入手机号" 
            required 
          />
        </div>
        
        <div class="form-group">
          <label for="login-password">密码</label> 
          <input 
            id="login-password" 
            type="password" 
            v-model="loginForm.password" 
            placeholder="请输入密码" 
            required 
          />
        </div>

        <div class="form-checkbox">
          <input 
            id="remember-me" 
            type="checkbox" 
            v-model="loginForm.rememberMe" 
          />
          <label for="remember-me">记住我</label>
        </div>

        <div v-if="loginErrors.length" class="error-messages">
          <p v-for="(error, index) in loginErrors" :key="index">{{ error }}</p>
        </div>

        <button 
          type="submit" 
          class="submit-btn" 
          :disabled="usernameError || passWordError || isSubmitting"
        >
          {{ isSubmitting ? '登录中...' : '登录' }}
        </button>
      </form>

      <!-- 注册表单 -->
      <form v-if="activeTab === 'register'" @submit.prevent="handleRegister" class="auth-form">
        <div class="form-group">
          <label for="register-username">手机号</label> 
          <input 
            id="register-username" 
            type="tel" 
            v-model="registerForm.userphone" 
            placeholder="请输入手机号" 
            required 
          />
          <p v-if="registerForm.userphone.length != 0 && registerForm.userphone.length != 11">请输入合法的手机号</p>
        </div>

        <div class="form-group">
          <label for="register-email">用户名</label> 
          <input 
            id="register-email" 
            type="text" 
            v-model="registerForm.username" 
            placeholder="请输入用户名" 
            required 
          />
        </div>
        
        <div class="form-group">
          <label for="register-password">密码</label> 
          <input 
            id="register-password" 
            type="password" 
            v-model="registerForm.password" 
            placeholder="请输入密码(至少6位)" 
            required 
          />
          <p v-if="registerForm.password != 0 && registerForm.password.length < 6">请输入至少6位密码</p>
        </div>
        
        <div class="form-group">
          <label for="register-confirm-password">确认密码</label> 
          <input 
            id="register-confirm-password" 
            type="password" 
            v-model="registerForm.confirmPassword" 
            placeholder="请再次输入密码" 
            required 
          />
          <p v-if="registerForm.password != registerForm.confirmPassword && registerForm.confirmPassword.length != 0">两次输入的密码不一致</p>
        </div>

        <div class="form-checkbox">
          <input 
            id="agree-terms" 
            type="checkbox" 
            v-model="registerForm.agreeTerms" 
            required
          />
          <label for="agree-terms">我已阅读并同意<a href="#">服务条款</a>和<a href="#">隐私政策</a></label>
        </div>

        <div v-if="registerErrors.length" class="error-messages">
          <p v-for="(error, index) in registerErrors" :key="index">{{ error }}</p>
        </div>

        <div v-if="registerSuccess" class="success-message">
          {{ registerSuccess }}
        </div>

        <button 
          type="submit" 
          class="submit-btn" 
          :disabled="registUserNameError || registUserPhoneError || registPassWordCheckError || registPassWordError || isSubmitting || registerSuccess === '注册成功！3秒后将自动跳转到登录页面...'"
        >
          {{ isSubmitting ? '注册中...' : '注册' }}
        </button>
      </form>
    </div>
  </div>
</template>

<script name='LoginRegister' setup>
//import { tr } from 'element-plus/es/locale'
import { ref, reactive,computed } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()

// 当前激活的标签页（登录/注册）
const activeTab = ref('login')

// 是否正在提交表单
const isSubmitting = ref(false)

// 登录表单数据
const loginForm = reactive({
  username: '',
  password: '',
  rememberMe: false
})

// 注册表单数据
const registerForm = reactive({
  username: '',
  userphone: '',
  password: '',
  confirmPassword: '',
  agreeTerms: false
})

// 错误信息
const loginErrors = ref([])
const registerErrors = ref([])

// 注册成功信息
const registerSuccess = ref('')
let usernameError = computed(() => loginForm.username.length === 0)
let passWordError = computed(() => loginForm.password.length === 0)

// 登录处理
const handleLogin = async () => {
  loginErrors.value = []
  
  // 如果有错误，则不提交
  if (usernameError.value || passWordError.value) {
    console.log(usernameError)
    console.log(passWordError)
    console.log(loginForm)
    return
  }
  
  try {
    isSubmitting.value = true
    
    let SendLoginForm = reactive({
      userid: loginForm.username,
      password: loginForm.password
    })
    
    console.log(SendLoginForm)
    const { data, status } = await axios.post(
      'http://localhost:8080/user/login', 
      SendLoginForm,
      {
        validateStatus: () => true
      })
    if(status !== 200){
      console.log(data.message)
      loginErrors.value.push(data.message)
      return
    }
    const loginData = data

    console.log(loginData)
    console.log(loginData.success)
    if(loginData.success === false){
      loginErrors.value.push(loginData.message)
      return
    }
    
    // 假设这里调用后端API进行实际登录
    console.log('登录数据:', {
      username: loginForm.username,
      password: loginForm.password,
      rememberMe: loginForm.rememberMe,
    })
    
    alert('登录成功！即将跳转到论坛首页')
    router.push({
      path:'/MainPage',
      query: {
        loginData: loginForm.username
      }})
  } catch (error) {
    console.log(error)
    loginErrors.value.push(error.message || '登录失败，请稍后再试')
  } finally {
    isSubmitting.value = false
  }
}


let registUserNameError = computed(() => registerForm.username.length === 0)
let registPassWordError = computed(() => registerForm.password.length === 0)
let registPassWordCheckError = computed(() => registerForm.password != registerForm.confirmPassword)
let registUserPhoneError = computed(() => registerForm.userphone.length === 0)
// 注册处理
const handleRegister = async () => {
  registerErrors.value = []
  registerSuccess.value = ''
  
  if (registerForm.password.length < 6) {
    registerErrors.value.push('密码长度不能少于6个字符')
  }
  
  if (registerForm.password !== registerForm.confirmPassword) {
    registerErrors.value.push('两次输入的密码不一致')
  }
  
  if (!registerForm.agreeTerms) {
    registerErrors.value.push('请阅读并同意服务条款和隐私政策')
  }
  
  // 如果有错误，则不提交
  if (registerErrors.value.length > 0) {
    return
  }
  
  try {
    isSubmitting.value = true

    let SendregisterForm = reactive({
      userid: registerForm.userphone,
      username: registerForm.username,
      password: registerForm.password
    })
    
    const {data,status} = await axios.post(
      'http://localhost:8080/user/register',
       SendregisterForm,
       {
        validateStatus: () => true
       }
    )
    if(status !== 200 ){
      registerErrors.value.push(data.message)
      return
    }
    const registerData = data
    if(registerData.success === false){
      registerErrors.value.push(registerData.message)
      return
    }

    
    // 假设这里调用后端API进行实际注册
    console.log('注册数据:', {
      username: registerForm.username,
      email: registerForm.email,
      password: registerForm.password
    })
    
    // 注册成功
    registerSuccess.value = '注册成功！3秒后将自动跳转到登录页面...'
    
    // 3秒后切换到登录页
    setTimeout(() => {
      activeTab.value = 'login'
      registerSuccess.value = ''

      router.push({
        path:'/MainPage',
        query: {
          loginData: registerForm.userphone
      }})

      // 清空注册表单
      Object.keys(registerForm).forEach(key => {
        registerForm[key] = key === 'agreeTerms' ? false : ''
      })
    }, 3000)
  } catch (error) {
    registerErrors.value.push(error.message || '注册失败，请稍后再试')
  } finally {
    isSubmitting.value = false
  }
}

</script>

<style scoped>
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 50%;
  min-width: 480px;
  width: 30%;
  padding-left: 20px;
  padding-right: 20px;
  padding-top: 16px;
  padding-bottom: 16px;
  font-family: 'Arial', sans-serif;
}

.auth-card {
  width: 100%;
  max-width: 450px;
  /*background-color: rgb(255, 255, 255);
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);*/

  background-color: transparent; /* 半透明背景 */
  backdrop-filter: blur(8px); /* 模糊效果 */
  -webkit-backdrop-filter: blur(8px); /* Safari 兼容性 */
  border-radius: 8px;
  border: 1px solid #fdfcfc;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}


.tabs {
  display: flex;
  border-bottom: 1px solid #e0e0e0;
}

/*
.tabs button {
  flex: 1;
  padding: 15px;
  background-color: rgba(255, 255, 255, 0.3); 
  filter: blur(8px);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  border: none;
  cursor: pointer;
  font-size: 16px;
  font-weight: 500;
  transition: background-color 0.2s;
}*/

.tabs button {
  flex: 1;
  padding: 15px;
  background-color: transparent;
  color:#fdfcfc;
  backdrop-filter: blur(8px); /* 背景模糊效果 */
  -webkit-backdrop-filter: blur(8px); /* Safari 兼容 */
  border: none;
  cursor: pointer;
  font-size: 16px;
  font-weight: 500;
  transition: all 0.2s;
  position: relative; /* 需要为backdrop-filter创建层叠上下文 */
  z-index: 1; /* 确保正确的层叠顺序 */
}

/* 可选：悬停效果 */
.tabs button:hover {
  background-color: rgba(255, 255, 255, 0.4);
}


.tabs button.active {
  background-color: white;
  border-bottom: 2px solid #fafbfb;
  color: black;
}

.auth-form {
  padding: 20px;
}

.form-group {
  /*font-family: KaiTi, "楷体", "楷体_GB2312", DFKai-SB, "STKaiti", serif;*/
  font-family: 'SimHei', '黑体', sans-serif;
  margin-bottom: 16px;
}

.form-group label {
  display: block;
  margin-bottom: 6px;
  font-weight: 500;
  color: #fcfbfb;
}

.form-group input {
  width: 94%;
  padding: 10px 12px;
  border: 1px solid #6c6b6b;
  color:white;
  background-color: transparent;
  border-radius: 4px;
  font-size: 14px;
  transition: border-color 0.2s;
}

.form-group input::placeholder{
  color:#8a8585;
}

.form-group input:focus {
  outline: none;
  border-color: white;
}

.form-group p {
  font-style: italic;
  font-size: 14px;
  color: #f00a0a;
}

.form-checkbox {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}

.form-checkbox input {
  margin-right: 8px;
}

.form-checkbox label {
  font-size: 14px;
  color: #ffffff;
}

.form-checkbox a {
  color: #4a6cf7;
  text-decoration: none;
}

.error-messages {
  margin-bottom: 16px;
  padding: 10px;
  background-color: #ffe6e6;
  border-radius: 4px;
  color: #d63031;
}

.success-message {
  margin-bottom: 16px;
  padding: 10px;
  background-color: #e6ffe6;
  border-radius: 4px;
  color: #009432;
}

.submit-btn {
  width: 100%;
  padding: 12px;
  background-color: white;
  color: black;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
}

.submit-btn:hover:not(:disabled) {
  background-color: #3b5fe2;
}

.submit-btn:disabled {
  background-color: #fdfcfc;
  cursor: not-allowed;
}

@media (max-width: 500px) {
  .auth-card {
    box-shadow: none;
  }
}
</style>

