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
        <table class="table">
          <thead>
          <tr>
            <th scope="col">#</th>
            <th scope="col">Occupied</th>
            <th scope="col">Mode</th>
            <th scope="col">Ban</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="i in spaces">
            <th scope="row">{{ i.spaceId }}</th>
            <td>{{ i.occupied == 0 ? "False" : "True" }}</td>
            <td>{{ formatMode(i.mode) }}</td>
            <td>{{ i.ban == 0 ? "False" : "True" }}</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import { useStore } from "vuex";
import axios from "axios";
import {ref} from "vue";

export default {
  name: "spaces",
  setup(){
    const store = useStore();
    let spaces = ref([]);

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