/* TAB SWITCHING */
var current_tab = "";

function tab(e, tabname) {
    var i, tabcontent, tablinks;
    
    if(tabname == current_tab) return;
    
    //get all elements with class="tabcontent" and remove the class "active"
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].className = tabcontent[i].className.replace(" active_tabcontent", "");
    }

    //get all elements with class="tablinks" and remove the class "active"
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }

    //show the current tab, and add an "active" class to the button that opened the tab
    document.getElementById(tabname).className += " active_tabcontent";
    e.currentTarget.className += " active";
    
    current_tab = tabname;
    
    if(tabname == "tab_solver"){
        loadSolverDetail_updater();
    }else if(tabname == "tab_optimizer"){
        loadOptimizerDetail_updater();        
    }
}

