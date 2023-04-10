<template>

  <div class="login-container">
    <div style="text-align: center;margin-top: 30px">
      <h1 style="margin: 20px 20px;color: #76aeb7;font-min-size:45px">医寻--智能文献检索平台</h1>
      <h1 style="margin: 40px 40px;color: #76aeb7;font-min-size:45px">后台管理</h1>
    </div>
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on" label-position="left">
      <div class="loginarea">
        <el-form-item prop="phonenumber">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
          <el-input
            ref="phonenumber"
            v-model="loginForm.phonenumber"
            placeholder="Phone number"
            name="phonenumber"
            type="text"
            tabindex="1"
            auto-complete="on"
          />
        </el-form-item>

        <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
          <el-input
            :key="passwordType"
            ref="password"
            v-model="loginForm.password"
            :type="passwordType"
            placeholder="Password"
            name="password"
            tabindex="2"
            auto-complete="on"
            @keyup.enter.native="handleLogin"
          />
          <span class="show-pwd" @click="showPwd">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
        </span>
        </el-form-item>

        <el-button :loading="loading" type="primary" style="background-color: #1f2d3d;width:100%;margin-bottom:30px;" @click.native.prevent="handleLogin">登录</el-button>
        <div class="tips">
          <span style=" font-weight: bolder;margin-right:20px;color: #974949;">* 本管理页面只向管理人员开放</span>
        </div>
      </div>



    </el-form>
  </div>
</template>

<script>

export default {
  name: 'Login',
  data() {
    const validatePhonenumber = (rule, value, callback) => {
      //数字
      const regPos = /^\d{4,}$/; // 非负整数
      if (!regPos.test(value)) {
        callback(new Error('Please enter the correct phone number'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      const regPos = /^\d{4,}$/; // 非负整数
      if (!regPos.test(value))  {
        callback(new Error('The password can not be other character but num'))
      } else {
        callback()
      }
    }
    return {
      loginForm: {
        phonenumber: '11111',
        password: '1111'
      },
      loginRules: {
        phonenumber: [{ required: true, trigger: 'blur', validator: validatePhonenumber }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }]
      },
      loading: false,
      passwordType: 'password',
      redirect: undefined
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  methods: {

    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        console.log(valid)
        if (valid) {
          // 验证登录逻辑
          // if(){
          //
          // }
          this.loading = true
          this.$store.dispatch('user/login', this.loginForm)
          .then(() => {
            this.$router.push({ path: this.redirect || '/' })
            this.loading = false
          }).catch(() => {
            this.loading = false
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    islogin(){

    }
  }
}
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg:#283443;
$light_gray:#fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: #000000;
      font-weight: bold;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }
  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: #00000000;
    border-radius: 5px;
    color: #454545;
  }

}

</style>

<style lang="scss" scoped>
$bg: #fdfdfd;
$dark_gray:#889aa4;
$light_gray:#eee;

.login-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;
.loginarea{
  border-radius: 20px;
  background-color: #cee5e9;
  padding: 30px;
  box-shadow: #f4f4f5;
}
  .loginarea:hover{
    transform: translateY(-5px);
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.55);
  }
  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 35px 35px 0;
    margin: 0 auto;
    overflow: hidden;

  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: #ffffff;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
}
</style>
