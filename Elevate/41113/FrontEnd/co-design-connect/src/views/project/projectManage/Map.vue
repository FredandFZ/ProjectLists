<template>
    <div class="wrapper">
      <input ref="searchInput" class="search-box" type="text"
             placeholder="Search place…" />
      <div ref="mapRef" class="map"></div>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted, onBeforeUnmount } from 'vue'
  import { Loader } from '@googlemaps/js-api-loader'
  import { ElMessageBox, ElMessage } from 'element-plus'
  import { Edit, Delete, Location } from '@element-plus/icons-vue'
  import request from '@/utils/request'
  import { useRoute } from 'vue-router'

  /* --------- 常量 ---------- */
  const API_KEY   = 'AIzaSyCZqloO81P9r4FbCNJo4PbyePcYtqOBxI8'
  const LIBS      = ['places']
  const DEFAULT_CENTER = { lat: -33.86, lng: 151.20 } // Sydney
  const DEFAULT_ZOOM   = 10

  /* --------- DOM 引用 ---------- */
  const mapRef      = ref(null)
  const searchInput = ref(null)
  const route       = useRoute()
  const projectId   = route.params.id
  
  /* --------- 运行时状态 ---------- */
  let map, infoWindow
  const markers = []   // [{ id, marker, title, desc }]
  
  /* --------- 生命周期 ---------- */
  onMounted(async () => {
    await new Loader({ apiKey: API_KEY, libraries: LIBS,
                       language: 'en' }).load()
  
    initMap()
    await fetchMarkersFromBackend() // 初始化时获取标记点
  })
  
  onBeforeUnmount(() => {
    if (!map) return
    const c = map.getCenter()
    localStorage.setItem('map-center', JSON.stringify({ lat: c.lat(), lng: c.lng() }))
    localStorage.setItem('map-zoom', map.getZoom())
  })
  
  /* --------- 初始化地图 & 搜索框 ---------- */
  function initMap () {
    const savedCenter =
      JSON.parse(localStorage.getItem('map-center')) || DEFAULT_CENTER
    const savedZoom = Number(localStorage.getItem('map-zoom')) || DEFAULT_ZOOM
  
    map = new google.maps.Map(mapRef.value, { center: savedCenter, zoom: savedZoom })
    infoWindow = new google.maps.InfoWindow()
  
    /* 点击空白处添加新标记 */
    map.addListener('click', e => createMarker(e.latLng))
  
    /* 搜索框自动补全 */
    const sb = new google.maps.places.SearchBox(searchInput.value)
    sb.addListener('places_changed', () => {
      const p = sb.getPlaces()
      if (!p.length) return
      map.panTo(p[0].geometry.location)
      map.setZoom(13)
    })
  }
  
  /* --------- 从后端获取标记点 ---------- */
  async function fetchMarkersFromBackend() {
    try {
      const response = await request.get('/markers', {
        params: { projectId: Number(projectId) }
      })
      const markersData = response.data || []
      
      // 清除现有标记
      markers.forEach(m => m.marker.setMap(null))
      markers.length = 0
      
      // 创建新标记
      markersData.forEach(data => {
        const marker = new google.maps.Marker({
          position: { lat: data.lat, lng: data.lng },
          map,
          draggable: false,
          icon: {
            url: 'https://maps.google.com/mapfiles/ms/icons/red-dot.png',
            scaledSize: new google.maps.Size(32, 32)
          }
        })
  
        const markerData = {
          id: data.id,
          marker,
          title: data.title,
          desc: data.description
        }
        markers.push(markerData)
  
        marker.addListener('dragend', () => {
          ElMessage({
            type: 'success',
            message: 'Location updated',
            duration: 2000
          })
          editMarker(markerData)
        })
  
        marker.addListener('click', () => openInfoWindow(markerData))
        
      })
    } catch (error) {
      ElMessage({
        type: 'error',
        message: 'Get marker failed',
        duration: 2000
      })
      console.error('Get marker failed:', error)
    }
  }
  

  
  /* --------- 创建标记 ---------- */
  function createMarker (latLng) {
    ElMessageBox.prompt('Enter marker title', 'New Marker', {
      confirmButtonText: 'Confirm',
      cancelButtonText: 'Cancel',
      inputValidator: (value) => {
        if (!value) {
          return 'Title cannot be empty'
        }
        return true
      }
    }).then(({ value: title }) => {
      ElMessageBox.prompt('Enter marker description', 'Description', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        inputType: 'textarea'
      }).then(async ({ value: desc }) => {
        try {
          const response = await request.post('/markers/create', {
            title,
            description: desc,
            lat: latLng.lat(),
            lng: latLng.lng(),
            projectId: Number(projectId)
          })

          const marker = new google.maps.Marker({
            position: latLng,
            map,
            draggable: false,
            icon: {
              url: 'https://maps.google.com/mapfiles/ms/icons/red-dot.png',
              scaledSize: new google.maps.Size(32, 32)
            }
          })

          const data = { 
            id: response.data.id,
            marker, 
            title, 
            desc 
          }
          markers.push(data)

          marker.addListener('dragend', () => {
            ElMessage({
              type: 'success',
              message: 'Location updated',
              duration: 2000
            })
            editMarker(data)
          })

          marker.addListener('click', () => openInfoWindow(data))
          
          ElMessage({
            type: 'success',
            message: 'Marker created successfully',
            duration: 2000
          })
        } catch (error) {
          ElMessage({
            type: 'error',
            message: 'Create marker failed',
            duration: 2000
          })
          console.error('Create marker failed:', error)
        }
      }).catch(() => {})
    }).catch(() => {})
  }
  
  /* --------- 打开可编辑 InfoWindow ---------- */
  function openInfoWindow (data) {
    const { id, marker, title, desc } = data
    const lat = marker.getPosition().lat().toFixed(6)
    const lng = marker.getPosition().lng().toFixed(6)
  
    const el = document.createElement('div')
    el.className = 'marker-info-window'
    el.innerHTML = `
      <div class="marker-info-content">
        <h3 class="marker-title">${title}</h3>
        <p class="marker-desc">${desc || '(No description)'}</p>
        <div class="marker-location">
          <el-icon><Location /></el-icon>
          <span>${lat}, ${lng}</span>
        </div>
        <div class="marker-actions">
          <el-button type="primary" size="small" id="edit-${id}">
            <el-icon><Edit /></el-icon>
            Edit
          </el-button>
          <el-button type="danger" size="small" id="del-${id}">
            <el-icon><Delete /></el-icon>
            Delete
          </el-button>
        </div>
      </div>
    `
  
    infoWindow.setContent(el)
    infoWindow.open(map, marker)
  
    google.maps.event.addListenerOnce(infoWindow, 'domready', () => {
      document.getElementById(`edit-${id}`).onclick = () => editMarker(data)
      document.getElementById(`del-${id}`).onclick = () => deleteMarker(data)
    })
  }
  
  /* --------- 编辑标记 ---------- */
  function editMarker (data) {
    ElMessageBox.prompt('Enter marker title', 'Edit Marker', {
      confirmButtonText: 'Confirm',
      cancelButtonText: 'Cancel',
      inputValue: data.title,
      inputValidator: (value) => {
        if (!value) {
          return 'Title cannot be empty'
        }
        return true
      }
    }).then(({ value: newTitle }) => {
      ElMessageBox.prompt('Enter marker description', 'Edit Description', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        inputValue: data.desc,
        inputType: 'textarea'
      }).then(async ({ value: newDesc }) => {
        try {
          await request.put(`/markers/${data.id}`, {
            title: newTitle,
            description: newDesc,
            lat: data.marker.getPosition().lat(),
            lng: data.marker.getPosition().lng(),
            projectId: Number(projectId)
          })
          data.title = newTitle
          data.desc = newDesc
          openInfoWindow(data)
          ElMessage({
            type: 'success',
            message: 'Update successfully',
            duration: 2000
          })
        } catch (error) {
          ElMessage({
            type: 'error',
            message: 'Update failed',
            duration: 2000
          })
          console.error('Update marker failed:', error)
        }
      }).catch(() => {})
    }).catch(() => {})
  }
  
  /* --------- 删除标记 ---------- */
  function deleteMarker (data) {
    ElMessageBox.confirm(
      'Are you sure you want to delete this marker?',
      'Delete Confirmation',
      {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }
    ).then(async () => {
      try {
        await request.delete(`/markers/${projectId}/${data.id}`)
        data.marker.setMap(null)
        const idx = markers.findIndex(m => m.id === data.id)
        markers.splice(idx, 1)
        infoWindow.close()
        ElMessage({
          type: 'success',
          message: 'Delete successfully',
          duration: 2000
        })
      } catch (error) {
        ElMessage({
          type: 'error',
          message: 'Delete failed',
          duration: 2000
        })
        console.error('Delete marker failed:', error)
      }
    }).catch(() => {})
  }
  </script>
  
  <style scoped>
  .wrapper  { position:relative; height:100vh; width:100%; }
  .map      { height:100%; width:100%; }
  .search-box{
    position:absolute; z-index:10;
    top:12px; left:50%; transform:translateX(-50%);
    width:320px; height:40px; padding:0 14px;
    border:1px solid #bbb; border-radius:6px; background:#fff;
    box-shadow:0 2px 6px rgba(0,0,0,.3);
  }
  
  :deep(.marker-info-window) {
    padding: 12px;
    min-width: 200px;
  }
  
  :deep(.marker-info-content) {
    display: flex;
    flex-direction: column;
    gap: 8px;
  }
  
  :deep(.marker-title) {
    margin: 0;
    font-size: 16px;
    font-weight: 600;
    color: #303133;
  }
  
  :deep(.marker-desc) {
    margin: 0;
    font-size: 14px;
    color: #606266;
  }
  
  :deep(.marker-location) {
    display: flex;
    align-items: center;
    gap: 4px;
    font-size: 12px;
    color: #909399;
  }
  
  :deep(.marker-actions) {
    display: flex;
    gap: 8px;
    margin-top: 8px;
  }
  </style>
  