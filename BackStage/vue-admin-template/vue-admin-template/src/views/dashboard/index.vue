<template>
  <div class="dashboard-container">
    <div class="dashboard-text">您的EmployeeID为{{ name }}</div>
    <div>
      <h2 style="color: #cb3c3c">员工操作时，需要上级管理审批，同时一切记录将被保存</h2>
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
