<template>
  <div>
    <div :class="{'alert':true, 'alert-danger':message[0] === 'danger', 'alert-success':message[0] === 'success',}" v-show="message[0].length !== 0">
      {{ message[1] }}
    </div>
  <div class="card container align-items-baseline title">
    <h1>My Orders</h1>
  </div>
    <div class="card container">
      <div class="card-header">Now Orders</div>
      <div class="card-body ">
        <table class="table">
          <thead>
          <tr>
            <th scope="col">#</th>
            <th scope="col">Car</th>
            <th scope="col">Space</th>
            <th scope="col">Start</th>
            <th scope="col">End</th>
            <th scope="col"></th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="i in nowOrders">
            <th scope="row">{{ i.orderId }}</th>
            <td>{{ i.licenseNumber }}</td>
            <td>{{ i.spaceId }}</td>
            <td>{{ formatDate(i.startTime,0) }}<br>{{ formatDate(i.startTime,1) }} </td>
            <td>{{ formatDate(i.endTime,0) }}<br>{{ formatDate(i.endTime,1) }}</td>
            <td><button :disabled="i.paid" class="btn btn-primary" @click="pay(i.orderId,i.price)">Pay</button><button class="btn btn-danger" @click="cancel(i.orderId)">Cancel</button></td>
          </tr>
          </tbody>
        </table>
        <div v-show="nowOrders.length === 0">No Data</div>
      </div>
    </div>
  <div class="card container">
    <div class="card-header">Past Orders</div>
    <div class="card-body ">
      <table class="table">
        <thead>
        <tr>
          <th scope="col">#</th>
          <th scope="col">Car</th>
          <th scope="col">Space</th>
          <th scope="col">Start</th>
          <th scope="col">End</th>
          <th scope="col"></th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="i in orders">
          <th scope="row">{{ i.orderId }}</th>
          <td>{{ i.licenseNumber }}</td>
          <td>{{ i.spaceId }}</td>
          <td>{{ formatDate(i.startTime,0) }} <br>{{ formatDate(i.startTime,1) }} </td>
          <td>{{ formatDate(i.endTime,0)  }} <br> {{ formatDate(i.endTime,1) }}</td>
          <td><button :disabled="i.paid" class="btn btn-primary" @click="pay(i.orderId,i.price)">Pay</button></td>
        </tr>
        </tbody>
      </table>
      <div v-show="orders.length === 0">No Data</div>
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
    const store = useStore()
    let orders = ref([])
    let message = ref(["",""])
    let nowOrders = ref([])
    const date = new Date();

    const formatDate = function (timestamp,type) {
      let date = new Date(timestamp)
      if( type === 0 ){
        let M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-'
        let D = (date.getDate() < 10 ? '0'+(date.getDate()) : date.getDate())
        return M + D
      }
      else{
        let h = (date.getHours() < 10 ? '0'+(date.getHours()) : date.getHours()) +  ':'
        let m = (date.getMinutes() < 10 ? '0'+(date.getMinutes()) : date.getMinutes())
        return h + m
      }
    }
    axios({
        method:"GET",
        url:"management/order",
        headers: {'token': localStorage.getItem("token")},
    }).then((res)=>{
      const tempOrders =  res.data["orderList"].reverse()
      for(let i = 0; i < tempOrders.length; i++){
        if(new Date(tempOrders[i].endTime) > date){
          nowOrders.value.push(tempOrders[i])
        }
        else{
          orders.value.push(tempOrders[i])
        }
      }
      store.dispatch("Finished")
    })
    const cancel = function (id) {
      document.body.scrollTop = document.documentElement.scrollTop = 0;
      store.dispatch("Load")
      axios({
        method:"PUT",
        url:"management/order/cancel/" + id,
        headers: {'token': localStorage.getItem("token")},
      }).then((res)=>{
        message.value[1] = "[OK] Successfully canceled."
        message.value[0] = "success"
        nowOrders.value = nowOrders.value.filter((order)=>{
          if(order.orderId !== id) return true;
          else return false;
        });
        store.dispatch("Finished");
      }).catch((err)=>{
        message.value[1] = "[ERROR]" + err.response.data.message[0];
        message.value[0] = "danger"
        store.dispatch("Finished");
      })
    }
    const pay = function (id, price) {
      alert("Reserved interface for payment.\n" + "[Order ID]" + id + "\n[Price]" + price)
    }
    return{
      orders,
      nowOrders,
      formatDate,
      cancel,
      pay,
      message
    }
  }
}
</script>

<style scoped>
td{
  vertical-align: middle;
  line-height: 20px;
  white-space: nowrap;
}
th{
  vertical-align: middle;
  line-height: 20px;
}
.card{
  padding: 0;
  margin-bottom: 10px;
}
.card-body{
  overflow: auto;
}
table{
  width: 100%;
}
button{
  margin: 2px;
}
.title{
  padding: 20px;
  margin-top: 10px;
  margin-bottom: 10px;
}
</style>