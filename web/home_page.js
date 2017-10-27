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
            for (var i = 0; i < data.length; i++) {
                var project_template = "<div class='card' style='width: 20rem;'>";
                if (data[i].image_filepath !== "") {
                    project_template += "<img class='card-img-top' src='" + data[i].image_filepath + "' alt='Card image cap'>";
                } else {
                    project_template += "<img class='card-img-top' src='Multimedia/General/default_project.jpg' alt='Card image cap'>";
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
        },

        error: function() {
            console.log("Failed all project loading");
        }
    });
}

get_all_projects();


