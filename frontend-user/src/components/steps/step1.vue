<template>
  <div>
  <div class="alert alert-danger" role="alert" v-show="wrongInput">
    [ERROR] Please Input right time interval and Car Number !
  </div>

  <div class="row">
    <div class="col-12">
      <label for="car-number" class="form-label">Car Number</label>
      <input type="text" class="form-control" id="car-number" placeholder="Your Car Number Here. eg. AE86" v-model="license">
    </div>
  </div>
  <br>
  <div class="row ">
    <div class="col-6">
      <label for="start-date" class="form-label">Start Date</label>
      <div><input type="date" id="start-date" v-model="startDate"></div>
    </div>
    <div class="col-6">
      <label for="start-time" class="form-label">Start Time</label>
      <div><input type="time" id="start-time" v-model="startTime"></div>
    </div>
  </div>
  <br>
  <div class="row">
    <div class="col-6">
      <label for="end-date" class="form-label">End Date</label>
      <div><input type="date" id="end-date" v-model="endDate"></div>
    </div>
    <div class="col-6">
      <label for="end-time" class="form-label">End Time</label>
      <div><input type="time" id="end-time" v-model="endTime"></div>
    </div>
  </div>
  <br>
  <div class="col-12">
    <button type="submit" class="btn btn-primary" @click="submit">Next</button>
  </div>
  </div>
</template>

<script>
import {ref} from "vue";
import {useStore} from "vuex";
import {useRouter} from "vue-router";
import axios from "axios";

export default {
  name: "step1",
  props: {
    nowStep:Array,
    avails:Array,
    info:Array
  },
  setup(props){
    const store = useStore();
    const router = useRouter();
    props.nowStep[0] = 0;
    props.info.length = 0;
    let date = new Date();
    date = new  Date(date.getTime() + 1000 * 60 * 60);
    let nowStamp = date.getTime();
    nowStamp += 1000 * 60 * 60;
    let newDate = new Date(nowStamp);
    const addPreZero = function (time) {
      if(time.toString().length === 1) return "0" + time;
      else return time;
    }
    let startDate = ref(date.getFullYear() + "-" + addPreZero(date.getMonth() + 1) + "-" + addPreZero(date.getDate()));
    let endDate = ref(newDate.getFullYear() + "-" + addPreZero(newDate.getMonth() + 1) + "-" + addPreZero(newDate.getDate()));
    let startTime = ref( addPreZero(date.getHours()) + ":" + addPreZero(date.getMinutes()));
    let endTime = ref(addPreZero(newDate.getHours()) + ":" + addPreZero(newDate.getMinutes()));

    let wrongInput = ref(false);
    let wrongSys = ref(false);

    let avails = props.avails;
    let mode = "day";
    let startTimestamp = "";
    let endTimestamp = "";
    let license = ref("");
    const submit = ()=>{
      startTimestamp = Date.parse(startDate.value + " " + startTime.value + ":00");
      endTimestamp = Date.parse(endDate.value + " " + endTime.value + ":00");
      if(startTimestamp == null || endTimestamp == null || startTimestamp >= endTimestamp || license.value === ""){
        wrongInput.value = true;
        return;
      }
      store.dispatch("Load");
      props.info.push(startTimestamp);
      props.info.push(endTimestamp);
      props.info.push(license.value);
      axios({
        method: "GET",
        url: "/management/order/" + mode + "/space/" + startTimestamp + "/" + endTimestamp,
        headers: {'token': localStorage.getItem("token")},
      }).then((res)=>{
        for(let i = 0; i < res.data['availableSpaceIdList'].length; i++){
            avails.push(res.data['availableSpaceIdList'][i]);
        }
        if(res.data['availableSpaceIdList'].length === 0 ){
          wrongSys.value = true;
          store.dispatch("Finished")
        }
        else router.push("/index/step2");
      })
    }
    store.dispatch("Finished")
    return{
      startDate,
      endDate,
      startTime,
      endTime,
      license,
      wrongInput,
      wrongSys,
      submit
    }
  }
}
</script>

<style scoped>

</style>