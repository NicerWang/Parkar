<template>
  <div>
    <br>
    <div class="card container">
      <div class="card-body ">
        <div class="form-floating">
          <select id="chartSelector" class="form-select" v-model="id">
            <option v-for="(i,idx) in graphs" :value="idx">{{ i }}</option>
          </select>
          <label for="chartSelector">Select the Chart</label>
        </div>
      </div>
    </div>
    <br>
    <div class="card container">
      <div class="card-header"><h2>{{ graphs[id] }}</h2></div>
      <div>
        <chart :id="id" :key="id"></chart>
      </div>
    </div>
    <br>
    <br>
  </div>

</template>

<script>
import chart from "../chart.vue";
import {ref} from "vue";
export default {

  name: "dataAnalyze",
  components:{
    chart
  },
  setup(){
    let id = ref(0);
    let graphs = ["Active Users","Day Reservations","Day Canceled","Place Occupy Rate"]
    return{
      graphs,
      id,
    }
  }
}
</script>

<style scoped>

.card{
  padding: 0;
}
</style>