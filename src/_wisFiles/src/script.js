/* ONLOAD */

function onload(){    
    document.getElementById('file_input').addEventListener('change', readSingleFile, false);   
    document.getElementById('tab_defaultOpen').click();
}








/* INFO BANNER */

function openInfo(type, text){
    if(document.getElementById("info").style.display!="none"){
        closeInfo();
        setTimeout(function(){ openInfo(type,text) }, 200);
        return;
    }
    
    setInfoColor(type);
    document.getElementById("info_text").innerHTML = text;
    document.getElementById("info").style.animationName = "fadeInfoIn";
    document.getElementById("info").style.animationDuration = "0.2s";
    document.getElementById("info").style.display = "block";   
}

function closeInfo(){
    document.getElementById("info").style.animationName = "fadeInfoOut";
    document.getElementById("info").style.animationDuration = "0.2s";
    
    var elem = document.getElementById("info");
    elem.parentNode.replaceChild(elem.cloneNode(true), elem);
    
    setTimeout(function(){ document.getElementById("info").style.display = "none"; }, 150);        
}

function setInfoColor(type){
    if(type=="suc" || type=="warn" || type=="err"){
        document.getElementById("info").className = type;
        document.getElementById("info_close_x").className = "white";
        if(type=="warn"){            
            document.getElementById("info_close_x").className = "grey";
        }
    } else {
        document.getElementById("info").className = "err";        
    }    
}








/* TAB SWITCHING */

function tab(e, tabname) {
    var i, tabcontent, tablinks;

    //get all elements with class="tabcontent" and hide them
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }

    //get all elements with class="tablinks" and remove the class "active"
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }

    //show the current tab, and add an "active" class to the button that opened the tab
    document.getElementById(tabname).style.display = "block";
    e.currentTarget.className += " active";
}





























function cmdInputExecute(){
    var cmd_string = document.getElementById("cmd_input").value;
    cmd_string = cmd_string.trim(); //leerzeichen am anfang und ende entfernen    
    cmd_string = cmd_string.replace(/\r/,"\n");
    cmd_string += "\n";
    
    var string_helper1;
    var string_helper2;
    //delete comments between //* */
    while(cmd_string.indexOf("/*") > -1){
        string_helper1 = cmd_string.substring(0, cmd_string.indexOf("/*"));
        string_helper2 = cmd_string.substring(cmd_string.indexOf("/*")+2,cmd_string.length);
        
        if(string_helper2.indexOf("*/") < 0) {
            cmd_string = string_helper1;
        } else {
            cmd_string = string_helper1 + string_helper2.substring(string_helper2.indexOf("*/")+2,string_helper2.length);       
        }    
    }
    
    //delete rest in line after // comments
    while(cmd_string.indexOf("//") > -1){
        string_helper1 = cmd_string.substring(0, cmd_string.indexOf("//"));
        string_helper2 = cmd_string.substring(cmd_string.indexOf("//"),cmd_string.length);
        
        cmd_string = string_helper1 + string_helper2.substring(string_helper2.indexOf("\n"),string_helper2.length);
    }
    
    //split by line
    var cmd_strings = cmd_string.split(/\n/);
    
    var datastring = "";
    var datacnt = 0;
    
    //build datastring
    for(i=0; i<cmd_strings.length; i++){
        if(cmd_strings[i] != ""){
            datastring += "&cmd"+datacnt+"="+cmd_strings[i].trim();
            datacnt++;
        }
    }
    
    if(datacnt > 0){
        var data = "cmdcnt="+datacnt+datastring;
        console.log(data);
        loadFile(data, 5000, showMessage, "Return Data:\n\n")
    }
}

function loadFile(data, timeout, callback) {
    var args = Array.prototype.slice.call(arguments, 3);
    var xhr = new XMLHttpRequest();
    xhr.ontimeout = function () {
        console.error("The request for " + url + " timed out.");
    };
    xhr.onload = function() {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                callback.apply(xhr, args);
            } else {
                console.error(xhr.statusText);
            }
        }
    };
    xhr.open("POST", "xhr", true);
    xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    xhr.timeout = timeout;
    xhr.send(data);
}
    


function showMessage (message) {
    console.log(this.responseText);
}











function readSingleFile(e) {
     var file = e.target.files[0];
     if (!file) {
       return;
     }
     var reader = new FileReader();
     reader.onload = function(e) {
       var contents = e.target.result;
       pushContentsToCmdInput(contents);
     };
     reader.readAsText(file);
}

function  pushContentsToCmdInput(contents) {
     var element = document.getElementById('cmd_input');
     element.textContent = contents;
}

