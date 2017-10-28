/**
 * Created by julia on 28/10/2017.
 */
function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for(var i = 0; i <ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

var email_cookie_value = getCookie("email");

if (email_cookie_value.length > 0) {
    document.getElementById('exampleInputEmail1').value = email_cookie_value;
}