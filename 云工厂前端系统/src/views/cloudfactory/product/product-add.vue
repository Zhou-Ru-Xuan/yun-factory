<template>
  <div class="app-container">
     <el-form label-width="120px" :model="product" :rules="rules" ref="product">
     
      <el-form-item prop="productName"   label="产品名称">
        <el-input v-model="product.productName"/>
      </el-form-item>

      <el-form-item label="产品类型" prop="typeName">
            <el-select v-model="product.typeName" placeholder="请选择产品类型">
            <el-option  v-for="(type,index) in productTypes" :key="index" :value="type.typeName">{{type.typeName}}</el-option>
            </el-select>
      </el-form-item>

      <el-form-item prop="norms"  label="产品规格">
        <el-input  v-model="product.norms"/>
      </el-form-item>

      <el-form-item prop="des"  label="产品描述">
        <el-input  v-model="product.des"/>
       </el-form-item>

      <el-form-item>
        <el-button :disabled="saveBtnDisabled" type="primary" @click="submitForm('product')">保存</el-button>
        <el-button @click="resetForm('product')">重置</el-button>
      </el-form-item>


    </el-form>

  </div>
</template>
<script>
import productApi from '@/api/product/product'
export default {
  data() {
    return {
      saveBtnDisabled:false,  // 保存按钮是否禁用,
      productTypes:[],
       product:{
        productName: '',
        norms: '',
        des: '',
        typeName:'',
      },
        rules: {
          productName: [
            { required: true, message: '请输入产品名称', trigger: 'blur' },
            // { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
          ],
          typeName: [
            { required: true, message: '请选择产品类型', trigger: 'change' }
          ]
        }
    };
  },
  created() { //页面渲染之前执行
    this.init()
    this.getProductTypes();
  },
  watch: {  //监听
    $route(to, from) { //路由发生变化，方法就会执行
      this.init()
    }
  },
  methods:{
    init() {
      //判断路径有id值,做修改
      if(this.$route.params && this.$route.params.id) {
          //从路径获取id值
          const id = this.$route.params.id
          //调用根据id查询的方法
          this.getInfo(id)
      } else { //路径没有id值，做添加
        //清空表单
        this.product = {}
      }
    },
    getProductTypes(){
        productApi.getProductTypes()
        .then(response =>{
            this.productTypes = response.data.productTypes
        })
    },
    //根据产品id查询的方法
    getInfo(id) {
      productApi.getProduct(id)
        .then(response => {
          this.product = response.data.product
        })
    },
     submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            // alert('submit!');
            this.saveOrUpdate();
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
       resetForm(formName) {
        this.$refs[formName].resetFields();
      },
    saveOrUpdate() {
      //判断修改还是添加
      //根据Product是否有id
      if(!this.product.id) {
        //添加
        this.saveProduct()
      } else {
        //修改
        this.updateProduct()
      }
    },
    //修改产品的方法
    updateProduct() {
      productApi.updateProduct(this.product)
        .then(response => {
          //提示信息
          this.$message({
              type: 'success',
              message: '修改成功!'
          });
          //回到列表页面 路由跳转
          this.$router.push({path:'/product/product-info'})
        })
        
    },
    //添加产品的方法
    saveProduct() {
                
      productApi.addProduct(this.product)
        .then(response => {//添加成功
          //提示信息
          this.$message({
              type: 'success',
              message: '添加成功!'
          });

        
          //回到列表页面 路由跳转
          this.$router.push({path:'/product/product-info'})
        })
        

 
    }

  }
}
</script>
