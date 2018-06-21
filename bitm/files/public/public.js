

define(function(require, exports, module){

    var XXTEA = require("../public/xxtea.js");

    var htp = "https",
        hostName = location.hostname,
        defaultHname = htp+"://test.izhuanbei.com";
    if((window.location.href.indexOf("https") != 0) && (hostName.indexOf("izhuanbei") > -1)){
        htp = "http";
    }
   if(hostName.indexOf("izhuanbei") > -1){
        defaultHname = htp+"://"+hostName;
    }

    var api = {
        initUrl : {
           localUrl : defaultHname+"/pro/api.php",
           loginUlr : defaultHname+"/passport/service.php?c=account",
             webUrl : defaultHname+"/yunying/rpc",
             webUrl2 : defaultHname+"/yunying2/rpc",
           moneyUrl : defaultHname+"/passport/service.php?c=reward",
            billUrl : defaultHname+"/passport/service.php?c=trade",
         messageUrl : defaultHname+"/message/message.php?c=msg",
           platUrl : defaultHname+ "/passport/service.php?c=channel",
           activePlatUrl : defaultHname+ "/passport/service.php?c=inside",
           huodongUrl : defaultHname+ "/operate/service.php?c=account",
        },
        JsonRpc : function(){
            var _temp = [];
             for(var i=0;i<arguments.length;i++){
                var jsonRpc = {
                     "jsonrpc": "2.0",
                     "params": [{}],
                     "id": 1
                 };
                 jsonRpc.id = i+1;
                for(var key in arguments[i]){
                    if(key == "method"){
                        jsonRpc.method = arguments[i][key];
                    }else{
                        jsonRpc.params[0][key]=arguments[i][key];
                    }
                 }
                 _temp.push(jsonRpc);
             }
             return _temp;
        },
        JsonpArr : function(){
            var _arr = [];
            for(var i=0;i<arguments.length;i++){
                var jsonRpc = {
                     "jsonrpc": "2.0",
                     "method": "",
                     "params": [{}],
                     "id": 1
                 };
                 jsonRpc.id = i+1;
                 jsonRpc.method = arguments[i][0];
                 if(arguments[i][1]){
                    for(var key in arguments[i][1]){
                        jsonRpc.params[0][key]=arguments[i][1][key];
                    } 
                 }else{
                    jsonRpc.params=[];
                 }                 
                 _arr.push(jsonRpc);
             }
             return _arr;
        },
        _isUrl : function(url){
            if(!url){
                return this.initUrl.localUrl;
            }else if(url.indexOf("http")>-1){
                return url;
            }else if(url.indexOf("loginUlr")>-1){
                return this.initUrl.loginUlr;
            }else if(url.indexOf("webUrl2")>-1){
                return this.initUrl.webUrl2;
            }else if(url.indexOf("webUrl")>-1){
                return this.initUrl.webUrl;
            }else if(url.indexOf("moneyUrl")>-1){
                return this.initUrl.moneyUrl;
            }else if(url.indexOf("billUrl")>-1){
                return this.initUrl.billUrl;
            }else if(url.indexOf("messageUrl")>-1){
                return this.initUrl.messageUrl;
            }else if(url.indexOf("platUrl")>-1){
                return this.initUrl.platUrl;
            }else if(url.indexOf("activePlatUrl")>-1){
                return this.initUrl.activePlatUrl;
            }else if(url.indexOf("huodongUrl")>-1){
            	return this.initUrl.huodongUrl;
            }else{
                return this.initUrl.localUrl;
            }
            
        },
        call : function(params,url){

            var localUrl = api._isUrl(url);
            var params = params.length == 1 ? params[0] : params;
            var atrue = true;
            if(hostName.indexOf("izhuanbei") > -1 && api.getId("xxtea") != "true"){
                var atrue = false;
                var key = "wlh5_H5~h5#H5";
                params = JSON.stringify(params);
                params = XXTEA.encryptToBase64(params, key);
                params ="7005" + params;      
            }else{
                params = JSON.stringify(params);
            }
            return $.ajax({
                url: localUrl,
                xhrFields: {
                    withCredentials: atrue
                },
                method:"post",
                data:params
            });
        },
        apply : function(method,params,sendUrl){
            var jsonpArr = new JsonpArr(method,params,sendUrl);
                jsonpArr._addJsonp();
                clearTimeout(t);
                var _this = this;
                t = setTimeout(function(){_this.call(jsonpArr._arr)},10);
        },
        getId : function (name){
            var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
            var r = window.location.search.substr(1).match(reg);
            if(r!=null)return  unescape(r[2]); return null;
        },
        setItem : function(key,data){
            var setcookie = new Setcookie(key,data);
            setcookie.SetItem();
        },
        getItem : function(data,key){
           return getItem(data,key);
        },
        back : function(){
            var arr = ((window.location.href).split("/"));
            arr.splice(0,3,"");
            var url = encodeURI(arr.join("/"));
            var back = '/login/login.html?back='+url;
            return back;
        },
        nav : function(nav){
            var href = window.location.pathname;
            if(href.indexOf(nav)>-1){
                return true ;
            }
            return false;
        },
        dots : function(params){
            var id = api.getItem("wlbuser","id"),
                temp = {
                    uid : id,
                    act : params.act || 1,
                    name : params.name,
                    pltf : params.pltf || "pc",
                    ts : Date.parse(new Date())
                };
            var _temp = $.extend({}, temp, params);
            var key = "wlh5_H5~h5#H5";
            _temp = JSON.stringify(_temp);
            _temp = XXTEA.encryptToBase64(_temp, key);
            _temp ="7005" + _temp;
            var http="https://stat.wanglibao.com/activity?callback=?";
            return $.ajax({
                url: http,
                dataType:"jsonp",
                method:"get",
                data:{"key" : _temp}
            });
        }
    }
    function JsonpArr(method,params,sendUrl){
        this.method = method;
        this.params = params;
        this.sendUrl = sendUrl;
        this.time = 20;
    }
    JsonpArr.prototype = {

        constructor : JsonpArr,
        _arr : [],
        _addJsonp : function(){
             var jsonRpc = {
                 "jsonrpc": "2.0",
                 "method": this.method,
                 "params": [{}],
                 "id": 1
             };
             if(this.params){
                for(var key in this.params){
                 jsonRpc.params[0][key]=this.params[key];
                }
             }else{
                jsonRpc.params=[];
             }
             
             this._arr.push(jsonRpc);
             this._setJsonp();
        },         
        _setJsonp : function(){
             var arr = this._arr;
             for(var i=0;i<arr.length;i++){
                 arr[i].id=i+1;
             }
             this._setClr();
        },
        _setClr : function(){
            var _this = this;
            setTimeout(function(){_this._arr.length=0},this.time);
        }
    };

    function Setcookie(key,data){
        this.data = data;
        this.key = key;
        this.ispos = new Ispos()._ispos();
    }
    Setcookie.prototype = {
        constructor : Setcookie, 
        SetItem : function(){
            if(!this.ispos) return "";
            localStorage.setItem(this.key,JSON.stringify(this.data));
        }   
    } 
    function getItem(data,key){
        var ispos = new Ispos()._ispos();
        if(!ispos) return "";
         var obj = localStorage.getItem(data);
         //debugger;
        var _obj = eval( "(" + obj + ")" );
        if(obj){
            if(!key){
                return _obj;
             }
             if(_obj[key]){
                return _obj[key];
             }else{
                return "";
             }  
        }else{
            return "";
        }

    }   
    function Ispos(){}
    Ispos.prototype = {
        constructor : Ispos,
         _ispos : function(){
            if(window.localStorage){
                return 1;
            }else{
              return "";
            }
        }
    }
// console.log(api.getItem("wlbuser","id"));
//localStorage.clear();
// ���÷���
     module.exports = {
         "JsonRpc" : api.JsonRpc,
        "JsonpArr" : api.JsonpArr,
            "call" : api.call,
           "getId" : api.getId,
         "setItem" : api.setItem,
         "getItem" : api.getItem,
            "back" : api.back,
             "nav" : api.nav,
            "dots" : api.dots
     };
     window.api =  {
        "JsonRpc" : api.JsonRpc,
        "JsonpArr" : api.JsonpArr,
            "call" : api.call,
           "getId" : api.getId,
         "setItem" : api.setItem,
         "getItem" : api.getItem,
            "back" : api.back,
             "nav" : api.nav,
            "dots" : api.dots
     };  
});
