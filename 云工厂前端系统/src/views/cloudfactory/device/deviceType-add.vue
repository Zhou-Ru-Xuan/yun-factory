<template>
  <div class="app-container">
     <el-form label-width="120px" :model="deviceType" :rules="rules" ref="deviceType">
     
      <el-form-item prop="typeName"   label="设备类型名称">
        <el-input v-model="deviceType.typeName"/>
      </el-form-item>

      <el-form-item>
        <el-button :disabled="saveBtnDisabled" type="primary" @click="submitForm('deviceType')">保存</el-button>
        <el-button @click="resetForm('deviceType')">重置</el-button>
      </el-form-item>


    </el-form>

  </div>
</template>
<script>
import deviceApi from '@/api/device/device'
export default {
  data() {
    return {
      saveBtnDisabled:false,  // 保存按钮是否禁用,
       deviceType:{
        typeName:'',
      },
        rules: {
          typeName: [
            { required: true, message: '请输入设备类型名称', trigger: 'blur' },
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
        this.deviceType = {}
      }
    },
   
    //根据设备类型id查询的方法
    getInfo(id) {
      deviceApi.getDeviceType(id)
        .then(response => {
          this.deviceType = response.data.deviceType
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
      //根据deviceType是否有id
      if(!this.deviceType.id) {
        //添加
        this.saveDeviceType()
      } else {
        //修改
        this.updateDeviceType()
      }
    },
    //修改设备类型的方法
    updateDeviceType() {
      deviceApi.updateDeviceType(this.deviceType)
        .then(response => {
          //提示信息
          this.$message({
              type: 'success',
              message: '修改成功!'
          });
          //回到列表页面 路由跳转
          this.$router.push({path:'/device/deviceType'})
        })
        
    },
    //添加设备类型的方法
    saveDeviceType() {
                
      deviceApi.addDeviceType(this.deviceType)
        .then(response => {//添加成功
          //提示信息
          this.$message({
              type: 'success',
              message: '添加成功!'
          });

        
          //回到列表页面 路由跳转
          this.$router.push({path:'/device/deviceType'})
        })
        

 
    }

  }
}
</script>
