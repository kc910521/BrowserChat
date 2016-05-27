<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<script type="text/javascript" src="js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="js/mainjs.js"></script>
<title>NEVER THE LAND</title>
</head>
<style style="text/css">
.msg_poll {
	width: 40%;
	min-width: 200px;
	height: 200px;
	overflow-y: scroll;
	background-color: #DDDDDD;
}

.name_for_click {
	cursor: pointer;
	text-decoration: underline;
	color: blue;
}
</style>
<body>
	<div class="container">
		<table>
			<tr>
				<td>对方姓名</td>
				<td><input type="text" id="othername"></td>
			</tr>
			<tr>
				<td>内容</td>
				<td><input type="text" id="message"></td>
			</tr>
			<tr>
				<td><input id="sendbutton" type="button" value="发送"
					onClick="click" disabled="true" /></td>
			</tr>
		</table>
		<div class="msg_poll">
			<p>最近的消息:</p>
		</div>
	</div>
</body>
<script type="text/javascript">
	/*   var ws = new WebSocket("ws://localhost:8080/WebSer/wsocket");
	 ws.onopen = function()
	 {
	 console.log("open");
	 ws.send("hello");
	 };
	 ws.onmessage = function(evt)
	 {
	 console.log(evt.data)
	 };
	 ws.onclose = function(evt)
	 {
	 console.log("WebSocketClosed!");
	 };
	 ws.onerror = function(evt)
	 {
	 console.log("WebSocketError!");
	 }; */
</script>

</html>
