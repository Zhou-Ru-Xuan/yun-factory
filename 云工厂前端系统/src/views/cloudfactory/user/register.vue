<template>
  <div class="app-container">
        <el-form :model="user" :rules="rules" ref="user" label-width="100px" class="demo-user">
      <el-form-item prop="username"   label="用户名">
        <el-input v-model="user.username"/>
      </el-form-item>
      <el-form-item prop="password"  label="密码">
        <el-input  v-model="user.password"/>
      </el-form-item>
      <el-form-item prop="realName"  label="真实姓名">
      <el-input  v-model="user.realName"/>
      </el-form-item>
      <el-form-item prop="phone"   label="联系方式">
        <el-input   v-model="user.phone"/>
      </el-form-item>
      <span v-if=" ! (this.$route.params && this.$route.params.id)">
           <el-form-item prop="roleName" label="角色">
             <el-radio-group v-model="user.roleName">
                  <el-radio   label="云工厂" >云工厂</el-radio>
                  <el-radio label="经销商">经销商</el-radio>
             </el-radio-group>
         
      </el-form-item>
    <span v-if="user.roleName==='云工厂'">

      <el-form-item prop="factoryName" label="工厂名称">
          <el-input  v-model="user.factoryName"/>
            </el-form-item>
          <el-form-item prop="des" label="工厂描述">
              <el-input v-model="user.des"/>
            </el-form-item>

      </span>
     
        
    </span>
    
  <el-form-item>
    <el-button type="primary" @click="submitForm('user')">立即创建</el-button>
    <el-button @click="resetForm('user')">重置</el-button>
  </el-form-item>
</el-form>
  </div>
</template>

<script>
import userApi from '@/api/user/user'
  export default {
    data() {
      return {
       
        user: {
          username: '',
          password: '',
          realName: '',
          phone: '',
          roleId: '',
          roleName: '云工厂',
          factoryName:'',
          des:'',
        },
        factory:{
          factoryName:'',
          des:'',
          username:''
        },
        rules: {
          username: [
            { required: true, message: '请输入用户名', trigger: 'blur' },
            // { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
          ],
          password: [
            { required: true, message: '请输入密码', trigger: 'blur' }
          ],
          realName: [
            { required: true, message: '请输入真实姓名', trigger: 'blur' }
          ],
           roleName: [
            { required: true, message: '请选择角色', trigger: 'change' }
          ],
          phone: [
            { required: true, message: '请输入联系方式', trigger: 'blur' }
          ],
          factoryName: [
            { required: true, message: '请输入工厂名称', trigger: 'blur' }
          ]
        }
      };
    },
    methods: {
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            // alert('submit!');
                this.user.roleId=this.user.roleName==='云工厂'?'2':'3';
           
                userApi.addUser(this.user)
                  .then(response => {//添加成功
                    //提示信息
                    this.$message({
                        type: 'success',
                        message: '注册成功，请登录!'
                    });
                        if(this.user.roleName==='云工厂'){
                      this.factory.username = this.user.username;
                      this.factory.factoryName  = this.user.factoryName;
                      this.factory.des = this.user.des;
                      userApi.addFactory(this.factory);
                  }
                  
                    //回到列表页面 路由跳转
                    this.$router.push({path:'/login'})
                  })
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      }
    }
  }
</script>