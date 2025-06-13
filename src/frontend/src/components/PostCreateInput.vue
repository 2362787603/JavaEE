<template>
    <div class="postTitle">
        <el-button class="search-button">主题</el-button>
        <el-input 
            type="text" 
            class="search-input" 
            v-model="titleText"
            placeholder="输入帖子标题..." 
        />
    </div>
  <div class="emoji-input-container">
    <!-- Text input area -->
    <div class="input-wrapper">
      <textarea
        ref="textareaRef"
        v-model="inputText"
        class="text-input"
        :placeholder="placeholder"
        @input="emitUpdate"
        @keydown.enter.prevent="handleEnter"
      ></textarea>
      
      <!-- Image preview area -->
      <div v-if="uploadedImages.length > 0" class="image-preview-container">
        <div 
          v-for="(image, idx) in uploadedImages" 
          :key="idx" 
          class="image-preview"
        >
          <img :src="image.url" alt="Uploaded image preview" />
          <button class="remove-image-btn" @click="removeImage(idx)">×</button>
        </div>
      </div>
      
      <div class="input-actions">
        <!-- Image upload button -->
        <button type="button" class="image-upload-toggle" @click="triggerFileUpload">
          <span class="image-icon">🖼️</span>
        </button>
        <!-- Hidden file input -->
        <input 
          type="file" 
          ref="fileInputRef" 
          class="file-input" 
          accept="image/*" 
          multiple
          @change="handleFileUpload" 
        />
        
        <button type="button" class="emoji-toggle" @click="toggleEmojiPicker">
          <span class="emoji-icon">😊</span>
        </button>
      </div>
    </div>
    
    <!-- Emoji picker -->
    <div v-if="showEmojiPicker" class="emoji-picker">
      <div class="emoji-grid">
        <button
          v-for="(emoji, index) in emojis"
          :key="index"
          class="emoji-button"
          @click="insertEmoji(emoji)"
        >
          {{ emoji }}
        </button>
      </div>
    </div>
  </div>
  <button type="button" class="send-post" @click="sendMessage">
        确认创建
  </button>
</template>

<script setup>
import { ref, defineEmits, defineProps,reactive } from 'vue';
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

const props = defineProps({
  userId: {
    type:String,
    default:"1"
  },
  forumId: {
    type: [Number,String],
    default:1
  },
  placeholder: {
    type: String,
    default: '说说您的看法...'
  },
  maxLength: {
    type: Number,
    default: 1000
  },
  maxImageSize: {
    type: Number,
    default: 5 // Maximum file size in MB
  },
  maxImages: {
    type: Number,
    default: 9 // Maximum number of images
  }
});

const emit = defineEmits(['update:modelValue', 'send']);
const textareaRef = ref(null);
const fileInputRef = ref(null);
const inputText = ref('');
const titleText = ref('')
const showEmojiPicker = ref(false);
const uploadedImages = ref([]);

