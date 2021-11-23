<template>
  <div>
    <div class="alert alert-danger" role="alert" v-show="wrongInput">
      [ERROR] Please Input right time interval and Car Number !
    </div>
    <div class="alert alert-danger" role="alert" v-show="wrongSys">
      [ERROR] No place for parking here !
    </div>
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
        <h1>{{ steps[0] }}</h1>
        <div class="row">
          <div class="col-12">
            <label for="car-number" class="form-label">Car Number</label>
            <input type="text" class="form-control" id="car-number" placeholder="AB1234" v-model="license">
          </div>
        </div>
        <div class="row ">
          <div class="col-6">
            <label for="start-date" class="form-label">Start Date</label>
            <div><input type="date" id="start-date" v-model="startDate" :min="today"></div>
          </div>
          <div class="col-6">
            <label for="start-time" class="form-label">Start Time</label>
            <div><input type="time" id="start-time" v-model="startTime" :min="time"></div>
          </div>
        </div>
        <div class="row">
          <div class="col-6">
            <label for="end-date" class="form-label">End Date</label>
            <div><input type="date" id="end-date" v-model="endDate" :min="startDate"></div>
          </div>
          <div class="col-6">
            <label for="end-time" class="form-label">End Time</label>
            <div><input type="time" id="end-time" v-model="endTime" :min="time"></div>
          </div>
        </div>
        <br>
        <div class="col-12">
          <button type="submit" class="btn btn-primary" @click="submit">Next</button>
        </div>
      </div>
    </div>
  </div>
  <router-view v-slot="{ Component }" v-show="!isLoading">
    <transition name="fade" mode="out-in">
      <component :is="Component"/>
    </transition>
  </router-view>

</template>

<script>
import { useStore } from "vuex";
import {computed, ref} from "vue";
import axios from "axios";
import { useRouter } from "vue-router";

export default {
  name: "index",
  setup(){
    const steps = [
      "Select your Car Number and Parking Time.",
      "Select your favorable place.",
      "Confirm all info."
    ]
    const date = new Date();

    let Y = date.getFullYear();
    let M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1);
    let D = (date.getDate() < 10 ? '0'+(date.getDate()) : date.getDate());
    let h = (date.getHours() < 10 ? '0'+ date.getHours() : date.getHours());
    let hp1 = ((date.getHours() + 1) < 10 ? '0'+(date.getHours() + 1) : date.getHours() + 1);
    let m = (date.getMinutes() < 10 ? '0'+(date.getMinutes()) : date.getMinutes());
    let hp2 = ((date.getHours() + 2) < 10 ? '0'+(date.getHours() + 2) : date.getHours() + 2);
    let isLoading = computed(() => {
      return store.state.isLoading;
    })
    let today = Y + "-" + M + "-" + D;
    let time = h + ":" + m;
    let time1 = hp1 + ":" + m;
    let time2 = hp2 + ":" + m;
    let startDate = ref(today);
    let endDate = ref(today);
    let startTime = ref(time1);
    let endTime = ref(time2);
    let wrongInput = ref(false);
    let wrongSys = ref(false);
    let avails = [];
    let mode = "day";
    let startTimestamp = "";
    let endTimestamp = "";
    let license = ref("");
    const store = useStore();
    const router = useRouter();
    const submit = ()=>{
      startTimestamp = Date.parse(startDate.value + " " + startTime.value + ":00");
      endTimestamp = Date.parse(endDate.value + " " + endTime.value + ":00");
      if(startTimestamp == null || endTimestamp == null || startTimestamp >= endTimestamp || startTimestamp < Date.parse(date.getTime()) || license.value === ""){
        wrongInput.value = true;
        return;
      }
      store.dispatch("Load");
      axios({
        method: "GET",
        url: "/management/order/" + mode + "/space/" + startTimestamp + "/" + endTimestamp,
        headers: {'token': localStorage.getItem("token")},
      }).then((res)=>{
        avails = res.data['availableSpaceIdList'];
        console.log(res);
        if(avails.length === 0 ){
          wrongSys.value = true;
          store.dispatch("Finished")
        }
      }).then(()=>{
        axios({
          method:"POST",
          url:"/management/order/" + mode + "/" + avails[0] + "/" + startTimestamp + "/" + endTimestamp + "/" + license.value,
          headers: {'token': localStorage.getItem("token")},
        }).then((res)=>{
          router.push("/mine");
          store.dispatch("Finished");
        }).catch((err)=>{
          wrongSys.value = true;
          store.dispatch("Finished");
        })
      });

    }
    store.dispatch("Finished")
    return{
      startDate,
      endDate,
      startTime,
      endTime,
      today,
      time,
      license,
      wrongInput,
      wrongSys,
      steps,
      isLoading,
      submit
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