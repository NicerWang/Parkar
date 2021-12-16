<template>
  <div class="container">
    <div class="row justify-content-evenly">
      <div class="card col-5">
        <br>
        <h2 class="card-title">User Infos</h2>
        <div class="card-body">
          <button class="btn btn-primary" @click="jmp(1)">Check</button>
        </div>
      </div>
      <div class="card col-5">
        <br>
        <h2 class="card-title">Order Infos</h2>
        <div class="card-body">
          <button class="btn btn-primary" @click="jmp(2)">Check</button>
        </div>
      </div>
    </div>
    <div class="row justify-content-evenly">
      <div class="card col-5">
        <br>
        <h2 class="card-title">Space Infos</h2>
        <div class="card-body">
          <button class="btn btn-primary" @click="jmp(3)">Check</button>
        </div>
      </div>
      <div class="card col-5">
        <br>
        <h2 class="card-title">Data Analysis</h2>
        <div class="card-body">
          <button class="btn btn-primary" @click="jmp(4)">Check</button>
        </div>
      </div>
    </div>
    <div class="position-fixed bottom-0 end-0 p-3" style="z-index: 11;display: block">
      <div id="liveToast" class="toast show" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="toast-header">
          <img src="../assets/logo.ico" class="rounded me-2" width="24" height="24" alt="icon"/>
          <strong class="me-auto">Developer</strong>
          <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
        <div class="toast-body">
          Hello, Enjoy The Parkar.
        </div>

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
        router.push("/info/users/all");
      }
      if(id === 2){
        router.push("/info/orders");
      }
      if(id === 3){
        router.push("/info/spaces");
      }
      if(id === 4){
        router.push("/info/data");
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