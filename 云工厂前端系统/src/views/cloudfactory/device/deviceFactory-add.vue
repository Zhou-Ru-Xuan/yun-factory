<template>
  <div class="app-container">
     <el-form label-width="120px" :model="device" :rules="rules" ref="device">
     
      <el-form-item prop="deviceName"   label="设备名称">
        <el-input v-model="device.deviceName"/>
      </el-form-item>

      <el-form-item label="设备类型" prop="typeName">
            <el-select v-model="device.typeName" placeholder="请选择设备类型">
            <el-option  v-for="(type,index) in deviceTypes" :key="index" :value="type.typeName">{{type.typeName}}</el-option>
            </el-select>
      </el-form-item>

      <el-form-item prop="norms"  label="设备规格">
        <el-input  v-model="device.norms"/>
      </el-form-item>

      <el-form-item prop="des"  label="设备描述">
        <el-input  v-model="device.des"/>
       </el-form-item>

      <el-form-item>
        <el-button :disabled="saveBtnDisabled" type="primary" @click="submitForm('device')">保存</el-button>
        <el-button @click="resetForm('device')">重置</el-button>
      </el-form-item>


    </el-form>

  </div>
</template>
<script>
import deviceApi from '@/api/device/device'
import cookie from 'js-cookie'
export default {
  data() {
    return {
      saveBtnDisabled:false,  // 保存按钮是否禁用,
      deviceTypes:[],
       device:{
        deviceName: '',
        norms: '',
        des: '',
        typeName:'',
        hireState:2,//未被租用
        deviceSource:1,//租用设备
        belong:'',
      },
        rules: {
          deviceName: [
            { required: true, message: '请输入设备名称', trigger: 'blur' },
            // { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
          ],
          typeName: [
            { required: true, message: '请选择设备类型', trigger: 'change' }
          ]
        }
    };
  },
  created() { //页面渲染之前执行
    this.init()
    this.getDeviceTypes();
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
        this.device = {}
      }
    },
    getDeviceTypes(){
        deviceApi.getDeviceTypes()
        .then(response =>{
            this.deviceTypes = response.data.deviceTypes
        })
    },
    //根据设备id查询的方法
    getInfo(id) {
      deviceApi.getDevice(id)
        .then(response => {
          this.device = response.data.device
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
      //根据Device是否有id
      if(!this.device.id) {
        //添加
        this.saveDevice()
      } else {
        //修改
        this.updateDevice()
      }
    },
    //修改设备的方法
    updateDevice() {
      deviceApi.updateDevice(this.device)
        .then(response => {
          //提示信息
          this.$message({
              type: 'success',
              message: '修改成功!'
          });
          //回到列表页面 路由跳转
          this.$router.push({path:'/deviceFactory'})
        })
        
    },
    //添加设备的方法
    saveDevice() {
      
         //设置新建设备时的默认值
            this.device.hireState = 0;
            this.device.deviceSource = 0;

             //从cookie获取用户信息
            var userStr = cookie.get('user_info')
            // 把字符串转换json对象(js对象)
          
            if(userStr) {
                let  user_info = JSON.parse(userStr)
                this.device.belong = user_info.username;
            }

            

      deviceApi.addDevice(this.device)
        .then(response => {//添加成功
          //提示信息
          this.$message({
              type: 'success',
              message: '添加成功!'
          });

        
          //回到列表页面 路由跳转
          this.$router.push({path:'/deviceFactory'})
        })
        

 
    }

  }
}
</script>
