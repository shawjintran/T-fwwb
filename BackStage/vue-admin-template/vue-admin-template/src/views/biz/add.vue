<template>
  <div class="app-container">
    <!-- 输入表单 -->
    <el-form label-width="120px">
      <el-form-item label="业务价格">
        <el-input v-model="business.bizPrice" />
      </el-form-item>
      <el-form-item label="业务积分">
        <el-input-number v-model="business.bizPoint" :min="1"/>
      </el-form-item>
      <el-form-item label="业务状态">
        <el-select v-model="business.bizStatus">
          <!--
            数据类型一定要和取出的json中的一致，否则没法回填
            因此，这里value使用动态绑定的值，保证其数据类型是number
            -->
          <el-option :value="0" label="已下架"></el-option>
          <el-option :value="1" label="售卖中"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="saveOrUpdate()">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import bizApi from "@/api/Business/biz";

export default{
  data(){
    return{
      business:{
        // 初始值
        bizPoint:0,
        bizPrice:0.0,
        bizStatus:1
      },
    }
  },
  created(){
    // 修改时获取路径中的id值,根据id查询得到数据
    if(this.$route.params.id){
      const id =this.$route.params.id
      console.log(id)
      this.fetchDataById(id)
    }
  },
  methods:{
    fetchDataById(id){
    bizApi.fetchById(id).then(response=>{
      if (response.code==200)
        this.business=response.data
      else
        this.$message.error(response.mes);
      })
    },
    save(business){
      bizApi.addBiz(business)
      .then(response =>{
        // 提示
        if (response.code==200)
        {this.$message({
          type: 'success',
          message:'添加成功'
        });
        this.$router.push({path:'/ex/biz/list'})
        }
        else
          this.$message.error(response.mes)
      })
    },
    update(business){
      bizApi.updateBiz(business)
      .then(response =>{
        // 提示
        if (response.code==200)
        {this.$message({
          type: 'success',
          message:'修改成功'
        });
          this.$router.push({path:'/ex/biz/list'})
        }
        else
          this.$message.error(response.mes)
      })
    },
    saveOrUpdate(){
      if(!this.business.bizId)
        this.save(this.business)
      else
        this.update(this.business)
    }
  }
}
</script>
