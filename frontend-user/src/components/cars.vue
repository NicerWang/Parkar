<template>
  <div>
    <div :class="{'alert':true, 'alert-danger':message[0] === 'danger', 'alert-success':message[0] === 'success',}" v-show="message[0].length !== 0">
      {{ message[1] }}
    </div>
    <div class="card container align-items-baseline">
      <br>
      <h1>&nbsp; My Cars</h1>
      <br>
    </div>
    <br>
    <div class="card container">
    <form class="row  align-items-center">
      <div class="col-7">
        <div class="input-group">
          <div class="input-group-text">CarNumber</div>
          <input type="text" class="form-control" id="inlineFormInputGroupUsername" placeholder="AE86" v-model="newNumber">
        </div>
      </div>

      <div class="col-5">
        <button type="button" class="btn btn-primary" id="add-new-car" @click="add(newNumber)">Add</button>
      </div>
    </form>
    </div>
    <br>
    <div class="card container">
      <div class="card-body ">
        <table class="table">
          <thead>
          <tr>
            <th scope="col">Car Number</th>
            <th scope="col">Manipulate</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="i in all_car">
            <th scope="row">{{ i }}</th>
            <td><button class="btn btn-danger" @click="remove(i)">Remove</button></td>
          </tr>
          </tbody>
        </table>
        <div v-show="all_car.length === 0">No Data</div>
      </div>
    </div>
  </div>
</template>

<script>
import { useStore } from "vuex";
import axios from "axios";
import {ref} from "vue";

export default {
  name: "cars",
  setup(){
    const store = useStore()
    let all_car = ref([])
    let message = ref(["",""])
    let newNumber = ref()

    axios({
      method:"GET",
      url:"user/getAllVehicleId",
      headers: {'token': localStorage.getItem("token")},
    }).then((res)=>{
      all_car.value = res.data.data["allVehicleIds"]
      store.dispatch("Finished")
    })
    const remove = function (id) {
      store.dispatch("Load")
      axios({
        method:"POST",
        url:"/user/deleteVehicle",
        data:{
          vehicleId:id
        },
        headers: {'token': localStorage.getItem("token")},
      }).then((res)=>{
        message.value[1] = "[OK] Successfully removed."
        message.value[0] = "success"
        all_car.value = all_car.value.filter((car)=>{
          if(car !== id) return true;
          else return false;
        });
        store.dispatch("Finished");
      }).catch((err)=>{
        message.value[1] = "[ERROR]Unknown error.";
        message.value[0] = "danger"
        store.dispatch("Finished");
      })
    }
    const add = function (id) {
      store.dispatch("Load")
      axios({
        method:"POST",
        url:"/user/addVehicle",
        data:{
          vehicleId:id
        },
        headers: {'token': localStorage.getItem("token")},
      }).then((res)=>{
        if(res.data['success'] === false){
          message.value[1] = "[ERROR]Unknown error, maybe duplicated car number.";
          message.value[0] = "danger"
          store.dispatch("Finished");
          return;
        }
        message.value[1] = "[OK] Successfully Added."
        message.value[0] = "success"
        all_car.value.push(id)
        store.dispatch("Finished");
      }).catch((err)=>{
        message.value[1] = "[ERROR]Unknown error, maybe duplicated car number.";
        message.value[0] = "danger"
        store.dispatch("Finished");
      })
    }

    return{
      all_car,
      message,
      newNumber,
      remove,
      add
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
  padding: 0px;
}
form{
  padding: 20px;
}
#add-new-car{
  width: 50%;
}
</style>