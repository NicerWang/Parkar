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
      url:"/management/admin/space/" + route.params.id,
      headers: {'token': localStorage.getItem("token")},
    }).then((res)=>{
      space.value = res.data['spaceInfo'];
      store.dispatch("Finished")
    })


    
    const updateInfo = function () {
      let mode;
      axios({
        method:"PUT",
        url:"/management/admin/space/" + space.value['spaceId'],
        data:{
          floor:space.value['floor'],
          ban:space.value['ban'],
          occupied:space.value['occupied'],
          xCoordinate:space.value['xCoordinate'],
          yCoordinate:space.value['yCoordinate'],
          mode:space.value['mode'],
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