<template>
  <div class="truncated-paragraph">
    <p 
      ref="textRef" 
      class="text-content" 
      :class="{ 'truncated': !showFull && isTruncatable }"
      :style="{ '-webkit-line-clamp': maxLines }"
    >
      <slot>{{ text }}</slot>
    </p>
    
    <button 
      v-if="isTruncatable" 
      @click="toggleShowFull" 
      class="toggle-button"
    >
      {{ showFull ? '收起' : '显示全部' }}
    </button>
  </div>
</template>

<script setup>
import { ref, onMounted, defineProps, nextTick, watch } from 'vue';

const props = defineProps({
  // 要显示的文本
  text: {
    type: String,
    default: '这是一个你认为的不存在的文本'
  },
  // 最大显示行数
  maxLines: {
    type: Number,
    default: 3
  }
});

const textRef = ref(null);
const showFull = ref(false);
const isTruncatable = ref(false);

// 检查文本是否需要截断
const checkTruncation = async () => {
  await nextTick();
  if (!textRef.value) return;

  const nowtext = props.text;
  const stringLenth = nowtext.length;
  console.log(stringLenth);
  
  // 如果实际高度大于最大高度，说明文本需要截断
  isTruncatable.value = stringLenth > 150;
};

// 切换显示完整/截断文本
const toggleShowFull = () => {
  showFull.value = !showFull.value;
};

// 监听内容变化
watch(() => props.text, () => {
  checkTruncation();
}, { immediate: false });

// 组件挂载后检查截断
onMounted(() => {
  checkTruncation();
  
  // 添加窗口大小变化监听，以便重新计算
  window.addEventListener('resize', checkTruncation);
});
</script>

<style scoped>
.truncated-paragraph {
  width: 95%;
  position: relative;
}

.text-content {
  margin: 0;
  margin-top: 8px;
  width: 100%;
  color:rgb(99, 98, 98);
  word-break: break-word;
  overflow-wrap: break-word;
}

.truncated {
  display: -webkit-box;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.toggle-button {
  margin-top: 4px;
  background: none;
  border: none;
  color: #0659ac;
  cursor: pointer;
  padding: 0;
  font-size: 0.875rem;
  text-decoration: none;
}

.toggle-button:hover {
  text-decoration: underline;
  color: #66b1ff;
}
</style>