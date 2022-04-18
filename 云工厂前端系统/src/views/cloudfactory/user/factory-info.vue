<template>
    <div class="app-container">
        <!--查询表单-->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input style="width:150px" v-model="factoryQuery.username" clearable placeholder="用户名" />
      </el-form-item>

      <el-form-item>
        <el-input style="width:150px" v-model="factoryQuery.factoryName" clearable placeholder="工厂名称"/>
      </el-form-item>

      <el-form-item>
        <el-input style="width:150px" v-model="factoryQuery.factoryState" clearable placeholder="工厂状态"/>
      </el-form-item>

      <el-form-item label="添加时间">
        <el-date-picker
          v-model="factoryQuery.begin"
          type="datetime"
          placeholder="选择开始时间"
          value-format="yyyy-MM-dd HH:mm:ss"
          default-time="00:00:00"
        />
      </el-form-item>
      <el-form-item>
        <el-date-picker
          v-model="factoryQuery.end"
          type="datetime"
          placeholder="选择截止时间"
          value-format="yyyy-MM-dd HH:mm:ss"
          default-time="00:00:00"
        />
      </el-form-item>

      <el-button type="primary" icon="el-icon-search" @click="getList()">查询</el-button>
      <el-button type="default" @click="resetData()">清空</el-button>
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

      <el-table-column prop="factoryName" label="工厂名称"/>

      <el-table-column prop="des" label="工厂描述"/>


      <el-table-column prop="username" label="工厂账号" />

        <el-table-column prop="factoryState" label="工厂状态" >
             <template slot-scope="scope">
              {{ scope.row.factoryState===1?'正常':'关停' }}
            </template>

        </el-table-column>

      <el-table-column prop="gmtCreate" label="添加时间" width="160"/>


      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope" >
        <span v-if="scope.row.factoryState===0">
            <el-button type="success" size="mini" icon="el-icon-open"  @click="changeState(scope.row.id,1)">打开</el-button>
        </span>
           <span v-else>
                <el-button type="danger" size="mini" icon="el-icon-turn-off" @click="changeState(scope.row.id,0)">关停</el-button>
        </span>
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
//引入调用userApi.js文件
import userApi from '@/api/user/user'

export default {
    //写核心代码位置

    data() { //定义变量和初始值
        return {
          list:null,//查询之后接口返回集合
          page:1,//当前页
          limit:5,//每页记录数
          total:0,//总记录数
          factoryQuery:{}, //条件封装对象
          // factory:{
          //   id:'',
          //   factoryState:'',
          //   factoryName:'',
          //   des:'',
          //   username:'',
          //   isDeleted:0,
          // },
        }
    },
    created() { //页面渲染之前执行，一般调用methods定义的方法
        //调用
        this.getList() 
    },
    methods:{  //创建具体的方法，调用factory.js定义的方法
        //用户列表的方法
        getList(page=1) {//给个默认参数，显示第一页
            this.page = page;
            userApi.getFactoryListPage(this.page,this.limit,this.factoryQuery)
                .then(response =>{//请求成功
                    //response接口返回的数据
                    console.log(response)
                    this.list = response.data.factorys
                    this.total = response.data.total
                    console.log(this.list)   
                    console.log(this.total)
                })
                .catch(error => {//请求失败
                    console.log(error)
                }) 
        },
        //更改工厂状态
        changeState(id,state) {
            userApi.getFactoryInfo(id)
                .then(response =>{
                    let factory = response.data.factory;
                    factory.factoryState = state;
                     userApi.updateFactoryInfo(factory)
                        .then(response => {

                         this.getList(this.page);
                          

                            //提示信息
                            this.$message({
                                type: 'success',
                                message: '修改成功!'
                            });
                        })
                });
    
        },
    
    }
}
</script>
