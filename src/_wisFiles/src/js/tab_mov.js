var REFRESH_RATE_MOV = REFRESH_RATE_MOV_ini;

function mov_start(){    
    sendCmds(["mv start"], REFRESH_RATE_MOV-100, tab_mov_responseHandler);
}

function mov_install(){
    sendCmds(["mv install"], REFRESH_RATE_MOV-100, tab_mov_responseHandler);    
}




function tab_mov_responseHandler(){   
    //console.log("tab_mov: \n"+this.responseText);
    var response = JSON.parse(this.responseText);
    
    if(response.cmd_ans){
        var cmd_ans = response.cmd_ans;
        
        for(var i=0; i < cmd_ans.length; i++){
            if(cmd_ans[i].cmd == "mv start"){
                // mo visualizer started
                if(!cmd_ans[i].err)
                    openInfo("suc", cmd_ans[i].ans);
            } else if(cmd_ans[i].cmd == "mv install"){
                // mo visualizer installer started
                if(!cmd_ans[i].err)
                    openInfo("suc", cmd_ans[i].ans);
            } else if(cmd_ans[i].err){
                openInfo("err", cmd_ans[i].err);
            }
            
        }
    } else {
        openInfo("err", "Could not read data from WebInterfaceServer.");
    }
}