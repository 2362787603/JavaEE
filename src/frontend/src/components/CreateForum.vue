<template>
  <div class="create-forum-container">
    <el-form 
      :model="forumForm" 
      :rules="rules" 
      ref="forumFormRef" 
      label-position="top" 
      class="forum-form"
    >
      <!-- 论坛名称 -->
      <el-form-item label="论坛名称" prop="name">
        <el-input 
          v-model="forumForm.name" 
          placeholder="请输入论坛名称（2-20个字符）"
          maxlength="20"
          show-word-limit
        />
        <div class="form-tip">简洁明了的名称更容易被用户记住</div>
      </el-form-item>

      <!-- 论坛介绍 -->
      <el-form-item label="论坛介绍" prop="description">
        <el-input 
          v-model="forumForm.description" 
          type="textarea" 
          placeholder="请简要描述论坛的主题和内容（10-200个字符）"
          maxlength="200"
          show-word-limit
          :rows="4"
        />
        <div class="form-tip">清晰的介绍有助于用户了解论坛的定位和内容</div>
      </el-form-item>

      <!-- 论坛图片 -->
      <el-form-item label="论坛图片" prop="image">
        <div class="image-upload-container">
          <div 
            class="image-upload-area" 
            :class="{ 'has-image': forumForm.imageUrl }"
            @click="triggerFileSelect"
          >
            <template v-if="forumForm.imageUrl">
              <img :src="forumForm.imageUrl" alt="论坛图片" class="preview-image" />
              <div class="image-overlay">
                <i class="el-icon-refresh">更换图片</i>
              </div>
            </template>
            <template v-else>
              <i class="el-icon-plus upload-icon"></i>
              <div class="upload-text">
                <span>点击上传图片</span>
                <p class="upload-hint">支持 JPG、PNG 格式，建议尺寸 300×300px</p>
              </div>
            </template>
          </div>
          <input 
            type="file" 
            ref="fileInput" 
            style="display: none" 
            accept="image/jpeg,image/png"
            @change="handleFileChange"
          />
        </div>
        <div class="form-tip">上传一个代表论坛主题的图片，将显示在论坛列表和页面顶部</div>
      </el-form-item>

      <!-- 其他选项（可选） -->
      <el-form-item label="论坛类别">
        <el-select v-model="forumForm.category" placeholder="请选择论坛类别">
          <el-option label="科技" value="technology" />
          <el-option label="游戏" value="games" />
          <el-option label="教育" value="education" />
          <el-option label="兴趣爱好" value="hobbies" />
          <el-option label="生活" value="lifestyle" />
          <el-option label="其他" value="others" />
        </el-select>
      </el-form-item>

      <el-form-item>
        <div class="form-buttons">
          <el-button type="default" @click="resetForm">取消</el-button>
          <el-button type="primary" @click="submitForm" :loading="submitting">创建论坛</el-button>
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive,computed} from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { useRoute } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const route = useRoute()

const forumFormRef = ref(null)
const fileInput = ref(null)
const submitting = ref(false)

const userId = computed(() => {
  const raw = route.query.userId
  if (!raw) return ''               // 没有就返回空字符串
  return raw
})
console.log('用户ID:', userId.value)


// 表单数据
const forumForm = reactive({
  name: '',
  description: '',
  category: '',
  image: null,
  imageUrl: ''
})

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入论坛名称', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入论坛介绍', trigger: 'blur' },
    { min: 10, max: 200, message: '长度在 10 到 200 个字符', trigger: 'blur' }
  ],
  image: [
    { required: false, message: '请上传论坛图片', trigger: 'change' }
  ]
}

// 触发文件选择
const triggerFileSelect = () => {
  fileInput.value.click()
}

// 处理文件变化
const handleFileChange = (e) => {
  const file = e.target.files[0];
  if (!file) return;

  // 验证文件类型
  const isValidType = ['image/jpeg', 'image/png'].includes(file.type);
  if (!isValidType) {
    ElMessage.error('只支持 JPG、PNG 格式的图片');
    return;
  }

  // 验证文件大小（2MB以内）
  const isValidSize = file.size / 1024 / 1024 < 2;
  if (!isValidSize) {
    ElMessage.error('图片大小不能超过 2MB');
    return;
  }

  forumForm.image = file;

  // 创建预览URL
  const reader = new FileReader();
  reader.onload = (e) => {
    forumForm.imageUrl = e.target.result;
  };
  reader.readAsDataURL(file);
};