// Common emojis - can be expanded or replaced with a library
const emojis = ref([
  // 基础表情
  '😀', '😃', '😄', '😁', '😆', '😅', '😂', '🤣', '😊', '😇',
  '🙂', '🙃', '😉', '😌', '😍', '🥰', '😘', '😗', '😙', '😚',
  '😋', '😛', '😝', '😜', '🤪', '🤨', '🧐', '🤓', '😎', '🤩',
  '🥳', '😏', '😒', '😞', '😔', '😟', '😕', '🙁', '☹️', '😣',
  
  // 更多表情
  '😭', '😤', '😠', '😡', '🤬', '🤯', '😳', '🥵', '🥶', '😱',
  '😨', '😰', '😥', '😓', '🤗', '🤔', '🤭', '🤫', '🤥', '😶',
  '😐', '😑', '😬', '🙄', '😯', '😦', '😧', '😮', '😲', '🥱',
  '😴', '🤤', '😪', '😵', '🤐', '🥴', '🤢', '🤮', '🤧', '😷',
  '🤒', '🤕', '🤑', '🤠', '😈', '👿', '👹', '👺', '🤡', '💩',
  '👻', '💀', '☠️', '👽', '👾', '🤖', '🎃', '😺', '😸', '😹',
  
  // 爱心系列
  '❤️', '🧡', '💛', '💚', '💙', '💜', '🖤', '💔', '❣️', '💕',
  '💞', '💓', '💗', '💖', '💘', '💝', '💟', '♥️', '💋', '💯',
  
  // 手势表情
  '👍', '👎', '👏', '🙌', '🤝', '💪', '🤲', '👐', '🙏', '✌️',
  '🤞', '🤟', '🤘', '🤙', '👈', '👉', '👆', '🖕', '👇', '☝️',
  '👋', '🤚', '🖐️', '✋', '🖖', '👌', '🤏', '✊', '👊', '🤛',
  '🤜', '👎', '👍', '🫵', '🫶', '🤌', '🫰', '🫳', '🫴', '👄',
  
  // 动物表情
  '🐶', '🐱', '🐭', '🐹', '🐰', '🦊', '🐻', '🐼', '🐨', '🐯',
  '🦁', '🐮', '🐷', '🐽', '🐸', '🐵', '🙈', '🙉', '🙊', '🐒',
  '🐔', '🐧', '🐦', '🐤', '🐣', '🐥', '🦆', '🦅', '🦉', '🦇',
  '🐺', '🐗', '🐴', '🦄', '🐝', '🐛', '🦋', '🐌', '🐞', '🐜',
  
  // 食物表情
  '🍎', '🍊', '🍋', '🍌', '🍉', '🍇', '🍓', '🍈', '🍒', '🍑',
  '🥭', '🍍', '🥥', '🥝', '🍅', '🍆', '🥑', '🥦', '🥒', '🌶️',
  '🌽', '🥕', '🧄', '🧅', '🥔', '🍠', '🥐', '🥖', '🍞', '🥨',
  '🧀', '🥚', '🍳', '🧈', '🥞', '🧇', '🥓', '🥩', '🍗', '🍖',
  '🌭', '🍔', '🍟', '🍕', '🥪', '🥙', '🌮', '🌯', '🥗', '🥘',
  
  // 活动表情
  '⚽', '🏀', '🏈', '⚾', '🥎', '🎾', '🏐', '🏉', '🥏', '🥊',
  '🥋', '🏓', '🏸', '🏒', '🏑', '🥍', '🏏', '🪃', '🥅', '⛳',
  '🪁', '🏹', '🎣', '🤿', '🥽', '🎿', '🛷', '🥌', '🎯', '🪀',
  
  // 自然表情
  '🌞', '🌝', '🌛', '🌜', '🌚', '🌕', '🌖', '🌗', '🌘', '🌑',
  '🌒', '🌓', '🌔', '🌙', '🌎', '🌍', '🌏', '🪐', '💫', '⭐',
  '🌟', '✨', '⚡', '☄️', '💥', '🔥', '🌪️', '🌈', '☀️', '🌤️',
  '⛅', '🌦️', '🌧️', '⛈️', '🌩️', '🌨️', '❄️', '☃️', '⛄', '🌬️',
  
  // 物品表情
  '📱', '📞', '☎️', '📟', '📠', '🔋', '🔌', '💻', '🖥️', '🖨️',
  '⌨️', '🖱️', '🖲️', '💽', '💾', '💿', '📀', '🧮', '🎥', '📹',
  '📷', '📸', '📼', '🔍', '🔎', '🕯️', '💡', '🔦', '🏮', '🪔',
  '📔', '📕', '📖', '📗', '📘', '📙', '📚', '📰', '🗞️', '📃',
  '📜', '📄', '📑', '🔖', '💰', '💴', '💵', '💶', '💷', '💸',
  
  // 符号表情
  '❗', '❓', '❔', '❕', '⭕', '❌', '💯', '💢', '💥', '💫',
  '💦', '💨', '🕳️', '💣', '💬', '🗨️', '🗯️', '💭', '💤', '👁️',
  '🔥', '⚡', '✨', '🌟', '💖', '💝', '🎉', '🎊', '🎈', '🎁'
]);

