<template>
  <div class="app-container">
    <!--查询表单-->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input
          v-model="deviceHireQuery.username"
          clearable
          placeholder="租用账号"
        />
      </el-form-item>

      <el-form-item>
        <el-input
          v-model="deviceHireQuery.deviceName"
          clearable
          placeholder="设备名称"
        />
      </el-form-item>

      <el-button type="primary" icon="el-icon-search" @click="getList()"
        >查询</el-button
      >
      <el-button type="default" @click="resetData()">清空</el-button>

      <!-- <div>
        <router-link to="/deviceHire/deviceHire-add">
          <el-button
            style="margin-bottom: 10px"
            type="success"
            icon="el-icon-plus"
            >添加</el-button
          >
        </router-link>
      </div> -->
    </el-form>

    <!-- 表格 -->
    <el-table :data="list" border fit highlight-current-row>
      <el-table-column label="序号" width="70" align="center">
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column prop="username" label="租用账号" />

      <el-table-column prop="deviceName" label="设备名称" />

      <el-table-column prop="startTime" label="开始时间" width="160" />
      
      <el-table-column prop="entTime" label="结束时间" width="160" />

      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <router-link :to="'/deviceHire/deviceHire-edit/' + scope.row.id">
            <el-button type="primary" size="mini" icon="el-icon-edit"
              >修改</el-button
            >
          </router-link>
          <el-button
            type="danger"
            size="mini"
            icon="el-icon-delete"
            @click="removeDataById(scope.row.id)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      :current-page="page"
      :page-size="limit"
      :total="total"
      style="padding: 30px 0; text-align: center"
      layout="total, prev, pager, next, jumper"
      @current-change="getList"
    />
  </div>
</template>

<script>
//引入调用deviceHireApi.js文件
import deviceHireApi from "@/api/device/deviceHire";

export default {
  //写核心代码位置

  data() {
    //定义变量和初始值
    return {
      list: null, //查询之后接口返回集合
      page: 1, //当前页
      limit: 5, //每页记录数
      total: 0, //总记录数
      deviceHireQuery: {}, //条件封装对象
    };
  },
  created() {
    //页面渲染之前执行，一般调用methods定义的方法
    //调用
    this.getList();
  },
  methods: {
    //创建具体的方法，调用deviceHire.js定义的方法
    //用户列表的方法
    getList(page = 1) {
      //给个默认参数，显示第一页
      this.page = page;
      deviceHireApi
        .getDeviceHireListPage(this.page, this.limit, this.deviceHireQuery)
        .then((response) => {
          //请求成功
          //response接口返回的数据
          console.log(response);
          this.list = response.data.deviceHires;
          this.total = response.data.total;
          console.log(this.list);
          console.log(this.total);
        })
        .catch((error) => {
          //请求失败
          console.log(error);
        });
    },
    resetData() {
      //清空的方法
      //表单输入项数据清空
      this.deviceHireQuery = {};
      //查询所有用户数据
      this.getList();
    },
    //删除用户的方法
    removeDataById(id) {
      this.$confirm("此操作将永久删除设备租用记录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        //点击确定，执行then方法
        //调用删除的方法
        deviceHireApi.deleteDeviceHireById(id).then((response) => {
          //删除成功
          //提示信息
          this.$message({
            type: "success",
            message: "删除成功!",
          });
          //回到列表页面
          this.getList();
        });
      }); //点击取消，执行catch方法
    },
  },
};
</script>
