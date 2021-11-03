<template>
  <main class="form-signin">
    <div class="alert alert-danger" role="alert" v-show="wrong">
      Invalid Password
    </div>
    <form>
      <br>
      <br>
      <img class="mb-4" src="../assets/update.svg" alt="" width="72" height="57">
      <h1 class="h3 mb-3 fw-normal">Update Password</h1>
      <br>
      <br>
      <div class="form-floating">
        <input type="password" :class="pwdCls" id="floatingPassword" placeholder=" " v-model="pwd">
        <label for="floatingPassword">New Password</label>
        <div class="invalid-tooltip">
          Need at least 8 characters(No special symbol).
        </div>
      </div>
      <br>
      <br>
      <button type="button" class="w-100 btn btn-lg btn-primary" @click="update">Update</button>
    </form>
  </main>
</template>

<script>
import axios from "axios";
import {ref, watch} from 'vue';
import {useRouter} from "vue-router";
import {useStore} from "vuex";


export default {
  name: "updateInfo",
  setup() {
    const store = useStore();
    const router = useRouter();
    store.dispatch("Load");
    const pwdPattern = /^[a-zA-Z0-9]{8,16}$/;

    let pwd = ref("");
    let wrong = ref(false);

    let pwdCls = ref({
      'form-control':true,
      'is-invalid':false
    });

    watch([pwd],()=>{
      if(pwd.value !== "") pwdCls.value['is-invalid'] = !pwdPattern.test(pwd.value);
    })

    const update = () => {
      if (pwd.value === "" || !pwdPattern.test(pwd.value)){
        return;
      }
      axios({
        method: "POST",
        url: "admin/update/pwd",
        params: {
          pwd: pwd.value.trim(),
        },
        headers: {'token': localStorage.getItem("token")},
        async: false
      }).then((res) => {
        if(res.data === "SUCCESS"){
          router.push("/index")
        }
        else{
          wrong.value = true;
        }

      })
    }
    store.dispatch("Finished")
    return {
      pwd,
      wrong,
      pwdCls,
      update,
    }
  }
}
</script>

<style scoped>
.form-signin {
  width: 100%;
  max-width: 330px;
  padding: 15px;
  margin: auto;
}

.form-signin .form-floating:focus-within {
  z-index: 2;
}


.form-signin input[type="password"] {
  margin-bottom: 10px;
  border-top-left-radius: 0;
  border-top-right-radius: 0;
}
</style>