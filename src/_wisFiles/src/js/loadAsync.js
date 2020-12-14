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



function sendCmds(dataarray, timeout, callback) {
    var data = "";
    var datacnt = 0;
    
    //build datastring
    for(i=0; i<dataarray.length; i++){
        dataarray[i] = dataarray[i].trim();
        if(dataarray[i] != "" && dataarray[i] != null){
            if(dataarray[i] == "show me memes" && easterEggUnlock(3)){
            } else if(dataarray[i] == "memes" && easterEggUnlock(2)){
            } else if(dataarray[i] == "easteregg" && easterEggUnlock(1)){
            } else if(dataarray[i] == "sfx on"){ easterEgg_sfxEnable();
            } else if(dataarray[i] == "sfx v+"){ easterEgg_sfxVolume(true);
            } else if(dataarray[i] == "sfx v-"){ easterEgg_sfxVolume(false);
            } else if(dataarray[i] == "sfx off"){ easterEgg_sfxDisable();   
            } else {
                if(data.length > 0) data += "&";
                data += "cmd"+datacnt+"="+dataarray[i];
                datacnt++;                
            }
        }
    }
    data = data.replaceAll("+",">>>plus<<<");
    
    if(datacnt > 0){    
        loadAsync("xhr", data, timeout, callback);
    }
}