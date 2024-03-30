<template>
  <el-row>
    <el-col :offset="1" span="22">
      <h2>日志查看</h2>
      <el-divider/>
      <el-card>
        <el-form :inline="true"  class="demo-form-inline">
          <el-form-item label="开始时间">
            <el-date-picker
              v-model="formInline.date1"
              type="date"
              placeholder="选择日期"
              value-format="yyyy-MM-dd"
              :picker-options="pickerOptions0">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="结束时间">
            <el-date-picker
              v-model="formInline.date2"
              type="date"
              placeholder="选择日期"
              value-format="yyyy-MM-dd"
              :picker-options="pickerOptions0">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="文件夹">
            <el-select v-model="formInline.docID" placeholder="请选择">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>

        <el-button type="primary" @click="onSubmit">查询</el-button>
        </el-form>
      </el-card>
      <el-divider/>
      <el-table style="width: 100%">
        <el-table-column prop="id" label="Doc" width="100"/>
        <el-table-column prop="userid" label="User" width="100"/>
        <el-table-column prop="ctime" label="time"  sortable />
        <el-table-column prop="content" label="content"  />
      </el-table>
    </el-col>
  </el-row>

</template>

<script>
export default {
  name: "log",
  data() {
    return{
      formInline: {
        date1: '',
        date2: '',
        docID: ''
      },
      pickerOptions0: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        }
      },
      options: [{
        value: '选项1',
        label: '黄金糕'
      }, {
        value: '选项2',
        label: '双皮奶'
      }, {
        value: '选项3',
      }],
  }
}
}
</script>

<style scoped>

</style>
