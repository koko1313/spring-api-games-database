// глобални променливи
//var currentPage = 1;
//const RESULTS_LOAD_AT_A_TIME = 6;
var PATH_TO_IMAGES = "assets/images/games/";
var resultDesign = 1;

// ##########################################################################

var genre = 0; // ще ни служи за зареждане по жанр
var platform = 0; // ще ни служи за зареждане по платформа

function setGenre(id) {
    genre = id;
}

function getGenre() {
    return genre;
}

function setPlatform(id) {
    platform = id;
}

function getPlatform() {
    return platform;
}

// ##########################################################################

// сетва избрания дизайн за показване на резултатите и ги визуализира по него
function setResultDesign(designNumber) {
    resultDesign = designNumber;
    clearResults();
    search();
}

// изчиства всички резултати
function clearResults() {
    // клонираме си шаблоните
    var resultTemplate1 = $("#result-template-1").clone();
    var resultTemplate2 = $("#result-template-2").clone();
    
    // изчистваме
    $("#results").html("");
    
    // поставяме си обратно шаблоните
    $("#results").append(resultTemplate1);
    $("#results").append(resultTemplate2);
}

// показва резултатите (извиква се като callback от ajax)
function showResult(resp) {
    // показва резултатите, спрямо дизайна, който e избран
    switch(resultDesign) {
        case 1 : showResultDesign1(resp); break;
        case 2 : showResultDesign2(resp); break;
    }

}

// показва резултата по първия дизайн
function showResultDesign1(games) {
    for(var i=0; i<games.length; i++) {
        var htmlResult = $("#result-template-1").clone();
        htmlResult.attr("id", "");

        var game = games[i];

        // заглавие
        htmlResult.find('h3').text(game.name);

        // картинка
        if(game.image != null) {
            htmlResult.find('img').attr("src", PATH_TO_IMAGES + game.image);
        }

        // разработчици
        var developer = null;
        if(game.developer != null) {
            developer = game.developer.name;
        }
        htmlResult.find('.developers').text(developer);

        // жанрове
        var genres = "";
        for(var j=0; j<game.genres.length; j++) {
            if(j > 0) genres += ", "; // слагаме запетая в случай че имаме повече от 1
            genres += game.genres[j].name;
        }
        htmlResult.find('.genres').text(genres);

        // платформи
        var platforms = "";
        for(var j=0; j<game.platforms.length; j++) {
            if(j > 0) platforms += ", "; // слагаме запетая в случай че имаме повече от 1
            platforms += game.platforms[j].name;
        }
        htmlResult.find('.platforms').text(platforms);

        htmlResult.show();
        $("#results").append(htmlResult);
    }
}

// показва резултата по втория дизайн
function showResultDesign2(games) {
    var resultCards = $("#result-template-2").clone();

    for(var i=0; i<games.length; i++) {
        var htmlResult = $("#result-template-2-card").clone();
        $(htmlResult).attr("id", "");
        var game = games[i];

        // заглавие
        htmlResult.find('h3').text(game.name);

        // картинка
        if(game.image != null) {
            htmlResult.find('img').attr("src", PATH_TO_IMAGES + game.image);
        }

        // разработчици
        var developer = null;
        if(game.developer != null) {
            developer = game.developer.name;
        }
        htmlResult.find('.developers').text(developer);

        // жанрове
        var genres = "";
        for(var j=0; j<game.genres.length; j++) {
            if(j > 0) genres += ", "; // слагаме запетая в случай че имаме повече от 1
            genres += game.genres[j].name;
        }
        htmlResult.find('.genres').text(genres);

        // платформи
        var platforms = "";
        for(var j=0; j<game.platforms.length; j++) {
            if(j > 0) platforms += ", "; // слагаме запетая в случай че имаме повече от 1
            platforms += game.platforms[j].name;
        }
        htmlResult.find('.platforms').text(platforms);

        htmlResult.show();
        resultCards.append(htmlResult);
    }

    resultCards.show();
    $("#results").append(resultCards);
}

