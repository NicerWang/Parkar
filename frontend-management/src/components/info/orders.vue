<template>
  <div>
    <br>
    <div class="card container align-items-baseline">
      <br>
      <h1>&nbsp; All Orders</h1>
      <br>
    </div>
    <br>
    <div class="card container">
      <div class="card-body ">
        <table class="table">
          <thead>
          <tr>
            <th scope="col">#</th>
            <th scope="col">User ID</th>
            <th scope="col">Car Number</th>
            <th scope="col">Space ID</th>
            <th scope="col">Start Time</th>
            <th scope="col">End Time</th>
            <th scope="col">Price</th>
            <th scope="col">Paid</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="i in orders">
            <th scope="row">{{ i.orderId }}</th>
            <th scope="row">{{ i.userId }}</th>
            <td>{{ i.licenseNumber }}</td>
            <td>{{ i.spaceId }}</td>
            <td>{{ formatDate(i.startTime) }}</td>
            <td>{{ formatDate(i.endTime)  }}</td>
            <td>{{ i.price }}</td>
            <td>{{ i.paid == 0 ? "NO" : "YES" }}</td>
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
  name: "orders",
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
      url:"management/administrator/parking/order/list",
      headers: {'token': localStorage.getItem("token")},
    }).then((res)=>{
      users.value =  res.data["orderList"].reverse();
      store.dispatch("Finished");
    })
    return{
      orders: users,
      formatDate
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