webpackJsonp([20],{"GS+7":function(t,e,i){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var a=i("Dd8w"),n=i.n(a),s=i("+cKO"),o=i("/QaM"),r=i("NYxO"),l={name:"Forgot",components:{EmailField:o.a},data:function(){return{email:""}},methods:n()({},Object(r.b)("profile/account",["passwordRecovery"]),{submitHandler:function(){var t=this;this.$v.$invalid?this.$v.$touch():this.passwordRecovery({email:this.email}).then(function(){t.$router.push({name:"ForgotSuccess"})})}}),validations:{email:{required:s.required,email:s.email}},i18n:{messages:{en:{title:"Write your e-mail",send:"Send"},ru:{title:"Напишите ваш e-mail",send:"Отправить"}}}},u={render:function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("div",{staticClass:"forgot"},[i("h2",{staticClass:"forgot__title form__title"},[t._v(t._s(t.$t("title")))]),i("form",{staticClass:"forgot__form",on:{submit:function(e){return e.preventDefault(),t.submitHandler(e)}}},[i("email-field",{attrs:{id:"forgot-email",v:t.$v.email},model:{value:t.email,callback:function(e){t.email=e},expression:"email"}}),i("div",{staticClass:"forgot__action"},[i("button-hover",{attrs:{tag:"button",type:"submit",variant:"white"}},[t._v(t._s(t.$t("send")))])],1)],1)])},staticRenderFns:[]};var c=i("VU/8")(l,u,!1,function(t){i("tHgd")},null,null);e.default=c.exports},tHgd:function(t,e){}});
//# sourceMappingURL=20.43a2a0c0337b8d3f213d.js.map