// ##########################################################################

// търси и показва резултатите
function search(param) {
    clearResults(); // когато е зададен нов параметър, трием показаните до момента резултати

    if(getGenre() != 0 && getPlatform() != 0) {
        ajax("GET", "/game/search?genres_id_list="+getGenre()+"&platforms_id_list="+getPlatform(), showResult);
    }
    else if(getGenre() != 0) {
        ajax("GET", "/game/search?genres_id_list="+getGenre(), showResult);
    }
    else if(getPlatform() != 0) {
        ajax("GET", "/game/search?platforms_id_list="+getPlatform(), showResult);
    }
    else {
        ajax("GET", "/game/all", showResult);
    }
}

// ##########################################################################

// селектиране на жанр - извикава се когато се кликне бутона на някой жанр
function selectGenre(htmlItem) {
    var genreId = htmlItem.dataset.value;

    var genreName = $(htmlItem).text();
    $("#openGenresModalButton").text("Жанр: "+ genreName);
    $("#genresModal").modal('hide');

    setGenre(genreId);

    // правим активни избраните жанрове и неактивни - неизбраните
    var genres = $(htmlItem).parent().children();
    for(var i=0; i<genres.length; i++) {
        if(getGenre() == genres[i].dataset.value) {
            $(genres[i]).addClass("active");
        } else {
            $(genres[i]).removeClass("active");
        }
    }

    search();
}

// селектиране на платформа - извиква се когато се кликне бутона на някоя платформа
function selectPlatform(htmlItem) {
    var platformId = htmlItem.dataset.value;

    var platformName = $(htmlItem).text();
    $("#openPlatformsModalButton").text("Платформа: "+ platformName);
    $("#platformsModal").modal('hide');

    setPlatform(platformId);

    // правим активни избраните платформи и неактивни - неизбраните
    var platforms = $(htmlItem).parent().children();
    for(var i=0; i<platforms.length; i++) {
        if(getPlatform() == platforms[i].dataset.value) {
            $(platforms[i]).addClass("active");
        } else {
            $(platforms[i]).removeClass("active");
        }
    }

    search();
}

// ##########################################################################

// попълва филтъра с жанровете
function loadGenres() {
    ajax("GET", "/genre/all", function(resp) {
        for(var i=0; i<resp.length; i++) {
            var genreItem = $("#genreItemTemplate").clone();
            genreItem.removeAttr("id");
            genreItem.attr("data-value", resp[i].id);
            genreItem.text(resp[i].name);

            genreItem.show();
            $("#genresModal").find(".modal-body").find("ul").append(genreItem);
        }
    });
}

// попълва филтъра с платформите
function loadPlatforms() {
    ajax("get", "platform/all", function(resp) {
        for(var i=0; i<resp.length; i++) {
            var platformItem = $("#platformItemTemplate").clone();
            platformItem.removeAttr("id");
            platformItem.attr("data-value", resp[i].id);
            platformItem.text(resp[i].name);

            platformItem.show();
            $("#platformsModal").find(".modal-body").find("ul").append(platformItem);
        }
    });
}

// попълва всички филтри
function loadFilters() {
    loadGenres();
    loadPlatforms();
}

// ##########################################################################

var position = $(window).scrollTop(); 
$(window).scroll(function() {

    // зарежда още резултати, когато скролнем до най-долу
    /*
    if ((window.innerHeight + window.scrollY) >= document.body.offsetHeight) {
        currentPage++;
        search();
    }
    */

    var scroll = $(window).scrollTop();

    // scroll down
    if(scroll > position) {
        $("#goTopButton").fadeOut();

    // scroll up
    } else {
        $("#goTopButton").slideDown();
    }

    position = scroll;

    // скриваме "go to top" бутона, когато сме най-горе
    if(position == 0) {
        $("#goTopButton").fadeOut();
    }
});

// скролва до началото на страницата
function goToTopOfPage() {
    $("html, body").animate({ scrollTop: 0 }, "medium");
}

$(document).ready(function() {
    search();
    loadFilters();
});