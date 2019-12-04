function clearResults() {
    // клонираме си шаблона
    var resultTemplate = $("#resultTemplate").clone();
    
    // изчистваме
    $("#results").html("");
    
    // поставяме си обратно шаблоните
    $("#results").append(resultTemplate);
}

function showResult(platforms) {
    for(var i=0; i<platforms.length; i++) {
        var htmlResult = $("#resultTemplate").clone();
        htmlResult.removeAttr("id");

        var platform = platforms[i];

        // платформа
        htmlResult.find(".platform-name").text(platform.name);
        htmlResult.find(".platform-edit").attr("onClick", "editPlatform("+ platform.id +")");
        htmlResult.find(".platform-delete").attr("onClick", "deletePlatform("+ platform.id +")");

        htmlResult.show();
        $("#results").append(htmlResult);
    }
}

function search() {
    clearResults();
    ajax("GET", "/platform/all", showResult);
}

// override modal close event
$('#platformFormModal').on('hidden.bs.modal', function () {
    $("#platformInputId").val("");
    $("#platformInputName").val("");
    $("#insertPlatformButton").show();
    $("#updatePlatformButton").hide();
});

// ########################################################################

function insertPlatform() {
    var platformName = $("#platformInputName").val();

    $.ajax({
        method: "POST",
        url: SERVER_URL + "/platform/insert",
        data: {
            name: platformName
        },
        complete: function(data) {
            switch(data.status) {
                case 201: 
                    $("#platformFormModal").modal("hide");
                    search();
                    break;
                case 409:
                    alert("Има платформа с това име!");
                    break;
                case 404:
                    alert("Нещо се обърка");
                    break;
            }
            
        }
    });
}

function editPlatform(id) {
    $.ajax({
        method: "GET",
        url: SERVER_URL + "/platform",
        data: {
            id: id
        },
        complete: function(data) {
            switch(data.status) {
                case 200: 
                    $("#platformInputId").val(data.responseJSON.id);
                    $("#platformInputName").val(data.responseJSON.name);
                    $("#insertPlatformButton").hide();
                    $("#updatePlatformButton").show();
                    $("#platformFormModal").modal("show");
                    break;
                case 404:
                    alert("Платформата не беше намерена!");
                    break;
            }
        }
    });
}

function updatePlatform() {
    var platformId = $("#platformInputId").val();
    var platformName = $("#platformInputName").val();

    $.ajax({
        method: "PUT",
        url: SERVER_URL + "/platform/update",
        data: {
            id: platformId,
            name: platformName
        },
        complete: function(data) {
            switch(data.status) {
                case 200: 
                    $("#platformFormModal").modal("hide");
                    search();
                    break;
                case 409:
                    alert("Има платформа с това име!");
                    break;
                case 404:
                    alert("Платформата, която се опитвате да редактирате не беше намерена!");
                    break;
            }
        }
    });
}

function deletePlatform(id) {
    if(!confirm("Сигурни ли сте?")) return;

    $.ajax({
        method: "DELETE",
        url: SERVER_URL + "/platform/delete",
        data: {
            id: id
        },
        complete: function(data) {
            switch(data.status) {
                case 200:
                    search();
                    break;
                case 404:
                    alert("Платформата, която се опитвате да изтриете не беше намерена!");
                    break;
            }
        }
    });
}


// ########################################################################


$(document).ready(function() {
    search();
});