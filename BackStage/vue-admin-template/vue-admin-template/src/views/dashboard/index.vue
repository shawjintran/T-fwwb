<template>
  <div class="dashboard-container">
    <div class="dashboard-text">您的员工ID为{{ name }}</div>
    <div>
<!--      <h2 style="color: rgba(203,60,60,0.67)">员工操作时，需要上级管理审批，同时您的一切操作将被记录</h2>-->
      <h2>员工操作手册</h2>
      <el-collapse >
        <el-collapse-item title="业务操作步骤">

        </el-collapse-item>
        <el-collapse-item title="文献审核步骤">

        </el-collapse-item>
        <el-collapse-item title="日志查看步骤">

        </el-collapse-item>

      </el-collapse>
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
