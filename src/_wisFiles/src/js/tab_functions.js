function ffLoadBoundaries(){
    sendCmds(["sm lsffbd -json"], 2000, tab_functions_responseHandler);
}

function tab_functions_responseHandler(){   
    console.log("tab_functions: \n"+this.responseText);
    var response = JSON.parse(this.responseText);
    
    if(response.cmd_ans){
        var cmd_ans = response.cmd_ans;
        
        for(var i=0; i < cmd_ans.length; i++){
            if(cmd_ans[i].err){
                openInfo("err", "Error while loading fitnessfunction boundaries: "+cmd_ans[i].err);
            } else if(cmd_ans[i].cmd == "sm lsffbd -json"){
                // display fitness function boundaries
                if(cmd_ans[i].ans){
                    ffDisplayBoundaries(cmd_ans[i].ans.ffBoundaries);
                } else {
                    openInfo("err", "Could not read data from WebInterfaceServer.");
                }
            }            
        }
    } else {
        openInfo("err", "Could not read data from WebInterfaceServer.");
    }
}


function ffDisplayBoundaries(json){    
    for(var i=0; i<json.length; i++){
        ffDisplayBoundary(json[i].ffid, json[i].lower, json[i].upper);
    }

    MathJax.Hub.Queue(["Typeset",MathJax.Hub]);    
}

function ffDisplayBoundary(ffid, lower, upper){
    if(document.getElementById("ff_"+ffid+"_boundaries") != null){
        var s = "\\begin{equation} "+lower+" &lt; x_i &lt; "+upper+" \\end{equation}";
        document.getElementById("ff_"+ffid+"_boundaries").innerHTML = s.replace("-","\\text{-}");
    }
}