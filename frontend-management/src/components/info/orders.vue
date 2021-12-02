<template>
  <div>
    <br>
    <div class="card container align-items-baseline">
      <br>
      <h1 v-if="$route.params.id !== 'all'">&nbsp; Specific User's Orders</h1>
      <h1 v-else>&nbsp; Orders</h1>
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
            <th scope="row" @click="$router.push('/info/users/' + i.userId)" style="cursor: pointer">{{ i.userId }}</th>
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
import {useRoute} from "vue-router";

export default {
  name: "orders",
  setup(){
    const store = useStore();
    const route = useRoute();
    let orders = ref([]);
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
    axios({
      method:"GET",
      url:"management/admin/order",
      headers: {'token': localStorage.getItem("token")},
    }).then((res)=>{
      orders.value =  res.data["orderList"].reverse();
      if(id !== "all")
        orders.value = orders.value.filter((order)=>{
          return order["userId"] === id;
        })
      store.dispatch("Finished");
    })
    return{
      orders,
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