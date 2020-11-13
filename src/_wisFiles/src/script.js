function onload(){    
    document.getElementById('file_input').addEventListener('change', readSingleFile, false);    
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
