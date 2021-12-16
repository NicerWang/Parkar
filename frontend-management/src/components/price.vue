<template>
  <div class="container">
    <br>
    <div class="card container align-items-baseline">
      <br>
      <h1>&nbsp; Price</h1>
      <br>
    </div>
    <br>
    <div class="card col-12 align-items-center">
      <div class="row">

      </div>
      <form class="col-8">
        <label class="form-label" for="temp">Temporary Price (Per half hour)</label>
        <div class="input-group mb-3">
          <span class="input-group-text">$</span>
          <input id="temp" class="form-control" placeholder="0.0" type="number" v-model="tempPrice">
          <button class="btn btn-primary" type="button" @click="updatePrice(0,tempPrice)">Update</button>
        </div>

        <label class="form-label" for="month">Month Price (Per month)</label>
        <div class="input-group mb-3">
          <span class="input-group-text">$</span>
          <input id="month" class="form-control" placeholder="0.0" type="number" v-model="monthPrice">
          <button class="btn btn-primary" type="button" @click="updatePrice(1,monthPrice)">Update</button>

        </div>
        <label class="form-label" for="year">Year Price (Per year)</label>
        <div class="input-group mb-3">
          <span class="input-group-text">$</span>
          <input id="year" class="form-control" placeholder="0.0" type="number" v-model="yearPrice">
          <button class="btn btn-primary" type="button" @click="updatePrice(2,yearPrice)">Update</button>

        </div>

      </form>
    </div>
  </div>
</template>

<script>
import {useStore} from "vuex";
import axios from "axios";
import {ref} from "vue";

export default {
  name: "price",
  setup(){
    const store = useStore();
    let tempPrice = ref(0.0);
    let monthPrice = ref(0.0);
    let yearPrice = ref(0.0);

    const update = function () {
      axios({
        url:"management/admin/price",
        headers: {'token': localStorage.getItem("token")},
      }).then((res)=>{
        tempPrice.value = res.data[0];
        monthPrice.value = res.data[1];
        yearPrice.value = res.data[2];
        store.dispatch("Finished");
      })
    }
    const updatePrice = function (id, newVal) {
      store.dispatch("Load");
      axios({
        method:"POST",
        url:"management/admin/price",
        params:{
          type:id,
          value:newVal,
        },
        headers: {'token': localStorage.getItem("token")},
      }).then((res)=>{
        update();
        alert("Successfully updated")
      })
    }
    update();
    return {
      updatePrice,
      tempPrice,
      monthPrice,
      yearPrice
    }
  }
}
</script>

<style scoped>
form{
  padding: 20px;
}
</style>