// sea 配置文件
//var hostName = location.hostname, js = "/js/";
//if (hostName.indexOf(":3001") > -1) {
//  js = '/iZhuanBei/js/';
//}
var hostName = location.hostname,
js = "/js/";
hostName.indexOf("izhuanbei") < 0 && (js = "/js/"),
seajs.config({
   /* // Sea.js 的基础路径
    base: js,
    // 路径映射
    paths: {},
    // 别名配置*/
    alias: {
        "jquery": "jquery.js?v=@version@",
        "header": "public/header.js?v=@version@",
        "template": "public/template.js?v=@version@",
        "public": "public/public.js?v=@version@",
        "jsonrpc": "public/jsonrpc.js?v=@version@",
        "wx": "public/wx.js?v=@version@",
        "webview": "public/webview.js?v=@version@",
        "sha1": "public/sha1.js?v=@version@",
        "xxtea": "public/xxtea.js?v=@version@",
        "shouye":"shouye/shouye.js?v=@version@",
        "nav_footer":"nav_footer/nav_footer.js?v=@version@",
        "gonggao":"gonggao/gonggao.js?v=@version@",
        "safety":"safety/safety.js?v=@version@",
        "beginner":"beginner/beginner.js?v=@version@",
        "xiangqingye":"gonggao/xiangqingye.js?v=@version@",
        "media":"media/media.js?v=@version@",
        'commonalities':'commonalities.js?v=@version@',
        "media_details":"media/media_details.js?v=@version@",
        "reeor":"reeor.js?v=@version@",
        "chinabank":"chinabank/chinabank.js?v=@version@"
    },
    // 文件编码
    charset: "utf-8"
});

if (typeof define === "function") {
    define.cmd = true;
}
;

















