<template>
  <el-row>
    <el-col :offset="1" span="22">
      <h2>审核文献</h2>
<!--      <el-divider/>-->
      <el-divider/>
      <el-table :data="pdfFiles">
        <el-table-column
          label="">

          <template slot="header" slot-scope="scope">
            <el-radio-group size="mini" v-model="dataType">
              <el-radio-button
                :label="1"
              >
                未审核/审核异常
              </el-radio-button>
              <el-radio-button
                :label="2"
              >
                审核通过
              </el-radio-button>
            </el-radio-group>
            <label style="margin-right: 10px;margin-left: 30px;" >用户ID</label>
<!--                <el-select v-model="formInline.docID" placeholder="请选择" size="mini">-->
<!--                  <el-option-->
<!--                    v-for="item in options"-->
<!--                    :key="item.value"-->
<!--                    :label="item.label"-->
<!--                    :value="item.value">-->
<!--                  </el-option>-->
<!--                </el-select>-->
            <el-input v-model="formInline.docID" placeholder="请输入用户ID" size="mini" style="width: 150px"></el-input>
              <el-button type="primary" @click="onSubmit" size="mini" style="margin-left: 10px">查询</el-button>

          </template>

          <template slot-scope="scope">
            <div style="height: 110px">
              <el-image :src="scope.row.urls[0]" fit="contain" style="width: 100px;height: 100px; vertical-align: middle">
              </el-image>
              <div style="margin-left: 15px;display: inline-flex;height: 100%;vertical-align: middle;flex-direction: column;
                justify-content: space-between;">
                <span style="opacity: 0">1</span>
                  <span style="font-size:22px">{{scope.row.pdfTitle}}</span>
                <span style="font-size: 11px;">UID {{scope.row.userId}}</span>
              </div>
            </div>

          </template>
        </el-table-column>
        <el-table-column type="expand">
          <template slot-scope="scope">
            <el-card>
              <el-descriptions :column="3-1" size="medium" border>
                <el-descriptions-item label="UID">{{scope.row.userId}}</el-descriptions-item>
                <el-descriptions-item label="文献标题">{{scope.row.pdfTitle}}</el-descriptions-item>
                <el-descriptions-item label="图片">
                  <el-image v-for="(url,index) in scope.row.urls" :key="url" :src="scope.row.urls[index]"
                            fit="contain" :preview-src-list="[scope.row.urls[index]]"
                            style="width: 100px;height: 100px; vertical-align: middle;margin-left: 10px">
                  </el-image>
                </el-descriptions-item>
              </el-descriptions>
            </el-card>
          </template>
        </el-table-column>
        <el-table-column
          prop=""
          label=""
          width="300">
          <template slot-scope="scope">
            <el-cascader
              v-model="editPdfs[scope.$index].pdfStatus"
              :options="statusOptions"
              size="mini"
              style="width: 150px"
              >
              <template slot="default" slot-scope="{ node, data }">
                <span class="custom-tree-node">
                  <span>{{ node.label }}</span>
                </span>
              </template>
            </el-cascader>
            <el-button
              size="mini"
              type="success" plain
              @click="handleEdit()"
              round
              icon="el-icon-check"
              style="margin-left: 10px"
            >提交
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: "examine",
  data() {
    return {
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
      dataType:1,
      pdfFiles: [{
        pdfId: 1,
        urls: [
          'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
          'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
          'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
        ],
        pdfTitle: '文献检索系统',
        userId: 1,
      }, {
        pdfId: '2',
        urls: ['https://fuss10.elemecdn.com/8/27/f01c15bb73e1ef3793e64e6b7bbccjpeg.jpeg'],
        pdfTitle: '文献检索系统',
        userId: 1,
      }],
      options: [{
        value: '选项1',
        label: '黄金糕'
      },
        {
          value: '选项2',
          label: '双皮奶'
        }, {
          value: '选项3',
        }
      ],
      editPdfs: [
        {pdfStatus: null, pdfId: 1},
        {pdfStatus: null, pdfId: 1},
      ],
      editPdf: {},
      statusOptions: [
        {
        value: 0,
        label: '审核通过',
        },
        {
        value: 3,
        label: '审核异常',
        children: [
          {
          value: 30,
          label: '审核异常：政治因素',
          },
          {
          value: 31,
          label: '审核异常：血腥不适',
          },
          {
            value: 32,
            label: '审核异常：涉及引流'
          },
          {
            value: 33,
            label: '审核异常'
          },
        ]
      },],
    }
  },
  methods:{
    onSubmit() {
      console.log('submit!');
    },
    handleEdit(row) {
      console.log(row);
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-table__expanded-cell{
  padding-left: 5px;
  padding-right: 5px;
}
//::v-deep .el-table__expand-column .cell {
//  display: none;
//}
</style>
