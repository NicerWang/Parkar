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
      <div class="card-body">
        <ul class="nav justify-content-center">
          <li class="nav-item">
            <a class="nav-link active" aria-current="page"><h2>Step 1</h2></a>
          </li>
          <li class="nav-item">
            <a class="nav-link disabled"><h4>Step 2</h4></a>
          </li>
          <li class="nav-item">
            <a class="nav-link disabled"><h4>Step 3</h4></a>
          </li>
        </ul>
        <br>
        <h1>{{ steps[nowStep] }}</h1>
        <router-view v-slot="{ Component }" >
          <transition name="fade" mode="out-in" >
            <component :is="Component" :avails="avails" />
          </transition>
        </router-view>
      </div>
    </div>
  </div>


</template>

<script>
import { useStore } from "vuex";

import { useRouter } from "vue-router";
import {computed, ref} from "vue";

export default {
  name: "index",
  setup(){
    const steps = [
      "Select your Car Number and Parking Time.",
      "Select your favorable place.",
      "Confirm all info."
    ]
    let avails = ref([999]);
    let nowStep = 1;
    const store = useStore();
    const router = useRouter();

    return{
      steps,
      avails,
      nowStep,
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
.row{
  margin: 20px;
}
input {
  border: 1px solid #ced4da;
  border-radius: .25rem;
  padding: .375rem .75rem;
  transition: border-color .15s ease-in-out, box-shadow .15s ease-in-out;
  color: #212529;
}
input:focus{
  color: #212529;
  background-color: #fff;
  border-color: #86b7fe;
  outline: 0;
  box-shadow: 0 0 0 .25rem rgba(13,110,253,.25);
}
</style>