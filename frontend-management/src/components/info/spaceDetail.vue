<template>
  <div>
    <br>
    <br>
  <div class="card container">
    <br>
    <div class="card-body">
      <form class="row g-3">
        <div class="col-md-12">
          <label for="space-id" class="form-label">Space ID</label>
          <input type="number" class="form-control" id="space-id" v-model="space['spaceId']" disabled>
        </div>
        <div class="col-md-12">
          <label for="floor" class="form-label">Floor</label>
          <input type="number" class="form-control" id="floor" v-model="space['floor']">
        </div>
        <div class="col-md-12">
          <label for="inputState" class="form-label">Type</label>
          <select id="inputState" class="form-select" v-model="space['mode']">
            <option value="0">day</option>
            <option value="1">month</option>
            <option value="2">year</option>
          </select>
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
              Ban
            </label>
          </div>
        </div>
        <div class="col-4">
          <div class="form-check">
            <input class="form-check-input" type="checkbox" id="occupy" v-model="space['occupied']">
            <label class="form-check-label" for="occupy">
              Occupy
            </label>
          </div>
        </div>
        <div class="col-4">
          <div class="form-check">
            <input class="form-check-input" type="checkbox" id="book" v-model="space['booked']">
            <label class="form-check-label" for="book">
              Book
            </label>
          </div>
        </div>
        <div class="col-12">
          <button type="button" class="btn btn-primary" @click="updateInfo">Update Status</button>
        </div>
      </form>
    </div>

  </div>
  </div>



</template>

<script>
import {useRoute} from "vue-router";
import {ref} from "vue";
import axios from "axios";
import {useStore} from "vuex";

export default {
  name: "spaceDetail",
  setup(){
    const route = useRoute();
    const store = useStore()
    let space = ref({})
    axios({
      method:"GET",
      url:"/management/administrator/parking/space/" + route.params.id,
      headers: {'token': localStorage.getItem("token")},
    }).then((res)=>{
      space.value = res.data['spaceInfo'];
      space.value['booked'] = Boolean(space.value['booked']);
      space.value['ban'] = Boolean(space.value['ban']);
      space.value['occupied'] = Boolean(space.value['occupied']);
      store.dispatch("Finished")
    })


    
    const updateInfo = function () {
      let mode;
      if(space.value['mode'] === 0) mode = "day";
      if(space.value['mode'] === 1) mode = "month";
      if(space.value['mode'] === 2) mode = "year";
      axios({
        method:"PUT",
        url:"/management/administrator/parking/space/" + space.value['spaceId'],
        params:{
          floor:space.value['floor'],
          ban:Number(space.value['ban']),
          booked:Number(space.value['booked']),
          occupied:Number(space.value['occupied']),
          xCoordinate:space.value['xCoordinate'],
          yCoordinate:space.value['yCoordinate'],
          mode:mode,
        },
        headers: {'token': localStorage.getItem("token")},
      }).then((res)=>{
        alert("Successfully updated!")
      }).catch((err)=>{
        alert("Update failed!")
      })
    }
    return{
      space,
      updateInfo
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