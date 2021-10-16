<template>
  <div class="card">
    <div class="card-body">
      This is index page.
    </div>
  </div>
</template>

<script>
import { useStore } from "vuex";

export default {
  name: "index",
  setup(){
    const store = useStore();
    store.dispatch("Finished")

  }
}
</script>

<style scoped>

</style>