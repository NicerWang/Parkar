<template>
  <div class="form-signin">
    <div class="alert alert-danger" role="alert" v-show="wrong">
      Wrong Password or Name
    </div>
    <form>
      <br>
      <br>
      <img class="mb-4" src="../assets/logo.jpg" alt="" width="72" height="72" style="border-radius: 10px">
      <h1 class="h3 mb-3 fw-normal">Sign in to<br>Parkar For Management</h1>
      <div class="form-floating">
        <input type="name" :class="nameCls" id="floatingInput" placeholder="Name" v-model="name">
        <label for="floatingInput">Name</label>
        <div class="invalid-tooltip">
          Need at least 4 characters(No special symbol).
        </div>
      </div>
      <div class="form-floating">
        <input type="password" :class="pwdCls" id="floatingPassword" placeholder="Password" v-model="pwd">
        <label for="floatingPassword">Password</label>
        <div class="invalid-tooltip">
          Need at least 8 characters(No special symbol).
        </div>
      </div>
      <br>
      <br>
      <button class="w-100 btn btn-lg btn-primary" @click="signIn" type="button">Sign in</button>
    </form>
  </div>
</template>

<script>

import {useStore} from 'vuex'
import {useRouter} from "vue-router";
import {ref, watch} from 'vue';
import axios from "axios";

export default {
  name: "login",
  setup() {

    const store = useStore();
    const router = useRouter();
    const namePattern = /^[a-zA-Z0-9]{4,}$/;
    const pwdPattern = /^[a-zA-Z0-9]{8,16}$/;

    let name = ref("");
    let pwd = ref("");
    let wrong = ref(false);
    let nameCls = ref({
      'form-control':true,
      'is-invalid':false
    })
    let pwdCls = ref({
      'form-control':true,
      'is-invalid':false
    })

    watch([name,pwd],()=>{
      if(name.value !== "") nameCls.value['is-invalid'] = !namePattern.test(name.value);
      if(pwd.value !== "") pwdCls.value['is-invalid'] = !pwdPattern.test(pwd.value);
    })

    const sendRequest = () => {
      axios({
        method: "POST",
        url: "admin/login",
        data: {
          name: name.value,
          pwd: pwd.value,
        }
      }).then((res) => {
        if (res.data === "Error") {
          pwd.value = "";
          wrong.value = true;
        } else {
          localStorage.setItem("token",res.data)
          store.dispatch("SignIn");
          router.push("/index");
        }
        store.dispatch("Finished");
      });

    }

    const signIn = () => {
      store.dispatch("Load")
      if (!namePattern.test(name.value) || !pwdPattern.test(pwd.value)) {
        store.dispatch("Finished")
      }
      else setTimeout(function () {
        sendRequest();
      },500)

    }
    store.dispatch("Finished");

    return {
      name,
      pwd,
      wrong,
      nameCls,
      pwdCls,
      signIn
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

.form-signin {
  font-weight: 400;
}

.form-signin .form-floating:focus-within {
  z-index: 2;
}

.form-signin input[type="name"] {
  margin-bottom: -1px;
  border-bottom-right-radius: 0;
  border-bottom-left-radius: 0;
}

.form-signin input[type="password"] {
  margin-bottom: 10px;
  border-top-left-radius: 0;
  border-top-right-radius: 0;
}
</style>