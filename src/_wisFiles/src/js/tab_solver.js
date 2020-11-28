/* TAB_SOLVER switching tabs */
var current_solver = "";

function switch_solver(e, tabname) {
    var i, tabcontent, tablinks;


    //get all elements with class="solver_content" and remove the class "active"
    tablinks = document.getElementsByClassName("solver_content");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }

    //get all elements with class="solver_list_elem" and remove the class "active"
    tablinks = document.getElementsByClassName("solver_list_elem");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
    //remove class "active" from solver_add button (label)
    document.getElementById("solver_add").className = document.getElementById("solver_add").className.replace(" active", "");

    //show the current tab, and add an "active" class to the button that opened the tab
    document.getElementById(tabname).className += " active";
    e.currentTarget.className += " active";
    
    current_tab = tabname;
}

