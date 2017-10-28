/**
 * Created by julia on 27/10/2017.
 */

//Use AJAX to pull all project data from server in json format
function get_all_projects() {

    //AJAX call to get all Projects
    $.ajax({

        url: "/get_all_projects_json",
        async: true,
        type: 'GET',
        dataType: 'json',

        success: function (data) {

            $('#content').empty();

            if (data.length === 0) {
                $('#content').append("<div class='alert alert-danger alert-dismissible fade show' role='alert'>" +
                                        "No projects currently available" +
                                    "</div>");
            } else {

                for (var i = 0; i < data.length; i++) {
                    var project_template = "<div class='card' style='width: 20rem;'>";
                    if (data[i].image_filepath !== "") {
                        project_template += "<a href='" + data[i].image_filepath + "'><div class='card-img-top' alt='Card image cap' style='background-image: url(" + data[i].image_filepath + "); background-position: center; background-size: cover; height: 200px; width: 100%; padding: 15px;' ></div></a>";
                    } else {
                        project_template += "<a href='Multimedia/General/default_project.jpg'><div class='card-img-top' alt='Card image cap' style='background-image: url('Multimedia/General/default_project.jpg'); background-position: center; background-size: cover; height: 200px; width: 100%; padding: 15px;' ></div></a>";
                    }
                    project_template += "<div class='card-body'>";
                    project_template += "<h4 class='card-title'>" + data[i].title + "</h4>";
                    project_template += "<p class='card-text'>" + data[i].description + "</p>";
                    project_template += "</div>";

                    if (data[i].hyperlink !== "" && data[i].hyperlink_description !== "") {
                        project_template += "<div class='card-body'>";
                        project_template += "<a href='" + data[i].hyperlink + "' class='card-link'>" + data[i].hyperlink_description + "</a>";
                        project_template += "</div>";
                    }

                    project_template += "</div>";

                    $('#content').append(project_template);
                }
            }
        },

        error: function () {
            console.log("Failed all project loading");
        }
    });
}

//Use AJAX to pull search project data from server in json format
function get_search_projects() {

    var searchTerm = document.getElementById('searchText').value;

    //AJAX call to get all Projects
    $.ajax({

        url: "/get_search_projects_json",
        data: {
            "searchTerm": searchTerm
        },
        async: true,
        type: 'GET',
        dataType: 'json',

        success: function (data) {

            $('#content').empty();

            if (data.length === 0) {
                $('#content').append("<div class='alert alert-danger alert-dismissible fade show' role='alert'>" +
                    "No projects for this search term" +
                    "</div>");
            } else {

                for (var i = 0; i < data.length; i++) {
                    var project_template = "<div class='card' style='width: 20rem;'>";
                    if (data[i].image_filepath !== "") {
                        project_template += "<a href='" + data[i].image_filepath + "'><div class='card-img-top' alt='Card image cap' style='background-image: url(" + data[i].image_filepath + "); background-position: center; background-size: cover; height: 200px; width: 100%; padding: 15px;' ></div></a>";
                    } else {
                        project_template += "<a href='Multimedia/General/default_project.jpg'><div class='card-img-top' alt='Card image cap' style='background-image: url('Multimedia/General/default_project.jpg'); background-position: center; background-size: cover; height: 200px; width: 100%; padding: 15px;' ></div></a>";
                    }
                    project_template += "<div class='card-body'>";
                    project_template += "<h4 class='card-title'>" + data[i].title + "</h4>";
                    project_template += "<p class='card-text'>" + data[i].description + "</p>";
                    project_template += "</div>";

                    if (data[i].hyperlink !== "" && data[i].hyperlink_description !== "") {
                        project_template += "<div class='card-body'>";
                        project_template += "<a href='" + data[i].hyperlink + "' class='card-link'>" + data[i].hyperlink_description + "</a>";
                        project_template += "</div>";
                    }

                    project_template += "</div>";

                    $('#content').append(project_template);
                }

                var returnButtonContent = $('#returnButtonContent').children();

                if (returnButtonContent.length === 0) {
                    $('#returnButtonContent').append("<button id='fullListButton' class='btn btn-outline-success my-2 my-sm-0' type='button'>Return to Full List</button>");
                }
                document.getElementById('searchText').value = "";
            }
        },

        error: function () {
            console.log("Failed search project loading");
        }
    });
}

document.getElementById('searchButton').addEventListener('click', function(){
    get_search_projects();
    $(document).on('click', '#fullListButton', function() {
       get_all_projects();
        $(this).remove();
    });
});

// If home has been accessed from a search, process search term otherwise, load all projects
if (window.location.href.indexOf("searchTerm") > -1) {
    var url = new URL(window.location.href);
    var searchTerm = url.searchParams.get("searchTerm");
    document.getElementById('searchText').value = searchTerm;
    get_search_projects();
    $(document).on('click', '#fullListButton', function() {
        window.location.href = "/home";
    });
} else {
    get_all_projects();
}

