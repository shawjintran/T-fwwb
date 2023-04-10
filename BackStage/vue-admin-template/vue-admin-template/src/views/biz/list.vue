<template>
  <div class="app-container">
    <el-table
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
      style="width: 80%;margin: auto auto"
    >
      <el-table-column align="center" label="#" width="90">
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="业务编号" width="150" align="center">
      <template slot-scope="scope">
        {{ scope.row.bizId }}
      </template>
      </el-table-column>
      <el-table-column label="业务价格" align="center">
        <template slot-scope="scope">
          {{ scope.row.bizPrice }}
        </template>
      </el-table-column>
      <el-table-column label="业务积分" width="90" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.bizPoint }}</span>
        </template>
      </el-table-column>

      <el-table-column class-name="status-col" label="业务状态" width="110" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.bizStatus === 0" type="success" size="mini">已下架</el-tag>
          <el-tag v-if="scope.row.bizStatus === 1" size="mini">售卖中</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="50" align="center">
        <template slot-scope="scope">
          <router-link :to="'/ex/biz/edit/'+scope.row.bizId">
            <!-- 使用隐藏路由 -->
            <el-button type="text" size="mini">修改</el-button>
          </router-link>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :current-page="page"
      :total="total"
      :page-size="limit"
      :page-sizes="[5, 10, 20, 30, 40, 50, 100]"
      style=" padding: 30px 0; text-align: center;"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="changePageSize"
      @current-change="changeCurrentPage" />
  </div>
</template>

<script>
import bizApi from '@/api/Business/biz';

export default {
  data() {
    return {
      list: null,
      total:0,
      limit:10,
      page:1
    }
  },
  created() {
    this.fetchData(this.page,this.limit)
  },
  methods: {
    changePageSize(size){
      this.limit=size
      this.page=1
      this.fetchData(1,size)
    },
    changeCurrentPage(current){
      this.page=current
      this.fetchData(current,this.limit)
    },
    fetchData(page,size) {
      this.listLoading = true
      bizApi.listBiz(page,size)
      .then(response => {
        if (response.code==200)
        {
          this.list = response.data.data
          this.total= response.data.total
        }else
        {
          this.$message.error(response.mes)
        }
      })
    }
  }
}
</script>
