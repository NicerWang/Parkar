<template>
  <div>
    <br>
    <div class="card container align-items-baseline">
      <br>
      <h1>&nbsp; All Users</h1>
      <br>
    </div>
    <br>
    <div class="card container">
      <div class="card-body ">
        <table class="table">
          <thead>
          <tr>
            <th scope="col">#</th>
            <th scope="col">User Name</th>
            <th scope="col">Telephone</th>
            <th scope="col">Register Time</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="i in users">
            <th scope="row">{{ i.id }}</th>
            <td>{{ i.username }}</td>
            <td>{{ i.phone }}</td>
            <td>{{ formatDate(i.registerTime) }}</td>
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
  name: "users",
  setup(){
    const store = useStore();
    let users = ref([]);

    const formatDate = function (timestamp) {
      let date = new Date(timestamp);
      let Y = date.getFullYear() + '-';
      let M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
      let D = (date.getDate() < 10 ? '0'+(date.getDate()) : date.getDate()) + ' ';
      let h = (date.getHours() < 10 ? '0'+(date.getHours()) : date.getHours()) + ':';
      let m = (date.getMinutes() < 10 ? '0'+(date.getMinutes()) : date.getMinutes())
      return Y+M+D+h+m;
    }
    axios({
      method:"GET",
      url:"user/getAllUsersInformation",
      headers: {'token': localStorage.getItem("token")},
    }).then((res)=>{
      users.value =  res.data.data["allUsers"].reverse();
      store.dispatch("Finished");
    })
    return{
      users,
      formatDate,
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