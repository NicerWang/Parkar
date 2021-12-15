<template>
  <div>
    <br>
    <br>
    <div class="card container">
      <br>
      <div class="card-body">
        <form class="row g-3">
          <div class="col-md-12">
            <label for="floor" class="form-label">Floor</label>
            <input type="number" class="form-control" id="floor" v-model="space['floor']">
          </div>
          <div class="col-md-6">
            <label for="xCoordinate" class="form-label">Position X</label>
            <input type="number" class="form-control" id="xCoordinate" v-model="space['xCoordinate']">
          </div>
          <div class="col-md-6">
            <label for="yCoordinate" class="form-label">Position Y</label>
            <input type="number" class="form-control" id="yCoordinate" v-model="space['yCoordinate']">
          </div>
          <div class="col-4">
            <div class="form-check">
              <input class="form-check-input" type="checkbox" id="ban" v-model="space['ban']">
              <label class="form-check-label" for="ban">
                Banned
              </label>
            </div>
          </div>
          <div class="col-4">
            <div class="form-check">
              <input class="form-check-input" type="checkbox" id="occupy" v-model="space['occupied']">
              <label class="form-check-label" for="occupy">
                Occupied
              </label>
            </div>
          </div>
          <div class="col-4">
            <div class="form-check">
              <input class="form-check-input" type="checkbox" id="book" v-model="space['booked']" disabled>
              <label class="form-check-label" for="book">
                Booked
              </label>
            </div>
          </div>
          <div class="col-12">
            <button type="button" class="btn btn-primary" @click="add">Add Place</button>
          </div>
        </form>
      </div>

    </div>
  </div>



</template>

<script>
import {useRoute} from "vue-router";
import {reactive} from "vue";
import axios from "axios";
import {useStore} from "vuex";

export default {
  name: "addSpace",
  setup(){
    const store = useStore()
    let space = reactive(
        {
          "spaceId": 100,
          "occupied": false,
          "mode": 0,
          "ban": false,
          "booked": false,
          "floor": 1,
          "xCoordinate": 0,
          "yCoordinate": 0
        }
    )

    const add = function () {
      let mode;
      axios({
        method:"PUT",
        url:"/management/admin/space/add",
        data:{
          floor:space['floor'],
          ban:space['ban'],
          occupied:space['occupied'],
          xCoordinate:space['xCoordinate'],
          yCoordinate:space['yCoordinate'],
          mode:space['mode'],
        },
        headers: {'token': localStorage.getItem("token")},
      }).then((res)=>{
        alert("Successfully Added!")
      }).catch((err)=>{
        alert("Add failed!")
      })
    }
    store.dispatch("Finished")
    return{
      space,
      add
    }
  }
}
</script>

<style scoped>
.card{
  padding-left: 20%;
  padding-right: 20%;
}
</style>