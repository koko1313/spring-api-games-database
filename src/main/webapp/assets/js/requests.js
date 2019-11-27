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

/*
function getAllGames() {
    ajax("GET", "/game/all", function(games) {
        return games;
    });
}

function getAllGenres() {
    ajax("GET", "/genre/all", function(genres) {
        return genres;
    });
}

function getAllPlatforms() {
    ajax("GET", "platform/all", function(platforms) {
        console.log(platforms);
    });
}

function getAllDevelopers() {
    ajax("GET", "developer/all", function(developers) {
        console.log(developers);
    });
}
*/