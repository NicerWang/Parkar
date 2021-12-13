<template>
  <div>
    <div :class="{'alert':true, 'alert-danger':message[0] === 'danger', 'alert-success':message[0] === 'success',}" v-show="message[0].length !== 0">
      {{ message[1] }}
    </div>
    <div class="modal fade" id="modal">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Select New End Time</h5>
          </div>
          <div class="modal-body">
            <div class="row justify-content-evenly">
              <div class="col-12">
                <label class="form-label" for="end-date">End Date</label>
                <div><input id="end-date" v-model="endDate" type="date"></div>
              </div>
              <div class="col-12">
                <label class="form-label">End Time</label>
                <div class="input-group">
                  <select  class="form-control" v-model="endOffset">
                    <option value="0">上午</option>
                    <option value="12">下午</option>
                  </select>
                  <select  class="form-control" v-model="endHour">
                    <option value="0">12</option>
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                    <option value="6">6</option>
                    <option value="7">7</option>
                    <option value="8">8</option>
                    <option value="9">9</option>
                    <option value="10">10</option>
                    <option value="11">11</option>
                  </select>
                  <span class="input-group-text colon">:</span>
                  <select  class="form-control" v-model="endMinute">
                    <option value="00">00</option>
                    <option value="30">30</option>
                  </select>
                </div>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
            <button type="button" class="btn btn-primary" data-bs-dismiss="modal" @click="extend">Confirm</button>
          </div>
        </div>
      </div>
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
          <tr v-for="(i,idx) in nowOrders">
            <th scope="row">{{ i.orderId }}</th>
            <td>{{ i.licenseNumber }}</td>
            <td>{{ i.spaceId }}</td>
            <td>{{ formatDate(i.startTime,0) }}<br>{{ formatDate(i.startTime,1) }} </td>
            <td>{{ formatDate(i.endTime,0) }}<br>{{ formatDate(i.endTime,1) }}</td>
            <td>
              <button :disabled="i.paid" class="btn btn-primary" @click="pay(i.orderId,i.price)">Pay</button>
              <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#modal" @click="showModal(idx)">Extend</button>
              <button class="btn btn-danger" @click="cancel(i.orderId)">Cancel</button></td>
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
import {collect} from "./data/collector";
import {useRouter} from "vue-router";

export default {
  name: "mine",
  setup(){
    const store = useStore()
    let orders = ref([])
    let message = ref(["",""])
    let nowOrders = ref([])
    const date = new Date();

    let endDate = ref("");
    let endOffset = ref(0)
    let endHour = ref(0);
    let endMinute = ref(0);
    let modelOrderIndex = ref(-1);

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

    const addPreZero = function (time) {
      if (time.toString().length === 1) return "0" + time;
      else return time;
    }

    const showModal = function (idx) {
      console.log(nowOrders.value[idx])
      let oldDateTimeStamp = Date.parse(nowOrders.value[idx].endTime);
      let oldDate = new Date(oldDateTimeStamp + 1000)
      endDate.value = oldDate.getFullYear() + "-" + addPreZero(oldDate.getMonth() + 1) + "-" + addPreZero(oldDate.getDate());
      endHour.value = oldDate.getHours();
      if(endHour.value >= 12){
        endHour.value -= 12;
        endOffset.value = 12;
      }
      endMinute.value = addPreZero(oldDate.getMinutes());
      modelOrderIndex.value = idx;
    }

    collect(10);

    const update = function () {
      axios({
        method:"GET",
        url:"management/order",
        headers: {'token': localStorage.getItem("token")},
      }).then((res)=>{
        const tempOrders =  res.data["orderList"].reverse()
        nowOrders.value = []
        orders.value = []
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
    }

    const cancel = function (id) {
      document.body.scrollTop = document.documentElement.scrollTop = 0;
      collect(11,"cancel");
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
      document.body.scrollTop = document.documentElement.scrollTop = 0;
      alert("Reserved interface for payment.\n" + "[Order ID]" + id + "\n[Price]" + price)
    }

    const extend = function () {
      document.body.scrollTop = document.documentElement.scrollTop = 0;
      let id = nowOrders.value[modelOrderIndex.value].orderId;
      let newEndTime = Date.parse(endDate.value.replaceAll("-","/") + " " + addPreZero(Number(endOffset.value) + Number(endHour.value)) + ":" + endMinute.value + ":00");
      if(newEndTime <= Date.parse(nowOrders.value[modelOrderIndex.value].endTime) + 1000){
        message.value[1] = "[ERROR] New end time should be no earlier than old's";
        message.value[0] = "danger"
        return;
      }
      store.dispatch("Load");
      axios({
        method: "POST",
        url:"management/order/extend/" + id,
        params:{
          newEndTime: newEndTime
        },
        headers: {'token': localStorage.getItem("token")},
      }).then((res)=>{
        message.value[1] = "[OK] Successfully extended."
        message.value[0] = "success"
        update();
      }).catch((err)=>{
        message.value[1] = "[ERROR]" + err.response.data.message[0];
        message.value[0] = "danger"
        store.dispatch("Finished");
      })
    }
    update();
    return{
      orders,
      nowOrders,
      formatDate,
      cancel,
      pay,
      showModal,
      extend,
      endOffset,
      endDate,
      endHour,
      endMinute,
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
input {
  border: 1px solid #ced4da;
  border-radius: .25rem;
  padding: .375rem .75rem;
  transition: border-color .15s ease-in-out, box-shadow .15s ease-in-out;
  color: #212529;
  width: 100%;
}
input:focus{
  color: #212529;
  background-color: #fff;
  border-color: #86b7fe;
  outline: 0;
  box-shadow: 0 0 0 .25rem rgba(13,110,253,.25);
}
.colon{
  font-weight: 900;
}
</style>