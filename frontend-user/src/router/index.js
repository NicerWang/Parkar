import { createRouter, createWebHashHistory} from 'vue-router'
const Login = ()=>import("../components/login.vue")
const UpdateInfo = ()=>import("../components/updateInfo.vue")
const Register = ()=>import("../components/register.vue")
const Index = ()=>import("../components/index.vue")
const Mine = ()=>import("../components/mine.vue")
const Step1 = ()=>import("../components/steps/step1.vue")
const Step2 = ()=>import("../components/steps/step2.vue")
const Step3 = ()=>import("../components/steps/step3.vue")

import store from "../store";


const routes = [
  {
    path: "/",
    redirect: "/login"
  },
  {
    path: "/index",
    name: 'Index',
    component: Index,
    children: [
      {
        path: "/index/step1",
        name: "Step1",
        component: Step1
      },
      {
        path: "/index/step2",
        name: "Step2",
        component: Step2
      },
      {
        path: "/index/step3",
        name: "Step3",
        component: Step3
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/update',
    name: 'UpdateInfo',
    component: UpdateInfo
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: "/mine",
    name: 'Mine',
    component: Mine
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})
router.beforeEach((to,from,next)=>{
  if(to.fullPath === "/login" && store.state.isSignedIn) {
    router.push("/index");
    return;
  }
  if(to.fullPath.search("index|mine|logout|update") !== -1){
    if(store.state.isSignedIn){
      store.dispatch("Load");
      next();
    }
    else{
      router.push("/login");
    }
  }
  else if(to.fullPath.search("register|login") !== -1){
    if(!store.state.isSignedIn){
      store.dispatch("Load");
      next();
    }
    else{
      router.push("/index");
    }
  }
})

export default router