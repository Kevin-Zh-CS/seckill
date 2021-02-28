let g_userView = {};
jQuery(document).ready(function () {
    const token = window.localStorage["token"];
    if(token == null){
        $("#userinfo").hide();
        $("#logintext").text("登录").show();
        return false;
    }
    $.ajax({
        type: "POST",
        connectType: "application/x-www-form-urlencoded",
        url: "http://localhost:8080/user/verifyLogin",
        data: {
            "token": token
        },
        xhrFields: {withCredentials: true},
        success: function (data) {
            if (data.status === "success") {
                g_userView = data.data;
                if (g_userView != null) {
                    $("#logintext").hide();
                    $("#userinfo").text(g_userView.name).show();
                }else{
                    $("#userinfo").hide();
                    $("#logintext").text("登录").show();
                }
            } else {
                alert("获取信息失败，" + data.data.errMessage);
            }
        },
        error: function (data) {
            alert("错误，" + data.responseText);
        }
    });
    // $("#header_login").on("click", function () {
    //     window.location.href = "login2.html";
    // })
});