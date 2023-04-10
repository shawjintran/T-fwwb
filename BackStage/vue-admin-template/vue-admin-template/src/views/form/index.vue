<template>
  <div class="app-container">
    <el-form ref="form" :model="form" label-width="120px">
      <el-form-item label="客户手机号">
        <el-input v-model="form.userPhone" />
      </el-form-item>
      <el-form-item label="客户密码">
        <el-input v-model="form.userPwd" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" style="background: #91a7c4" @click="onSubmit">创建用户</el-button>
        <el-button @click="onSet">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {addCustom} from "@/api/Custom/custom";

export default {
  data() {
    return {
      form: {
        userPhone:'',
        userPwd:''
      }
    }
  },
  methods: {
    onSubmit() {
      if (this.form.userPhone=='' || this.form.userPwd=='')
      {
        this.$message.warning("请填写用户信息");
        return
      }
      addCustom(this.form).then(response=>{
        console.log(response)
        if (response.code==200)
        {
          this.$message.success(response.mes)
          this.form.userPhone=''
          this.form.userPwd=''
        }
        else
          this.$message.error(response.mes)
      }).catch(error =>{
        this.$message.error('Wrong')
      })
    },
    onSet() {
      this.form.userPwd=''
      this.form.userPhone=''
      this.$message({
        message: '数据已清空',
        type: 'info'
      })
    }
  }
}
</script>

<style scoped>

</style>

