var prePsdLength;

$(function() {
	bindPasswordInput();
	bindEnterKeyEvent();
	var userStr = document.cookie.replace(/.*userId=(.*)/, "$1");
	var userId = userStr.split((";"))[0];
	var passwordStr = document.cookie.replace(/.*password=(.*)/, "$1");
	var password = passwordStr.split((";"))[0];
	var rememberStr = document.cookie.replace(/.*remember=(.*)/, "$1");
	rememberStr =rememberStr.split((";"))[0];
	var remember = rememberStr == "true";
	prePsdLength = password.length;
	var psdStr = "";
	var pos = 0;
	while(pos < prePsdLength) {
		psdStr += "*";
		pos++;
	}
	$("#password").val(psdStr);
	$("#password-value").val(password);
	$("#userId").val(userId);
	$("#remember-password").attr('checked', remember);
	
});

function bindEnterKeyEvent() {
	$(document).keyup(function(event){
		  if(event.keyCode ==13){
			  onLogin();
		  }
		});
}

function bindPasswordInput() {
	$("#password").bind("input propertychange", function(){
		var value = $("#password").val();
		var curPsdLength = value.length;
		if (curPsdLength > prePsdLength) {
			var dx = curPsdLength - prePsdLength;
			var append = value.substr(value.length - dx);
			$("#password-value").val($("#password-value").val() + append);
		} else {
			var dx = prePsdLength - curPsdLength;
			$("#password-value").val($("#password-value").val().substring(0, $("#password-value").val().length - dx));
		}
		var psdStr = "";
		var pos = 0;
		while(pos < curPsdLength) {
			psdStr += "*";
			pos++;
		}
		$("#password").val(psdStr);
		prePsdLength = curPsdLength;
	});
}

function onLogin() {
	var remember = $("#remember-password")[0].checked;
	var userId = $("#userId").val();
	var password = $("#password-value").val();
	$.get("/DashBoard/su/login", {
		username: userId,
		password: password
	}, function(data) {
		data=$.parseJSON(data);
		if(data.code==0){
			if(remember) {
				setCookie("userId", userId);
				setCookie("password", password);
				setCookie("remember", remember);
			} else {
				setCookie("userId", "");
				setCookie("password", "");
				setCookie("remember", remember);
			}
			var url = window.location.href;
			window.location.href=url.replace('https', 'http').replace('login.html', '') + 'index.html';
		} else {
			alert("用户名或密码错误！！！");
		}
	});
}

function setCookie(name, value) {
	var time = new Date();
	time.setDate(time.getDate() + 1);
	document.cookie = encodeURI(name + "=" + value) + ";expires=" + time.toUTCString();
}