// Toggle emoji picker visibility
const toggleEmojiPicker = () => {
  showEmojiPicker.value = !showEmojiPicker.value;
};

// Trigger file upload dialog
const triggerFileUpload = () => {
  fileInputRef.value.click();
};

// Handle file upload
const handleFileUpload = (event) => {
  const files = event.target.files;
  
  if (!files || files.length === 0) return;
  
  // Check if adding these files would exceed the maximum
  if (uploadedImages.value.length + files.length > props.maxImages) {
    alert(`最多只能上传${props.maxImages}张图片`);
    return;
  }
  
  // Process each file
  Array.from(files).forEach(file => {
    // Check if file is an image
    if (!file.type.startsWith('image/')) {
      alert('请上传图片文件');
      return;
    }
    
    // Check file size
    const fileSizeMB = file.size / (1024 * 1024);
    if (fileSizeMB > props.maxImageSize) {
      alert(`图片大小不能超过${props.maxImageSize}MB`);
      return;
    }
    
    // Add to uploaded images
    uploadedImages.value.push(
      file,
    );
  });
  
  // Reset file input to allow selecting the same file again
  event.target.value = '';
};

// Remove image from preview
const removeImage = (index) => {
  // Revoke the object URL to free memory
  URL.revokeObjectURL(uploadedImages.value[index].url);
  // Remove from array
  uploadedImages.value.splice(index, 1);
};

// Insert emoji at cursor position
const insertEmoji = (emoji) => {
  const textarea = textareaRef.value;
  const startPos = textarea.selectionStart;
  const endPos = textarea.selectionEnd;
  
  inputText.value = 
    inputText.value.substring(0, startPos) + 
    emoji + 
    inputText.value.substring(endPos);
  
  // Emit the updated value
  emitUpdate();
  
  // Focus back on textarea and set cursor position after the inserted emoji
  nextTick(() => {
    textarea.focus();
    textarea.selectionStart = startPos + emoji.length;
    textarea.selectionEnd = startPos + emoji.length;
  });
};

// Handle Enter key press
const handleEnter = (e) => {
  if (!e.shiftKey) {
    sendMessage();
  } else {
    // Allow Shift+Enter for line break
    const startPos = textareaRef.value.selectionStart;
    const endPos = textareaRef.value.selectionEnd;
    
    inputText.value = 
      inputText.value.substring(0, startPos) + 
      '\n' + 
      inputText.value.substring(endPos);
    
    // Set cursor position after the inserted line break
    nextTick(() => {
      textareaRef.value.selectionStart = startPos + 1;
      textareaRef.value.selectionEnd = startPos + 1;
    });
  }
};

// Emit update event when text changes
const emitUpdate = () => {
  emit('update:modelValue', inputText.value);
};

// Send message
const sendMessage = async () => {
  // Only send if there's text or images

  if (inputText.value.trim() || uploadedImages.value.length > 0) {
    // Create message data object with text and images
    const messageData = reactive({
      userID: props.userId,
      forumID: Number(props.forumId),
      title: titleText.value,
      content: inputText.value/*,
      images: uploadedImages.value.map(img => img.file)*/
    });

    const { data, status } = await axios.post(
    'http://localhost:8080/post/create',messageData, 
    {
      validateStatus: () => true
    })

    if(status == 200){
        console.log(data)

        console.log("7777777777777777777777777")
        console.log(uploadedImages)
        if(uploadedImages.value.length > 0){
            for(const image of uploadedImages.value){
                const messageData = {
                    file:image
                }
                const { data:myimagedata, status:mystatus } = await axios.post(
                'http://localhost:8080/image/upload',messageData, 
                {
                  headers: {
                    'Content-Type': 'multipart/form-data'
                  },
                validateStatus: () => true
                })
                if(mystatus == 200){
                    const mappingData = {
                        postId: data.postId,
                        imageId: myimagedata.imageId
                    }

                    const { data:mydata, status:mystatus } = await axios.post(
                    'http://localhost:8080/postImageMapping/add',mappingData, 
                    {
                    validateStatus: () => true
                    })
                    console.log(mystatus)
                    console.log(mydata)
                }
            }
        }

        router.push({
            path:'/post',
            query: {
                userId: props.userId,
                forumId:props.forumId
        }})
    }
    
    // Clear the input and images
    inputText.value = '';
    uploadedImages.value.forEach(img => URL.revokeObjectURL(img.url));
    uploadedImages.value = [];
  }
};

