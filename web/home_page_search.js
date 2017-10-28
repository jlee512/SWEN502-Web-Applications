/**
 * Created by julia on 27/10/2017.
 */
var searchTerm;

//Use AJAX to pull search project data from server in json format
function get_search_projects() {
    //Redirect to home page with searchTerm as a parameter
    window.location.href = "/home?searchTerm=" + searchTerm;
}

document.getElementById('searchButton').addEventListener('click', function () {
    searchTerm = document.getElementById('searchText').value;
    get_search_projects();
});

