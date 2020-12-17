var REFRESH_RATE_SETTINGS = REFRESH_RATE_SETTINGS_ini;

function loadAppInfo(){
    sendCmds(["app info -json"], (REFRESH_RATE_SETTINGS/2)-100, tab_settings_responseHandler);
}

function loadAppSettings(){
    sendCmds(["cfg -list -json"], REFRESH_RATE_SETTINGS-100, tab_settings_responseHandler);
    setTimeout(loadAppSettings, REFRESH_RATE_SETTINGS);
}

function tab_settings_responseHandler(){   
    //console.log("tab_settings: \n"+this.responseText);
    var response = JSON.parse(this.responseText);
    
    if(response.cmd_ans){
        var cmd_ans = response.cmd_ans;
        
        for(var i=0; i < cmd_ans.length; i++){
            if(cmd_ans[i].cmd == "app info -json"){
                // set app info
                document.getElementById("appinfo_date").innerHTML = cmd_ans[i].ans.date;
                document.getElementById("appinfo_developers").innerHTML = cmd_ans[i].ans.developers.replaceAll("\n","<br>").replaceAll("\r","<br>");
                document.getElementById("appinfo_version").innerHTML = cmd_ans[i].ans.version;
                
            }else if(cmd_ans[i].cmd == "cfg -list -json"){
                // create/change list of settings
                if(cmd_ans[i].ans.cfg_list){
                    var cfg_list = cmd_ans[i].ans.cfg_list;
                    
                    for(var j=0; j < cfg_list.length; j++){
                        createOrChangeSettingHTML(cfg_list[j].key, cfg_list[j].value);
                    }
                } 
                
            }else if(cmd_ans[i].cmd == "cfg -reset"){
                // settings reset to default
                if(cmd_ans[i].ans){
                    openInfo("suc", cmd_ans[i].ans);
                    loadAppSettings();
                } else if(cmd_ans[i].err){
                    openInfo("err", cmd_ans[i].err);
                } 
                
            }else if(cmd_ans[i].cmd.includes("cfg ")){
                // setting changed
                if(cmd_ans[i].ans){
                    openInfo("suc", cmd_ans[i].ans);
                    loadAppSettings();
                } else if(cmd_ans[i].err){
                    openInfo("err", cmd_ans[i].err);
                } 
            }
            
        }
    } else {
        openInfo("err", "Could not read data from WebInterfaceServer.");
    }
}


/* Settings */

function createOrChangeSettingHTML(key, value){    
    if(key == "WebGUIshowDelSolvers"){
        setShowDelSolvers(value);
    }  
    if(key == "WebGUIshowDelOptims"){
        setShowDelOptimizers(value);
    }
    
    if(document.getElementById("settings_"+key)){
        if(!(document.getElementById("settings_"+key).lastChild.firstChild===document.activeElement)){
            document.getElementById("settings_"+key).lastChild.innerHTML = '<input type="text" value="'+value+'" onchange="settingsChange(\''+key+'\', this.value)">';
        } else {
            return;
        }
        
    } else {        
        var tr = document.getElementById("tab_settings_table").insertRow(-1);
        var td_key = document.createElement("td");
        var td_value = document.createElement("td");

        tr.setAttribute("id","settings_"+key);

        td_key.innerHTML = key;
        td_value.innerHTML = '<input type="text" value="'+value+'" onchange="settingsChange(\''+key+'\', this.value)">';

        tr.appendChild(td_key);
        tr.appendChild(td_value);
        
    }
}


function settingsChange(key, value){
    sendCmds(["cfg "+key+" "+value], (REFRESH_RATE_SETTINGS/2)-100, tab_settings_responseHandler);
}



function settingsReset(){
    if(window.confirm("Reset application settings?"))
        sendCmds(["cfg -reset"], (REFRESH_RATE_SETTINGS/2)-100, tab_settings_responseHandler);    
}



/* help & documentation */
function openHelp(){
    window.open('mo_help.pdf', '_blank');
}
function openDoc(){
    window.open('mo_help.pdf', '_blank');
}