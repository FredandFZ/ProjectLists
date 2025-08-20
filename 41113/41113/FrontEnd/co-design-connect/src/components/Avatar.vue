<template>
  <div class="avatar" :style="avatarStyle">
    {{ initials }}
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  username: {
    type: String,
    required: true
  },
  size: {
    type: Number,
    default: 40
  }
})

// 处理用户名，获取首字母
const initials = computed(() => {
  const nameParts = props.username.split(' ')
  const firstInitial = nameParts[0]?.[0] || ''
  const lastInitial = nameParts[1]?.[0] || ''
  return (firstInitial + lastInitial).toUpperCase()
})

function stringToColor(str) {
  let hash = 0
  for (let i = 0; i < str.length; i++) {
    hash = str.charCodeAt(i) + ((hash << 5) - hash)
  }
  let color = '#'
  for (let i = 0; i < 3; i++) {
    color += ('00' + ((hash >> (i * 8)) & 0xff).toString(16)).slice(-2)
  }
  return color
}

const bgColor = computed(() => stringToColor(props.username))
const avatarStyle = computed(() => ({
  backgroundColor: bgColor.value,
  width: props.size + 'px',
  height: props.size + 'px',
  fontSize: (props.size * 0.5) + 'px'
}))
</script>

<style scoped>
.avatar {
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  color: #fff;
  font-weight: bold;
  user-select: none;
}
</style> 