<template>
  <div>
    <div class="col-12">
      <div class="form-check">
        <input id="autoSelect" v-model="autoSelect" class="form-check-input" type="checkbox">
        <label class="form-check-label" for="autoSelect" style="font-size: 20px">
          [Recommend] Select the optimized place for me
        </label>
      </div>
    </div>
    <br>
    <br>
    <div class="col-12">
      <div v-show="!autoSelect" class="form-floating">
        <select id="floorSelector" v-model="selectedFloor" class="form-select">
          <option value="1">The First Floor</option>
          <option value="2">The Second Floor</option>
          <option value="3">The Third Floor</option>
        </select>
        <label for="floorSelector">Select your floor</label>
      </div>
      <br>
      <br>
      <div v-show="selectedFloor !== 0 && !autoSelect" class="form-floating">
        <select id="floorSelector" v-model="selectedPosition" class="form-select">
          <option v-for="i in floorPositions" :value="i">{{ i }}</option>
        </select>
        <label for="floorSelector">Select your favorable place</label>
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

export default {
  name: "step2",
  props: {
    avails: Array,
    nowStep: Array,
    info: Array,
    message: Array
  },
  setup(props) {
    const store = useStore();
    const router = useRouter();
    store.dispatch("Finished");
    props.nowStep[0] = 1;
    props.message[0] = false
    if (props.info.length < 3)
      router.push("/index/step1");
    props.info.length = 3;
    let selectedFloor = ref(0);
    let floorPositions = ref([]);
    let selectedPosition = ref(0);
    let autoSelect = ref(false);
    watch(selectedFloor, (newValue, oldValue) => {
      console.log(oldValue, newValue)
      floorPositions.value.length = 0;
      for (let i = 0; i < avails.length; i++) {
        if (avails[i] < 36 * (newValue - 1))
          continue;
        else if (avails[i] > 36 * newValue)
          break;
        else {
          floorPositions.value.push(avails[i]);
        }
      }
      console.log(floorPositions.value);
    });

    const next = function () {
      if (autoSelect) {
        selectedPosition.value = avails[Math.ceil(Math.random() * avails.length)];
        if (selectedPosition.value <= 36) selectedFloor.value = 1;
        else if (selectedPosition.value > 72) selectedFloor.value = 3;
        else selectedFloor.value = 2;
      }
      if (selectedFloor.value === 0) {
        console.log(props.message)
        props.message[1] = "[ERROR] Empty Floor Selection"
        props.message[0] = true
        return
      } else if (selectedPosition.value === 0) {
        console.log(props.message)
        props.message[1] = "[ERROR] Empty Position Selection"
        props.message[0] = true
        return
      }
      props.info.push(selectedFloor.value)
      props.info.push(selectedPosition.value)
      router.push("/index/step3")
    }

    let avails = props.avails;
    return {
      avails,
      selectedFloor,
      floorPositions,
      selectedPosition,
      autoSelect,
      next
    }
  }
}
</script>

<style scoped>
button {
  width: 50%;
}

</style>