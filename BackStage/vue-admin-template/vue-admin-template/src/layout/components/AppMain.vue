<template>
  <section class="app-main">
    <transition name="fade-transform" mode="out-in">
      <router-view :key="key" />
    </transition>
  </section>
</template>

<script>
export default {
  name: 'AppMain',
  computed: {
    key() {
      return this.$route.path
    }
  }
}
</script>

<style scoped>
.app-main {
  /*50 = navbar  */
  background-image: url("../../assets/img_1.png");
  background-repeat: no-repeat;
  /*背景图像的位置是在视口内固定，或者随着包含它的区块滚动。*/
  /*background-attachment: local;*/
  /*把背景图像扩展至足够大，以使背景图像完全覆盖背景区域*/
  /*background-size: cover ;*/
  background-position: center;
  /*设置了元素溢出时所需的行为，出现滑动*/
  overflow:auto;
  /*filter: opacity(40%);*/
  min-height: calc(100vh - 50px);
  width: 100%;
  position: relative;
  overflow: hidden;
}
.fixed-header+.app-main {
  padding-top: 50px;
}

</style>

<style lang="scss">
// fix css style bug in open el-dialog
.el-popup-parent--hidden {
  .fixed-header {
    padding-right: 15px;
  }
}
</style>
