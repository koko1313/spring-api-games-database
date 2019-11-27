// глобални променливи
var currentPage = 1;
const RESULTS_LOAD_AT_A_TIME = 6;
var resultDesign = 1;

var genre = null; // ще ни служи за зареждане по жанр
var platform = null; // ще ни служи за зареждане по платформа
var orderByReleased = false; // ще ни служи за сортиране по дата на публикуване

// връща URL-а, нужен ни за търсене в API-то
/*
function getURL() {
    var url = "https://rawg-video-games-database.p.rapidapi.com/games?page=" + currentPage + "&page_size=" + RESULTS_LOAD_AT_A_TIME;
    
    // ако има избран жанр, търсим за него
    if(genre) {
        url += "&genres=" + genre;
    }

    // ако има избрана платформа
    if(platform) {
        url += "&parent_platforms=" + platform;
    }

    // ако е избрано сортиране
    if(orderByReleased) {
        url += "&ordering=released";
    }

    return url;
}
*/

// прави ajax заявката и се обръща към callback функция
/*
function ajax(url, callback, async = true) {
    $("#loadingBar").show();
    $.ajax({
        type: "GET",
        url: url,
        async: async,
        headers: {
            "x-rapidapi-host": "rawg-video-games-database.p.rapidapi.com",
            "x-rapidapi-key": "c0b2d8dca2msh0671a66d5c7bdf7p112ca6jsnd725d307e9ef"
        },
    }).done(function(resp) {
        $("#loadingBar").hide();
        callback(resp);
    });
}
*/

// връща масив с обекти - игри
/*
function generateArrayOfGames(resp) {
    var gamesArray = [];

    for(var i=0; i<resp.results.length; i++) {
        var currentResult = resp.results[i];
        
        var name = currentResult.name;
        var image = currentResult.background_image;
        var releaseDate = currentResult.released;
        var rating = currentResult.rating + " / " + currentResult.rating_top + " (" + currentResult.ratings_count + " гласа)";
        var platforms = [];
        var genres = [];
        var developers = [];

        // generating list of platforms
        for(var j=0; j<currentResult.platforms.length; j++) {
            platforms.push(currentResult.platforms[j].platform.name);
        }

        ajax("https://rawg-video-games-database.p.rapidapi.com/games/"+currentResult.id, function(resp) {
            // generating list of genres
            for(var i=0; i<resp.genres.length; i++) {
                genres.push(resp.genres[i].name);
            }
            // generating list of developers
            for(var i=0; i<resp.developers.length; i++) {
                developers.push(resp.developers[i].name);
            }
        }, false); // call it async

        var game = {
            "name" : name,
            "image" : image,
            "releaseDate" : releaseDate,
            "rating" : rating,
            "platforms" : platforms,
            "genres" : genres,
            "developers" : developers,
        }

        gamesArray.push(game);
    }

    return gamesArray;
}
*/

// сетва избрания дизайн за показване на резултатите и ги визуализира по него

// изчиства всички резултати

// показва резултатите (извиква се като callback от ajax)

// показва резултата по първия дизайн

// показва резултата по втория дизайн

// търси и показва резултатите

// селектиране на жанр - извикава се когато се кликне бутона на някой жанр
function selectGenre(htmlItem) {

    // премахваме класът за активен бутон от всички бутони
    var genres = $(htmlItem).parent().children();
    for(var i=0; i<genres.length; i++) {
        genres.removeClass("active");
    }

    $(htmlItem).addClass("active");
    var genreId = htmlItem.dataset.value;

    var genreName = $(htmlItem).text();
    $("#openGenresModalButton").text("Жанр: "+ genreName);
    $("#genresModal").modal('hide');

    // да се покажат всички
    if(genreId == 0) {
        genre = null;
        clearResults();
        search();
        return;
    }

    search({genre: genreId});
}

// селектиране на платформа - извиква се когато се кликне бутона на някоя платформа
/*
function selectPlatform(htmlItem) {

    // премахваме класът за активен бутон от всички бутони
    var platforms = $(htmlItem).parent().children();
    for(var i=0; i<platforms.length; i++) {
        platforms.removeClass("active");
    }

    $(htmlItem).addClass("active");
    var platformId = htmlItem.dataset.value;

    var platformName = $(htmlItem).text();
    $("#openPlatformsModalButton").text("Платформа: "+ platformName);
    $("#platformsModal").modal('hide');

    // да се покажат всички
    if(platformId == 0) {
        platform = null;
        clearResults();
        search();
        return;
    }

    search({platform: platformId});
}
*/

// тригърва сортирането по дата
/*
function trigerOrderByReleased() {
    orderByReleased = !orderByReleased;
    
    clearResults();
    search();

    // променяме иконката на бутона
    if(orderByReleased) {
        $("#trigerOrderByReleasedButton").html('<i class="fas fa-sort-numeric-up"></i>');
    } else {
        $("#trigerOrderByReleasedButton").html('<i class="fas fa-sort-numeric-down-alt"></i>');
    }
}
*/

// попълва филтъра с жанровете

// попълва филтъра с платформите

// попълва всички филтри

// скролва до началото на страницата