<template>
  <div class="container">
      <div class="card col-5" style="float: left">
        <br>
        <h2 class="card-title">Users</h2>
        <div class="card-body">
          <button class="btn btn-primary" @click="jmp(1)">Check</button>
        </div>
      </div>
      <div class="card col-5" style="float: right">
        <br>
        <h2 class="card-title">Orders</h2>
        <div class="card-body">
          <button class="btn btn-primary" @click="jmp(2)">Check</button>
        </div>
      </div>
    <div class="card col-5" style="float: left">
      <br>
      <h2 class="card-title">Spaces</h2>
      <div class="card-body">
        <button class="btn btn-primary" @click="jmp(3)">Check</button>
      </div>
    </div>
  </div>

</template>

<script>
import { useStore } from "vuex";
import axios from "axios";
import {useRouter} from "vue-router";

export default {
  name: "index",
  setup(){
    const store = useStore();
    const router = useRouter();
    const jmp = function (id){
      store.dispatch("Load");
      if(id === 1){
        router.push("/info/users");
      }
      if(id === 2){
        router.push("/info/orders");
      }
      if(id === 3){
        router.push("/info/spaces");
      }
    }
    store.dispatch("Finished")
    return{
      jmp
    }
  }
}
</script>

<style scoped>
.card{
  box-sizing: border-box;
  margin-top: 100px;
}
</style>