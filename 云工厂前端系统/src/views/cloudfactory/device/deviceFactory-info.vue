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
          v-model="deviceQuery.deviceNo"
          clearable
          placeholder="设备编号"
        />
      </el-form-item>

      <el-form-item>
        <el-select v-model="deviceQuery.deviceState" placeholder="设备状态">
          <el-option label="已关闭" value="0"></el-option>
          <el-option label="生产中" value="1"></el-option>
          <el-option label="闲置中" value="2"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-select v-model="deviceQuery.deviceSource" placeholder="设备来源">
          <el-option label="自有设备" value="0"></el-option>
          <el-option label="租用设备" value="1"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-input
          v-model="deviceQuery.typeName"
          clearable
          placeholder="设备类型"
        />
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

      <el-button type="primary" icon="el-icon-search" @click="getList()"
        >查询</el-button
      >
      <el-button type="default" @click="resetData()">清空</el-button>

      <div>
        <router-link to="/deviceFactory/deviceFactory-add">
          <el-button
            style="margin-bottom: 10px"
            type="success"
            icon="el-icon-plus"
            >添加</el-button
          >
        </router-link>
      </div>
    </el-form>

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

      <el-table-column prop="deviceSource" label="设备来源">
        <template slot-scope="scope">
          <p v-if="scope.row.deviceSource === 0">自有设备</p>
          <p v-else-if="scope.row.deviceSource === 1">租用设备</p>
        </template>
      </el-table-column>

      <el-table-column prop="gmtCreate" label="添加时间" width="160" />

      <el-table-column label="操作" width="350" align="center">
        <template slot-scope="scope">
          <p v-if="scope.row.deviceSource === 0">
            <router-link
              :to="'/deviceFactory/deviceFactory-edit/' + scope.row.id"
            >
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
          </p>

          <span v-if="scope.row.deviceState === 0">
            <el-button
              type="success"
              size="mini"
              icon="el-icon-turn-off"
              @click="changeState(scope.row.id, 2)"
              >打开</el-button
            >
          </span>
          <span v-else>
            <el-button
              type="warning"
              size="mini"
              icon="el-icon-open"
              @click="changeState(scope.row.id, 0)"
              >关闭</el-button
            >
          </span>
          <router-link
            :to="
              '/deviceFactory/deviceFactory-configure/' + scope.row.deviceName
            "
          >
            <el-button type="info" size="mini" icon="el-icon-info"
              >配置产能</el-button
            >
          </router-link>
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
      deviceQuery: {}, //条件封装对象
    };
  },
  created() {
    //页面渲染之前执行，一般调用methods定义的方法
    //调用
    this.getList();
  },
  methods: {
    //创建具体的方法，调用device.js定义的方法
    //用户列表的方法
    getList(page = 1) {
      //给个默认参数，显示第一页
      this.page = page;

      //
      let userStr = cookie.get("user_info");
      if (userStr != null) {
        let user = JSON.parse(userStr);
        this.deviceQuery.belong = user.username;
      }
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
    //删除用户的方法
    removeDataById(id) {
      this.$confirm("此操作将永久删除用户记录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        //点击确定，执行then方法
        //调用删除的方法
        deviceApi.deleteDeviceById(id).then((response) => {
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
    //更改设备状态
    changeState(id, state) {
      deviceApi.getDevice(id).then((response) => {
        let device = response.data.device;
        device.deviceState = state;
        deviceApi.updateDevice(device).then((response) => {
          this.getList(this.page);

          //提示信息
          this.$message({
            type: "success",
            message: "修改成功!",
          });
        });
      });
    },
  },
};
</script>
