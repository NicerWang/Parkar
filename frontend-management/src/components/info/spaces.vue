<template>
  <div>
    <br>
    <div class="card container align-items-baseline">
      <br>
      <h1>&nbsp; All spaces</h1>
      <br>
    </div>
    <br>
    <div class="card container">
      <div class="card-body ">
        <div class="form-floating">
          <select id="floorSelector" v-model="selectInfo[0]" class="form-select">
            <option value="1">The First Floor</option>
            <option value="2">The Second Floor</option>
            <option value="3">The Third Floor</option>
          </select>
          <label for="floorSelector">Select the floor</label>
        </div>
        <br>
          <selector v-show="selectInfo[0] !== 0" :key="selectInfo[0]" :select="selectInfo" :position="spaces"></selector>
      </div>
    </div>
  </div>
</template>

<script>
import { useStore } from "vuex";
import axios from "axios";
import {ref} from "vue";
import Selector from "../selector.vue";

export default {
  name: "spaces",
  components: {Selector},
  setup(){
    const store = useStore();
    let spaces = ref([]);
    let selectInfo = ref([0,0])

    const formatMode = (id)=>{
      if(id === 0) return "day"
      if(id === 1) return "month"
      if(id === 2) return "year"
    }

    axios({
      method:"GET",
      url:"management/administrator/parking/space/list",
      headers: {'token': localStorage.getItem("token")},
    }).then((res)=>{
      spaces.value =  res.data["spaceList"].reverse();
      store.dispatch("Finished");
    })

    return{
      spaces,
      selectInfo,
      formatMode,
    }
  }
}
</script>

<style scoped>
td{
  vertical-align: middle;
  line-height: 20px;
}
th{
  vertical-align: middle;
  line-height: 20px;
}
</style>