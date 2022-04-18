<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input
          v-model="orderSchedulingQuery.deviceName"
          clearable
          placeholder="设备名称"
        />
      </el-form-item>

      <el-button type="primary" icon="el-icon-search" @click="getList()"
        >查询</el-button
      >
      <el-button type="default" @click="resetData()">清空</el-button>
    </el-form>

    <div>
      <el-button
        style="margin-bottom: 10px"
        type="success"
        icon="el-icon-plus"
        @click="dialogShow"
        >配置排产设备</el-button
      >
    </div>

    <!-- 配置排产弹窗 -->

    <el-dialog title="竞标弹窗" :visible.sync="dialogFormVisible">
      <el-form :model="orderScheduling" :rules="rules" ref="orderScheduling">
        <el-form-item label="设备名称" prop="deviceName">
          <el-select
            v-model="orderScheduling.deviceName"
            placeholder="请选择设备"
          >
            <el-option
              v-for="(device, index) in devices"
              :key="index"
              :value="device.deviceName"
              >{{ device.deviceName }}</el-option
            >
          </el-select>
        </el-form-item>

        <el-form-item label="开始日期" prop="startTime">
          <el-date-picker
            type="datetime"
            placeholder="选择日期"
            v-model="orderScheduling.startTime"
            value-format="yyyy-MM-dd hh:mm:ss"
          ></el-date-picker>
        </el-form-item>

        <el-form-item label="结束日期" prop="endTime">
          <el-date-picker
            type="datetime"
            placeholder="选择日期"
            v-model="orderScheduling.endTime"
            value-format="yyyy-MM-dd hh:mm:ss"
          ></el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm('orderScheduling')"
          >确 定</el-button
        >
      </div>
    </el-dialog>

    <!-- 表格 -->
    <el-table :data="list" border fit highlight-current-row>
      <el-table-column label="序号" width="70" align="center">
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column prop="deviceName" label="设备名称" />

      <el-table-column prop="startTime" label="开始时间" />

      <el-table-column prop="endTime" label="结束时间" />

      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <router-link :to="'/user/edit/' + scope.row.id">
            <el-button type="primary" size="mini" icon="el-icon-edit"
              >修改</el-button
            >
          </router-link>
          <el-button
            type="danger"
            size="mini"
            icon="el-icon-delete"
            @click="
              removeDataById(
                scope.row.id,
                scope.row.username,
                scope.row.roleName
              )
            "
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
//引入调用orderSchedulingApi.js文件
import orderSchedulingApi from "@/api/order/orderScheduling";
import deviceApi from "@/api/device/device";

import cookie from "js-cookie";

export default {
  //写核心代码位置

  data() {
    //定义变量和初始值
    return {
      list: null, //查询之后接口返回集合
      page: 1, //当前页
      limit: 5, //每页记录数
      total: 0, //总记录数
      orderSchedulingQuery: {}, //条件封装对象
      devices: [], //排产设备的选项
      orderScheduling: {
        //订单排产对象封装
        startTime: "",
        endTime: "",
        deviceName: "",
        orderId: "",
      },
      dialogFormVisible: false,

      rules: {
        deviceName: [
          { required: true, message: "请选择设备", trigger: "change" },
          // { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
        ],
        endTime: [
          {
            // type: "date",
            required: true,
            message: "请选择日期",
            trigger: "change",
          },
        ],
        startTime: [
          {
            // type: "date",
            required: true,
            message: "请选择日期",
            trigger: "change",
          },
        ],
      },
    };
  },
  created() {
    //页面渲染之前执行，一般调用methods定义的方法
    //调用
    this.getList();
  },
  methods: {
    //表单提交校验
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
         this.addOrderScheduling(); 
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    //获取属于该工厂但未对该订单排产的设备；
    getDevices() {
      let belong = "";
      let userStr = cookie.get("user_info");
      if (userStr != null) {
        let user = JSON.parse(userStr);
        belong = user.username;
      }

      deviceApi
        .getDeviceByBelongAndOrderId(belong, this.$route.params.id)
        .then((response) => {
          console.log(response);
          this.devices = response.data.devices;
        });
    },
    //创建具体的方法，调用orderScheduling.js定义的方法
    //用户列表的方法
    getList(page = 1) {
      //给个默认参数，显示第一页
      this.page = page;

      //查询排产信息中，orderId为该订单的设备
      this.orderSchedulingQuery.orderId = this.$route.params.id;

      orderSchedulingApi
        .getOrderSchedulingListPage(
          this.page,
          this.limit,
          this.orderSchedulingQuery
        )
        .then((response) => {
          //请求成功
          //response接口返回的数据
          console.log(response);
          this.list = response.data.orderSchedulings;
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
      this.orderSchedulingQuery = {};
      //查询所有用户数据
      this.getList();
    },
    // 弹出添加排产设备的对话框
    dialogShow() {
      this.dialogFormVisible = true;
      //弹出添加排产设备的对话框时，获取设备信息
      this.getDevices();
    },
    //确认添加添加排产信息
    addOrderScheduling() {
      this.dialogFormVisible = false;

      this.orderScheduling.orderId = this.$route.params.id;

      orderSchedulingApi
        .addOrderScheduling(this.orderScheduling)
        .then((response) => {
          this.$message({
            type: "success",
            message: "设置完成!",
          });
          //更新信息
          this.orderScheduling = {};
          this.devices = [];
          //回到列表页面
          this.getList();
          //清除验证  
          this.$refs['orderScheduling'].resetFields()
        });
    },
    //删除用户的方法
    removeDataById(id) {
      this.$confirm("此操作将永久删除用户记录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        //点击确定，执行then方法
        //调用删除的方法
        orderSchedulingApi.deleteOrderSchedulingById(id).then((response) => {
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
