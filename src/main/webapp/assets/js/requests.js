var SERVER_URL = "http://localhost:9080";

// do an ajax request and call the callback function
function ajax(method, url, callback) {
    $("#loadingBar").show();
    $.ajax({
        type: method,
        url: SERVER_URL + url,
    }).done(function(resp) {
        callback(resp);
    });
}