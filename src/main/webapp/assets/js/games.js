var PATH_TO_IMAGES = "assets/images/games/";
var resultDesign = 1;

// сетва избрания дизайн за показване на резултатите и ги визуализира по него
function setResultDesign(designNumber) {
    resultDesign = designNumber;
    clearResults();
    search();
}

// изчиства всички резултати
function clearResults() {
    // клонираме си шаблоните
    var resultTemplate1 = $("#resultTemplate1").clone();
    var resultTemplate2 = $("#resultTemplate2").clone();
    
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
        var htmlResult = $("#resultTemplate1").clone();
        htmlResult.removeAttr("id");

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
    var resultCards = $("#resultTemplate2").clone();

    for(var i=0; i<games.length; i++) {
        var htmlResult = $("#resultTemplate2Card").clone();
        $(htmlResult).removeAttr("id");
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

// търси и показва резултатите
function search() {
    clearResults(); // когато е зададен нов параметър, трием показаните до момента резултати

    if(selectedGenres.includes(0) && selectedPlatforms.includes(0)) {
        ajax("GET", "/game/all", showResult);
    }
    else if(!selectedGenres.includes(0) && !selectedPlatforms.includes(0)) {
        ajax("GET", "/game/search?genres_id_list=" + selectedGenres.join(",") + "&platforms_id_list=" + selectedPlatforms.join(","), showResult);
    }
    else if(!selectedGenres.includes(0)) {
        ajax("GET", "/game/search?genres_id_list=" + selectedGenres.join(","), showResult);
    }
    else if(!selectedPlatforms.includes(0)) {
        ajax("GET", "/game/search?platforms_id_list=" + selectedPlatforms.join(","), showResult);
    }
}


// ########################################################################


var selectedGenres = [0];

var selectedPlatforms = [0];

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

function setGenre(id) {
    id = parseInt(id);

    // ако в масива има елемент 0 го премахваме
    if(selectedGenres[0] == 0) {
        selectedGenres.pop();
    }

    // ако влезе id 0, изчистваме масива
    if(id == 0) {
        selectedGenres = [];
    }

    // ако в масива го има елемента го махаме
    if(selectedGenres.includes(id)) {
        if(selectedGenres.indexOf(id) != -1) {
            selectedGenres.splice(selectedGenres.indexOf(id), 1);
        }
    } 
    // иначе го добавяме
    else {
        selectedGenres.push(id);
    }
}

function setPlatform(id) {
    id = parseInt(id);

    // ако в масива има елемент 0 го премахваме
    if(selectedPlatforms[0] == 0) {
        selectedPlatforms.pop();
    }

    // ако влезе id 0, изчистваме масива
    if(id == 0) {
        selectedPlatforms = [];
    }

    // ако в масива го има елемента го махаме
    if(selectedPlatforms.includes(id)) {
        if(selectedPlatforms.indexOf(id) != -1) {
            selectedPlatforms.splice(selectedPlatforms.indexOf(id), 1);
        }
    } 
    // иначе го добавяме
    else {
        selectedPlatforms.push(id);
    }
}

// селектиране на жанр - извикава се когато се кликне бутона на някой жанр
function selectGenre(htmlItem) {
    var genreId = htmlItem.dataset.value;

    setGenre(genreId);

    if(genreId == 0) {
        $("#openGenresModalButton").text("Жанр (Всички)");
    } else {
        $("#openGenresModalButton").text("Жанр ("+ selectedGenres.length +")");
    }
    
    var genresItems = $(htmlItem).parent().children();

    // ако сме селектирали Всички - правим всички неактивни
    if(genreId == 0) {
        for(var i=0; i<genresItems.length; i++) {
            $(genresItems[i]).removeClass("active");
        }
        $(genresItems[0]).addClass("active");
    // иначе правим елемента "Всички" неактивен и активираме/деактивираме избрания
    } else {
        $(genresItems[0]).removeClass("active");
        $(htmlItem).toggleClass("active");
    }

    // ако нямаме нищо избрано - викаме клик евент на първия елемент, който ни е "Всики"
    if(selectedGenres.length == 0) {
        $(genresItems[0]).click();
        return;
    }

    search();
}

// селектиране на платформа - извиква се когато се кликне бутона на някоя платформа
function selectPlatform(htmlItem) {
    var platformId = htmlItem.dataset.value;

    setPlatform(platformId);

    if(platformId == 0) {
        $("#openPlatformsModalButton").text("Платформа (Всички)");
    } else {
        $("#openPlatformsModalButton").text("Платформа ("+ selectedPlatforms.length +")");
    }
    
    var platformItems = $(htmlItem).parent().children();
    // ако сме селектирали Всички - правим всички неактивни и само първия - активен
    if(platformId == 0) {
        for(var i=0; i<platformItems.length; i++) {
            $(platformItems[i]).removeClass("active");
        }
        $(platformItems[0]).addClass("active");
    // иначе правим елемента "Всички" неактивен и активираме/деактивираме избрания
    } else {
        $(platformItems[0]).removeClass("active");
        $(htmlItem).toggleClass("active");
    }

    // ако нямаме нищо избрано - викаме клик евент на първия елемент, който ни е "Всики"
    if(selectedPlatforms.length == 0) {
        $(platformItems[0]).click();
        return;
    } 

    search();
}


// ########################################################################


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