<template>
  <div class="dashboard-container">
    <div class="dashboard-text">您的员工ID为{{ name }}</div>
    <div>
      <h2 style="color: rgba(203,60,60,0.67)">员工操作时，需要上级管理审批，同时您的一切操作将被记录</h2>
    </div>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'

export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters([
      'name'
    ])
  }
}
</script>

<style lang="scss" scoped>
.dashboard {

  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
