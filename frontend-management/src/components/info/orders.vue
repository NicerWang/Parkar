<template>
  <div>
    <br>
    <div class="card container align-items-baseline">
      <br>
      <h1>&nbsp; Orders</h1>
      <br>
    </div>
    <br>
    <div class="card container">
      <form class="row align-items-center">
        <div class="col-2">
          <div class="input-group">
            <div class="input-group-text">License</div>
            <input type="text" class="form-control" v-model="carNumber">
          </div>
        </div>
        <div class="col-2">
          <div class="input-group">
            <div class="input-group-text">UID</div>
            <input type="text" class="form-control" v-model="userId">
          </div>
        </div>
        <div class="col-2">
          <div class="input-group">
            <div class="input-group-text">Space</div>
            <input type="text" class="form-control" v-model="spaceId">
          </div>
        </div>
        <div class="col-2">
          <div class="input-group">
            <div class="input-group-text">Mode</div>
            <select class="form-control" v-model="mode">
              <option value="-1">Any</option>
              <option value="0">Temporary</option>
              <option value="1">Month</option>
              <option value="2">Year</option>
            </select>
          </div>
        </div>
        <div class="col-2">
          <div class="input-group">
            <div class="input-group-text">Paid</div>
            <select class="form-control" v-model="paid">
              <option value="-1">Any</option>
              <option value="0">No</option>
              <option value="1">Yes</option>
            </select>
          </div>
        </div>
        <div class="col-2">
          <button type="button" class="btn btn-primary" @click="update">Search</button>
        </div>
      </form>
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
            <th scope="col">Mode</th>
            <th scope="col">Start Time</th>
            <th scope="col">End Time</th>
            <th scope="col">Price</th>
            <th scope="col">Paid</th>
            <th scope="col">Remove</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="i in orders">
            <th scope="row">{{ i.orderId }}</th>
            <th scope="row" @click="$router.push('/info/users/' + i.userId)" style="cursor: pointer">{{ i.userId }}</th>
            <td>{{ i.licenseNumber }}</td>
            <td>{{ i.spaceId }}</td>
            <td>{{ i.mode === 0 ? "Temporary" : (i.mode === 1 ? "Month" : "Year" )}}</td>
            <td>{{ formatDate(i.startTime) }}</td>
            <td>{{ formatDate(i.endTime)  }}</td>
            <td>{{ i.price }}</td>
            <td>{{ i.paid === 0 ? "NO" : "YES" }}</td>
            <td>
              <button class="btn btn-danger" @click="remove(i.orderId)">Remove</button>
            </td>
          </tr>
          </tbody>
        </table>
        <div v-show="orders.length == 0"> No Data</div>
      </div>
    </div>
    <br>
    <div class="card container">
      <form class="row align-items-center">
        <div class="col-5">
          <button :disabled="page === 1" type="button" class="btn btn-secondary" @click="pre">&nbsp;&nbsp;Pre&nbsp;&nbsp;</button>
        </div>
        <div class="col-2">
          {{ page }}
        </div>
        <div class="col-5">
          <button :disabled="orders.length < 10" type="button" class="btn btn-secondary" @click="next">&nbsp;&nbsp;Next&nbsp;&nbsp;</button>
        </div>

      </form>
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
    let page = ref(1);
    const pageSize = 10;
    let carNumber = ref("");
    let userId = ref("");
    let spaceId = ref("");
    let paid = ref(-1);
    let mode = ref(-1);

    const formatDate = function (timestamp) {
      let date = new Date(timestamp);
      let Y = date.getFullYear() + '-';
      let M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
      let D = (date.getDate() < 10 ? '0'+(date.getDate()) : date.getDate()) + ' ';
      let h = (date.getHours() < 10 ? '0'+(date.getHours()) : date.getHours()) + ':';
      let m = (date.getMinutes() < 10 ? '0'+(date.getMinutes()) : date.getMinutes())
      return Y+M+D+h+m;
    }

    const update = function () {
      store.dispatch("Load");
      let requestParams = {
        page: page.value,
        pageSize: pageSize,
      };

      if(carNumber.value.length !== 0) requestParams['licenseNumber'] = carNumber.value.trim();
      if(userId.value.length !== 0) requestParams['userId'] = userId.value.trim();
      if(spaceId.value.length !== 0) requestParams['space_id'] = spaceId.value.trim();
      if(Number(paid.value) !== -1) requestParams['paid'] = paid.value;
      if(Number(mode.value) !== -1) requestParams['mode'] = mode.value;


      axios({
        method:"GET",
        url:"management/admin/order",
        params: requestParams,
        headers: {'token': localStorage.getItem("token")},
      }).then((res)=>{
        orders.value =  res.data["orderList"];
        store.dispatch("Finished");
      })
    }
    const getByOrderId = function () {
      store.dispatch("Load")
      axios({
        method:"GET",
        url:"management/admin/order",
        params:{
          page: page.value,
          pageSize: pageSize,
          orderId: orderId.value
        },
        headers: {'token': localStorage.getItem("token")},
      }).then((res)=>{
        orders.value =  res.data["orderList"];
        store.dispatch("Finished");
      })
    }
    const remove = function (id) {
      store.dispatch("Load")
      axios({
        method:"GET",
        url:"management/admin/order/remove/" + id,
        headers: {'token': localStorage.getItem("token")},
      }).then((res)=>{
        update();
        alert("Successfully removed")
      })
    }
    const next = function () {
      store.dispatch("Load")
      page.value++;
      update();
    }
    const pre = function () {
      store.dispatch("Load")
      page.value--;
      update();
    }
    
    update();
    return{
      orders,
      page,
      formatDate,
      update,
      remove,
      getByOrderId,
      next,
      pre,
      carNumber,
      userId,
      spaceId,
      paid,
      mode
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
.card{
  padding: 0;
  margin-bottom: 10px;
}
form{
  padding: 20px;
}
</style>