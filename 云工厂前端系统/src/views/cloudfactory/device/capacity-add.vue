<template>
  <div class="app-container">
     <el-form label-width="120px" :model="capacity" :rules="rules" ref="capacity">
     
    <!-- 修改界面不需要修改产品名称，只修改产能即可 -->
    <span v-if="!(this.$route.params && this.$route.params.id)">
         <el-form-item label="产品名称" prop="productName">
            <el-select v-model="capacity.productName" placeholder="请选择产品">
            <el-option  v-for="(product,index) in products" :key="index" :value="product.productName">{{product.productName}}</el-option>
            </el-select>
      </el-form-item>
    </span>
       
       
       <el-form-item prop="power"   label="产能">
        <el-input v-model="capacity.power"/>
      </el-form-item>


      <el-form-item>
        <el-button :disabled="saveBtnDisabled" type="primary" @click="submitForm('capacity')">保存</el-button>
        <el-button @click="resetForm('capacity')">重置</el-button>
      </el-form-item>


    </el-form>

  </div>
</template>
<script>
import capacityApi from '@/api/device/capacity'
import cookie from 'js-cookie'
export default {
  data() {
    return {
      saveBtnDisabled:false,  // 保存按钮是否禁用,
      products:[],
      capacityQuery:{},
       capacity:{
        productName: '',
        power: 0,
        deviceName:'',
        belong:'',
      },
        rules: {
          power: [
            { required: true, message: '请输入产能', trigger: 'blur' },
            // { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
          ],
          productName: [
            { required: true, message: '请选择产品名称', trigger: 'change' }
          ]
        }
    };
  },
  created() { //页面渲染之前执行
    this.init()

    this.capacityQuery.deviceName = this.$route.params.deviceName;
    
      //从cookie获取用户信息
      var userStr = cookie.get('user_info')
      // 把字符串转换json对象(js对象)
      if(userStr) {
        let user = JSON.parse(userStr)
        this.capacityQuery.belong = user.username;
      }

    this.getProducts(this.capacityQuery);
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
        this.capacity = {}
      }
    },
    getProducts(capacityQuery){
        capacityApi.getProducts(capacityQuery)
        .then(response =>{
            this.products = response.data.products
        })
    },
    //根据产能id查询的方法
    getInfo(id) {
      capacityApi.getCapacity(id)
        .then(response => {
          this.capacity = response.data.capacity
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
      //根据Capacity是否有id
      if(!this.capacity.id) {
        //添加
        this.saveCapacity()
      } else {
        //修改
        this.updateCapacity()
      }
    },
    //修改产能的方法
    updateCapacity() {
      capacityApi.updateCapacity(this.capacity)
        .then(response => {
          //提示信息
          this.$message({
              type: 'success',
              message: '修改成功!'
          });
          //回到列表页面 路由跳转
          this.$router.go(-1)
        })
        
    },
    //添加产能的方法
    saveCapacity() {
      
         //设置新建产能时的默认值
            this.capacity.deviceName = this.$route.params.deviceName;
             //从cookie获取用户信息
            var userStr = cookie.get('user_info')
            // 把字符串转换json对象(js对象)
          
            if(userStr) {
                let  user_info = JSON.parse(userStr)
                this.capacity.belong = user_info.username;
            }

            

      capacityApi.addCapacity(this.capacity)
        .then(response => {//添加成功
          //提示信息
          this.$message({
              type: 'success',
              message: '添加成功!'
          });

        
          //回到列表页面 路由跳转
        //   this.$router.push({path:'/device/deviceFactory-configure'})
        this.$router.go(-1)
        })
        

 
    }

  }
}
</script>
