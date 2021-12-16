<template>
  <div>
    <br>
    <div class="card container align-items-baseline">
      <br>
      <h1 v-if="$route.params.id !== 'all'">&nbsp; Specific User</h1>
      <h1 v-else>&nbsp; Users</h1>
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
            <th scope="col">Remove</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="i in users">
            <th scope="row">{{ i.id }}</th>
            <td>{{ i.username }}</td>
            <td>{{ i.phone }}</td>
            <td>{{ formatDate(i.registerTime) }}</td>
            <td>
              <button class="btn btn-danger" @click="remove(i.id)">Remove</button>
            </td>
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
import {useRoute} from "vue-router";

export default {
  name: "users",
  setup(){
    const store = useStore();
    const route = useRoute();
    let users = ref([]);
    let id = route.params.id;


    const formatDate = function (timestamp) {
      let date = new Date(timestamp);
      let Y = date.getFullYear() + '-';
      let M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
      let D = (date.getDate() < 10 ? '0'+(date.getDate()) : date.getDate()) + ' ';
      let h = (date.getHours() < 10 ? '0'+(date.getHours()) : date.getHours()) + ':';
      let m = (date.getMinutes() < 10 ? '0'+(date.getMinutes()) : date.getMinutes())
      return Y+M+D+h+m;
    }

    const sortFunc = function (user1,user2){
      if (user1['phone'] < user2['phone'])
        return -1;
      if (user1['phone'] > user2['phone']) {
        return 1;
      }
      return 0;
    }
    const update = function () {
      axios({
        method:"GET",
        url:"user/getAllUsersInformation",
        headers: {'token': localStorage.getItem("token")},
      }).then((res)=>{
        users.value =  res.data.data["allUsers"].sort(sortFunc);
        if(id !== "all"){
          users.value = users.value.filter((user)=>{
            return user['id'] === id;
          })
        }
        store.dispatch("Finished");
      })
    }

    const remove = function (id) {
      store.dispatch("Load")
      axios({
        method:"POST",
        url:"user/remove/" + id,
        headers: {'token': localStorage.getItem("token")},
      }).then(()=>{
        update();
        alert("Successfully removed")
      })
    }
    update();
    return{
      users,
      formatDate,
      remove
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