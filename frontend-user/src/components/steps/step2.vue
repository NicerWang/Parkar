<template>
  <div>
    <div class="col-12">
      <div class="form-check form-switch">
        <input id="autoSelect" v-model="autoSelect" class="form-check-input" type="checkbox" role="switch">
        <label class="form-check-label" for="autoSelect" style="font-size: 20px">
          [Recommend] Select the optimized place for me
        </label>
      </div>
    </div>
    <br>
    <br>
    <div class="col-12">
      <div v-show="!autoSelect" class="form-floating">
        <select id="floorSelector" v-model="selectedInfo[0]" class="form-select">
          <option value="1">The First Floor</option>
          <option value="2">The Second Floor</option>
          <option value="3">The Third Floor</option>
        </select>
        <label for="floorSelector">Select your floor</label>
      </div>
      <br>
      <br>
      <div v-if="selectedInfo[0] !== 0 && !autoSelect" class="form-floating selector">
        <label>Select your Position</label>
        <br>
        <br>
        <selector :key="selectedInfo[0]" :position="positions" :select="selectedInfo" :avails="avails"></selector>
      </div>

      <br>
      <br>
      <div class="col-12">
        <button class="btn btn-primary" type="submit" @click="next">Next</button>
      </div>
    </div>
  </div>
</template>

<script>
import {useStore} from "vuex";
import {ref, watch} from "vue";
import {useRouter} from "vue-router";
import Selector from "../selector.vue";
import axios from "axios";

export default {
  name: "step2",
  components: {Selector},
  props: {
    avails: Array,
    nowStep: Array,
    info: Array,
    message: Array
  },
  setup(props) {
    const store = useStore();
    const router = useRouter();
    props.nowStep[0] = 1;
    props.message[0] = false
    if (props.info.length < 4)
      router.push("/index/step1");
    props.info.length = 4;
    let selectedInfo = ref([0,0]);
    let autoSelect = ref(false);
    let positions = ref([])

    const next = function () {
      document.body.scrollTop = document.documentElement.scrollTop = 0;
      if (autoSelect.value) {
        axios({
          method: "GET",
          url: "/management/order/space/rec",
          params:{
            startTime: props.info[1],
            endTime: props.info[2],
          },
          headers: {'token': localStorage.getItem("token")},
          async:false
        }).then((res)=>{
          selectedInfo.value[1] = res.data
          for(let i = 0; i < positions.value.length; i++){
            if(positions.value[i]['spaceId'] == selectedInfo.value[1])
              selectedInfo.value[0] = positions.value[i]['floor']
          }
          props.info.push(selectedInfo.value[0])
          props.info.push(selectedInfo.value[1])
          router.push("/index/step3")
        })
      }
      else{
        if (selectedInfo.value[0] === 0) {
          props.message[1] = "[ERROR] Empty Floor Selection"
          props.message[0] = true
          return
        } else if (selectedInfo.value[1] === 0) {
          props.message[1] = "[ERROR] Empty Position Selection"
          props.message[0] = true
          return
        }
        props.info.push(selectedInfo.value[0])
        props.info.push(selectedInfo.value[1])
        router.push("/index/step3")
      }

    }

    axios({
      method: "GET",
      url: "/management/order/space/list",
      headers: {'token': localStorage.getItem("token")},
      async:false
    }).then((res) => {
      positions.value = res.data['spaceList'];
      let ptr = 0;
      for(let i = 0; i < positions.value.length; i++){
        positions.value[i].isSelected = false;
        positions.value[i].isOccupied = true;
        if(props.avails[ptr] === positions.value[i]['spaceId']){
          positions.value[i].isOccupied = false;
          ptr++;
        }
      }
      store.dispatch("Finished");
    }).catch((err) => {
      props.message[1] = "[ERROR]" + err.response.data.message[0];
      props.message[0] = true
      store.dispatch("Finished");
    })

    let avails = props.avails;
    return {
      avails,
      selectedInfo,
      autoSelect,
      positions,
      next
    }
  }
}
</script>

<style scoped>
button {
  width: 50%;
}
.selector{
  border: 1px solid #ced4da;
  border-radius: .25rem;
}
</style>