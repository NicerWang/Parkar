<template>
  <div>

    <br>
    <div class="card container align-items-baseline">
      <br>
      <h1>&nbsp; Reserve</h1>
      <br>
    </div>
    <br>
    <div class="card container card-form align-items-center">
      <div class="card-body container justify-content-center">
        <ul class="nav justify-content-center">
          <li class="nav-item">
            <a :class="{'nav-link':true,'active':nowStep[0] === 0}" href="/index/step1"><h2>Step 1</h2></a>
          </li>
          <li class="nav-item">
            <a :class="{'nav-link':true,'active':nowStep[0] === 1}" href="/index/step2"><h2>Step 2</h2></a>
          </li>
          <li class="nav-item">
            <a :class="{'nav-link':true,'active':nowStep[0] === 2}" href="/index/step3"><h2>Step 3</h2></a>
          </li>
        </ul>
        <br>
        <div class="row">
          <div class="col-1"></div>
          <div class="col-10">
            <h1>{{ steps[nowStep[0]] }}</h1>
            <br>
            <div class="alert alert-danger" role="alert" v-show="message[0]">
              {{ message[1] }}
            </div>
          </div>
        </div>

        <br>
        <div class="row">
        <div class="col-2"></div>
        <div class="col-8">
          <router-view v-slot="{ Component }" >
            <transition name="fade" mode="out-in" >
              <component :is="Component" :avails="avails" :now-step="nowStep" :info="info" :message="message" />
            </transition>
          </router-view>
        </div>
          </div>

      </div>
    </div>
  </div>


</template>

<script>
import { useStore } from "vuex";

import { useRouter } from "vue-router";
import { ref } from "vue";

export default {
  name: "index",
  setup(){
    const steps = [
      "Input your Car Number and Parking Time.",
      "Select your favorable place.",
      "Confirm all info.",
      "All Done."
    ]
    let avails = ref([]);
    let nowStep = ref([0]);
    let info = ref([]);
    let message = ref([false,""]);

    const store = useStore();
    const router = useRouter();

    return{
      steps,
      avails,
      nowStep,
      info,
      message
    }
  },
}
</script>

<style scoped>
.card-form{
  padding: 50px;
}
.form-check{
  margin: 50px;
}
label{
  font-size: 18px;
}
.row {
  margin: 20px;
}
a.active h2{
  font-size: 45px !important;
}

</style>