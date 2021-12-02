<template>
  <div>
    <div class="row">
      <div class="col-12">
        <label class="form-label" for="car-number">Car Number</label>
        <input id="car-number" v-model="license" class="form-control" list="datalistOptions"
               placeholder="Your Car Number Here. eg. AE86">
        <datalist id="datalistOptions">
          <option v-for="i in all_cars" :value="i"/>
        </datalist>

      </div>
    </div>
    <br>
    <div class="row justify-content-evenly">
      <div class="col-3">
        <label class="form-label" for="start-date">Start Date</label>
        <div><input id="start-date" v-model="startDate" type="date"></div>
      </div>
      <div class="col-3">
        <label class="form-label" for="start-date">Start Time</label>
        <div class="input-group">
          <input type="number" class="form-control" min="0" max="23" v-model="startHour">
          <span class="input-group-text colon">:</span>
          <select  class="form-control" v-model="startMinute">
            <option value="0">00</option>
            <option value="30">30</option>
          </select>
        </div>
      </div>
    </div>
    <br>
    <div class="row justify-content-evenly">
      <div class="col-3">
        <label class="form-label" for="end-date">End Date</label>
        <div><input id="end-date" v-model="endDate" type="date"></div>
      </div>
      <div class="col-3">
        <label class="form-label" for="start-date">End Time</label>
        <div class="input-group mb-3">
          <input type="number" class="form-control" min="0" max="23" v-model="endHour">
          <span class="input-group-text colon">:</span>
          <select  class="form-control" v-model="endMinute">
            <option value="0">00</option>
            <option value="30">30</option>
          </select>
        </div>
      </div>
    </div>
    <br>
    <br>
    <div class="col-12">
      <button class="btn btn-primary" type="submit" @click="submit">Next</button>
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
    nowStep: Array,
    avails: Array,
    info: Array,
    message: Array
  },
  setup(props) {
    const store = useStore();
    const router = useRouter();
    props.nowStep[0] = 0;
    props.info.length = 0;
    props.message[0] = false
    let date = new Date();
    date = new Date(date.getTime() + 1000 * 60 * 60);
    let nowStamp = date.getTime();
    nowStamp += 1000 * 60 * 60;
    let newDate = new Date(nowStamp);
    const addPreZero = function (time) {
      if (time.toString().length === 1) return "0" + time;
      else return time;
    }
    let startDate = ref(date.getFullYear() + "-" + addPreZero(date.getMonth() + 1) + "-" + addPreZero(date.getDate()));
    let endDate = ref(newDate.getFullYear() + "-" + addPreZero(newDate.getMonth() + 1) + "-" + addPreZero(newDate.getDate()));
    let startHour = ref(addPreZero(date.getHours()));
    let endHour = ref(addPreZero(newDate.getHours()));
    let startMinute = ref(0);
    let endMinute = ref(0);

    let avails = props.avails;
    let startTimestamp = "";
    let endTimestamp = "";
    let license = ref("");
    let all_cars = ref([])
    const submit = () => {
      startTimestamp = Date.parse(startDate.value.replaceAll("-","/") + " " + startHour.value + ":" + startMinute.value + ":00");
      endTimestamp = Date.parse(endDate.value.replaceAll("-","/") + " " + endHour.value + ":" + endMinute.value + ":00");
      if (startTimestamp == null || endTimestamp == null || startTimestamp >= endTimestamp || license.value === "") {
        props.message[1] = "[ERROR]Need Car Number"
        props.message[0] = true
        return;
      }
      store.dispatch("Load");
      props.info.push(startTimestamp);
      props.info.push(endTimestamp);
      props.info.push(license.value);
      axios({
        method: "GET",
        url: "/management/order/space",
        params:{
          startTime: startTimestamp,
          endTime: endTimestamp,
        },
        headers: {'token': localStorage.getItem("token")},
      }).then((res) => {
        for (let i = 0; i < res.data['availableSpaceIdList'].length; i++) {
          avails.push(res.data['availableSpaceIdList'][i]);
        }
        if (res.data['availableSpaceIdList'].length === 0) {
          props.message[1] = "[ERROR] No Parking Spaces here."
          props.message[0] = true;
          store.dispatch("Finished")
        } else router.push("/index/step2")
      }).catch((err) => {
        props.message[1] = "[ERROR] Request Failed, Please Try Again."
        props.message[0] = true;
        router.push("/index")
      })
    }
    axios({
      method: "GET",
      url: "user/getAllVehicleId",
      headers: {'token': localStorage.getItem("token")},
    }).then((res) => {
      all_cars.value = res.data.data["allVehicleIds"]
      store.dispatch("Finished")
    })
    store.dispatch("Finished")
    return {
      startDate,
      endDate,
      startHour,
      endHour,
      startMinute,
      endMinute,
      license,
      all_cars,
      submit
    }
  }
}
</script>

<style scoped>
button {
  width: 50%;
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
.colon{
  font-weight: 900;
}
</style>