webpackJsonp([6],{Eu1L:function(e,t,i){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var s=i("Dd8w"),n=i.n(s),a=i("UBpT"),o=i("XjPE"),l=i("tJ7h"),r=i("7PTA"),c=i("0Hd5"),f=i("NYxO"),d={name:"Profile",components:{FriendsPossible:a.a,FriendsRequest:o.a,ProfileInfo:l.a,NewsAdd:r.a,NewsBlock:c.a},data:function(){return{activeTab:"POSTED"}},computed:n()({},Object(f.c)("profile/info",["getInfo"]),Object(f.c)("users/info",["getWall","getWallPostedLength","getWallQueuedLength","getWallDeletedLength"]),{activeWall:function(){var e=[];for(var t in this.getWall)this.getWall[t].type===this.activeTab&&e.push(this.getWall[t]);return e}}),methods:n()({},Object(f.b)("users/info",["apiWall"]),{changeTab:function(e){this.activeTab=e}}),watch:{getInfo:function(){this.apiWall({id:this.getInfo.id})}},created:function(){this.getInfo&&this.apiWall({id:this.getInfo.id})},i18n:{messages:{en:{posted:"My publications",queued:"Queued publication",deleted:"Deleted publications"},ru:{posted:"Мои публикации",queued:"Отложенные публикации",deleted:"Удаленные публикации"}}}},_={render:function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",{staticClass:"profile inner-page"},[i("div",{staticClass:"inner-page__main"},[i("div",{staticClass:"profile__info"},[i("profile-info",{attrs:{me:"me",online:"online",info:e.getInfo}})],1),i("div",{staticClass:"profile__news"},[i("div",{staticClass:"profile__tabs"},[e.getWallPostedLength>0?i("span",{staticClass:"profile__tab",class:{active:"POSTED"===e.activeTab},on:{click:function(t){return e.changeTab("POSTED")}}},[e._v(e._s(e.$t("posted"))+" ("+e._s(e.getWallPostedLength)+")")]):e._e(),e.getWallQueuedLength>0?i("span",{staticClass:"profile__tab",class:{active:"QUEUED"===e.activeTab},on:{click:function(t){return e.changeTab("QUEUED")}}},[e._v(e._s(e.$t("queued"))+" ("+e._s(e.getWallQueuedLength)+")")]):e._e(),e.getWallDeletedLength>0?i("span",{staticClass:"profile__tab",class:{active:"DELETED"===e.activeTab},on:{click:function(t){return e.changeTab("DELETED")}}},[e._v(e._s(e.$t("deleted"))+" ("+e._s(e.getWallDeletedLength)+")")]):e._e()]),i("div",{staticClass:"profile__add"},[i("news-add")],1),i("div",{staticClass:"profile__news-list"},e._l(e.activeWall,function(t){return i("news-block",{key:t.id,attrs:{edit:"edit",deleted:"deleted",deffered:"QUEUED"===e.activeTab,info:t}})}),1)])]),i("div",{staticClass:"inner-page__aside"},[i("friends-request"),i("br"),i("friends-possible")],1)])},staticRenderFns:[]},u=i("VU/8")(d,_,!1,null,null,null);t.default=u.exports},serr:function(e,t){},tJ7h:function(e,t,i){"use strict";var s=i("Dd8w"),n=i.n(s),a=i("NYxO");function o(e,t){return t[e%100>4&&e%100<20?2:[2,0,1,1,1,2][e%10<5?e%10:5]]}var l={name:"ProfileInfo",components:{Modal:i("/o5o").a},props:{me:Boolean,online:Boolean,blocked:Boolean,friend:Boolean,info:Object},data:function(){return{modalShow:!1,modalText:"",modalType:"deleteFriend"}},computed:n()({},Object(a.c)("profile/dialogs",["dialogs"]),{statusText:function(){return"en"===localStorage.getItem("lang")?this.online?"online":"offline":this.online?"онлайн":"не в сети"},blockedText:function(){return"en"===localStorage.getItem("lang")?this.blocked?"The user is blocked":"Block":this.blocked?"Пользователь заблокирован":"Заблокировать"},btnVariantInfo:function(){return"en"===localStorage.getItem("lang")?this.blocked?{variant:"red",text:"Unblock"}:this.friend?{variant:"red",text:"Remove from friends"}:{variant:"white",text:"Add as Friend"}:this.blocked?{variant:"red",text:"Разблокировать"}:this.friend?{variant:"red",text:"Удалить из друзей"}:{variant:"white",text:"Добавить в друзья"}}}),methods:n()({declOfNum:o,yearsOld:function(e){return"en"===localStorage.getItem("lang")?"years":o(e,["год","года","лет"])}},Object(a.b)("users/actions",["apiBlockUser","apiUnblockUser"]),Object(a.b)("profile/friends",["apiAddFriends","apiDeleteFriends"]),Object(a.b)("profile/dialogs",["createDialogWithUser","apiLoadAllDialogs"]),Object(a.b)("users/info",["apiInfo"]),{blockedUser:function(){this.blocked||(this.modalText="en"===localStorage.getItem("lang")?"Are you sure you want to block the user "+this.info.fullName:"Вы уверены, что хотите заблокировать пользователя "+this.info.fullName,this.modalShow=!0,this.modalType="block")},profileAction:function(){var e=this;if(!this.blocked)return this.friend?(this.modalText="Вы уверены, что хотите удалить пользователя "+this.info.fullName+" из друзей?",this.modalShow=!0,void(this.modalType="deleteFriend")):void this.apiAddFriends(this.info.id).then(function(){e.apiInfo(e.$route.params.id)});this.apiUnblockUser(this.$route.params.id).then(function(){e.apiInfo(e.$route.params.id)})},closeModal:function(){this.modalShow=!1},onConfirm:function(){var e=this;"block"!==this.modalType?this.apiDeleteFriends(this.$route.params.id).then(function(){e.apiInfo(e.$route.params.id),e.closeModal()}):this.apiBlockUser(this.$route.params.id).then(function(){e.apiInfo(e.$route.params.id),e.closeModal()})},onSentMessage:function(){if(this.blocked)return!1;this.$router.push({name:"Im",query:{userId:this.info.id}})}}),i18n:{messages:{en:{sendMessage:"Send message",birthday:"Date of Birth",tel:"Telephone",city:"Country, city",myself:"About myself",info:"not filled",yes:"Yes",cancel:"Сancel"},ru:{sendMessage:"Написать сообщение",birthday:"Дата рождения",tel:"Телефон",city:"Страна, город",myself:"О себе",info:"не заполнено",yes:"Да",cancel:"Отмена"}}}},r={render:function(){var e=this,t=e.$createElement,i=e._self._c||t;return e.info?i("div",{staticClass:"profile-info"},[i("div",{staticClass:"profile-info__pic"},[i("div",{staticClass:"profile-info__img",class:{offline:!e.online&&!e.me}},[i("img",{attrs:{src:e.info.photo,alt:e.info.fullName}})]),e.me?e._e():i("div",{staticClass:"profile-info__actions"},[i("button-hover",{attrs:{disable:e.blocked},nativeOn:{click:function(t){return e.onSentMessage(t)}}},[e._v(e._s(e.$t("sendMessage")))]),i("button-hover",{staticClass:"profile-info__add",attrs:{variant:e.btnVariantInfo.variant,bordered:"bordered"},nativeOn:{click:function(t){return e.profileAction(t)}}},[e._v(e._s(e.btnVariantInfo.text))])],1)]),i("div",{staticClass:"profile-info__main"},[e.me?i("router-link",{staticClass:"edit",attrs:{to:{name:"Settings"}}},[i("simple-svg",{attrs:{filepath:"/static/img/edit.svg"}})],1):i("span",{staticClass:"profile-info__blocked",class:{blocked:e.blocked},on:{click:e.blockedUser}},[e._v(e._s(e.blockedText))]),i("div",{staticClass:"profile-info__header"},[i("h1",{staticClass:"profile-info__name"},[e._v(e._s(e.info.fullName))]),i("span",{staticClass:"user-status",class:{online:e.online,offline:!e.online}},[e._v(e._s(e.statusText))])]),i("div",{staticClass:"profile-info__block"},[i("span",{staticClass:"profile-info__title"},[e._v(e._s(e.$t("birthday"))+":")]),e.info.birth_date?i("span",{staticClass:"profile-info__val"},[e._v(e._s(e._f("moment")(e.info.birth_date,"D MMMM YYYY"))+" ("+e._s(e.info.ages)+" "+e._s(e.yearsOld(e.info.ages))+")")]):i("span",{staticClass:"profile-info__val"},[e._v(e._s(e.$t("info")))])]),i("div",{staticClass:"profile-info__block"},[i("span",{staticClass:"profile-info__title"},[e._v(e._s(e.$t("tel"))+":")]),e.info.phone?i("a",{staticClass:"profile-info__val",attrs:{href:"tel:"+e.info.phone}},[e._v(e._s(e._f("phone")(e.info.phone)))]):i("a",{staticClass:"profile-info__val"},[e._v(e._s(e.$t("info")))])]),i("div",{staticClass:"profile-info__block"},[i("span",{staticClass:"profile-info__title"},[e._v(e._s(e.$t("city"))+":")]),e.info.country?i("span",{staticClass:"profile-info__val"},[e._v(e._s(e.info.country)+", "+e._s(e.info.city))]):i("span",{staticClass:"profile-info__val"},[e._v(e._s(e.$t("info")))])]),i("div",{staticClass:"profile-info__block"},[i("span",{staticClass:"profile-info__title"},[e._v(e._s(e.$t("myself"))+":")]),e.info.about?i("span",{staticClass:"profile-info__val"},[e._v(e._s(e.info.about))]):i("span",{staticClass:"profile-info__val"},[e._v(e._s(e.$t("info")))])])],1),i("modal",{model:{value:e.modalShow,callback:function(t){e.modalShow=t},expression:"modalShow"}},[e.modalText?i("p",[e._v(e._s(e.modalText))]):e._e(),i("template",{slot:"actions"},[i("button-hover",{nativeOn:{click:function(t){return t.preventDefault(),e.onConfirm(t)}}},[e._v(e._s(e.$t("yes")))]),i("button-hover",{attrs:{variant:"red",bordered:"bordered"},nativeOn:{click:function(t){return e.closeModal(t)}}},[e._v(e._s(e.$t("cancel")))])],1)],2)],1):e._e()},staticRenderFns:[]};var c=i("VU/8")(l,r,!1,function(e){i("serr")},null,null);t.a=c.exports}});
//# sourceMappingURL=6.db559cbcf1814fc61afc.js.map