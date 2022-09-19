$(function() {
	var bindUrl = '/shop2u/frontend/bindlocalauth';

	$('#submit').click(function() {
		var userName = $('#username').val();
		var password = $('#psw').val();
		var verifyCodeActual = $('#j_captcha').val();
		var needVerify = false;
		if (!verifyCodeActual) {
			$.toast('Please enter verification code！');
			return;
		}
		$.ajax({
			url : bindUrl,
			async : false,
			cache : false,
			type : "post",
			dataType : 'json',
			data : {
				userName : userName,
				password : password,
				verifyCodeActual : verifyCodeActual
			},
			success : function(data) {
				if (data.success) {
					$.toast('Binding Successful！');
					window.location.href = 'javascript :history.back(-1);';
				} else {
					$.toast('Binding Unsuccessful！');
					$('#captcha_img').click();
				}
			}
		});
	});
});