webpackJsonp([15],{"GS+7":function(t,e,i){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var a=i("Dd8w"),n=i.n(a),o=i("+cKO"),s=i("/QaM"),r=i("NYxO"),l={name:"Forgot",components:{EmailField:s.a},data:function(){return{email:""}},methods:n()({},Object(r.b)("profile/account",["passwordRecoveryConfirmation"]),Object(r.d)("profile/account",["setEmail"]),{submitHandler:function(){var t=this;this.$v.$invalid?this.$v.$touch():this.passwordRecoveryConfirmation({email:this.email}).then(function(e){200==e.status&&(t.setEmail(t.email),t.$router.push({name:"ForgotSuccessConfirmation"}))})}}),validations:{email:{required:o.required,email:o.email}},i18n:{messages:{en:{title:"Write your code",send:"Send"},ru:{title:"Напишите ваш code",send:"Отправить"}}}},c={render:function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("div",{staticClass:"forgot"},[i("h2",{staticClass:"forgot__title form__title"},[t._v(t._s(t.$t("title")))]),i("form",{staticClass:"forgot__form",on:{submit:function(e){return e.preventDefault(),t.submitHandler(e)}}},[i("email-field",{attrs:{id:"forgot-email",v:t.$v.email},model:{value:t.email,callback:function(e){t.email=e},expression:"email"}}),i("div",{staticClass:"forgot__action"},[i("button-hover",{attrs:{tag:"button",type:"submit",variant:"white"}},[t._v(t._s(t.$t("send")))])],1)],1)])},staticRenderFns:[]};var u=i("VU/8")(l,c,!1,function(t){i("cb5h")},null,null);e.default=u.exports},cb5h:function(t,e){}});
//# sourceMappingURL=15.16b7db72ae3bc2080f8b.js.map