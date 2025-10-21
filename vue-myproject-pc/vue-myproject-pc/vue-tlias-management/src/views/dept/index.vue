<script setup>
import { ref,onMounted } from 'vue';

import { queryAll,addApi,queryAllById,updateApi,deleteApi} from '@/api/dept';
import { ElMessage, ElMessageBox } from 'element-plus'
onMounted(()=>
{
  search();
})
const search= async()=> 
{
  //const result= await axios.get("https://m1.apifoxmock.com/m1/6842858-6557059-default/depts")
  // if(result.data.code==1)
  // {
  //   deptlist.value=result.data.data;
  // }

   const result= await queryAll()
  if(result.code==1)
    deptlist.value=result.data;
  
  
}
const deptlist=ref([])

const dialogFormVisible=ref(false)

const dept=ref({name:""})
//保存函数
const save= async()=>
{
  if(!deptFormref.value) 
  return

  //表单校验函数
  deptFormref.value.validate(async(valid)=>
  {
    if(valid)
    {
      let result
        if(dept.value.id)//根据id编辑部门
        {
          result=await updateApi(dept.value)
        }
        else//新增
        {
          result=await addApi(dept.value)

        }

      if(result.code==1)
      {
  //提示信息
      ElMessage.success("操作成功")
  //关闭对话框
      dialogFormVisible.value=false
      search()
      }
    else
        ElMessage.error(result.msg)
  }
  else
  ElMessage.error("数据错误")
  }


)



}
//新增部门按钮
const addDept=()=>
{
  dialogFormVisible.value=true
  formtitle.value='新增部门'
  dept.value={name:''}
  if (deptFormref.value)
{
  deptFormref.value.resetFields()
}
}

const formtitle=ref('')




const rules = ref({
  name: [
    { required: true, message: '部门名称为必填项', trigger: 'blur' },
    { min: 2, max: 10, message: '名称长度在2~10位', trigger: 'blur' },
  ]
})
const deptFormref=ref();

//查询回显
const edit= async(id)=>
{
  formtitle.value="修改部门"

  if (deptFormref.value)
{
  deptFormref.value.resetFields()
}

  const result=await queryAllById(id)
  if(result.code==1)
{
  dialogFormVisible.value=true
 dept.value=result.data
}

}

const delByid=async (id)=>
{
   ElMessageBox.confirm(
    '确认删除改部门吗?',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
  //确认函数
    .then(async() => {
      const result=await deleteApi(id)
     if(result.code==1)
      {
        ElMessage.success('删除成功')
        search()
      }
      else
      {
        ElMessage.error(result.msg)
      }
    }

  )
    //取消
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '取消删除',
      })
    })
}

</script>

<template>
<h1>部门管理</h1>
<div class="container">
  <el-button type="primary" @click="addDept">+新增部门</el-button>

  </div>

<!-- 表格 -->
<div class="table">
  <el-table :data="deptlist" border style="width: 100%">
      <el-table-column type="index" label="序号" width="100" align="center"/>
      <el-table-column prop="name" label="部门名称" width="260" align="center"/>
      <el-table-column prop="updateTime" label="最后操作时间" align="center"/>
      <el-table-column label="操作"width="200" align="center">
        <template #default="scope">
        <el-button type="primary" @click="edit(scope.row.id)">编辑</el-button>
        <el-button type="danger" @click="delByid(scope.row.id)">删除</el-button>
        </template>


      </el-table-column>
  </el-table>
</div>


<!-- 一个对话框组件 -->

  <el-dialog v-model="dialogFormVisible" :title=formtitle width="500">
    <el-form :model="dept" v-bind:rules ref="deptFormref">
      <el-form-item label="部门名称" label-width="80px" prop="name" >
        <el-input v-model="dept.name"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="save">
          确定
        </el-button>
      </div>
    </template>
  </el-dialog>

</template>

<style scoped>

.container {
   /* 上右下左顺时针 */
  margin: 10px 0px; 
}

</style>
