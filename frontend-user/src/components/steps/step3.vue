<template>
  <div>
    <div v-show="!ok" class="info">
      <div class="input-group mb-3">
        <span class="input-group-text">Start Time</span>
        <input v-model="startTime" class="form-control" disabled type="text">
      </div>
      <div class="input-group mb-3">
        <span class="input-group-text">End Time</span>
        <input v-model="endTime" class="form-control" disabled type="text">
      </div>
      <div class="input-group mb-3">
        <span class="input-group-text">Car Number</span>
        <input v-model="info[2]" class="form-control" disabled type="text">
      </div>
      <div class="input-group mb-3">
        <span class="input-group-text">Floor</span>
        <input v-model="info[3]" class="form-control" disabled type="text">
      </div>
      <div class="input-group mb-3">
        <span class="input-group-text">Position ID</span>
        <input v-model="info[4]" class="form-control" disabled type="text">
      </div>
      <div class="input-group mb-3">
        <span class="input-group-text">Price</span>
        <input v-model="price" class="form-control" disabled type="text">
      </div>
      <br>
      <br>
      <div class="col-12">
        <button class="btn btn-primary" type="submit" @click="confirm">Confirm</button>
      </div>
    </div>
    <div v-show="ok" class="checked">
      <img alt="" class="mb-4" height="200" src="../../assets/check.svg" width="200">
      <br>
      <br>
      <br>
      <div class="col-12">
        <button class="btn btn-primary" type="submit" @click="gotoMine">Go Back to Mine</button>
      </div>
    </div>

  </div>
</template>

<script>
import {useStore} from "vuex";
import axios from "axios";
import {useRouter} from "vue-router";
import {ref} from "vue";

export default {
  name: "step3",
  props: {
    info: Array,
    nowStep: Array,
    message: Array
  },
  setup(props) {
    const store = useStore();
    const router = useRouter();
    let info = props.info;
    let ok = ref(false);
    let wrongSys = ref(false);
    let startTime = new Date(info[0]).toString().slice(0, 21);
    let endTime = new Date(info[1]).toString().slice(0, 21);
    props.nowStep[0] = 2;
    props.message[0] = false
    if (props.info.length < 5)
      router.push("/index/step2");
    let price = ref(0)

    axios({
      method: "GET",
      url: "/management/order/price",
      params:{
        mode:0,
        startTime: info[0],
        endTime: info[1],
      }
    }).then((res)=>{
      price.value = res.data.toFixed(2);
    })

    const confirm = () => {
      document.body.scrollTop = document.documentElement.scrollTop = 0;
      axios({
        method: "POST",
        url: "/management/order/submit",
        params:{
          mode: 0,
          spaceId: info[4],
          startTime: info[0],
          endTime: info[1],
          licenseNumber: info[2],
        },
        headers: {'token': localStorage.getItem("token")},
      }).then((res) => {
        ok.value = true;
        props.nowStep[0] = 3;
        store.dispatch("Finished");
      }).catch((err) => {
        props.message[1] = "[ERROR]" + err.response.data.message[0];
        props.message[0] = true
        store.dispatch("Finished");
      })
    }
    const gotoMine = function () {
      router.push("/mine");
    }

    store.dispatch("Finished");
    return {
      info,
      wrongSys,
      confirm,
      gotoMine,
      startTime,
      endTime,
      ok,
      price
    }
  }
}
</script>

<style scoped>
button {
  width: 50%;
}
</style>