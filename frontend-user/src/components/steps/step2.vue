<template>

  <div class="col-12">
    <div class="form-floating">
      <select class="form-select" id="floorSelector" v-model="selectedFloor">
        <option value="1">The First Floor</option>
        <option value="2">The Second Floor</option>
        <option value="3">The Third Floor</option>
      </select>
      <label for="floorSelector">Select your floor</label>
    </div>
    <br>
    <br>
    <div class="form-floating" v-show="selectedFloor !== 0">
      <select class="form-select" id="floorSelector" v-model="selectedPosition">
        <option v-for="i in floorPositions" :value="i">{{ i }}</option>
      </select>
      <label for="floorSelector">Select your favorable place</label>
    </div>
    <br>
    <br>
    <div class="col-12">
      <button type="submit" class="btn btn-primary" @click="next">Next</button>
    </div>
  </div>
</template>

<script>
import {useStore} from "vuex";
import {ref, watch} from "vue";
import {useRouter} from "vue-router";

export default {
  name: "step2",
  props:{
    avails:Array,
    nowStep:Array,
    info:Array
  },
  setup(props){
    const store = useStore();
    const router = useRouter();
    store.dispatch("Finished");
    console.log(props.avails)
    props.nowStep[0] = 1;
    if(props.info.length < 3)
      router.push("/index/step1");
    props.info.length = 3;
    let selectedFloor = ref(0);
    let floorPositions = ref([]);
    let selectedPosition = ref(0);
    watch(selectedFloor,(newValue,oldValue)=>{
      console.log(oldValue,newValue)
      floorPositions.value.length = 0;
      for(let i = 0; i < avails.length; i++){
        if(avails[i] < 36 * (newValue - 1))
          continue;
        else if(avails[i] > 36 * newValue)
          break;
        else{
          floorPositions.value.push(avails[i]);
        }
      }
      console.log(floorPositions.value);
    });

    const next = function () {
      props.info.push(selectedFloor.value)
      props.info.push(selectedPosition.value)
      router.push("/index/step3")
    }

    let avails = props.avails;
    return{
      avails,
      selectedFloor,
      floorPositions,
      selectedPosition,
      next
    }
  }
}
</script>

<style scoped>

</style>