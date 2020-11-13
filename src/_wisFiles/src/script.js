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
    window.alert(message + this.responseText);
}





function cmdInputExecute(){
    var cmd_string = document.getElementById("cmd_input").value;
    cmd_string = cmd_string.trim(); //leerzeichen am anfang und ende entfernen
    var cmd_strings = cmd_string.replace(/\r/,"\n").split(/\n/);
    
    var datastring = "";
    var datacnt = 0;
    
    for(i=0; i<cmd_strings.length; i++){
        if(cmd_strings[i] != ""){
            datastring += "&cmd"+datacnt+"="+cmd_strings[i];
            datacnt++;
        }
    }
    
    if(datacnt > 0){
        var data = "cmdcnt="+datacnt+datastring;
        loadFile(data, 2000, showMessage, "Return Data:\n\n")
    }
}