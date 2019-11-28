// do an ajax request and call the callback function
function ajax(method, url, callback) {
    $("#loadingBar").show();
    $.ajax({
        type: method,
        url: url,
    }).done(function(resp) {
        callback(resp);
    });
}