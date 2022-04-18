<template>
  <div class="app-container">
    <!--查询表单-->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input
          v-model="orderItemQuery.orderNo"
          clearable
          placeholder="订单编号"
        />
      </el-form-item>

      <el-form-item>
        <el-input
          v-model="orderItemQuery.productName"
          clearable
          placeholder="产品名称"
        />
      </el-form-item>

      <el-form-item>
        <el-input
          v-model="orderItemQuery.receiver"
          clearable
          placeholder="收货人"
        />
      </el-form-item>

      <el-button type="primary" icon="el-icon-search" @click="getList()"
        >查询</el-button
      >
      <el-button type="default" @click="resetData()">清空</el-button>
    </el-form>

    <!--  -->

    <el-dialog title="竞标弹窗" :visible.sync="dialogFormVisible">
      <el-form :model="bidItem" :rules="rules" ref="bidPrice">
        <el-form-item prop="bidPrice" label="竞标价格">
          <el-input v-model="bidItem.bidPrice" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogFormVisible = false"
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

      <el-table-column prop="orderNo" label="订单编号" />

      <el-table-column prop="productName" label="产品名称" />

      <el-table-column prop="quantity" label="订购数量" />

      <el-table-column prop="endTime" label="交付日期" />

      <el-table-column prop="bidEndTime" label="竞标截止日期" />

      <el-table-column prop="receiverName" label="收货人" />
      <el-table-column prop="receiverPhone" label="收货人联系方式" />
      <el-table-column prop="receiverAddress" label="收货人地址" />

      <el-table-column prop="orderState" label="订单状态">
        <template slot-scope="scope">
          <p v-if="scope.row.orderState === 1">正常</p>
          <p v-else-if="scope.row.orderState === 2">已发布</p>
          <p v-else-if="scope.row.orderState === 3">投标结束</p>
          <p v-else-if="scope.row.orderState === 4">已排产</p>
          <p v-else-if="scope.row.orderState === 5">已完工</p>
          <p v-else-if="scope.row.orderState === 6">已发货</p>
          <p v-else-if="scope.row.orderState === 7">已收货</p>
          <p v-else-if="scope.row.orderState === 8">已完成</p>
        </template>
      </el-table-column>

      <!-- <el-table-column prop="gmtCreate" label="添加时间" width="160" /> -->

      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.orderState === 3 || scope.row.orderState === 4">
            <!-- 进入排产信息页面，查看之前排产信息、添加新的排产信息 -->
            <router-link
              :to="
                '/orderItemFactory/orderItemFactory-schedule/' + scope.row.id
              "
            >
              <el-button type="primary" size="mini" icon="el-icon-edit"
                >排产</el-button
              >
            </router-link>
            
          </span>
           <span v-if="scope.row.orderState === 4">
            <!-- 进入排产信息页面，查看之前排产信息、添加新的排产信息 -->
              <el-button
              type="success"
              size="mini"
              icon="el-icon-refresh-right"
              @click="changeState(scope.row.id, scope.row.orderState)"
              >完工</el-button
            >
            
          </span>
      
          <p v-if="scope.row.orderState === 5">
            <el-button
              type="success"
              size="mini"
              icon="el-icon-video-pause"
              @click="changeState(scope.row.id, scope.row.orderState)"
              >发货</el-button
            >
          </p>
          <p v-if="scope.row.orderState === 7">
            <el-button
              type="success"
              size="mini"
              icon="el-icon-video-play"
              @click="changeState(scope.row.id, scope.row.orderState)"
              >完成</el-button
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
//引入调用orderItemApi.js文件
import orderItemApi from "@/api/order/order";
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
      orderItemQuery: {}, //条件封装对象
      dialogFormVisible: false,
      bidItem: {
        bidPrice: 0,
      },
      rules: {
        bidPrice: [
          { required: true, message: "请输入竞标价格", trigger: "blur" },
          // { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
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
    //更改状态
    changeState(orderId, orderState) {
      let message = "";
      if (orderState == 4) {
        //正常改为已发布
        message = "是否确认订单完工？";
      } else if (orderState == 5) {
        //已发货改为已收货
        message = "是否确认订单发货？";
      } else if (orderState == 7) {
        //已发货改为已收货
        message = "是否确认完成订单？";
      }
      this.$confirm(message, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        //获取要修改状态的订单
        orderItemApi.getOrderItem(orderId).then((response) => {
          console.log(response);
          this.orderItem = response.data.orderItem;
          // debugger;
          // console.log(this.orderItem);
          //修改状态值
          this.orderItem.orderState++;
          // console.log(this.orderItem);
          // debugger;
          //更新订单状态
          orderItemApi.updateOrderItem(this.orderItem).then(() => {
            this.$message({
              type: "success",
              message: "操作成功",
            });
            //回到列表页面
            this.getList();
          });
        });
      });
    },
    //创建具体的方法，调用orderItem.js定义的方法
    //用户列表的方法
    getList(page = 1) {
      //给个默认参数，显示第一页
      this.page = page;

      //排产页面，显示中标，状态为竞标结束的订单
      let userStr = cookie.get("user_info");
      if (userStr != null) {
        let user = JSON.parse(userStr);
        this.orderItemQuery.winner = user.username;
      }
      this.orderItemQuery.orderState = 3;

      orderItemApi
        .getOrderItemListPage(this.page, this.limit, this.orderItemQuery)
        .then((response) => {
          //请求成功
          //response接口返回的数据
          console.log(response);
          this.list = response.data.orderItems;
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
      this.orderItemQuery = {};
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
        orderItemApi.deleteOrderItemById(id).then((response) => {
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
