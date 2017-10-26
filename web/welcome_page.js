/**
 * Created by julian on 25/10/2017.
 */

//Global reference variables
var project_types = ["Baking", "Cards", "Crafts", "Food", "Wedding"];
var project_numbers = [25, 16, 2, 14, 27];
var logo_indices = [33, 34, 35, 36, 42, 43, 44, 45, 46, 47, 52, 53, 54, 55, 56, 57, 62, 63, 64 ,65, 66, 67, 73, 74, 75, 76];

// Experimentation with JSON parameters
var hex_color_palette = {
    yellow_100: "#FFF9C4",
    yellow_200: "#FFF59D",
    cyan_100: "#B2EBF2"
};

for(var i = 0; i < 10; i++) {
    var clone = document.getElementById('disco-column-template').cloneNode(true);
    clone.setAttribute('id', "column-clone-" + i);
    document.getElementById('disco-template-append').append(clone);
}

var background_color_timer = setInterval(function() {
    var image_units = document.getElementsByClassName('image-unit');
    //Choose three random image_units
    var change_background_index1 = Math.floor(Math.random() * image_units.length);
    var change_background_index2 = Math.floor(Math.random() * image_units.length);
    var change_background_index3 = Math.floor(Math.random() * image_units.length);
    var change_backgroundimage_index4 = Math.floor(Math.random() * image_units.length);

    //Assign palette colors to randomised image_units
    image_units[change_background_index1].style.backgroundColor = hex_color_palette.yellow_100;
    image_units[change_background_index1].style.backgroundImage = 'none';
    image_units[change_background_index2].style.backgroundColor = hex_color_palette.yellow_200;
    image_units[change_background_index2].style.backgroundImage = 'none';
    image_units[change_background_index3].style.backgroundColor = hex_color_palette.cyan_100;
    image_units[change_background_index3].style.backgroundImage = 'none';

    //Assign background image of a random project to one element (as long as it will not impinge on logo)
    if (logo_indices.indexOf(change_backgroundimage_index4) === -1) {
        var filepath = "Multimedia/";
        var project_type_selector = Math.floor(Math.random() * project_types.length);
        filepath += project_types[project_type_selector] + "/";
        filepath += Math.ceil(Math.random() * project_numbers[project_type_selector]);
        filepath += ".jpg";


        image_units[change_backgroundimage_index4].style.backgroundImage = 'url(' + filepath + ')';
    }

}, 500);

//Returns the maximum number of images within a folder for a given project category
function getMaxImageNum(project_type) {
    switch (project_type) {
        case "Baking":
            return 25;
        case "Cards":
            return 16;
        case "Crafts":
            return 2;
        case "Food":
            return 14;
        case "Wedding":
            return 27;
        default:
            return -1;
        }
}

//Attaches the button click-listener to the 'Enter the site' button
document.getElementById('entry-text').addEventListener('click', function() {
   location.href="/home";
});




