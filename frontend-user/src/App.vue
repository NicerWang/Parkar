<template>
  <navbar ref="nav"></navbar>
  <div :style="{width:'100%',height:blankHeight}"></div>
  <router-view v-slot="{ Component }" v-show="!isLoading">
    <transition name="fade" mode="out-in">
      <component :is="Component"/>
    </transition>
  </router-view>
  <br>
  <br>
</template>

<script>
import navbar from "./components/navBar.vue";
import {computed, onMounted, ref} from "vue";
import {useStore} from "vuex";

export default {
  components: {
    navbar,
  },
  setup() {
    const store = useStore();
    const nav = ref(null);
    let blankHeight = ref('137px');

    const saveState = () => {
      localStorage.setItem('state', JSON.stringify(store.state))
    }
    let isLoading = computed(() => {
      return store.state.isLoading;
    })
    onMounted(() => {
      window.addEventListener('unload', saveState)
      window.onresize = function () {
        blankHeight.value = nav.value.$el.scrollHeight + 20 + 'px';
      }
    })
    return{
      isLoading,
      nav,
      blankHeight
    }
  },
}

</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

* {
  user-select: none
}

body {
  background-color: #f8f9fa;
  min-width: 425px;
}

header {
  background-color: #ffffff;
}
.header::after {content: "";display: block;clear: both;}

</style>
