<template>
  <div class="user-profile-edit">
    <form @submit.prevent="handleSubmit" class="edit-form">
      <h2 class="form-title">编辑论坛信息</h2>
      
      <!-- 成功提示 -->
      <div v-if="submitSuccess" class="alert success">
        <div class="alert-icon">✓</div>
        <div class="alert-message">论坛信息更新成功</div>
      </div>
      
      <!-- 错误提示 -->
      <div v-if="submitError" class="alert error">
        <div class="alert-icon">!</div>
        <div class="alert-message">{{ submitError }}</div>
      </div>
      
      <!-- 用户名 -->
      <div class="form-group">
        <label for="username">论坛名</label>
        <input
          type="text"
          id="username"
          v-model="form.username"
          @blur="validateUsername"
          :class="{ 'input-error': errors.username }"
          placeholder="请输入新的论坛名"
        />
        <div v-if="errors.username" class="error-message">{{ errors.username }}</div>
      </div>
      
      <!-- 电话 -->
      <div class="form-group">
        <label for="phone">论坛介绍</label>
        <input
          type="tel"
          id="phone"
          v-model="form.phone"
          @blur="validatePhone"
          :class="{ 'input-error': errors.phone }"
          placeholder="请输入论坛介绍"
        />
        <div v-if="errors.phone" class="error-message">{{ errors.phone }}</div>
      </div>
      
      <!-- 操作按钮 -->
      <div class="form-actions">
        <button
          type="button"
          class="btn-cancel"
          @click="handleCancel"
        >
          取消
        </button>
        <button
          type="submit"
          class="btn-save"
          :disabled="isSubmitting || !hasChanges"
        >
          {{ isSubmitting ? '保存中...' : '保存修改' }}
        </button>
      </div>
    </form>
  </div>
</template>



<script setup>
import { ref, reactive, computed,defineProps,defineEmits } from 'vue'
import axios from 'axios'

const props = defineProps({
  userId:{
    type:String,
    default:"1"
  },
  forumId:{
    type:Number,
    default:1
  },
  initialUsername: {
    type: String,
    default: ''
  },
  initialPhone: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['update', 'cancel'])

// 表单数据
const form = reactive({
  username: props.initialUsername,
  phone: props.initialPhone,
  password: '',
  confirmPassword: ''
})

// 表单验证状态
const errors = reactive({
  username: '',
  phone: '',
  password: '',
  confirmPassword: ''
})

// 是否正在修改密码
const isChangingPassword = ref(false)

// 表单提交状态
const isSubmitting = ref(false)
const submitSuccess = ref(false)
const submitError = ref('')

// 用户名验证规则
const validateUsername = () => {
  if (!form.username) {
    errors.username = '论坛名不能为空'
    return false
  }
  
  errors.username = ''
  return true
}

// 电话验证规则
const validatePhone = () => {
  return true
}

// 表单验证
const validateForm = () => {
  const isUsernameValid = validateUsername()
  const isPhoneValid = validatePhone()
  
  return isUsernameValid && isPhoneValid
}

// 提交表单
const handleSubmit = async () => {
  if (!validateForm()) return
  
  try {
    isSubmitting.value = true
    submitError.value = ''
    
    // 构建要提交的数据对象
    const updateData = {
      userID: props.userId,
      name:form.username,
      introduction:form.phone
    }

    const { data, status } = await axios.put(
    'http://localhost:8080/forum/' + props.forumId,updateData,
    {
      validateStatus: () => true
    })
    console.log(data)
    console.log(status)

    
    // 发送到父组件处理
    emit('update', updateData)
    
    // 成功提示
    submitSuccess.value = true
    
    // 重置密码字段
    form.password = ''
    form.confirmPassword = ''
    isChangingPassword.value = false
    
    // 延迟清除成功提示
    setTimeout(() => {
      submitSuccess.value = false
    }, 3000)
    
  } catch (error) {
    submitError.value = '提交失败，请稍后重试'
    console.error('提交失败:', error)
  } finally {
    isSubmitting.value = false
  }
}

// 取消修改
const handleCancel = () => {
  emit('cancel')
}

// 切换密码修改状态
/*
const togglePasswordChange = () => {
  isChangingPassword.value = !isChangingPassword.value
  if (!isChangingPassword.value) {
    form.password = ''
    form.confirmPassword = ''
    errors.password = ''
    errors.confirmPassword = ''
  }
}*/

// 是否有修改
const hasChanges = computed(() => {
  const usernameChanged = form.username !== props.initialUsername
  const phoneChanged = form.phone !== props.initialPhone
  const passwordChanged = isChangingPassword.value && form.password
  
  return usernameChanged || phoneChanged || passwordChanged
})
</script>

<style scoped>
.user-profile-edit {
  max-width: 500px;
  margin: 0 auto;
  padding: 20px;
  background-color: #efecec;
}

.form-title {
  font-size: 1.5rem;
  color: #333;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.edit-form {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
  padding: 24px;
}

.form-group {
  margin-bottom: 20px;
  width: 94%;
}

label {
  display: block;
  margin-bottom: 6px;
  font-weight: 500;
  color: #555;
}

input {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
  transition: border-color 0.3s, box-shadow 0.3s;
}

input:focus {
  outline: none;
  border-color: #4f46e5;
  box-shadow: 0 0 0 2px rgba(79, 70, 229, 0.1);
}

.input-error {
  border-color: #ef4444;
}

.error-message {
  color: #ef4444;
  font-size: 0.85rem;
  margin-top: 4px;
}

.password-hint {
  color: #6b7280;
  font-size: 0.85rem;
  margin-top: 4px;
}

.password-toggle {
  margin-bottom: 20px;
}

.toggle-label {
  display: flex;
  align-items: center;
  cursor: pointer;
  width: 50px;
}

.toggle-text {
  margin-left: 8px;
  white-space: nowrap;
}

.password-section {
  background-color: #f9fafb;
  padding: 16px;
  border-radius: 6px;
  margin-bottom: 20px;
  border-left: 3px solid #4f46e5;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 24px;
  gap: 12px;
}

.btn-cancel, .btn-save {
  padding: 10px 20px;
  border-radius: 4px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-cancel {
  background-color: #f3f4f6;
  color: #4b5563;
  border: 1px solid #e5e7eb;
}

.btn-cancel:hover {
  background-color: #e5e7eb;
}

.btn-save {
  background-color: #4f46e5;
  color: white;
  border: none;
}

.btn-save:hover:not(:disabled) {
  background-color: #4338ca;
}

.btn-save:disabled {
  background-color: #a5a3e2;
  cursor: not-allowed;
}

.alert {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  border-radius: 6px;
  margin-bottom: 20px;
}

.alert-icon {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  font-weight: bold;
}

.alert.success {
  background-color: #ecfdf5;
  color: #047857;
}

.alert.success .alert-icon {
  background-color: #10b981;
  color: white;
}

.alert.error {
  background-color: #fef2f2;
  color: #b91c1c;
}

.alert.error .alert-icon {
  background-color: #ef4444;
  color: white;
}
</style>