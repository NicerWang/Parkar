<template>
  <div class="board">
    <div class="park-position" v-for="(i,idx) in positions" :style="{left:i.xCoordinate + 'px',top:i.yCoordinate + 'px'}" @click="selectPosition(idx)">
      <div v-show="(i.occupied || i.booked) && !i.ban">
        <img class="symbol" src="../assets/check.svg" alt="">
      </div>
      <div v-show="i.ban">
        <img class="symbol" src="../assets/close.svg" alt="">
      </div>
      <div v-show="!i.occupied && !i.ban && !i.booked">
        <img class="symbol" src="../assets/round.svg" alt="">
      </div>
      {{ i.spaceId }}
    </div>
    <div class="in-out" style="left: 0;top:95px">Entry</div>
    <div class="in-out" style="left: 545px;top:95px">Exit</div>

  </div>
</template>

<script>
import {useRouter} from "vue-router";

export default {
  name: "selector",
  props:{
    position:Array,
    select:Array
  },
  setup(props){
    let positions = []
    const router = useRouter();
    for(let i = 0; i < props.position.length; i++){
      if(props.select[0] == props.position[i]['floor']){
        positions.push(props.position[i])
      }
    }
    const selectPosition = function (idx) {
      router.push("/info/spaces/" + positions[idx]['spaceId']);
    }
    return{
        positions,
        selectPosition
    }
  }
}
</script>

<style scoped>
.board{
  padding: 80px;
  width: 620px;
  margin: 0 auto;
  position: relative;
}
.park-position{
  position: absolute;
  width: 25px;
  height: 50px;
  border: 1px solid #ced4da;
  display: inline-block;
  border-radius: 3px;
  cursor: pointer;
}
.park-position:hover{
  background: #efefef;
  animation: pulse;
  animation-duration: 1s;
  animation-iteration-count: infinite;
}
.symbol{
  width: 15px;
  height: 15px;
}
.in-out{
  position: absolute;
  width: 60px;
  height: 50px;
  border: 1px solid #ced4da;
  display: inline-block;
  border-radius: 3px;
  line-height: 50px;
}
</style>