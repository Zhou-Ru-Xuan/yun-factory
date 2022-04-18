<template>
  <div class="app-container">
    <el-form
      label-width="120px"
      :model="orderItem"
      :rules="rules"
      ref="orderItem"
    >
      <el-form-item label="产品名称" prop="productName">
        <el-select v-model="orderItem.productName" placeholder="请选择产品">
          <el-option
            v-for="(product, index) in products"
            :key="index"
            :value="product.productName"
            >{{ product.productName }}</el-option
          >
        </el-select>
      </el-form-item>

      <el-form-item prop="quantity" label="数量">
        <el-input v-model="orderItem.quantity" />
      </el-form-item>

      <el-form-item label="交付日期" prop="endTime">
        <el-date-picker
          type="datetime"
          placeholder="选择日期"
          v-model="orderItem.endTime"
          value-format="yyyy-MM-dd hh:mm:ss"
        ></el-date-picker>
      </el-form-item>

      <el-form-item label="竞标截止日期" prop="bidEndTime">
        <el-date-picker
          type="datetime"
          placeholder="选择日期"
          v-model="orderItem.bidEndTime"
          value-format="yyyy-MM-dd hh:mm:ss"
        ></el-date-picker>
      </el-form-item>

      <el-form-item prop="receiverName" label="收货人">
        <el-input v-model="orderItem.receiverName" />
      </el-form-item>

      <el-form-item prop="receiverPhone" label="收货人联系方式">
        <el-input v-model="orderItem.receiverPhone" />
      </el-form-item>

      <el-form-item prop="receiverAddress" label="收货人地址">
        <el-input v-model="orderItem.receiverAddress" />
      </el-form-item>

      <el-form-item>
        <el-button
          :disabled="saveBtnDisabled"
          type="primary"
          @click="submitForm('orderItem')"
          >保存</el-button
        >
        <el-button @click="resetForm('orderItem')">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import orderItemApi from "@/api/order/order";
import productApi from "@/api/product/product";
import cookie from "js-cookie";
export default {
  data() {
    return {
      saveBtnDisabled: false, // 保存按钮是否禁用,
      products: [],
      orderItem: {
        productName: "",
        quantity: 0,
        endTime: "",
        bidEndTime: "",
        belong: "",
        receiverName: "",
        receiverPhone: "",
        receiverAddress: "",
      },
      rules: {
        quantity: [
          { required: true, message: "请输入订购数量", trigger: "blur" },
          // { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
        ],
        productName: [
          { required: true, message: "请选择产品类型", trigger: "change" },
        ],
        endTime: [
          {
            // type: "date",
            required: true,
            message: "请选择日期",
            trigger: "change",
          },
        ],
        bidEndTime: [
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
    //页面渲染之前执行
    this.init();
    this.getProducts();
    console.log("products = " + this.products);
  },
  watch: {
    //监听
    $route(to, from) {
      //路由发生变化，方法就会执行
      this.init();
    },
  },
  methods: {
    init() {
      //判断路径有id值,做修改
      if (this.$route.params && this.$route.params.id) {
        //从路径获取id值
        const id = this.$route.params.id;
        //调用根据id查询的方法
        this.getInfo(id);
      } else {
        //路径没有id值，做添加
        //清空表单
        this.orderItem = {};
      }
    },
    getProducts() {
      productApi.getProducts().then((response) => {
        this.products = response.data.products;
      });
    },
    //根据产品id查询的方法
    getInfo(id) {
      orderItemApi.getOrderItem(id).then((response) => {
        this.orderItem = response.data.orderItem;
      });
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // alert('submit!');
          this.saveOrUpdate();
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    saveOrUpdate() {
      //判断修改还是添加
      //根据OrderItem是否有id
      if (!this.orderItem.id) {
        //添加
        this.saveOrderItem();
      } else {
        //修改
        this.updateOrderItem();
      }
    },
    //修改产品的方法
    updateOrderItem() {
      orderItemApi.updateOrderItem(this.orderItem).then((response) => {
        //提示信息
        this.$message({
          type: "success",
          message: "修改成功!",
        });
        //回到列表页面 路由跳转
        this.$router.push({ path: "/orderItemDealer/orderItemDealer-info" });
      });
    },
    //添加产品的方法
    saveOrderItem() {
       //从cookie获取用户信息
      var userStr = cookie.get('user_info')
      // 把字符串转换json对象(js对象)
      if(userStr) {
        let user_info = JSON.parse(userStr);
        this.orderItem.belong = user_info.username;

      }
      orderItemApi.addOrderItem(this.orderItem).then((response) => {
        //添加成功
        //提示信息
        this.$message({
          type: "success",
          message: "添加成功!",
        });

        //回到列表页面 路由跳转
        this.$router.push({ path: "/orderItemDealer/orderItemDealer-info" });
      });
    },
  },
};
</script>
