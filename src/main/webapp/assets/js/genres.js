function clearResults() {
    // клонираме си шаблона
    var resultTemplate = $("#resultTemplate").clone();
    
    // изчистваме
    $("#results").html("");
    
    // поставяме си обратно шаблоните
    $("#results").append(resultTemplate);
}

function showResult(genres) {
    for(var i=0; i<genres.length; i++) {
        var htmlResult = $("#resultTemplate").clone();
        htmlResult.removeAttr("id");

        var genre = genres[i];

        // платформа
        htmlResult.find(".genre-name").text(genre.name);
        htmlResult.find(".genre-edit").attr("onClick", "editGenre("+ genre.id +")");
        htmlResult.find(".genre-delete").attr("onClick", "deleteGenre("+ genre.id +")");

        htmlResult.show();
        $("#results").append(htmlResult);
    }
}

function search() {
    clearResults();
    ajax("GET", "/genre/all", showResult);
}

// override modal close event
$('#genreFormModal').on('hidden.bs.modal', function () {
    $("#genreInputId").val("");
    $("#genreInputName").val("");
    $("#insertGenreButton").show();
    $("#updateGenreButton").hide();
});

// ########################################################################

function insertGenre() {
    var genreName = $("#genreInputName").val();

    $.ajax({
        method: "POST",
        url: SERVER_URL + "/genre/insert",
        data: {
            name: genreName
        },
        complete: function(data) {
            switch(data.status) {
                case 201: 
                    $("#genreFormModal").modal("hide");
                    search();
                    break;
                case 409:
                    alert("Има жанр с това име!");
                    break;
                case 404:
                    alert("Нещо се обърка");
                    break;
            }
            
        }
    });
}

function editGenre(id) {
    $.ajax({
        method: "GET",
        url: SERVER_URL + "/genre",
        data: {
            id: id
        },
        complete: function(data) {
            switch(data.status) {
                case 200: 
                    $("#genreInputId").val(data.responseJSON.id);
                    $("#genreInputName").val(data.responseJSON.name);
                    $("#insertGenreButton").hide();
                    $("#updateGenreButton").show();
                    $("#genreFormModal").modal("show");
                    break;
                case 404:
                    alert("Жанра не беше намерен!");
                    break;
            }
        }
    });
}

function updateGenre() {
    var genreId = $("#genreInputId").val();
    var genreName = $("#genreInputName").val();

    $.ajax({
        method: "PUT",
        url: SERVER_URL + "/genre/update",
        data: {
            id: genreId,
            name: genreName
        },
        complete: function(data) {
            switch(data.status) {
                case 200: 
                    $("#genreFormModal").modal("hide");
                    search();
                    break;
                case 409:
                    alert("Има жанр с това име!");
                    break;
                case 404:
                    alert("Жанрът, който се опитвате да редактирате не беше намерен!");
                    break;
            }
        }
    });
}

function deleteGenre(id) {
    if(!confirm("Сигурни ли сте?")) return;

    $.ajax({
        method: "DELETE",
        url: SERVER_URL + "/genre/delete",
        data: {
            id: id
        },
        complete: function(data) {
            switch(data.status) {
                case 200:
                    search();
                    break;
                case 404:
                    alert("Жанрът, който се опитвате да изтриете не беше намерен!");
                    break;
            }
        }
    });
}


// ########################################################################


$(document).ready(function() {
    search();
});