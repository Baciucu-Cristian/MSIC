var selectedRowForEdit;
var operation;

function getSubjectTypes()
{
    var request = new XMLHttpRequest();
    request.open("GET", "templates/subjectTypeTableRowTemplate.html", false);
    request.send(null);
    var template = request.responseText;
    var table = document.getElementById("table-body");
    
    $.ajax({
        type : "GET",
        url : "SubjectTypeTableServlet",
        headers : {
          Accept : "text/plain",
          "Content-Type" : "text/plain"
        },
        success: function(data)
        {
            if (data !== "Eroare de conectare la server-ul de baze de date")
            {
                var subjectTypes = JSON.parse(data);
                var tmpl;

                for (var i in subjectTypes)
                {
                    tmpl = template;

                    tmpl = tmpl.replace("#id_tip_disciplina", subjectTypes[i].id_tip_disciplina);
                    tmpl = tmpl.replace("#denumire", subjectTypes[i].denumire);
                    tmpl = tmpl.replace("#prioritate", subjectTypes[i].prioritate);
                    table.insertAdjacentHTML('beforeend', tmpl);
                }
                
                removeAdminRights();
            }
            else
            {
                SpecialAlert(data, "error");
            }
        }
    });
}

function insertSubjectType()
{
    var request = new XMLHttpRequest();
    request.open("GET", "templates/subjectTypeTableRowTemplate.html", false);
    request.send(null);
    var template = request.responseText;
    var table = document.getElementById("table-body");
    
    $.ajax({
        type : "POST",
        url : "SubjectTypeTableServlet?operation=insert",
        data: {subjectType_id : $("#inputSubjectType_Id").val(), subjectType_name : $("#inputSubjectType_Name").val(), subjectType_priority : $("#inputSubjectType_Priority").val()},
        success: function(data)
        {
            if (data !== "Eroare de conectare la server-ul de baze de date")
            {
                template = template.replace("#id_tip_disciplina", $("#inputSubjectType_Id").val());
                template = template.replace("#denumire", $("#inputSubjectType_Name").val());
                template = template.replace("#prioritate", $("#inputSubjectType_Priority").val());
                table.insertAdjacentHTML('afterbegin', template);
                $("#popupform").css("display", "none");
                SpecialAlert(data, "success");
            }
            else
            {
                SpecialAlert(data, "error");
            }
        }
    });
}

function deleteSubjectType(button)
{
    var row = button.closest('tr'); 
    var cells = row.children('td');

    $.ajax({
        type : "POST",
        url : "SubjectTypeTableServlet?operation=delete",
        data: {subjectType_id : cells[0].innerText},
        success: function(data)
        {
            if (data !== "Eroare de conectare la server-ul de baze de date")
            {
                row.remove();
                SpecialAlert(data, "success");
            }
            else
            {
                SpecialAlert(data, "error");
            }
        }
    });
}

function updateSubjectType()
{
    var cells = selectedRowForEdit.children('td');

    $.ajax({
        type : "POST",
        url : "SubjectTypeTableServlet?operation=update",
        data: {subjectType_id : $("#inputSubjectType_Id").val(), subjectType_name : $("#inputSubjectType_Name").val(), subjectType_priority : $("#inputSubjectType_Priority").val()},
        success: function(data)
        {
            if (data !== "Eroare de conectare la server-ul de baze de date")
            {
                cells[1].innerText = $("#inputSubjectType_Name").val();
                cells[2].innerText = $("#inputSubjectType_Priority").val();
                 $("#popupform").css("display", "none");
                SpecialAlert(data, "success");
            }
            else
            {
                SpecialAlert(data, "error");
            }
        }
    });
}

function setUpInfo(button)
{
    $("#popupform").css("display", "block");
    
    if (button === null)
    {
        operation = "insert";
        $("#inputSubjectType_Id").prop('required', true);
        $("#inputSubjectType_Id").prop("disabled", false);
        $("#inputSubjectType_Id").val("");
        $("#inputSubjectType_Name").val("");
        $("#inputSubjectType_Priority").val("");
        $("#operation-title").text("Inserare");
        $("#inputSubjectType_Id").focus();
    }
    else
    {
        operation = "update";
        selectedRowForEdit = button.closest('tr');
        var cells = selectedRowForEdit.children('td');
        $("#inputSubjectType_Id").prop('required', false);
        $("#inputSubjectType_Id").prop("disabled", true);
        $("#inputSubjectType_Id").val(cells[0].innerText);
        $("#inputSubjectType_Name").val(cells[1].innerText);
        $("#inputSubjectType_Priority").val(cells[2].innerText);
        $("#operation-title").text("Editare");
        $("#inputSubjectType_Name").focus();
    } 
}

$(document).ready( function ()
{
    $('#form').submit( function ()
    {
        if (operation === "insert")
        {
            insertSubjectType();
        }
        else
        {
            updateSubjectType();
        }
        
        return false;
    });
});