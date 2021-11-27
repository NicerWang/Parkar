<template>
  <navbar></navbar>
  <br>
  <br>
  <br>
  <br>
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
import {computed, onMounted} from "vue";
import {useStore} from "vuex";

export default {
  components: {
    navbar,
  },
  setup() {
    const store = useStore();
    const saveState = () => {
      sessionStorage.setItem('state', JSON.stringify(store.state))
    }
    let isLoading = computed(() => {
      return store.state.isLoading;
    })
    onMounted(() => window.addEventListener('unload', saveState))
    return{
      isLoading,
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
}

header {
  background-color: #ffffff;
}


</style>
