<template>
  <div class="app-container">
    <!--查询表单-->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input
          v-model="orderBidQuery.username"
          clearable
          placeholder="工厂负责人"
        />
      </el-form-item>

      <el-form-item>
        <el-input
          v-model="orderBidQuery.factoryName"
          clearable
          placeholder="工厂名称"
        />
      </el-form-item>

      <el-form-item>
        <el-select v-model="orderBidQuery.orderBidState" placeholder="竞标状态">
          <el-option label="未中标" value="0"></el-option>
          <el-option label="已中标" value="1"></el-option>
          <el-option label="等待中" value="2"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="添加时间">
        <el-date-picker
          v-model="orderBidQuery.begin"
          type="datetime"
          placeholder="选择开始时间"
          value-format="yyyy-MM-dd HH:mm:ss"
          default-time="00:00:00"
        />
      </el-form-item>
      <el-form-item>
        <el-date-picker
          v-model="orderBidQuery.end"
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
    </el-form>

    <!-- 表格 -->
    <el-table :data="list" border fit highlight-current-row>
      <el-table-column label="序号" width="70" align="center">
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column prop="factoryName" label="投标工厂" />

      <el-table-column prop="username" label="工厂负责人" />

      <el-table-column prop="bidPrice" label="投标价格" />

      <el-table-column prop="orderBidState" label="竞标状态">
        <template slot-scope="scope">
          <p v-if="scope.row.orderBidState === 0">未中标</p>
          <p v-else-if="scope.row.orderBidState === 1">已中标</p>
          <p v-else-if="scope.row.orderBidState === 2">等待中</p>
        </template>
      </el-table-column>

      <el-table-column prop="gmtCreate" label="添加时间" width="160" />

      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <p v-if="scope.row.orderBidState === 2">
            <el-button
              type="danger"
              size="mini"
              icon="el-icon-delete"
              @click="confirmBid(scope.row.id)"
              >选标</el-button
            >
          </p>
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
//引入调用orderBidApi.js文件
import orderBidApi from "@/api/order/orderBid";
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
      orderBidQuery: {}, //条件封装对象
    };
  },
  created() {
    //页面渲染之前执行，一般调用methods定义的方法
    //调用
    this.getList();
  },
  methods: {
    //创建具体的方法，调用orderBid.js定义的方法
    //用户列表的方法
    getList(page = 1) {
      //给个默认参数，显示第一页
      this.page = page;

      this.orderBidQuery.orderId = this.$route.params.id;

      orderBidApi
        .getOrderBidListPage(this.page, this.limit, this.orderBidQuery)
        .then((response) => {
          //请求成功
          //response接口返回的数据
          console.log(response);
          this.list = response.data.orderBids;
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
      this.orderBidQuery = {};
      //查询所有用户数据
      this.getList();
    },
    confirmBid(id) {
      this.$confirm("你确定选标吗", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "info",
      }).then(() => {
        //点击确定，执行then方法
        let orderId = this.$route.params.id;
        orderBidApi.updateOrderBidForComfirm(id, orderId).then((response) => {
          //删除成功
          //提示信息
          this.$message({
            type: "success",
            message: "选标成功!",
          });
          //回到列表页面
          this.$router.push("/orderItemDealer/orderItemDealer-info");
        });
      }); //点击取消，执行catch方法
    },
  },
};
</script>
