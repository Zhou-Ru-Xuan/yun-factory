<template>
  <div class="app-container">
    <!--查询表单-->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input
          v-model="deviceQuery.deviceName"
          clearable
          placeholder="设备名称"
        />
      </el-form-item>

      <el-form-item>
        <el-input
          v-model="deviceQuery.typeName"
          clearable
          placeholder="设备类型"
        />
      </el-form-item>

      <el-button type="primary" icon="el-icon-search" @click="getList()"
        >查询</el-button
      >
      <el-button type="default" @click="resetData()">清空</el-button>
    </el-form>

    <!-- 配置排产弹窗 -->

    <el-dialog title="竞标弹窗" :visible.sync="dialogFormVisible">
      <el-form :model="deviceHire" :rules="rules" ref="deviceHire">
        <el-form-item label="开始日期" prop="startTime">
          <el-date-picker
            type="datetime"
            placeholder="选择日期"
            v-model="deviceHire.startTime"
            value-format="yyyy-MM-dd hh:mm:ss"
          ></el-date-picker>
        </el-form-item>

        <el-form-item label="结束日期" prop="endTime">
          <el-date-picker
            type="datetime"
            placeholder="选择日期"
            v-model="deviceHire.endTime"
            value-format="yyyy-MM-dd hh:mm:ss"
          ></el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm('deviceHire')"
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

      <el-table-column prop="deviceNo" label="设备编号" />

      <el-table-column prop="deviceName" label="设备名称" />

      <el-table-column prop="typeName" label="设备类型" />

      <el-table-column prop="norms" label="设备规格" />

      <el-table-column prop="des" label="设备描述" />

      <el-table-column prop="deviceState" label="设备状态">
        <template slot-scope="scope">
          <p v-if="scope.row.deviceState === 0">已关闭</p>
          <p v-else-if="scope.row.deviceState === 1">生产中</p>
          <p v-if="scope.row.deviceState === 2">闲置中</p>
        </template>
      </el-table-column>

      <el-table-column prop="hireState" label="租用状态">
        <template slot-scope="scope">
          <p v-if="scope.row.hireState === 0">工厂设备</p>
          <p v-else-if="scope.row.hireState === 1">已被租用</p>
          <p v-if="scope.row.hireState === 2">未被租用</p>
        </template>
      </el-table-column>

      <el-table-column prop="belong" label="归属" />

      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
            <el-button
              type="success"
              size="mini"
              icon="el-icon-circle-plus-outline"
              @click="dialogShow(scope.row.deviceName,scope.row.id)"
              >租用</el-button
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
//引入调用deviceApi.js文件
import deviceApi from "@/api/device/device";
import deviceHireApi from "@/api/device/deviceHire";
import cookie from 'js-cookie'

export default {
  //写核心代码位置

  data() {
    //定义变量和初始值
    return {
      list: null, //查询之后接口返回集合
      page: 1, //当前页
      limit: 5, //每页记录数
      total: 0, //总记录数
      deviceQuery: {
        //条件封装对象
        hireState: 2,
        deviceSource:1,
      },
      deviceHire: {
        username: "",
        deviceName: "",
        startTime: "",
        endTime: "",
        deviceId:"",
      },
      dialogFormVisible: false,
      rules: {
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
    //为deviceHire对象的username赋值
    let userStr = cookie.get("user_info");
    if (userStr != null) {
      let user = JSON.parse(userStr);
      this.deviceHire.username = user.username;
    }
  },
  methods: {
    //表单提交校验
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.addDeviceHire();
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    // 弹出添加排产设备的对话框
    dialogShow(deviceName,deviceId) {
      this.dialogFormVisible = true;
      
      this.deviceHire.deviceName = deviceName;

      this.deviceHire.deviceId = deviceId;
    },
    addDeviceHire() {
      this.dialogFormVisible = false;

      this.deviceHire.orderId = this.$route.params.id;

      deviceHireApi.addDeviceHire(this.deviceHire).then((response) => {
        this.$message({
          type: "success",
          message: "添加完成!",
        });
        //更新信息
        this.deviceHire = {};
        //回到列表页面
        this.getList();
        //清除验证
        this.$refs["deviceHire"].resetFields();
      });
    },
    //创建具体的方法，调用device.js定义的方法
    //用户列表的方法
    getList(page = 1) {
      //给个默认参数，显示第一页
      this.page = page;
      this.deviceQuery.hireState = 2;
      this.deviceQuery.deviceSource=1;

      deviceApi
        .getDeviceListPage(this.page, this.limit, this.deviceQuery)
        .then((response) => {
          //请求成功
          //response接口返回的数据
          console.log(response);
          this.list = response.data.devices;
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
      this.deviceQuery = {};
      //查询所有用户数据
      this.getList();
    },
 
  },
};
</script>
