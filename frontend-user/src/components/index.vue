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
        <div class="form-check col-12">
          <input class="form-check-input" type="checkbox" value="" id="flexCheckChecked" disabled checked>
          <label class="form-check-label" for="flexCheckChecked">
            [ONLY] Random select a position
          </label>
        </div>
        <div class="col-12">
          <button type="submit" class="btn btn-primary" @click="submit">Confirm</button>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
import { useStore } from "vuex";
import {ref} from "vue";
import axios from "axios";
import { useRouter } from "vue-router";

export default {
  name: "index",
  setup(){
    const now = new Date();
    let today = now.getFullYear() + "-" + (now.getMonth() + 1 < 10 ? "0" + (now.getMonth() + 1) : now.getMonth() + 1) + "-" + (now.getDay() + 1 < 10 ? "0" + (now.getDay()) : now.getDay());
    let time = now.getHours() + 1 + ":" + now.getMinutes();
    let timeAfter1 = now.getHours() + 1 + ":" + now.getMinutes();
    let startDate = ref(today);
    let endDate = ref(today);
    let startTime = ref(time);
    let endTime = ref(timeAfter1);
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
      if(startTimestamp == null || endTimestamp == null || startTimestamp >= endTimestamp || startTimestamp < Date.parse(now.getTime()) || license.value === ""){
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

</style>