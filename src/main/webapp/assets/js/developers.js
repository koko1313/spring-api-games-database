function clearResults() {
    // клонираме си шаблона
    var resultTemplate = $("#resultTemplate").clone();
    
    // изчистваме
    $("#results").html("");
    
    // поставяме си обратно шаблоните
    $("#results").append(resultTemplate);
}

function showResult(developers) {
    for(var i=0; i<developers.length; i++) {
        var htmlResult = $("#resultTemplate").clone();
        htmlResult.removeAttr("id");

        var developer = developers[i];

        // платформа
        htmlResult.find(".developer-name").text(developer.name);
        htmlResult.find(".developer-description").text(developer.description);
        htmlResult.find(".developer-edit").attr("onClick", "editDeveloper("+ developer.id +")");
        htmlResult.find(".developer-delete").attr("onClick", "deleteDeveloper("+ developer.id +")");

        htmlResult.show();
        $("#results").append(htmlResult);
    }
}

function search() {
    clearResults();
    ajax("GET", "/developer/all", showResult);
}

// override modal close event
$('#developerFormModal').on('hidden.bs.modal', function () {
    $("#developerInputId").val("");
    $("#developerInputName").val("");
    $("#developerInputDescription").val("");
    $("#insertDeveloperButton").show();
    $("#updateDeveloperButton").hide();
});

// ########################################################################

function insertDeveloper() {
    var developerName = $("#developerInputName").val();
    var developerDescription = $("#developerInputDescription").val();

    $.ajax({
        method: "POST",
        url: SERVER_URL + "/developer/insert",
        data: {
            name: developerName,
            description: developerDescription
        },
        complete: function(data) {
            switch(data.status) {
                case 201: 
                    $("#developerFormModal").modal("hide");
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

function editDeveloper(id) {
    $.ajax({
        method: "GET",
        url: SERVER_URL + "/developer",
        data: {
            id: id
        },
        complete: function(data) {
            switch(data.status) {
                case 200: 
                    $("#developerInputId").val(data.responseJSON.id);
                    $("#developerInputName").val(data.responseJSON.name);
                    $("#developerInputDescription").val(data.responseJSON.description);
                    $("#insertDeveloperButton").hide();
                    $("#updateDeveloperButton").show();
                    $("#developerFormModal").modal("show");
                    break;
                case 404:
                    alert("Разработчика не беше намерен!");
                    break;
            }
        }
    });
}

function updateDeveloper() {
    var developerId = $("#developerInputId").val();
    var developerName = $("#developerInputName").val();
    var developerDescription = $("#developerInputDescription").val();

    $.ajax({
        method: "PUT",
        url: SERVER_URL + "/developer/update",
        data: {
            id: developerId,
            name: developerName,
            description: developerDescription
        },
        complete: function(data) {
            switch(data.status) {
                case 200: 
                    $("#developerFormModal").modal("hide");
                    search();
                    break;
                case 409:
                    alert("Има разработчик с това име!");
                    break;
                case 404:
                    alert("Разработчикът, койко се опитвате да редактирате не беше намерен!");
                    break;
            }
        }
    });
}

function deleteDeveloper(id) {
    if(!confirm("Сигурни ли сте?")) return;

    $.ajax({
        method: "DELETE",
        url: SERVER_URL + "/developer/delete",
        data: {
            id: id
        },
        complete: function(data) {
            switch(data.status) {
                case 200:
                    search();
                    break;
                case 404:
                    alert("Разработчикът, който се опитвате да изтриете не беше намерен!");
                    break;
            }
        }
    });
}


// ########################################################################


$(document).ready(function() {
    search();
});