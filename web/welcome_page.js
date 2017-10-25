/**
 * Created by julia on 25/10/2017.
 */

var project_types = ["Baking", "Cards", "Crafts", "Food", "Wedding"];

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