// 提交表单
const submitForm = async () => {
  try {
    // 表单验证
    const valid = await forumFormRef.value.validate()
    if (!valid) {
      ElMessage.warning('请完成必填项')
      return
    }
    
    // 开始提交
    submitting.value = true;

    let imageId = null;
    if (forumForm.image) {
      // 上传图片
      const formData = new FormData();
      formData.append('file', forumForm.image);
      const { data: imageResponse, status: imageStatus } = await axios.post(
        'http://localhost:8080/image/upload', 
        formData, 
        {
          headers: {
            'Content-Type': 'multipart/form-data'
          },
          validateStatus: () => true
        }
      );

      if (imageStatus === 200) {
        imageId = imageResponse.imageId;
      } else {
        const errorMessage = imageResponse?.message || `图片上传失败 (错误码: ${imageStatus})`;
        ElMessage.error(errorMessage);
        return;
      }
    }

    // 构建提交数据，包含图片 ID
    const requestData = {
      userID: userId.value,
      name: forumForm.name,
      introduction: forumForm.description,
      imageId: imageId
    };

    // 发送创建论坛的 API 请求
    const { data, status } = await axios.post(
      'http://localhost:8080/forum/create', 
      requestData, 
      {
        validateStatus: () => true
      }
    );

    console.log('响应数据:', data);
    console.log('响应状态:', status);

    // 处理响应
    if (status === 200) {
      ElMessage.success('论坛创建成功！');

      // 跳转到主页面
      router.push({
        path: '/MainPage',
        query: {
          loginData: userId.value
        }
      });
    } else {
      // 根据不同状态码显示不同错误信息
      const errorMessage = data?.message || getErrorMessage(status);
      ElMessage.error(errorMessage);
    }
  } catch (error) {
    console.error('提交论坛时出错:', error);

    // 处理网络错误或其他异常
    if (error.response) {
      // 服务器响应了错误状态码
      const status = error.response.status;
      const message = error.response.data?.message || getErrorMessage(status);
      ElMessage.error(message);
    } else if (error.request) {
      // 请求发出但没有收到响应
      ElMessage.error('网络连接失败，请检查网络连接');
    } else {
      // 其他错误
      ElMessage.error('提交失败，请重试');
    }
  } finally {
    // 无论成功失败都要重置提交状态
    submitting.value = false;
  }
};

// 辅助函数：根据状态码获取错误信息
const getErrorMessage = (status) => {
  switch (status) {
    case 400:
      return '请求参数错误';
    case 401:
      return '用户未登录或登录已过期';
    case 403:
      return '没有权限执行此操作';
    case 409:
      return '论坛名称已存在，请换一个名称';
    case 500:
      return '服务器内部错误，请稍后重试';
    default:
      return `操作失败 (错误码: ${status})`;
  }
};

// 重置表单
const resetForm = () => {
  forumFormRef.value.resetFields();
  forumForm.imageUrl = '';
  forumForm.image = null;
};
</script>

<style scoped>
.create-forum-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 30px 20px;
  display: flex;
  flex-direction: column;
  gap: 30px;
}

@media (min-width: 1024px) {
  .create-forum-container {
    flex-direction: row;
    align-items: flex-start;
  }
  
  .forum-form {
    flex: 2;
  }
  
  .forum-preview {
    flex: 1;
    position: sticky;
    top: 20px;
  }
}

.form-header {
  margin-bottom: 30px;
}

.form-header h1 {
  font-size: 28px;
  margin-bottom: 10px;
  color: #303133;
}

.subtitle {
  color: #606266;
  font-size: 16px;
}

.forum-form {
  background-color: #fff;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.form-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}

.image-upload-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.image-upload-area {
  width: 300px;
  height: 200px;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  transition: all 0.3s;
  background-color: #fafafa;
}

.image-upload-area:hover {
  border-color: #409EFF;
}

.image-upload-area.has-image {
  border: none;
  background-color: transparent;
}

.upload-icon {
  font-size: 28px;
  color: #8c939d;
  margin-bottom: 10px;
}

.upload-text {
  color: #606266;
  font-size: 14px;
  text-align: center;
}

.upload-hint {
  margin: 10px 0 0;
  font-size: 12px;
  color: #909399;
}

.preview-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  opacity: 0;
  transition: opacity 0.3s;
  color: white;
}

.image-upload-area:hover .image-overlay {
  opacity: 1;
}

.form-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}

/* 预览区域样式 */
.forum-preview {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.forum-preview h3 {
  font-size: 18px;
  margin-bottom: 20px;
  color: #303133;
  font-weight: 500;
}

.preview-card {
  border: 1px solid #ebeef5;
  border-radius: 4px;
  overflow: hidden;
  transition: all 0.3s;
}

.preview-card:hover {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.preview-header {
  display: flex;
  padding: 16px;
  background-color: #f5f7fa;
}

.preview-image-container {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  overflow: hidden;
  margin-right: 16px;
}

.preview-forum-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.preview-info {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.preview-info h4 {
  margin: 0 0 8px;
  font-size: 18px;
  color: #303133;
}

.preview-category {
  display: inline-block;
  padding: 2px 8px;
  background-color: #ecf5ff;
  color: #409EFF;
  border-radius: 4px;
  font-size: 12px;
}

.preview-description {
  padding: 16px;
  color: #606266;
  font-size: 14px;
  line-height: 1.6;
  max-height: 100px;
  overflow-y: auto;
  border-bottom: 1px solid #ebeef5;
}

.preview-stats {
  display: flex;
  padding: 12px 16px;
  color: #909399;
  font-size: 13px;
}

.stat-item {
  display: flex;
  align-items: center;
  margin-right: 16px;
}

.stat-item i {
  margin-right: 4px;
  font-size: 16px;
}
</style>