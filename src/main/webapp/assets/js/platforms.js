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
    ajax("GET", "platform/all", showResult);
}


// ########################################################################


$(document).ready(function() {
    search();
});