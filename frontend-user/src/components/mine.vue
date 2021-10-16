<template>
  <div class="card">
    <div class="card-body">
      This is mine page.
    </div>
  </div>
</template>

<script>
import { useStore } from "vuex";

export default {
  name: "mine",
  setup(){
    const store = useStore();
    store.dispatch("Finished")

  }
}
</script>

<style scoped>

</style>