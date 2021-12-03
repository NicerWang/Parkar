<template>
  <div class="board">
    <div class="park-position" v-for="(i,idx) in positions" :style="{left:i.xCoordinate / scale + 'px',top:i.yCoordinate / scale + 'px'}" @click="selectPosition(idx)">
      <div v-show="i.isSelected">
        <img class="symbol" src="../assets/check.svg" alt="">
      </div>
      <div v-show="i.isOccupied">
        <img class="symbol" src="../assets/close.svg" alt="">
      </div>
      <div v-show="!i.isOccupied && !i.isSelected">
        <img class="symbol" src="../assets/round.svg" alt="">
      </div>
      <span class="park-space-id">{{ i.spaceId }}</span>
    </div>
    <div class="in-out" :style="{left: 0,top: (95 / scale) + 'px'}">Entry</div>
    <div class="in-out" :style="{left:(545 /scale) + 'px',top: (95 / scale) + 'px'}">Exit</div>

  </div>
</template>

<script>
import {onMounted, ref} from "vue";

export default {
  name: "selector",
  props:{
    position:Array,
    select:Array,
    avails:Array
  },
  setup(props){
    let positions = []
    props.select[1] = 0;
    let scale = ref(1);
    if(window.outerWidth < 768){
      scale.value = 2;
    }
    else scale.value = 1;
    for(let i = 0; i < props.position.length; i++){
      if(props.select[0] == props.position[i]['floor']){
        positions.push(props.position[i])
      }
    }
    const selectPosition = function (idx) {
      if(positions[idx].isOccupied) return;
      else for(let i = 0; i < positions.length; i++){
        positions[i].isSelected = false;
      }
      positions[idx].isSelected = true;
      props.select[1] = positions[idx]['spaceId'];
    }
    onMounted(()=>{
      window.onresize = ()=>{
          if(window.outerWidth < 768){
            scale.value = 2;
          }
          else scale.value = 1;
      }
    })
    return{
        positions,
        selectPosition,
        scale
    }
  }
}
</script>

<style scoped>
@media (max-width:767px) {
  .board {
    padding: 45px;
    width: 310px;
  }
  .park-position{
    width: 15px;
    height: 30px;
    border: 1px solid #ced4da;
    border-radius: 2px;
  }
  .symbol{
    width: 13px;
    height: 25px;
  }
  .in-out{
    word-wrap: normal;
    width: 32px;
    height: 30px;
    border-radius: 2px;
    line-height: 30px;
    font-size: 10px;
  }
  .park-space-id{
    display: none;
  }
}
@media (min-width: 768px) {
  .board {
    padding: 80px;
    width: 605px;
  }
  .park-position{
    width: 25px;
    height: 50px;
    border: 1px solid #ced4da;
    border-radius: 3px;
  }
  .symbol{
    width: 15px;
    height: 25px;
  }
  .in-out{
    width: 60px;
    height: 50px;
    border-radius: 3px;
    line-height: 50px;
  }
}
.board{
  margin: 0 auto;
  position: relative;
}
.park-position{
  position: absolute;
  word-wrap: normal;
  display: inline-block;
  cursor: pointer;
}
.park-position:hover{
  background: #efefef;
  animation: pulse;
  animation-duration: 1s;
  animation-iteration-count: infinite;
}

.in-out{
  position: absolute;
  border: 1px solid #ced4da;
  display: inline-block;
}
</style>