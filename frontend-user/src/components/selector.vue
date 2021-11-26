<template>
  <div class="board">
    <div class="park-position" v-for="(i,idx) in positions" :style="{left:i.xCoordinate + 'px',top:i.yCoordinate + 'px'}" @click="selectPosition(idx)">
      <div v-show="i.isSelected">
        <img class="symbol" src="../assets/check.svg" alt="">
      </div>
      <div v-show="i.isOccupied">
        <img class="symbol" src="../assets/close.svg" alt="">
      </div>
      <div v-show="!i.isOccupied && !i.isSelected">
        <img class="symbol" src="../assets/round.svg" alt="">
      </div>
      {{ i.spaceId }}
    </div>
    <div class="in-out" style="left: 0;top:95px">Entry</div>
    <div class="in-out" style="left: 545px;top:95px">Exit</div>

  </div>
</template>

<script>
export default {
  name: "selector",
  props:{
    position:Array,
    select:Array,
    avails:Array
  },
  setup(props){
    let positions = []
    console.log(props.select[0])
    for(let i = 0; i < props.position.length; i++){
      console.log(props.select[0],props.position[i]['floor'])
      if(props.select[0] == props.position[i]['floor']){
        console.log("check")
        positions.push(props.position[i])
      }
    }
    console.log(props.position)
    console.log(positions)
    const selectPosition = function (idx) {
      if(positions[idx].isOccupied) return;
      else for(let i = 0; i < positions.length; i++){
        positions[i].isSelected = false;
      }
      positions[idx].isSelected = true;
      props.select[1] = positions[idx]['spaceId'];
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
  padding: 100px;
  width: 650px;
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