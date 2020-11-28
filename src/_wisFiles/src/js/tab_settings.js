function loadAppInfo(){
    sendCmds(["app info -json"], 2000, tab_settings_responseHandler);
}

function loadAppSettings(){
    sendCmds(["cfg -list -json"], 2000, tab_settings_responseHandler);
}

function tab_settings_responseHandler(){   
    console.log("tab_settings: \n"+this.responseText);
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
            }
            
        }
    } else {
        openInfo("err", "Could not read data from WebInterfaceServer.");
    }
}





function createOrChangeSettingHTML(key, value){
    /* tbi */
    console.log(key +": "+ value);
}