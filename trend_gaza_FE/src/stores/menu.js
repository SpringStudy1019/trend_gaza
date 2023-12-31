import { ref } from "vue";
import { defineStore } from "pinia";


export const useMenuStore = defineStore("menuStore", () => {
    const menuList = ref([
      { name: "회원가입", show: true, routeName: "user-join" },
      { name: "로그인", show: true, routeName: "user-login" },
      { name: "알림", show: false, routeName: "notification"}, 
      { name: "마이플랜", show: false, routeName: "plan-list"}, 
      { name: "마이페이지", show: false, routeName: "user-mypage" },
      { name: "로그아웃", show: false, routeName: "main" },
    ]);
  
  const changeMenuState = () => { 
      menuList.value = menuList.value.map((item) => ({ ...item, show: !item.show }));
  };
  
  const loginState = () => {
    menuList.value = [
      { name: "회원가입", show: false, routeName: "user-join" },
      { name: "로그인", show: false, routeName: "user-login" },
      { name: "알림", show: true, routeName: "notification" },
      { name: "마이플랜", show: true, routeName: "plan-list" },
      { name: "마이페이지", show: true, routeName: "user-mypage" },
      { name: "로그아웃", show: true, routeName: "main" },
    ]
  };

  const logoutState = () => {
    menuList.value = [
      { name: "회원가입", show: true, routeName: "user-join" },
      { name: "로그인", show: true, routeName: "user-login" },
      { name: "알림", show: false, routeName: "notification" },
      { name: "마이플랜", show: false, routeName: "plan-list" },
      { name: "마이페이지", show: false, routeName: "user-mypage" },
      { name: "로그아웃", show: false, routeName: "main" },
    ]
  };
  
    return {
      menuList,
      changeMenuState,
      loginState,
      logoutState
    };
  });
  