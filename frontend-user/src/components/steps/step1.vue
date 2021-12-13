<template>
  <div>
    <div class="row justify-content-evenly">
      <div class="col-12 col-lg-6">
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
      <div class="col-12 col-sm-6 col-lg-4">
        <label class="form-label" for="start-date">Start Date</label>
        <div><input id="start-date" v-model="startDate" type="date"></div>
      </div>
      <div class="col-12 col-sm-6 col-lg-4">
        <label class="form-label">Start Time</label>
        <div class="input-group">
          <select  class="form-control" v-model="startOffset">
            <option value="0">上午</option>
            <option value="12">下午</option>
          </select>
          <select  class="form-control" v-model="startHour">
            <option value="0">12</option>
            <option value="1">1</option>
            <option value="2">2</option>
            <option value="3">3</option>
            <option value="4">4</option>
            <option value="5">5</option>
            <option value="6">6</option>
            <option value="7">7</option>
            <option value="8">8</option>
            <option value="9">9</option>
            <option value="10">10</option>
            <option value="11">11</option>
          </select>
          <span class="input-group-text colon">:</span>
          <select  class="form-control" v-model="startMinute">
            <option value="00">00</option>
            <option value="30">30</option>
          </select>
        </div>
      </div>
    </div>
    <br>
    <div class="row justify-content-evenly">
      <div class="col-12 col-sm-6 col-lg-4">
        <label class="form-label" for="end-date">End Date</label>
        <div><input id="end-date" v-model="endDate" type="date"></div>
      </div>
      <div class="col-12 col-sm-6 col-lg-4">
        <label class="form-label">End Time</label>
        <div class="input-group">
          <select  class="form-control" v-model="endOffset">
            <option value="0">上午</option>
            <option value="12">下午</option>
          </select>
          <select  class="form-control" v-model="endHour">
            <option value="0">12</option>
            <option value="1">1</option>
            <option value="2">2</option>
            <option value="3">3</option>
            <option value="4">4</option>
            <option value="5">5</option>
            <option value="6">6</option>
            <option value="7">7</option>
            <option value="8">8</option>
            <option value="9">9</option>
            <option value="10">10</option>
            <option value="11">11</option>
          </select>
          <span class="input-group-text colon">:</span>
          <select  class="form-control" v-model="endMinute">
            <option value="00">00</option>
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

    date = new Date(Math.ceil((date.getTime() + 1000 * 30 * 60) / 1800000) * 1800000);
    let nowStamp = date.getTime();
    nowStamp += 1000 * 60 * 60;
    let newDate = new Date(nowStamp);
    const addPreZero = function (time) {
      if (time.toString().length === 1) return "0" + time;
      else return time;
    }

    let startDate = ref(date.getFullYear() + "-" + addPreZero(date.getMonth() + 1) + "-" + addPreZero(date.getDate()));
    let endDate = ref(newDate.getFullYear() + "-" + addPreZero(newDate.getMonth() + 1) + "-" + addPreZero(newDate.getDate()));
    let startOffset = ref(0)
    let startHour = ref(date.getHours());
    let endOffset = ref(0)
    let endHour = ref(newDate.getHours());
    if(startHour.value >= 12){
      startHour.value -= 12;
      startOffset.value = 12;
    }
    if(endHour.value >= 12){
      endHour.value -= 12;
      endOffset.value = 12;
    }
    let startMinute = ref(addPreZero(date.getMinutes()));
    let endMinute = ref(addPreZero(newDate.getMinutes()));
    let avails = props.avails;
    let startTimestamp = "";
    let endTimestamp = "";
    let license = ref("");
    let all_cars = ref([])
    const submit = () => {
      document.body.scrollTop = document.documentElement.scrollTop = 0;
      startTimestamp = Date.parse(startDate.value.replaceAll("-","/") + " " + addPreZero(Number(startOffset.value) + Number(startHour.value)) + ":" + startMinute.value + ":00");
      endTimestamp = Date.parse(endDate.value.replaceAll("-","/") + " " + addPreZero(Number(endOffset.value) + Number(endHour.value)) + ":" + endMinute.value + ":00");
      if (startTimestamp == null || endTimestamp == null || license.value === "") {
        props.message[1] = "[ERROR]Need Car Number and Time"
        props.message[0] = true
        return;
      }
      if ( startTimestamp < new Date((new Date).getTime() + 1800000)) {
        props.message[1] = "[ERROR]Reserve time must start at least 30 minutes later"
        props.message[0] = true
        return;
      }
      if (startTimestamp >= endTimestamp) {
        props.message[1] = "[ERROR]Start Time should not be earlier than end time"
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
      startOffset,
      endHour,
      endOffset,
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
  width: 100%;
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