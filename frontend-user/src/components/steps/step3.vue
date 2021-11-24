<template>
  <div>
    <div class="alert alert-danger" role="alert" v-show="wrongSys">
      [ERROR] No place for parking here !
    </div>
    <div class="input-group mb-3">
      <span class="input-group-text">Start Time</span>
      <input type="text" class="form-control" v-model="info[0]" disabled>
    </div>
    <div class="input-group mb-3">
      <span class="input-group-text">End Time</span>
      <input type="text" class="form-control" v-model="info[1]" disabled>
    </div>
    <div class="input-group mb-3">
      <span class="input-group-text">Car Number</span>
      <input type="text" class="form-control" v-model="info[2]" disabled>
    </div>
    <div class="input-group mb-3">
      <span class="input-group-text">Floor</span>
      <input type="text" class="form-control" v-model="info[3]" disabled>
    </div>
    <div class="input-group mb-3">
      <span class="input-group-text">Position ID</span>
      <input type="text" class="form-control" v-model="info[4]" disabled>
    </div>
    <div class="col-12">
      <button type="submit" class="btn btn-primary" @click="confirm">Confirm</button>
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
  props:{
    info:Array,
    nowStep:Array
  },
  setup(props){
    const store = useStore();
    const router = useRouter();
    let info = props.info;
    let wrongSys = ref(false);
    props.nowStep[0] = 2;
    if(props.info.length < 5)
      router.push("/index/step2");

    const confirm = ()=>{
      axios({
        method:"POST",
        url:"/management/order/day/" + info[4] + "/" + info[0] + "/" + info[1] + "/" + info[2],
        headers: {'token': localStorage.getItem("token")},
      }).then((res)=>{
        router.push("/mine");
        store.dispatch("Finished");
      }).catch((err)=>{
        wrongSys.value = true;
        store.dispatch("Finished");
      })
    }

    store.dispatch("Finished");
    return {
      info,
      wrongSys,
      confirm
    }
  }
}
</script>

<style scoped>

</style>