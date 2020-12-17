const REFRESH_RATE_SOLVER_ini = 800;
const REFRESH_RATE_OPTIMIZER_ini = 1000;
const REFRESH_RATE_CMD_ini = 2000;
const REFRESH_RATE_FUNCTIONS_ini = 5000;
const REFRESH_RATE_SETTINGS_ini = 5000;
const REFRESH_RATE_MOV_ini = 2000;

const REFRESH_RATE_SOLVER_max = 2500;
const REFRESH_RATE_OPTIMIZER_max = 2500;
const REFRESH_RATE_CMD_max = 4000;
const REFRESH_RATE_FUNCTIONS_max = 10000;
const REFRESH_RATE_SETTINGS_max = 10000;
const REFRESH_RATE_MOV_max = 5000;

var async_timeout_counter = 0;
var async_increaseRefRate_timeout;

function loadAsync(adr, data, timeout, callback) {
    var xhr = new XMLHttpRequest();
    xhr.ontimeout = function () {
        if(REFRESH_RATE_SOLVER < REFRESH_RATE_SOLVER_max)
            REFRESH_RATE_SOLVER += ((REFRESH_RATE_SOLVER_max-REFRESH_RATE_SOLVER_ini)/20);        
        if(REFRESH_RATE_OPTIMIZER < REFRESH_RATE_OPTIMIZER_max)
            REFRESH_RATE_OPTIMIZER += ((REFRESH_RATE_OPTIMIZER_max-REFRESH_RATE_OPTIMIZER_ini)/20);        
        if(REFRESH_RATE_CMD < REFRESH_RATE_CMD_max)
            REFRESH_RATE_CMD += ((REFRESH_RATE_CMD_max-REFRESH_RATE_CMD_ini)/20);        
        if(REFRESH_RATE_FUNCTIONS < REFRESH_RATE_FUNCTIONS_max)
            REFRESH_RATE_FUNCTIONS += ((REFRESH_RATE_FUNCTIONS_max-REFRESH_RATE_FUNCTIONS_ini)/20);        
        if(REFRESH_RATE_SETTINGS < REFRESH_RATE_SETTINGS_max)
            REFRESH_RATE_SETTINGS += ((REFRESH_RATE_SETTINGS_max-REFRESH_RATE_SETTINGS_ini)/20);        
        if(REFRESH_RATE_MOV < REFRESH_RATE_MOV_max)
            REFRESH_RATE_MOV += ((REFRESH_RATE_MOV_max-REFRESH_RATE_MOV_ini)/20);
        
        //console.log("functions: "+REFRESH_RATE_FUNCTIONS+"\noptimizer: "+REFRESH_RATE_OPTIMIZER+"\nsolver: "+REFRESH_RATE_SOLVER+"\ncmd: "+REFRESH_RATE_CMD+"\nsettings: "+REFRESH_RATE_SETTINGS+"\nmov: "+REFRESH_RATE_MOV);
        
        async_timeout_counter++;
        if(async_timeout_counter > 5)
            openInfoNWerr("The connection timed out. Check if WIS is active.");
        
        clearTimeout(async_increaseRefRate_timeout);
        async_increaseRefRate_timeout = setTimeout(increaseRefRate, 60000);
    };
    xhr.onerror = function () {
        openInfoNWerr("Connection lost. Check if WIS is active and reload WebGUI.");        
    }
    xhr.onload = function() {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                closeInfoNWerr();
                async_timeout_counter = 0;
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

function increaseRefRate() {
    if(REFRESH_RATE_SOLVER > REFRESH_RATE_SOLVER_ini)
        REFRESH_RATE_SOLVER -= ((REFRESH_RATE_SOLVER_max-REFRESH_RATE_SOLVER_ini)/20);        
    if(REFRESH_RATE_OPTIMIZER > REFRESH_RATE_OPTIMIZER_ini)
        REFRESH_RATE_OPTIMIZER -= ((REFRESH_RATE_OPTIMIZER_max-REFRESH_RATE_OPTIMIZER_ini)/20);        
    if(REFRESH_RATE_CMD > REFRESH_RATE_CMD_ini)
        REFRESH_RATE_CMD -= ((REFRESH_RATE_CMD_max-REFRESH_RATE_CMD_ini)/20);        
    if(REFRESH_RATE_FUNCTIONS > REFRESH_RATE_FUNCTIONS_ini)
        REFRESH_RATE_FUNCTIONS -= ((REFRESH_RATE_FUNCTIONS_max-REFRESH_RATE_FUNCTIONS_ini)/20);        
    if(REFRESH_RATE_SETTINGS > REFRESH_RATE_SETTINGS_ini)
        REFRESH_RATE_SETTINGS -= ((REFRESH_RATE_SETTINGS_max-REFRESH_RATE_SETTINGS_ini)/20);        
    if(REFRESH_RATE_MOV > REFRESH_RATE_MOV_ini)
        REFRESH_RATE_MOV -= ((REFRESH_RATE_MOV_max-REFRESH_RATE_MOV_ini)/20);

    //console.log("functions: "+REFRESH_RATE_FUNCTIONS+"\noptimizer: "+REFRESH_RATE_OPTIMIZER+"\nsolver: "+REFRESH_RATE_SOLVER+"\ncmd: "+REFRESH_RATE_CMD+"\nsettings: "+REFRESH_RATE_SETTINGS+"\nmov: "+REFRESH_RATE_MOV);  
    
    clearTimeout(async_increaseRefRate_timeout);
    async_increaseRefRate_timeout = setTimeout(increaseRefRate, 6000);  
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
            } else if(dataarray[i] == "help"){ openHelp();
            } else if(dataarray[i] == "doc"){ openDoc();
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