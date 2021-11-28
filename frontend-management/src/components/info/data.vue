<template>
  <div>
    UnFinished.
  </div>

</template>

<script>
import {useStore} from "vuex";

export default {
  name: "data",
  setup(){
    const store = useStore();
    store.dispatch("Finished")
  }
}
</script>

<style scoped>

</style>