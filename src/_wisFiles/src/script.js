/* ONLOAD */

function onload(){    
    document.getElementById('cmd_file').addEventListener('change', readFileSystemdialogue, false);   
    document.addEventListener("keydown", cmd_keylistener, false);
    document.getElementById('tab_defaultOpen').click();
    openInfoNWcon("Connected.");
    checkConnection();
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
    
    infonwerr_open = false;
    infonwcon_open = false;
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







/* CHECK CONNECTION TO WIS */

function checkConnection(){
    loadAsync("con", "", 1000, closeInfoNWerr);
    setTimeout(checkConnection, 3000);
}

var infonwerr_open = false;
function openInfoNWerr(text){
    if(infonwerr_open == false){
        openInfo("err", text);
        infonwerr_open = true;  
    }
}

function closeInfoNWerr(){
    if(infonwerr_open){
        closeInfo();
        openInfoNWcon("Connection reestablished.")        
    }
}


var infonwcon_open = false;
function openInfoNWcon(text){
    if(!infonwcon_open){
        openInfo("suc", text);
        infonwcon_open = true;
        setTimeout(function(){closeInfoNWcon();},2000)
    }
}

function closeInfoNWcon(){
    if(infonwcon_open)
        closeInfo();
}






/* TAB SWITCHING */
var current_tab = "";

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
    
    current_tab = tabname;
}







/* TAB CMD load and save */

var drag_active = false;
function cmdInputDragOver(e){
    e.preventDefault();  
    if(!drag_active){
        drag_active = true;
        document.getElementById("cmd_input").style.transition = "0.1s";
        document.getElementById("cmd_input").className = "ondragover";
    }
}

function cmdInputDragLeave(e){
    e.preventDefault();  
    if(drag_active){
        drag_active = false;
        document.getElementById("cmd_input").className = "ondragleave";
        setTimeout(function(){document.getElementById("cmd_input").style.transition = "0s";},100)
    }
}


function cmdInputDrop(e){
    if(drag_active){
        drag_active = false;
        document.getElementById("cmd_input").className = "ondragleave";
        setTimeout(function(){document.getElementById("cmd_input").style.transition = "0s";},100)
    }
    
    e.preventDefault();
    
    if (e.dataTransfer.items) {
        // Use DataTransferItemList interface to access the file(s)
        for (var i = 0; i < e.dataTransfer.items.length; i++) {
            // If dropped items aren't files, reject them
            if (e.dataTransfer.items[i].kind === 'file') {
                fileReaderCmdInput( e.dataTransfer.items[i].getAsFile());
            }
        }
    } else {
        // Use DataTransfer interface to access the file(s)
        for (var i = 0; i < e.dataTransfer.files.length; i++) {
            fileReaderCmdInput( e.dataTransfer.files[i]);
        }
    }  
}


function fileReaderCmdInput(file){
    if(file.name.substr(file.name.length - 5).indexOf(".mocl") > -1 
    || file.name.substr(file.name.length - 5).indexOf(".MOCL") > -1
    || file.name.substr(file.name.length - 5).indexOf(".txt") > -1
    || file.name.substr(file.name.length - 5).indexOf(".TXT") > -1){
        var reader = new FileReader();
        reader.onload = function(e) {
            var contents = e.target.result;
            addToCmdInput("/********************************************** \n FILE: "+file.name+" \n**********************************************/");
            addToCmdInput(contents);
        };
        reader.readAsText(file);        
    } else {
        openInfo("warn", "Only .mocl or .txt files as input!");
    }
}


function readFileSystemdialogue(e) {
    for (var i = 0; i < e.target.files.length; i++) {
        if (!e.target.files[i]) {
           continue;
        }
        fileReaderCmdInput( e.target.files[i]);
    }
}

function  addToCmdInput(contents) {
    var element = document.getElementById('cmd_input');
    if(element.value != ""){
        element.value += "\n\n";
    }
    element.value += contents;
}



function cmd_keylistener(e) {
    if ((window.navigator.platform.match("Mac") ? e.metaKey : e.ctrlKey)  && e.keyCode == 83) {
        e.preventDefault();
        
        if(current_tab == "tab_cmd"){
            saveAsMoclFile();
        }
    }
}



function saveAsMoclFile(){    
    var textData = document.getElementById('cmd_input').value; 
    
    if(textData.trim() == "") return;
                
    document.getElementById("cmd_input").style.transition = "0.1s";
    document.getElementById("cmd_input").className = "onsave";

    setTimeout(function(){
        document.getElementById("cmd_input").className = "ondragleave";
        setTimeout(function(){document.getElementById("cmd_input").style.transition = "0s";},100);                
    },100);
    
    var date = new Date();
    var filename = "mocmdlist_"+date.getFullYear()+(date.getMonth()+1)+date.getDate()+"_"+(date.getHours() < 10 ? "0" : "")+date.getHours()+(date.getMinutes() < 10 ? "0" : "")+date.getMinutes()+".mocl";
        
    var blob = new Blob([textData], {type: "text/plain;charset=utf-8"});
    saveAs(blob, filename);
}




















function cmdInputExecute(){
    var cmd_string = document.getElementById("cmd_input").value;
    cmd_string = cmd_string.trim(); //leerzeichen am anfang und ende entfernen    
    cmd_string = cmd_string.replace(/\r/,"\n").replace(/\t/," ");
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
        cmd_strings[i] = cmd_strings[i].trim();
        if(cmd_strings[i] != "" && cmd_strings[i] != null){
            datastring += "&cmd"+datacnt+"="+cmd_strings[i].trim();
            datacnt++;
        }
    }
    
    if(datacnt > 0){
        var data = "cmdcnt="+datacnt+datastring;
        console.log(data);
        loadAsync("xhr", data, 2000, showMessage)
    }
}




function loadAsync(adr, data, timeout, callback) {
    var xhr = new XMLHttpRequest();
    xhr.ontimeout = function () {
        openInfoNWerr("The connection timed out. Check if WIS is active.");
    };
    xhr.onerror = function () {
        openInfoNWerr("Connection lost. Check if WIS is active and reload WebGUI.");        
    }
    xhr.onload = function() {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                closeInfoNWerr();
                if(adr=="xhr") callback.apply(xhr);
            } else {
                openInfoNWerr("Connection lost. Check if WIS is active and reload WebGUI.");      
            }
        }
    };
    xhr.open("POST", adr, true);
    xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    xhr.timeout = timeout;
    xhr.send(data);
}
    


function showMessage () {
    console.log(this.responseText);
}







