/**
 * 
 */
var username = window.prompt("您的尊姓大名:");
var ws;
document.write("Welcome<p id=\"username\">"+username+"</p>");

if (!window.WebSocket && window.MozWebSocket){
	 window.WebSocket=window.MozWebSocket;
}
if (!window.WebSocket){
	 alert("No Support WebSocket");
}

 $(document).ready(function(){
	 $("#sendbutton").attr("disabled", false);
	 if (username != undefined && username != ""){
		 startWebSocket();
		 $("#sendbutton").click(sendMessage);
	     takeLastMsg(username);
	 }else {
		 window.location.reload();
	 }
	 //快捷选择人物效果
	 $(".container").on("click",".name_for_click",function(){
		 findContacts($(this).text());
	 });
	 
 });

 function sendMessage()
 {
//     var othername=$("#othername").val();
//     var msg="MSG\t"+username+"_"+othername+"_"+$("#message").val();
     send(packMsgToJson("commonMsg",username,$("#othername").val(),$("#message").val()));
     showGettedMsg(username,$("#othername").val(),$("#message").val());
     $("#message").val("");
 }
 
 /**
  * 组装为json
  * @param from
  * @param to
  * @param content
  * @param inRegister
  * @returns {String}
  */
 function packMsgToJson(regId,from,to,content,inRegister){
	 return "{" +
	 		"\"regId\":\""+regId+"\"," +
	 		"\"from\":\""+from+"\"," +
	 		"\"to\":\""+to+"\"," +
	 		"\"content\":\""+content+"\"," +
	 		"\"inRegister\":\""+(inRegister == undefined?"false":inRegister)+"\"" +
	 				"}";
 }
 
 function send(data)
 {
     console.log("Send:"+data);
     ws.send(data);
 }
 function startWebSocket()
 {    
	 console.log("location.host:"+location.host);
     ws = new WebSocket("ws://" + location.host + "/WebSer/wsocket");
     ws.onopen = function(){
         console.log("success open");
         $("#sendbutton").attr("disabled", false);
     };
      ws.onmessage = function(event)
      {
          console.log("RECEIVE:"+event.data);
          handleData(event.data); 
      };
      ws.onclose = function(event) { 
     console.log("Client notified socket has closed",event); 
   }; 
   
 }

 function handleData(data)
 {
	 //在这里，第一次的建立连接会走入case的
	 //name回掉一下
     var jsnVals=JSON.parse(data);//data.split("\t");
     //var msgType=vals[0];
     //true为第一次进入的注册请求，false为普通消息推送
     if (jsnVals != undefined && jsnVals["inRegister"] != undefined){
    	 if (jsnVals["inRegister"] == true){
    		 //注册
	         //var msg=vals[1];
	         //var mes="NAME"+"\t"+msg+"_"+ username;
	         var mes = packMsgToJson(jsnVals["regId"],username,"",jsnVals["content"],true);
	         send(mes);
    	 }else{
    		 //得到消息
	         //var val2s=vals[1].split("_");
	         showGettedMsg(jsnVals["from"],jsnVals["to"],jsnVals["content"]);
    	 }
     }else{
    	 alert("dame error:"+data);
     }
 }
 //进入之后从redis获得消息
 function takeLastMsg(uname){
	 $.ajax({
		 type : "POST",
		 url : "MsgSaver",
		 data : {
			 uname : uname+""
		 },
		 sync: true,
		 success : function(dt){
			 //alert("inredis:"+dt);
			 if (dt.length > 2){
				 var json1 = JSON.parse(dt);
				 //console.log(JSON.parse(json1[0]).from+",e1:"+json1[0]);
				 for (var idx = 0; idx < json1.length; idx ++){
					 var jsOj = JSON.parse(json1[idx]);
					 $(".msg_poll").append("<p>"+jsOj.from+" 对 "+jsOj.to+" 说： "+jsOj.content+"<\/p>");
				 }
				 //var msgArr = dt.replace(/(\[|\])/g,"").split(",");
//				 for (var a = 0;msgArr != undefined&&msgArr.length>0&&a < msgArr.length;a ++){
//					 $(".msg_poll").append("<p>"+msgArr[a]+"<\/p>");
//				 }
			 }
			 //alert("dy:"+msgArr);
		 },
		 error : function (){
			 alert("connect error");
		 }
	 });
 }
 
 function showGettedMsg(from,to,msgBody){
	 $(".msg_poll").append("<p><span class=\"name_for_click\">"+from+"<\/span> 对<span class=\"name_for_click\">"+((to == undefined || to == "" )?"你":to)+"<\/span>说:‘"+msgBody+"’<\/p>");
 }
 //点击传参，快速设置username到接收者input
 function findContacts(user){
	 if ("你" != user){
		 $("#othername").val(user+"");
	 }
 }
