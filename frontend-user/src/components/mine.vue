<template>
  <div>
    <div class="alert alert-danger" role="alert" v-show="wrongSys">
      [ERROR] Orders can only be cancelled 30 minutes prior to the effective time !
    </div>
    <div class="alert alert-success" role="alert" v-show="ok">
      [OK] Successfully canceled !
    </div>
    <br>
  <div class="card container align-items-baseline">
    <br>
    <h1>&nbsp; My Orders</h1>
    <br>
  </div>
  <br>
  <div class="card container">
    <div class="card-body ">
      <table class="table">
        <thead>
        <tr>
          <th scope="col">#</th>
          <th scope="col">Car Number</th>
          <th scope="col">Space ID</th>
          <th scope="col">Start Time</th>
          <th scope="col">End Time</th>
          <th scope="col">Price</th>
          <th scope="col">Cancel</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="i in orders">
          <th scope="row">{{ i.orderId }}</th>
          <td>{{ i.licenseNumber }}</td>
          <td>{{ i.spaceId }}</td>
          <td>{{ formatDate(i.startTime) }}</td>
          <td>{{ formatDate(i.endTime)  }}</td>
          <td>{{ i.price }}</td>
          <td><button class="btn btn-danger" @click="cancel(i.orderId)">Cancel</button></td>
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
  name: "mine",
  setup(){
    const store = useStore();
    let orders = ref([]);
    let wrongSys = ref(false);
    let ok = ref(false);
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
        url:"management/order",
        headers: {'token': localStorage.getItem("token")},
    }).then((res)=>{
      orders.value =  res.data["orderList"].reverse();
      console.log(orders)
      store.dispatch("Finished");
    })
    const cancel = function (id) {
      store.dispatch("Load")
      axios({
        method:"PUT",
        url:"management/order/cancel/" + id,
        headers: {'token': localStorage.getItem("token")},
      }).then((res)=>{
        wrongSys.value = false;
        ok.value = true;
        orders.value = orders.value.filter((order)=>{
          if(order.orderId !== id) return true;
          else return false;
        });
        store.dispatch("Finished");
      }).catch((err)=>{
        wrongSys.value = true;
        store.dispatch("Finished");
      })
    }
    return{
      orders,
      formatDate,
      cancel,
      wrongSys,
      ok
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