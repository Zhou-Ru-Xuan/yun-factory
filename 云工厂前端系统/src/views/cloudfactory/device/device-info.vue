<template>
    <div class="app-container">
        <!--查询表单-->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input  v-model="deviceQuery.deviceName" clearable placeholder="设备名称" />
      </el-form-item>

      <el-form-item>
        <el-input  v-model="deviceQuery.deviceNo" clearable placeholder="设备编号"/>
      </el-form-item>

    <el-form-item >
        <el-select  v-model="deviceQuery.deviceState" placeholder="设备状态">
            <el-option label="已关闭" value=0></el-option>
            <el-option label="生产中" value=1></el-option>
            <el-option label="闲置中" value=2></el-option>
        </el-select>
    </el-form-item>

    <el-form-item >
        <el-select  v-model="deviceQuery.hireState" placeholder="租用状态">
            <el-option label="工厂设备" value=0></el-option>
            <el-option label="已被租用" value=1></el-option>
            <el-option label="未被租用" value=2></el-option>
        </el-select>
    </el-form-item>

      <el-form-item>
        <el-input  v-model="deviceQuery.belong" clearable placeholder="设备归属"/>
      </el-form-item>

        
      <el-form-item>
        <el-input  v-model="deviceQuery.typeName" clearable placeholder="设备类型"/>
      </el-form-item>

      <el-form-item label="添加时间">
        <el-date-picker
          v-model="deviceQuery.begin"
          type="datetime"
          placeholder="选择开始时间"
          value-format="yyyy-MM-dd HH:mm:ss"
          default-time="00:00:00"
        />
      </el-form-item>
      <el-form-item>
        <el-date-picker
          v-model="deviceQuery.end"
          type="datetime"
          placeholder="选择截止时间"
          value-format="yyyy-MM-dd HH:mm:ss"
          default-time="00:00:00"
        />
      </el-form-item>

      <el-button type="primary" icon="el-icon-search" @click="getList()">查询</el-button>
      <el-button type="default" @click="resetData()">清空</el-button>

     <div>
        <router-link to="/device/device-add">
                  <el-button style="margin-bottom:10px" type="success" icon="el-icon-plus">添加</el-button>
        </router-link>

      </div>

    </el-form>

    <!-- 表格 -->
    <el-table
      :data="list"
      border
      fit
      highlight-current-row>

      <el-table-column
        label="序号"
        width="70"
        align="center">
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column prop="deviceNo" label="设备编号"/>

      <el-table-column prop="deviceName" label="设备名称"/>


      <el-table-column prop="typeName" label="设备类型" />

      <el-table-column prop="norms" label="设备规格" />

      <el-table-column prop="des" label="设备描述" />

     <el-table-column prop="deviceState" label="设备状态" >
        <template slot-scope="scope">
           <p v-if="scope.row.deviceState===0">
               已关闭
           </p>
           <p v-else-if="scope.row.deviceState===1">
               生产中
           </p>
           <p v-if="scope.row.deviceState===2">
               闲置中
           </p>
       </template>
     </el-table-column>

     <el-table-column prop="hireState" label="租用状态" >
         <template slot-scope="scope">
           <p v-if="scope.row.hireState===0">
               工厂设备
           </p>
           <p v-else-if="scope.row.hireState===1">
               已被租用
           </p>
           <p v-if="scope.row.hireState===2">
               未被租用
           </p>
       </template>
     </el-table-column>

 
     

      <el-table-column prop="belong" label="归属" />


      <el-table-column prop="gmtCreate" label="添加时间" width="160"/>


      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <router-link :to="'/device/device-edit/'+scope.row.id">
            <el-button type="primary" size="mini" icon="el-icon-edit">修改</el-button>
          </router-link>
          <el-button type="danger" size="mini" icon="el-icon-delete" @click="removeDataById(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

  <!-- 分页 -->
    <el-pagination
      :current-page="page"
      :page-size="limit"
      :total="total"
      style="padding: 30px 0; text-align: center;"
      layout="total, prev, pager, next, jumper"
      @current-change="getList"  
      
    />
    </div>
    
</template>

<script>
//引入调用deviceApi.js文件
import deviceApi from '@/api/device/device'

export default {
    //写核心代码位置

    data() { //定义变量和初始值
        return {
          list:null,//查询之后接口返回集合
          page:1,//当前页
          limit:5,//每页记录数
          total:0,//总记录数
          deviceQuery:{} //条件封装对象
        }
    },
    created() { //页面渲染之前执行，一般调用methods定义的方法
        //调用
        this.getList() 
    },
    methods:{  //创建具体的方法，调用device.js定义的方法
        //用户列表的方法
        getList(page=1) {//给个默认参数，显示第一页
            this.page = page;
            deviceApi.getDeviceListPage(this.page,this.limit,this.deviceQuery)
                .then(response =>{//请求成功
                    //response接口返回的数据
                    console.log(response)
                    this.list = response.data.devices
                    this.total = response.data.total
                    console.log(this.list)   
                    console.log(this.total)
                })
                .catch(error => {//请求失败
                    console.log(error)
                }) 
        },
        resetData() {//清空的方法
            //表单输入项数据清空
            this.deviceQuery = {}
            //查询所有用户数据
            this.getList()
        },
        //删除用户的方法
        removeDataById(id) {
            this.$confirm('此操作将永久删除用户记录, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {  //点击确定，执行then方法
                //调用删除的方法
                deviceApi.deleteDeviceById(id)
                    .then(response =>{//删除成功
                    //提示信息
                    this.$message({
                        type: 'success',
                        message: '删除成功!'
                    });
                    //回到列表页面
                    this.getList()
                })
            }) //点击取消，执行catch方法
        }
 
    }
}
</script>
