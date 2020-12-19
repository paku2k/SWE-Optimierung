var REFRESH_RATE_FUNCTIONS = REFRESH_RATE_FUNCTIONS_ini;


function ffLoadBoundaries(){
    sendCmds(["ffm lsbd -json"], (REFRESH_RATE_FUNCTIONS/2)-100, tab_functions_responseHandler);
}

function tab_functions_responseHandler(){   
    //console.log("tab_functions: \n"+this.responseText);
    var response = JSON.parse(this.responseText);
    
    if(response.cmd_ans){
        var cmd_ans = response.cmd_ans;
        
        for(var i=0; i < cmd_ans.length; i++){
            if(cmd_ans[i].cmd == "ffm lsbd -json"){
                // display fitness function boundaries
                if(cmd_ans[i].err){
                    openInfo("err", "Error while loading fitnessfunction boundaries: "+cmd_ans[i].err);
                } else if(cmd_ans[i].ans){
                    ffDisplayBoundaries(cmd_ans[i].ans.ffBoundaries);
                } else {
                    openInfo("err", "Could not read data from WebInterfaceServer.");
                }
            } else if(cmd_ans[i].cmd == "ffm list -json"){
                // list of custom fitness functions
                if(cmd_ans[i].ans){
                    cffList = cmd_ans[i].ans.cff;
                    cff_createOrChangeHTML();
                } else {
                    openInfo("err", "Could not read data from WebInterfaceServer.");
                }
            } else if(cmd_ans[i].cmd.includes("ffm create ")){
                // created new cff
                if(cmd_ans[i].ans){
                    sendCmds(["ffm list -json"], (REFRESH_RATE_FUNCTIONS/2)-100, tab_functions_responseHandler);
                    document.getElementById("ff_editor_in").value = "";
                    document.getElementById("ff_editor_lowerBoundary").value = "";
                    document.getElementById("ff_editor_upperBoundary").value = "";
                    ffEditor_oninput();
                } else if(cmd_ans[i].err){
                    openInfo("err", cmd_ans[i].err);
                } else {
                    openInfo("err", "Could not read data from WebInterfaceServer.");
                }
            } else if(cmd_ans[i].cmd.includes("ffm delete ")){
                // deleted cff
                if(cmd_ans[i].ans){
                    sendCmds(["ffm list -json"], (REFRESH_RATE_FUNCTIONS/2)-100, tab_functions_responseHandler);
                } else if(cmd_ans[i].err){
                    openInfo("err", cmd_ans[i].err);
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




/* custom fitness functions */ 

var cffList;

function checkCffList(){    
    sendCmds(["ffm list -json"], (REFRESH_RATE_FUNCTIONS/2)-100, tab_functions_responseHandler);
    setTimeout(checkCffList, REFRESH_RATE_FUNCTIONS);
}

function cff_createOrChangeHTML() {
    //check if element still in list or has to be deleted
    var nodes = document.getElementById("ffEditor_fields").parentElement.getElementsByTagName("tr");
    for(var i=1; i<nodes.length; i++){
        var id = nodes[i].getAttribute("id").replace("cff_","");
        var del = true;
        if(cffList != null){
            for(var j=0; j<cffList.length; j++){
                if(id == cffList[j].id){
                    del = false;
                    continue;
                }
            }
        }
        if(del){
            document.getElementById("ffEditor_fields").parentElement.removeChild(nodes[i]);
            i--;            
        }
    }
    
    //check for changes or new equations
    if(cffList != null){
        for(var i=0; i<cffList.length; i++){
            var cff = cffList[i]; 
            if(document.getElementById("cff_"+cff.id)){
                //if exists check for changes (equation)
                if(document.getElementById("cff_"+cff.id+"_equation").style.display != "none"){
                    if(document.getElementById("cff_"+cff.id+"_equation").getElementsByTagName("script")[0] != null){
                        if(document.getElementById("cff_"+cff.id+"_equation").getElementsByTagName("script")[0].innerHTML != "\\begin{equation} "+cff.functionString+" \\end{equation}"){
                            document.getElementById("cff_"+cff.id+"_equation2").innerHTML = "\\begin{equation} "+cff.functionString+" \\end{equation}";
                            MathJax.Hub.Queue(["Typeset",MathJax.Hub, "cff_"+cff.id+"_equation2"]);   
                            setTimeout(function(id){ 
                                document.getElementById("cff_"+id+"_equation").style.display = "none";
                                document.getElementById("cff_"+id+"_equation2").style.display = "inherit"; 
                            }.bind(this, cff.id),500);
                        }               
                    }                    
                } else {
                    if(document.getElementById("cff_"+cff.id+"_equation2").getElementsByTagName("script")[0] != null){
                        if(document.getElementById("cff_"+cff.id+"_equation2").getElementsByTagName("script")[0].innerHTML != "\\begin{equation} "+cff.functionString+" \\end{equation}"){
                            document.getElementById("cff_"+cff.id+"_equation").innerHTML = "\\begin{equation} "+cff.functionString+" \\end{equation}";
                            MathJax.Hub.Queue(["Typeset",MathJax.Hub, "cff_"+cff.id+"_equation"]);   
                            setTimeout(function(id){     
                                document.getElementById("cff_"+id+"_equation2").style.display = "none";
                                document.getElementById("cff_"+id+"_equation").style.display = "inherit"; 
                            }.bind(this, cff.id),500);
                        }  
                    }
                }
                
                //if exists check for changes (boundaries)
                if(document.getElementById("cff_"+cff.id+"_boundaries").style.display != "none"){
                    if(document.getElementById("cff_"+cff.id+"_boundaries").getElementsByTagName("script")[0] != null){
                        if(document.getElementById("cff_"+cff.id+"_boundaries").getElementsByTagName("script")[0].innerHTML != ('\\begin{equation} '+((cff.bdl!=null)?cff.bdl:'\\text{not set}~')+' < x_i < '+((cff.bdu!=null)?cff.bdu:'\\text{not set}~')+' \\end{equation}').replace("-","\\text{-}")){
                            document.getElementById("cff_"+cff.id+"_boundaries2").innerHTML = ('\\begin{equation} '+((cff.bdl!=null)?cff.bdl:'\\text{not set}~')+' < x_i < '+((cff.bdu!=null)?cff.bdu:'\\text{not set}~')+' \\end{equation}').replace("-","\\text{-}");
                            MathJax.Hub.Queue(["Typeset",MathJax.Hub, "cff_"+cff.id+"_boundaries2"]);   
                            setTimeout(function(id){ 
                                document.getElementById("cff_"+id+"_boundaries").style.display = "none";
                                document.getElementById("cff_"+cff.id+"_boundaries2").style.display = ""; 
                            }.bind(this, cff.id),500);
                        }               
                    }                    
                } else {
                    if(document.getElementById("cff_"+cff.id+"_boundaries2").getElementsByTagName("script")[0] != null){
                        if(document.getElementById("cff_"+cff.id+"_boundaries2").getElementsByTagName("script")[0].innerHTML != ('\\begin{equation} '+((cff.bdl!=null)?cff.bdl:'\\text{not set}~')+' < x_i < '+((cff.bdu!=null)?cff.bdu:'\\text{not set}~')+' \\end{equation}').replace("-","\\text{-}")){
                            document.getElementById("cff_"+cff.id+"_boundaries").innerHTML = ('\\begin{equation} '+((cff.bdl!=null)?cff.bdl:'\\text{not set}~')+' < x_i < '+((cff.bdu!=null)?cff.bdu:'\\text{not set}~')+' \\end{equation}').replace("-","\\text{-}");
                            MathJax.Hub.Queue(["Typeset",MathJax.Hub, "cff_"+cff.id+"_boundaries"]);   
                            setTimeout(function(id){     
                                document.getElementById("cff_"+cff.id+"_boundaries2").style.display = "none";
                                document.getElementById("cff_"+id+"_boundaries").style.display = ""; 
                            }.bind(this, cff.id),500);
                        }      
                    }
                }
            } else {
                //does not exists -> create new
                var tr = document.createElement("tr");
                tr.setAttribute("id","cff_"+cff.id);
                tr.innerHTML = '<td id="cff_'+cff.id+'_id">'+cff.id+'</td><td ondblclick="cffCopyToEditor('+cff.id+')"><div id="cff_'+cff.id+'_equation" class="math">\\begin{equation} '+cff.functionString+' \\end{equation}</div><div id="cff_'+cff.id+'_equation2" class="math"></div></td><td><div class="math" id="cff_'+cff.id+'_boundaries">\\begin{equation} '+((cff.bdl!=null)?cff.bdl:'\\text{not set}~')+' < x_i < '+((cff.bdu!=null)?cff.bdu:'\\text{not set}~')+' \\end{equation}</div><div class="math" id="cff_'+cff.id+'_boundaries2"></div></td><td onclick="cff_delete('+cff.id+');">delete</td>';
                document.getElementById("ffEditor_fields").parentElement.appendChild(tr);
                document.getElementById("cff_"+cff.id).style.display = "none";
                document.getElementById("cff_"+cff.id+"_equation2").style.display = "none";
                document.getElementById("cff_"+cff.id+"_boundaries2").style.display = "none";                
                document.getElementById("cff_"+cff.id+"_boundaries").innerHTML = document.getElementById("cff_"+cff.id+"_boundaries").innerHTML.replace("-","\\text{-}");
                document.getElementById("cff_"+cff.id+"_boundaries2").innerHTML = document.getElementById("cff_"+cff.id+"_boundaries2").innerHTML.replace("-","\\text{-}");
                
                MathJax.Hub.Queue(["Typeset",MathJax.Hub, "cff_"+cff.id+"_equation"]);     
                MathJax.Hub.Queue(["Typeset",MathJax.Hub, "cff_"+cff.id+"_boundaries"]);     
                setTimeout(function(id){
                    document.getElementById("cff_"+id).style.display = "inherit";
                }.bind(this, cff.id),500);
            }
        }
    }
}


function cff_delete(id){
    sendCmds(["ffm delete "+id], (REFRESH_RATE_FUNCTIONS/2)-100, tab_functions_responseHandler);    
}
function cff_create(){
    var eq = document.getElementById("ff_editor_in").value;
    var bdl = document.getElementById("ff_editor_lowerBoundary").value.replace(",",".").trim();
    var bdu = document.getElementById("ff_editor_upperBoundary").value.replace(",",".").trim();
    if(eq.trim() != ""){
        if(bdl != "" && bdu != ""){
            var cmd = ["ffm create "+eq, "ffm set bdl "+bdl, "ffm set bdu "+bdu];            
        } else if(bdu != ""){
            var cmd = ["ffm create "+eq, "ffm set bdu "+bdu];            
        } else if(bdl != ""){
            var cmd = ["ffm create "+eq, "ffm set bdl "+bdl];            
        } else {
            var cmd = ["ffm create "+eq];            
        }
        sendCmds(cmd, (REFRESH_RATE_FUNCTIONS/2)-100, tab_functions_responseHandler);
    }
}




function cffCopyToEditor(id){
    var cff;
    if(cffList != null){
        for(var i=0; i<cffList.length; i++){
            if(id == cffList[i].id){
                cff = cffList[i];
                break;
            }
        }
        if(cff != null){
            document.getElementById("ff_editor_in").value = cff.functionString;
            document.getElementById("ff_editor_lowerBoundary").value = cff.bdl;
            document.getElementById("ff_editor_upperBoundary").value = cff.bdu;
            ffEditor_oninput();
        }
    }
}




/* function editor */

var ff_editor_out_current = 1;
var ff_editor_out_valuecurrent = "";
var ff_editor_lock = false;

function ffEditor_oninput(){
    if(ff_editor_lock) return;
    var e = document.getElementById("ff_editor_in");
    if(e.value.trim() == ff_editor_out_valuecurrent) return;
    
    ff_editor_lock = true;
    ff_editor_out_valuecurrent = e.value.trim();
    var equationString = "\\begin{equation}" + e.value.trim() + "\\end{equation}";
    
    if(ff_editor_out_current == 1){
        document.getElementById("ff_editor_out2").innerHTML = equationString;
        MathJax.Hub.Queue(["Typeset",MathJax.Hub,"ff_editor_out2"]); 
        setTimeout(function(){
            document.getElementById("ff_editor_out1").style.display = "none";
            document.getElementById("ff_editor_out2").style.display = "table";
            ff_editor_lock = false;
            if(e.value.trim() != ff_editor_out_valuecurrent) ffEditor_oninput();
        },333);
        ff_editor_out_current = 2;
    } else {
        document.getElementById("ff_editor_out1").innerHTML = equationString;
        MathJax.Hub.Queue(["Typeset",MathJax.Hub,"ff_editor_out1"]); 
        setTimeout(function(){
            document.getElementById("ff_editor_out2").style.display = "none";
            document.getElementById("ff_editor_out1").style.display = "table";
            ff_editor_lock = false;
            if(e.value.trim() != ff_editor_out_valuecurrent) ffEditor_oninput();
        },333);
        ff_editor_out_current = 1;
    }
}

