/* ONLOAD */

function onload(){    
    document.getElementById('cmd_file').addEventListener('change', readFileSystemdialogue, false);  
    document.addEventListener("keydown", cmd_keylistener, false);
    document.getElementById('tab_defaultOpen').click();
   
    
    openInfoNWcon("Connected.");
   
   
    checkConnection();
    loadAppInfo();
    loadAppSettings();
    
    document.getElementById('solver_add').click();
    loadSolverInitial();
}


/* CHECK CONNECTION TO WIS */

function checkConnection(){
    loadAsync("con", "", 1000, closeInfoNWerr);
    setTimeout(checkConnection, 3000);
}

var infonwerr_open = false;
function openInfoNWerr(text){
    if(infonwerr_open == false){
        openInfoAdvanced("err", text, -1);
        infonwerr_open = true;  
    }
}

function closeInfoNWerr(){
    if(infonwerr_open){
        closeInfo();
        openInfoNWcon("Connection reestablished.");        
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
