<template>
  <div>
    <div style="margin-bottom: 3px">
      <el-input v-model="name"  placeholder="请输入物品名" suffix-icon="el-icon-search" style="width: 200px"
                @keyup.enter.n.native="loadPost"></el-input>
      <el-select v-model="storage" placeholder="请选择仓库" style="margin-left: 5px">
        <el-option
            v-for="item in storageData"
            :key="item.id"
            :label="item.name"
            :value="item.id">
        </el-option>
      </el-select>
      <el-select v-model="goodstype" placeholder="请选择分类" style="margin-left: 5px">
        <el-option
            v-for="item in goodsTypeData"
            :key="item.id"
            :label="item.name"
            :value="item.id">
        </el-option>
      </el-select>
      <el-button type="primary"  style="margin-left: 3px" @click="loadPost">查询</el-button>
      <el-button type="danger" @click="resetParam">重置</el-button>


    </div>
    <el-table :data="tableData" :header-cell-style="{ background: '#f3f6fd', color: '#555',textAlign: 'center' }" border>
      <el-table-column prop="id" label="ID" width="60">
      </el-table-column>
<!--排序
-->

      <el-table-column prop="goodsname" label="物品名" sortable width="150">
      </el-table-column>
      <el-table-column prop="storagename" label="仓库" sortable width="140" >
      </el-table-column>
      <el-table-column prop="goodstypename" label="分类" sortable width="120" >
      </el-table-column>
      <el-table-column prop="adminname" label="操作人" sortable width="120">
      </el-table-column>
      <el-table-column prop="username" label="申请人" sortable width="120">
      </el-table-column>
      <el-table-column prop="count" label="数量" width="100">
      </el-table-column>
      <el-table-column prop="createtime" label="操作时间" sortable width="180">
      </el-table-column>
      <el-table-column prop="remark" label="备注" >
      </el-table-column>

    </el-table>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[ 5, 10, 20,30]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>

  </div>
</template>

<script>




export default {
  name: "RecordManage",
  data() {


    return {
      user : JSON.parse(sessionStorage.getItem('CurUser')),
      storage:'',
      goodstype:'',
      goodsTypeData:[],
      storageData:[],
      tableData:[],
      pageSize:10,
      pageNum:1,
      total:0,
      name:'',
      centerDialogVisible:false,
      form:{
        id:'',
        name:'',
        remark:'',
        storage:'',
        goodstype:'',
        count:'',

      }
    }
  },
  methods:{
    formatStorage(row){
      let temp = this.storageData.find(item=>{
        return item.id===row.storage
      })
      return temp && temp.name
    },
    formatGoodsType(row){
      let temp = this.goodsTypeData.find(item=>{
        return item.id===row.goodstype
      })
      return temp && temp.name
    },
    resetForm() {
      this.$refs.form.resetFields();
    },

    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.pageSize=val
      this.loadPost()
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.pageNum=val
      this.loadPost()
    },

    resetParam(){
      this.name=''
      this.storage='',
      this.goodstype=''
    },
    loadPost(){
      this.$axios.post(this.$httpUrl+'/record/listPage',{
        pageSize:this.pageSize,
        pageNum:this.pageNum,
        param:{
          name:this.name,
          goodstype: this.goodstype+'',
          storage:this.storage+'',
          roleId:this.user.roleId+'',
          userId:this.user.id+''
        }
      }).then(res=>res.data).then(res=>{
        console.log(res)
        if(res.code == 200){
          this.tableData=res.data
          this.total=res.total
        }else {
          alert('获取数据失败')
        }

      })
    },
    loadStorage(){
      this.$axios.get(this.$httpUrl+'/storage/list').then(res=>res.data).then(res=>{
        console.log(res)
        if(res.code == 200){
          this.storageData=res.data

        }else {
          alert('获取数据失败')
        }

      })
    },
    loadGoodsType(){
      this.$axios.get(this.$httpUrl+'/goodstype/list').then(res=>res.data).then(res=>{
        console.log(res)
        if(res.code == 200){
          this.goodsTypeData=res.data

        }else {
          alert('获取数据失败')
        }

      })
    }

  },
  beforeMount() {
    this.loadStorage()
    this.loadGoodsType()
    this.loadPost()

  }
}
</script>

<style scoped>

</style>