// Import nextTick for DOM updates
import { nextTick } from 'vue';
</script>

<style scoped>

.postTitle {
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 10px; /* 元素间距 */
  margin-left: 10px;
  max-width: 980px;
}

.search-input {
  flex-grow: 1; /* 输入框自动扩展 */
  height: 40px;
  border-radius: 4px;
  margin-top: 0px; /* 微调输入框位置 */
}

.search-button {
    width: 80px;
    height: 40px;
}
.emoji-input-container {
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  overflow: hidden;
  width: 980px;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  margin-left: 10px;
  margin-top:20px;
}

.input-wrapper {
  display: flex;
  flex-direction: column;
  position: relative;
  width: 100%;
}

.text-input {
  width: 100%;
  min-height: 80px;
  padding: 12px;
  border: none;
  outline: none;
  resize: vertical;
  font-size: 14px;
  line-height: 1.5;
  color: #333;
  min-height: 280px;
}

/* Image preview styles */
.image-preview-container {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  padding: 8px 12px;
  background-color: #f9f9f9;
  border-top: 1px solid #ebeef5;
}

.image-preview {
  position: relative;
  width: 80px;
  height: 80px;
  border-radius: 4px;
  overflow: hidden;
  border: 1px solid #ebeef5;
}

.image-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.remove-image-btn {
  position: absolute;
  top: 2px;
  right: 2px;
  width: 20px;
  height: 20px;
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  border: none;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 14px;
  padding: 0;
  line-height: 1;
}

.input-actions {
  display: flex;
  align-items: center;
  padding: 0 12px 12px;
  position: absolute;
  bottom: 0;
  right: 0;
}

.file-input {
  display: none; /* Hide the native file input */
}

.image-upload-toggle {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 20px;
  padding: 6px;
  margin-right: 10px;
  transition: transform 0.2s;
}

.image-upload-toggle:hover {
  transform: scale(1.1);
}

.emoji-toggle {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 20px;
  padding: 6px;
  margin-right: 10px;
  transition: transform 0.2s;
}

.emoji-toggle:hover {
  transform: scale(1.1);
}

.emoji-icon {
  font-size: 20px;
}

.send-button {
  background-color: #409eff;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 6px 16px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s;
}

.send-post {
  background-color: #409eff;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 6px 16px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s;
  width: 100px;
  height: 40px;
  margin-top:15px;
  margin-left: 450px;
}

.send-button:hover {
  background-color: #66b1ff;
}

.emoji-picker {
  background-color: #fff;
  border-top: 1px solid #ebeef5;
  padding: 10px;
  max-height: 200px;
  overflow-y: auto;
}

.emoji-grid {
  display: grid;
  grid-template-columns: repeat(10, 1fr);
  gap: 8px;
}

.emoji-button {
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
  padding: 5px;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.emoji-button:hover {
  background-color: #f5f7fa;
}

@media (max-width: 600px) {
  .emoji-grid {
    grid-template-columns: repeat(8, 1fr);
  }
  
  .image-preview {
    width: 60px;
    height: 60px;
  }
}
</style>