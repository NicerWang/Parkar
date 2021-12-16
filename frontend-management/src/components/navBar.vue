<template>
    <header class="d-flex flex-wrap justify-content-center py-3  border-bottom">
      <a href="/" class="d-flex align-items-center mb-0  me-auto text-dark text-decoration-none">
        &nbsp;&nbsp;&nbsp;
        <img src="../assets/logo.jpg" alt="" width="40" height="40" style="border-radius: 5px">
        <span class="fs-4">&nbsp;Parkar For Management</span>
      </a>
      <ul class="nav nav-pills">
        <li class="nav-item" v-for="(item,idx) in items_before"><router-link :to="item.to" :class="item.cls">{{ item.msg }}</router-link></li>

        <li class="nav-item"><button type="button" class="btn btn-danger" @click="logout" v-show="status">Logout</button></li>
        &nbsp;
      </ul>
    </header>
</template>

<script>

import {ref, reactive, computed} from 'vue'
import {useStore} from 'vuex'
import {useRouter} from "vue-router";
import axios from "axios";

export default {
  name: "navbar",
  setup(){
    const store = useStore();
    const router = useRouter();
    const activated = ["nav-link","active"]
    const common = ["nav-link"]
    let status = computed(()=>{
      return store.state.isSignedIn;
    })
    let items_before = ref([
      reactive({to:"/login",msg:"Sign in",cls: activated}),
    ])
    if(status.value === true){
      items_before = ref([
        reactive({to:"/index",msg:"Home",cls: activated}),
        reactive({to:"/price",msg:"Price",cls: common}),
        reactive({to:"/info/users/all",msg:"User",cls: common}),
        reactive({to:"/info/orders",msg:"Order",cls: common}),
        reactive({to:"/info/spaces",msg:"Space",cls: common}),
        reactive({to:"/info/data",msg:"Analyze",cls: common}),
      ])
    }
    const logout = ()=>{
      localStorage.removeItem("token")
      store.dispatch("LogOut");
      store.dispatch("Finished")
      router.push("/login");
    }
    return{
      items_before,
      activated,
      common,
      status,
      logout
    }
  },
  watch:{
    $route(to,from){
      for(let i = 0; i < this.items_before.length; i++){
        this.items_before[i].cls = this.common;
        if(to.fullPath.search(this.items_before[i].to) !== -1){
          this.items_before[i].cls = this.activated;
        }
      }
    },
    "$store.state.isSignedIn"(newVal,oldVal){
      if(newVal === oldVal){}
      else if(newVal){
        this.items_before = ref([
          reactive({to:"/index",msg:"Home",cls: this.activated}),
          reactive({to:"/price",msg:"Price",cls: this.common}),
          reactive({to:"/info/users/all",msg:"User",cls: this.common}),
          reactive({to:"/info/orders",msg:"Order",cls: this.common}),
          reactive({to:"/info/spaces",msg:"Space",cls: this.common}),
          reactive({to:"/info/data",msg:"Analyze",cls: this.common}),
        ])
      }else{
        this.items_before = ref([
          reactive({to:"/login",msg:"Sign in",cls: this.activated}),
        ])
      }
    }
  },
}
</script>
<style scoped>
li{
  height: 40px;
  margin: 2px;
}
button{
  height: 40px;
}

</style>