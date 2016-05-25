/**
 * 
 */
var username = window.prompt("尊姓大名:");
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
 });

 function sendMessage()
 {
     var othername=$("#othername").val();
     var msg="MSG\t"+username+"_"+othername+"_"+$("#message").val();
     showGettedMsg("你",othername,$("#message").val());
     $("#message").val("");
     send(msg);
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
     var vals=data.split("\t");
     var msgType=vals[0];
     switch(msgType)
     {
     case "NAME":
         var msg=vals[1];
         var mes="NAME"+"\t"+msg+"_"+ username;
         send(mes);
         break;
     case "MSG":
         var val2s=vals[1].split("_");
         showGettedMsg(val2s[0],null,val2s[2]);
         //alert();
         break;
     default:
         break;
             
     }
 }
 //进入之后从redis获得消息
 function takeLastMsg(uname){
	 $.ajax({
		 type : "GET",
		 url : "MsgSaver?uname="+uname,
		 sync: true,
		 success : function(dt){
			 //alert(dt);
			 if (dt.length > 2){
				 var msgArr = dt.replace(/(\[|\])/g,"").split(",");
				 for (var a = 0;msgArr != undefined&&msgArr.length>0&&a < msgArr.length;a ++){
					 $(".msg_poll").append("<p>"+msgArr[a]+"<\/p>");
				 }
			 }

			 //alert("dy:"+msgArr);
		 },
		 error : function (){
			 alert("connect error");
		 }
	 });
	 
	 
 }
 
 function showGettedMsg(from,to,msgBody){
	 $(".msg_poll").append("<p>"+from+" 对"+((to == undefined || to == "" )?"你":to)+"说:‘"+msgBody+"’<\/p>");
 }
