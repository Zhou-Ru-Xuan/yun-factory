<template>
  <div class="app-container">
     <el-form label-width="120px" :model="productType" :rules="rules" ref="productType">
     
      <el-form-item prop="typeName"   label="产品类型名称">
        <el-input v-model="productType.typeName"/>
      </el-form-item>

      <el-form-item>
        <el-button :disabled="saveBtnDisabled" type="primary" @click="submitForm('productType')">保存</el-button>
        <el-button @click="resetForm('productType')">重置</el-button>
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
       productType:{
        typeName:'',
      },
        rules: {
          typeName: [
            { required: true, message: '请输入产品类型名称', trigger: 'blur' },
            // { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
          ],
         
        }
    };
  },
  created() { //页面渲染之前执行
    this.init()
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
        this.productType = {}
      }
    },
   
    //根据产品类型id查询的方法
    getInfo(id) {
      productApi.getProductType(id)
        .then(response => {
          this.productType = response.data.productType
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
      //根据productType是否有id
      if(!this.productType.id) {
        //添加
        this.saveProductType()
      } else {
        //修改
        this.updateProductType()
      }
    },
    //修改产品类型的方法
    updateProductType() {
      productApi.updateProductType(this.productType)
        .then(response => {
          //提示信息
          this.$message({
              type: 'success',
              message: '修改成功!'
          });
          //回到列表页面 路由跳转
          this.$router.push({path:'/product/productType'})
        })
        
    },
    //添加产品类型的方法
    saveProductType() {
                
      productApi.addProductType(this.productType)
        .then(response => {//添加成功
          //提示信息
          this.$message({
              type: 'success',
              message: '添加成功!'
          });

        
          //回到列表页面 路由跳转
          this.$router.push({path:'/product/productType'})
        })
        

 
    }

  }
}
</script>
