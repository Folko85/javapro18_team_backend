webpackJsonp([9],{Ju2O:function(e,t){e.exports=function(e){var t={};function i(n){if(t[n])return t[n].exports;var r=t[n]={i:n,l:!1,exports:{}};return e[n].call(r.exports,r,r.exports,i),r.l=!0,r.exports}return i.m=e,i.c=t,i.d=function(e,t,n){i.o(e,t)||Object.defineProperty(e,t,{enumerable:!0,get:n})},i.r=function(e){"undefined"!=typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},i.t=function(e,t){if(1&t&&(e=i(e)),8&t)return e;if(4&t&&"object"==typeof e&&e&&e.__esModule)return e;var n=Object.create(null);if(i.r(n),Object.defineProperty(n,"default",{enumerable:!0,value:e}),2&t&&"string"!=typeof e)for(var r in e)i.d(n,r,function(t){return e[t]}.bind(null,r));return n},i.n=function(e){var t=e&&e.__esModule?function(){return e.default}:function(){return e};return i.d(t,"a",t),t},i.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},i.p="",i(i.s="fb15")}({"1eb2":function(e,t,i){"use strict";var n;"undefined"!=typeof window&&((n=window.document.currentScript)&&(n=n.src.match(/(.+\/)[^/]+\.js(\?.*)?$/))&&(i.p=n[1]))},2877:function(e,t,i){"use strict";function n(e,t,i,n,r,a,s,o){var c,d="function"==typeof e?e.options:e;if(t&&(d.render=t,d.staticRenderFns=i,d._compiled=!0),n&&(d.functional=!0),a&&(d._scopeId="data-v-"+a),s?(c=function(e){(e=e||this.$vnode&&this.$vnode.ssrContext||this.parent&&this.parent.$vnode&&this.parent.$vnode.ssrContext)||"undefined"==typeof __VUE_SSR_CONTEXT__||(e=__VUE_SSR_CONTEXT__),r&&r.call(this,e),e&&e._registeredComponents&&e._registeredComponents.add(s)},d._ssrRegister=c):r&&(c=o?function(){r.call(this,this.$root.$options.shadowRoot)}:r),c)if(d.functional){d._injectStyles=c;var l=d.render;d.render=function(e,t){return c.call(t),l(e,t)}}else{var u=d.beforeCreate;d.beforeCreate=u?[].concat(u,c):[c]}return{exports:e,options:d}}i.d(t,"a",function(){return n})},"407a":function(e,t,i){"use strict";i.r(t);var n=i("b445"),r=i("fa1f");for(var a in r)"default"!==a&&function(e){i.d(t,e,function(){return r[e]})}(a);var s=i("2877"),o=Object(s.a)(r.default,n.a,n.b,!1,null,null,null);o.options.__file="hcaptcha.vue",t.default=o.exports},"56d7":function(e,t,i){"use strict";(function(e){Object.defineProperty(t,"__esModule",{value:!0}),t.install=a,t.default=void 0;var n,r=(n=i("407a"))&&n.__esModule?n:{default:n};function a(e){a.installed||(a.installed=!0,e.component("vuehcaptcha",r.default),e.component("VueHcaptcha",r.default))}var s={install:a},o=null;"undefined"!=typeof window?o=window.Vue:void 0!==e&&(o=e.Vue),o&&o.use(s);var c=r.default;t.default=c}).call(this,i("c8ba"))},"7c22":function(e,t,i){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),t.default=void 0;var n=i("dfcd"),r={name:"VueHcaptcha",props:{sitekey:{type:String,required:!0},theme:{type:String,default:void 0},size:{type:String,default:void 0},tabindex:{type:String,default:void 0},language:{type:String,default:void 0},reCaptchaCompat:{type:Boolean,default:!0},challengeContainer:{type:String,default:void 0},rqdata:{type:String,default:void 0},sentry:{type:Boolean,default:!0},apiEndpoint:{type:String,default:"https://hcaptcha.com/1/api.js"},endpoint:{type:String,default:void 0},reportapi:{type:String,default:void 0},assethost:{type:String,default:void 0},imghost:{type:String,default:void 0}},data:function(){return{widgetId:null,hcaptcha:null}},mounted:function(){return(0,n.loadApiEndpointIfNotAlready)(this.$props).then(this.onApiLoaded).catch(this.onError)},unmounted:function(){var e=this;this.widgetId&&this.hcaptcha.then(function(){e.hcaptcha.reset(e.widgetId),e.hcaptcha.remove(e.widgetId)})},methods:{onApiLoaded:function(){this.hcaptcha=window.hcaptcha;var e={sitekey:this.sitekey,theme:this.theme,size:this.size,tabindex:this.tabindex,callback:this.onVerify,"expired-callback":this.onExpired,"chalexpired-callback":this.onChallengeExpired,"error-callback":this.onError,"open-callback":this.onOpen,"close-callback":this.onClose};this.challengeContainer&&(e["challenge-container"]=this.challengeContainer),this.widgetId=this.hcaptcha.render(this.$el,e),this.rqdata&&this.hcaptcha.setData(this.widgetId,{rqdata:this.rqdata}),this.onRendered()},execute:function(){this.widgetId?(this.hcaptcha.execute(this.widgetId),this.onExecuted()):this.$on("rendered",this.execute)},reset:function(){this.widgetId?(this.hcaptcha.reset(this.widgetId),this.onReset()):this.$emit("error","Element is not rendered yet and thus cannot reset it. Wait for `rendered` event to safely call reset.")},onRendered:function(){this.$emit("rendered")},onExecuted:function(){this.$emit("executed")},onReset:function(){this.$emit("reset")},onError:function(e){this.$emit("error",e),this.reset()},onVerify:function(){var e=this.hcaptcha.getResponse(this.widgetId),t=this.hcaptcha.getRespKey(this.widgetId);this.$emit("verify",e,t)},onExpired:function(){this.$emit("expired")},onChallengeExpired:function(){this.$emit("challengeExpired")},onOpen:function(){this.$emit("opened")},onClose:function(){this.$emit("closed")}}};t.default=r},b445:function(e,t,i){"use strict";var n=function(){var e=this.$createElement;return(this._self._c||e)("div",{attrs:{id:"hcap-script"}})},r=[];i.d(t,"a",function(){return n}),i.d(t,"b",function(){return r})},c8ba:function(e,t){var i;i=function(){return this}();try{i=i||new Function("return this")()}catch(e){"object"==typeof window&&(i=window)}e.exports=i},dfcd:function(e,t,i){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),t.loadApiEndpointIfNotAlready=function(e){if(window.hcaptcha)return r(),o;if(document.getElementById(n))return o;window[s]=r;var t=c(e),i=document.createElement("script");return i.id=n,i.src=t,i.async=!0,i.defer=!0,i.onerror=function(e){console.error("Failed to load api: "+t,e),a("Failed to load api.js")},document.head.appendChild(i),o},t.getScriptSrc=c,t.addQueryParamIfDefined=d,t.SCRIPT_ID=void 0;var n="hcaptcha-api-script-id";t.SCRIPT_ID=n;var r,a,s="_hcaptchaOnLoad",o=new Promise(function(e,t){r=e,a=t});function c(e){var t=e.apiEndpoint;return t=d(t=d(t=d(t=d(t=d(t=d(t=d(t=d(t=d(t,"render","explicit"),"onload",s),"recaptchacompat",!1===e.reCaptchaCompat?"off":null),"hl",e.language),"sentry",e.sentry),"endpoint",e.endpoint),"assethost",e.assethost),"imghost",e.imghost),"reportapi",e.reportapi)}function d(e,t,i){return void 0!==i&&null!==i?e+(e.includes("?")?"&":"?")+t+"="+encodeURIComponent(i):e}},fa1f:function(e,t,i){"use strict";i.r(t);var n=i("7c22"),r=i.n(n);for(var a in n)"default"!==a&&function(e){i.d(t,e,function(){return n[e]})}(a);t.default=r.a},fb15:function(e,t,i){"use strict";i.r(t);i("1eb2");var n=i("56d7"),r=i.n(n);for(var a in n)"default"!==a&&function(e){i.d(t,e,function(){return n[e]})}(a);t.default=r.a}})},"T+lW":function(e,t){},cxj3:function(e,t,i){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var n=i("Dd8w"),r=i.n(n),a=i("NYxO"),s=i("+cKO"),o=i("TYx6"),c=i("i53X"),d=i("/QaM"),l={name:"EmailField",props:{value:{type:String,default:""},v:{type:Object,required:!0},label:{type:String,default:"Имя"},id:{type:String,required:!0}},computed:{name:{get:function(){return this.value},set:function(e){this.$emit("input",e)}}},i18n:{messages:{en:{errorRequired:"Required field",errorMin:"Minimum number of characters"},ru:{errorRequired:"Обязательно поле",errorMin:"Минимальное количество символов"}}}},u={render:function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",{staticClass:"form__group",class:{fill:e.name.length>0}},[i("input",{directives:[{name:"model",rawName:"v-model",value:e.name,expression:"name"}],staticClass:"form__input",class:{invalid:e.v.$dirty&&!e.v.required||e.v.$dirty&&!e.v.minLength},attrs:{id:e.id,name:"name"},domProps:{value:e.name},on:{change:function(t){return e.v.$touch()},input:function(t){t.target.composing||(e.name=t.target.value)}}}),i("label",{staticClass:"form__label",attrs:{for:e.id}},[e._v(e._s(e.label))]),e.v.$dirty&&!e.v.required?i("span",{staticClass:"form__error"},[e._v(e._s(e.$t("errorRequired")))]):e.v.$dirty&&!e.v.minLength?i("span",{staticClass:"form__error"},[e._v(e._s(e.$t("errorMin"))+" "+e._s(e.v.minLength))]):e._e()])},staticRenderFns:[]},f=i("VU/8")(l,u,!1,null,null,null).exports,p={name:"ConfirmField",props:{value:{type:Boolean,default:""},v:{type:Object,required:!0},id:{type:String,required:!0}},methods:r()({},Object(a.b)("auth/api",["modalOn"])),computed:{confirm:{get:function(){return this.value},set:function(e){this.$emit("input",e)}}},i18n:{messages:{en:{policy:"I agree with",policy2:"the privacy policy",policy3:"and the transfer",policy4:"of personal data",header:"Privacy Policy",header2:"Personal Information"},ru:{policy:"Я согласен с",policy2:"политикой конфиденциальности",policy3:"и передачей",policy4:"персональных данных",header:"Политика конфиденциальности",header2:"Персональные данные"}}}},m={render:function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",{staticClass:"form__group"},[i("input",{directives:[{name:"model",rawName:"v-model",value:e.confirm,expression:"confirm"}],staticClass:"form__checkbox",class:{invalid:e.v.$dirty&&!e.v.sameAs},attrs:{type:"checkbox",name:"confirm",id:e.id},domProps:{checked:Array.isArray(e.confirm)?e._i(e.confirm,null)>-1:e.confirm},on:{change:function(t){var i=e.confirm,n=t.target,r=!!n.checked;if(Array.isArray(i)){var a=e._i(i,null);n.checked?a<0&&(e.confirm=i.concat([null])):a>-1&&(e.confirm=i.slice(0,a).concat(i.slice(a+1)))}else e.confirm=r}}}),i("label",{staticClass:"form__checkbox-label",attrs:{for:e.id}},[e._v(e._s(e.$t("policy"))+"  "),i("a",{on:{click:function(t){e.modalOn({header:e.$t("header"),link:"http://31.40.251.201:8086/policy.html"})}}},[e._v(e._s(e.$t("policy2"))+"  ")]),e._v(e._s(e.$t("policy3"))+"  "),i("a",{on:{click:function(t){e.modalOn({header:e.$t("header2"),link:"http://31.40.251.201:8086/personal-data.html"})}}},[e._v(e._s(e.$t("policy4"))+".")])])])},staticRenderFns:[]},h=i("VU/8")(p,m,!1,null,null,null).exports,v=i("Ju2O"),g=i.n(v),_={name:"Registration",components:{PasswordField:o.a,EmailField:d.a,NameField:f,PasswordRepeatField:c.a,ConfirmField:h,VueHcaptcha:g.a},data:function(){return{email:"",passwd1:"",passwd2:"",firstName:"",lastName:"",verified:!1,token:null,eKey:null,confirm:!1}},computed:{getLang:function(){return localStorage.getItem("lang")}},methods:r()({onVerify:function(e){this.verified=!0,this.token=e}},Object(a.b)("auth/api",["register"]),{submitHandler:function(){if(console.log(this.$invalid),this.$v.$invalid)this.$v.$touch();else{var e=this.email,t=this.passwd1,i=this.firstName,n=this.lastName,r=this.token;this.register({email:e,passwd1:t,firstName:i,lastName:n,token:r})}}}),validations:{confirm:{sameAs:Object(s.sameAs)(function(){return!0})},email:{required:s.required,email:s.email},passwd1:{required:s.required,minLength:Object(s.minLength)(8)},passwd2:{required:s.required,minLength:Object(s.minLength)(8),sameAsPassword:Object(s.sameAs)("passwd1")},firstName:{required:s.required,minLength:Object(s.minLength)(3)},lastName:{required:s.required,minLength:Object(s.minLength)(3)}},i18n:{messages:{en:{account:"Account",personal:"Personal data",registration:"Registration",name:"Name",lastname:"Last name"},ru:{account:"Аккаунт",personal:"Личные данные",registration:"Зарегистрироваться",name:"Имя",lastname:"Фамилия"}}}},y={render:function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",{staticClass:"registration"},[i("form",{staticClass:"registration__form",on:{submit:function(t){return t.preventDefault(),e.submitHandler(t)}}},[i("div",{staticClass:"form__block"},[i("h4",{staticClass:"form__subtitle"},[e._v(e._s(e.$t("account")))]),i("email-field",{class:{checked:e.$v.email.required&&e.$v.email.email},attrs:{id:"register-email",v:e.$v.email},model:{value:e.email,callback:function(t){e.email=t},expression:"email"}}),i("password-field",{class:{checked:e.$v.passwd1.required&&e.$v.passwd2.sameAsPassword&&e.$v.passwd1.minLength},attrs:{id:"register-password",v:e.$v.passwd1,info:"info",registration:"registration",autocomplete:"new-password"},model:{value:e.passwd1,callback:function(t){e.passwd1=t},expression:"passwd1"}}),i("password-repeat-field",{class:{checked:e.$v.passwd1.required&&e.$v.passwd2.sameAsPassword&&e.$v.passwd2.minLength},attrs:{id:"register-repeat-password",v:e.$v.passwd2,autocomplete:"new-password"},model:{value:e.passwd2,callback:function(t){e.passwd2=t},expression:"passwd2"}})],1),i("div",{staticClass:"form__block"},[i("h4",{staticClass:"form__subtitle"},[e._v(e._s(e.$t("personal")))]),i("name-field",{attrs:{id:"register-firstName",v:e.$v.firstName,label:e.$t("name")},model:{value:e.firstName,callback:function(t){e.firstName=t},expression:"firstName"}}),i("name-field",{attrs:{id:"register-lastName",v:e.$v.lastName,label:e.$t("lastname")},model:{value:e.lastName,callback:function(t){e.lastName=t},expression:"lastName"}})],1),i("div",{staticClass:"form__block"},[i("confirm-field",{attrs:{id:"register-confirm",v:e.$v.confirm},model:{value:e.confirm,callback:function(t){e.confirm=t},expression:"confirm"}})],1),i("div",{staticClass:"registration__action"},[i("button-hover",{attrs:{tag:"button",type:"submit",variant:"white"}},[e._v(e._s(e.$t("registration")))])],1)])])},staticRenderFns:[]};var b=i("VU/8")(_,y,!1,function(e){i("T+lW")},null,null);t.default=b.exports}});
//# sourceMappingURL=9.7bbd2548dbcfd2153f33.js.map