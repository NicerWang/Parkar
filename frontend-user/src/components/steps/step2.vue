<template>
  <div>  <div v-for="i in avails">{{i}}</div></div>

</template>

<script>
import {useStore} from "vuex";
import {inject} from "vue";

export default {
  name: "step2",
  props:{
    avails:Array
  },
  setup(props){
    const store = useStore();
    store.dispatch("Finished");

    let avails = props.avails;
    console.log(avails)
    return{
      avails
    }
  }
}
</script>

<style scoped>

</style>