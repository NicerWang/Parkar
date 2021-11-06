import { createRouter, createWebHashHistory} from 'vue-router'
const Login = ()=>import("../components/login.vue")
const UpdateInfo = ()=>import("../components/updateInfo.vue")
const Index = ()=>import("../components/index.vue")
const Users = ()=>import("../components/info/users.vue")
const Orders = ()=>import("../components/info/orders.vue")
const Spaces = ()=>import("../components/info/spaces.vue")

import store from "../store";

const routes = [
  {
    path: "/",
    redirect: "/login"
  },
  {
    path: "/index",
    name: 'Index',
    component: Index
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
    path: '/info/users',
    name: 'Users',
    component: Users
  },
  {
    path: '/info/orders',
    name: 'Orders',
    component: Orders
  },
  {
    path: '/info/spaces',
    name: 'Spaces',
    component: Spaces
  },
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
  if(to.fullPath.search("index|info|logout|update") !== -1){
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