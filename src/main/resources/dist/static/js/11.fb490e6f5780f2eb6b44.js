webpackJsonp([11],{ZNFZ:function(e,a,i){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var t=i("Dd8w"),l=i.n(t),n=i("NYxO"),s=i("+cKO"),r={name:"ShiftEmail",components:{EmailField:i("/QaM").a},data:function(){return{email:""}},methods:l()({},Object(n.b)("profile/account",["changeEmail"]),{submitHandler:function(){var e=this;this.$v.$invalid?this.$v.$touch():this.changeEmail(this.email).then(function(){e.$router.push({name:"Login"})})}}),validations:{email:{required:s.required,email:s.email}},i18n:{messages:{en:{email:"Change mail",placeholder:"New mail",change:"Change"},ru:{email:"Смена почтового ящика",placeholder:"Новый почтовый ящик",change:"Сменить"}}}},c={render:function(){var e=this,a=e.$createElement,i=e._self._c||a;return i("div",{staticClass:"shift-email"},[i("form",{staticClass:"shift-email__form",on:{submit:function(a){return a.preventDefault(),e.submitHandler(a)}}},[i("div",{staticClass:"form__block"},[i("h4",{staticClass:"form__subtitle"},[e._v(e._s(e.$t("email")))]),i("email-field",{class:{checked:e.$v.email.required&&e.$v.email.email},attrs:{id:"shift-email",v:e.$v.email,placeholder:e.$t("placeholder")},model:{value:e.email,callback:function(a){e.email=a},expression:"email"}})],1),i("div",{staticClass:"shift-email__action"},[i("button-hover",{attrs:{tag:"button",type:"submit",variant:"white"}},[e._v(e._s(e.$t("change")))])],1)])])},staticRenderFns:[]};var m=i("VU/8")(r,c,!1,function(e){i("qqdj")},null,null);a.default=m.exports},qqdj:function(e,a){}});
//# sourceMappingURL=11.fb490e6f5780f2eb6b44.js.map