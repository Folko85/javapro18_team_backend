webpackJsonp([19],{TwRG:function(t,i,e){"use strict";Object.defineProperty(i,"__esModule",{value:!0});var n=e("Dd8w"),a=e.n(n),o=e("NYxO"),s=e("+cKO"),r=e("TYx6"),l=e("/QaM"),u={name:"Login",components:{PasswordField:r.a,EmailField:l.a},data:function(){return{email:"",password:""}},computed:{redirectUrl:function(){return this.$route.query.redirect}},methods:a()({},Object(o.b)("auth/api",["login"]),Object(o.b)("profile/info",["apiInfo"]),{submitHandler:function(){var t=this;this.$v.$invalid?this.$v.$touch():this.login({email:this.email,password:this.password}).then(function(){t.apiInfo().then(function(){t.$router.push({name:t.redirectUrl||"News"})})}).catch(function(t){})}}),validations:{email:{required:s.required,email:s.email},password:{required:s.required,minLength:Object(s.minLength)(8)}},i18n:{messages:{en:{title:"Log in",login:"Sign in",forgot:"Forgot your password?"},ru:{title:"Войдите в аккаунт",login:"Войти",forgot:"Забыли пароль?"}}}},d={render:function(){var t=this,i=t.$createElement,e=t._self._c||i;return e("div",{staticClass:"login"},[e("h2",{staticClass:"login__title form__title"},[t._v(t._s(t.$t("title")))]),e("form",{staticClass:"login__form",on:{submit:function(i){return i.preventDefault(),t.submitHandler(i)}}},[e("email-field",{attrs:{id:"login-email",v:t.$v.email},model:{value:t.email,callback:function(i){t.email=i},expression:"email"}}),e("password-field",{attrs:{id:"login-password",v:t.$v.password,autocomplete:"current-password"},model:{value:t.password,callback:function(i){t.password=i},expression:"password"}}),e("div",{staticClass:"login__action"},[e("button-hover",{attrs:{tag:"button",type:"submit",variant:"white"}},[t._v(t._s(t.$t("login")))]),e("router-link",{staticClass:"login__link",attrs:{to:{name:"Forgot"}}},[t._v(t._s(t.$t("forgot")))])],1)],1)])},staticRenderFns:[]};var c=e("VU/8")(u,d,!1,function(t){e("iKNG")},"data-v-698a7de0",null);i.default=c.exports},iKNG:function(t,i){}});
//# sourceMappingURL=19.4f52dbdcff5f65ddaa94.js.map