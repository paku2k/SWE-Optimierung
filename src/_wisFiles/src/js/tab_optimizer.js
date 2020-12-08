var REFRESH_RATE_OPTIMIZER = 1000;


/* TAB_OPTIMIZER switching tabs */
var current_optimizer = "";

function switch_optimizer(e, tabname) {
    var i, tabcontent, tablinks;

    //get all elements with class="optimizer_content" and remove the class "active"
    tablinks = document.getElementsByClassName("optimizer_content");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }

    //get all elements with class="optimizer_list_elem" and remove the class "active"
    tablinks = document.getElementsByClassName("optimizer_list_elem");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
    //remove class "active" from optimizer_add button (label)
    document.getElementById("optimizer_add").className = document.getElementById("optimizer_add").className.replace(" active", "");

    //show the current tab, and add an "active" class to the button that opened the tab
    document.getElementById(tabname).className += " active";
    e.currentTarget.className += " active";
    
    current_optimizer = tabname;
    loadOptimizerDetail_updater();
}





/* initial data load */

var OptiAlgorithms_JSON;

function loadOptimizerInitial(){
    checkOptimizerList();
    loadOptimizerDetail_updater();
    
    sendCmds(["om lsalgo -json"], 1000, tab_optimizer_responseHandler);
}



/* loading data LIST */
var currentOptimizerList_JSON;

function checkOptimizerList(){    
    sendCmds(["om list -json"], 1000, tab_optimizer_responseHandler);
    setTimeout(checkOptimizerList, REFRESH_RATE_OPTIMIZER);
}

function tab_optimizer_responseHandler(){       
    var response = JSON.parse(this.responseText);
    
    if(response.cmd_ans){
        var cmd_ans = response.cmd_ans;
        
        for(var i=0; i < cmd_ans.length; i++){
            if(cmd_ans[i].cmd == "om list -json"){
                // create/change list of optimizers
                if(cmd_ans[i].ans.optimizers){
                    var optimizers = cmd_ans[i].ans.optimizers;
                    
                    currentOptimizerList_JSON = optimizers;
                    
                    for(var j=0; j < optimizers.length; j++){
                        createOrChangeOptimizerListHTML(optimizers[j]);
                    }
                } 
            } else if(cmd_ans[i].cmd == "om lsalgo -json"){
                // create list of algorithms in add optimizer div
                if(cmd_ans[i].ans.algorithms){
                    var algorithms = cmd_ans[i].ans.algorithms;
                    
                    OptiAlgorithms_JSON = algorithms;
                    
                    for(var j=0; j < algorithms.length; j++){
                        createOptiAlgorithmSelectHTML(algorithms[j].algorithm);
                    }
                } 
            } else if(cmd_ans[i].err){
                openInfo("err", cmd_ans[i].err);
            }
            
        }
    } else {
        openInfo("err", "Could not read data from WebInterfaceServer.");
    }
}



function createOrChangeOptimizerListHTML(optimizer){
    if(optimizer.deleted == true){
        if(document.getElementById("optimizerBtn_"+optimizer.id)){ //exists in list
            var btn = document.getElementById("optimizerBtn_"+optimizer.id);
            if(btn.className.includes("active")){
                if(btn.nextElementSibling && btn.nextElementSibling !== document.getElementById('optimizer_add')){
                    btn.nextElementSibling.click();
                } else if(btn.previousElementSibling){
                    btn.previousElementSibling.click();                
                } else {
                    document.getElementById('optimizer_add').click();                       
                }             
            }
            btn.className = "optimizer_list_elem deleted";
            btn.removeAttribute("onclick");
            btn.removeAttribute("style");
            
            if(!showDelOptimizers) btn.parentElement.removeChild(btn);
            
            if(document.getElementById("optimizer_"+optimizer.id))
                document.getElementById("optimizer_"+optimizer.id).innerHTML = "";   
            
            optimizerCompareRemove(optimizer.id);           
            
        } else if(showDelOptimizers) { //does not exist in list
            var button = document.createElement("button");
            button.setAttribute("class", "optimizer_list_elem deleted");
            button.setAttribute("id", "optimizerBtn_"+optimizer.id);
            button.innerHTML = optimizer.id;
            if(optimizer.id > 0){
                document.getElementById("optimizerBtn_"+(optimizer.id-1)).after(button);
            } else {
                document.getElementById("optimizer_list").insertBefore(button, document.getElementById("optimizer_list").firstChild);
            }
        }
        
    } else {        
        if(!document.getElementById("optimizerBtn_"+optimizer.id)){ //does not exist in list
            var button = document.createElement("button");
            button.setAttribute("class", "optimizer_list_elem");
            button.setAttribute("onclick", "switch_optimizer(event, 'optimizer_"+optimizer.id+"')");
            button.setAttribute("id", "optimizerBtn_"+optimizer.id);
            button.innerHTML = optimizer.id+": "+optimizer.algorithm;
            if(optimizer.id > 0){
                var i = optimizer.id - 1;
                while(!document.getElementById("optimizerBtn_"+i) && i >= 0){
                    i--;
                }
                if(i < 0){     
                    document.getElementById("optimizer_list").insertBefore(button, document.getElementById("optimizer_list").firstChild);
                } else {
                    document.getElementById("optimizerBtn_"+i).after(button);                    
                }
            } else {
                document.getElementById("optimizer_list").insertBefore(button, document.getElementById("optimizer_list").firstChild);
            }           
        }
        
        if(document.getElementById("optimizer_"+optimizer.id) == null) 
            newOptimizerContentDiv(optimizer.id, optimizer.algorithm, optimizer.creator);
        
        
        //update status color
        
        var btn = document.getElementById("optimizerBtn_"+optimizer.id);
        btn.removeAttribute("style");
        
        var was_active = false;
        
        if(optimizer.status == -2){
            //initialized
            was_active = btn.className.includes("active");
            
            btn.className = "optimizer_list_elem";
            btn.className += " initialized"
            if(was_active) btn.className += " active";
            
        } else if(optimizer.status == -1){
            //configured
            was_active = btn.className.includes("active");
            
            btn.className = "optimizer_list_elem";
            btn.className += " configured"
            if(was_active) btn.className += " active";
            
        } else if(optimizer.status >= 0 && optimizer.status <= 100){
            //running
            was_active = btn.className.includes("active");
            
            btn.className = "optimizer_list_elem";
            if(was_active) btn.className += " active";
            
            btn.style = "background-image: linear-gradient(to right, green 0%, green "+optimizer.status+"%, #eee "+optimizer.status+"%, #eee 100%);";   
            
        } else if(optimizer.status == 101){
            //result ready
            was_active = btn.className.includes("active");
            
            btn.className = "optimizer_list_elem";
            btn.className += " finished"
            if(was_active) btn.className += " active";
            
        } else if(optimizer.status == 102){
            //terminated
            was_active = btn.className.includes("active");
            
            btn.className = "optimizer_list_elem";
            btn.className += " error"
            if(was_active) btn.className += " active";
            
        } else if(optimizer.status == 103){
            //error
            was_active = btn.className.includes("active");
            
            btn.className = "optimizer_list_elem";
            btn.className += " error"
            if(was_active) btn.className += " active";
            
        }
    }
}


var showDelOptimizers = true;
function setShowDelOptimizers(bool){
    showDelOptimizers = bool;
}






/* loading data OPTIMIZER DETAIL VIEW */

var load_sd_timeout;
var current_optimizer_detail = "";

function loadOptimizerDetail_updater(){
    if(current_optimizer != "add_optimizer"){
        var id = current_optimizer.replace("optimizer_","");
        loadOptimizerStatus(id);    
        if(current_optimizer_detail != current_optimizer){
            current_optimizer_detail = current_optimizer;
            loadOptimizerConfiguration(id);
            loadOptimizerResult(id);
        }
    }
    
    clearTimeout(load_sd_timeout);
    load_sd_timeout = setTimeout(loadOptimizerDetail_updater, REFRESH_RATE_OPTIMIZER);
}




function loadOptimizerStatus(id){
    sendCmds(["om status "+id], 1000, tab_optimizer_detail_responseHandler);     
}

function loadOptimizerConfiguration(id){
    sendCmds(["om config "+id+" -get -json"], 1000, tab_optimizer_detail_responseHandler); 
}

function loadOptimizerResult(id){
    sendCmds(["om result "+id+" -json"], 1000, tab_optimizer_detail_responseHandler);       
}



function tab_optimizer_detail_responseHandler(){   
    //console.log("tab_optimizer_detail: \n"+this.responseText);
    var response = JSON.parse(this.responseText);
    
    if(response.cmd_ans){
        var cmd_ans = response.cmd_ans;
        
        for(var i=0; i < cmd_ans.length; i++){
            if(cmd_ans[i].cmd.includes("om config") && cmd_ans[i].cmd.includes("-get -json")){
                // update configuration values
                var id = cmd_ans[i].cmd.replace("om config ","").replace(" -get -json","");
                if(cmd_ans[i].ans !== undefined)
                    var ans_cfg =  cmd_ans[i].ans.cfg;
                else
                    var ans_cfg = null;
                updateOptimizerConfiguration(id, ans_cfg, cmd_ans[i].err)
                
            } else if(cmd_ans[i].cmd.includes("om config") && cmd_ans[i].cmd.includes("-reset")){
                // info successful reset
                if(cmd_ans[i].ans){
                    openInfoAdvanced("suc", cmd_ans[i].ans, 3000);
                } else if(cmd_ans[i].err){
                    openInfo("err", cmd_ans[i].err);
                }
                
            } else if(cmd_ans[i].cmd.includes("om status")){
                // update status
                var id = cmd_ans[i].cmd.replace("om status ","");
                var stat ="";
                if(cmd_ans[i].ans) stat = cmd_ans[i].ans.replace("Status: ","");
                updateOptimizerStatus(id, stat, cmd_ans[i].err);
                                
            } else if(cmd_ans[i].cmd.includes("om result")){
                // update status
                var id = cmd_ans[i].cmd.replace("om result ","").replace(" -json","");
                updateOptimizerResult(id, cmd_ans[i].ans, cmd_ans[i].err)
                
            } else if(cmd_ans[i].err){
                openInfo("err", cmd_ans[i].err);
            }
            
        }
    } else {
        openInfo("err", "Could not read data from WebInterfaceServer.");
    }
}







function updateOptimizerStatus(id, status, err){
    if(err){
        openInfo("err", err); 
        return;
    }
    
    var statusstring = "";
    
    if(status == -2){
        statusstring = "initialized";
    } else if(status == -1){
        statusstring = "configured";        
    } else if(status >= 0 && status <= 100){
        statusstring = "running ("+Math.round(status)+"%)";          
    } else if(status == 101){
        statusstring = "finished";          
    } else if(status == 102){
        statusstring = "terminated";    
    } else if(status == 103){
        statusstring = "error";               
    }
    
    document.getElementById("optimizer_"+id+"_status").innerHTML = statusstring;
    
    if( ((status == 101 || status == 103) && document.getElementById("optimizer_"+id+"_result").style.display == "none") ||
        ((status != 101 && status != 103) && document.getElementById("optimizer_"+id+"_result").style.display != "none") )
    {
        loadOptimizerResult(id);        
    }
        
    
    if(status < 0){        
        document.getElementById("optimizer_"+id+"_startBtn").style.display = "block"; 
        document.getElementById("optimizer_"+id+"_termBtn").style.display = "none"; 
        document.getElementById("optimizer_"+id+"_clearBtn").style.display = "none";  
        document.getElementById("optimizer_"+id+"_copyfromBtn").style.display = "inherit";
        document.getElementById("optimizer_"+id+"_copyfrom").style.display = "inherit";
        
        lockOptimizerConfiguration(id, false);
        loadOptimizerConfiguration(id);        
        
    } else if(status >= 0 && status <= 100){        
        document.getElementById("optimizer_"+id+"_startBtn").style.display = "none"; 
        document.getElementById("optimizer_"+id+"_termBtn").style.display = "block"; 
        document.getElementById("optimizer_"+id+"_clearBtn").style.display = "none"; 
        document.getElementById("optimizer_"+id+"_copyfromBtn").style.display = "none";
        document.getElementById("optimizer_"+id+"_copyfrom").style.display = "none";
        
        lockOptimizerConfiguration(id, true);
        
    } else if(status > 100){        
        document.getElementById("optimizer_"+id+"_startBtn").style.display = "none";
        document.getElementById("optimizer_"+id+"_termBtn").style.display = "none"; 
        document.getElementById("optimizer_"+id+"_clearBtn").style.display = "block";  
        document.getElementById("optimizer_"+id+"_copyfromBtn").style.display = "none";
        document.getElementById("optimizer_"+id+"_copyfrom").style.display = "none";  
        
        lockOptimizerConfiguration(id, true);
             
    }
}




function updateOptimizerConfiguration(id, array, err){        
    if(!err && array){
        var nodes = document.getElementById("optimizer_"+id+"_config_solver_tableheader").parentElement.childNodes;

        for(var j=2; j < nodes.length; j++){
            var applicable = false;
            for(var i=0; i < array.length; i++){  
                if(nodes[j].childNodes[0].innerHTML == array[i].key){
                    applicable = true;
                }
            }
            if(!applicable){                
                document.getElementById("optimizer_"+id+"_config_solver_tableheader").parentElement.removeChild(nodes[j]);
            }
        }
                   
                   
                   
        for(var i=0; i < array.length; i++){            
            if(document.getElementById("optimizer_"+id+"_cfg_"+array[i].key)){
                if(!(document.getElementById("optimizer_"+id+"_cfg_"+array[i].key)===document.activeElement))
                    document.getElementById("optimizer_"+id+"_cfg_"+array[i].key).value = array[i].value;
            } else if(document.getElementById("optimizer_"+id+"_cfg_"+array[i].key+"_min")) {
                if(!(document.getElementById("optimizer_"+id+"_cfg_"+array[i].key+"_min")===document.activeElement))
                    document.getElementById("optimizer_"+id+"_cfg_"+array[i].key+"_min").value = array[i].min_value;
                if(!(document.getElementById("optimizer_"+id+"_cfg_"+array[i].key+"_max")===document.activeElement))
                    document.getElementById("optimizer_"+id+"_cfg_"+array[i].key+"_max").value = array[i].max_value;                  
            
            } else {
                //create new 
                var par = array[i].key;
                if(par == "SHP") break;
                
                var tr = document.createElement("tr");
                                
                if(array[i].min_value){
                    var min_value = array[i].min_value;
                    var max_value = array[i].max_value;

                    tr.innerHTML = '<td>'+par+'</td><td><input type="text" value="'+min_value+'" id="optimizer_'+id+'_cfg_'+par+'_min" autocomplete="off" onchange="optimizerCfgChangeMMPar('+id+',\''+par+'\')"></td><td>to</td><td><input type="text" value="'+max_value+'" id="optimizer_'+id+'_cfg_'+par+'_max" autocomplete="off" onchange="optimizerCfgChangeMMPar('+id+',\''+par+'\')"></td><td onclick="optimizerCfgRemoveSHP('+id+',\''+par+'\')">remove</td>';

                    document.getElementById("optimizer_"+id+"_config_solver_tableheader").parentElement.appendChild(tr);
                } else {
                    var value = array[i].value;

                    tr.innerHTML = '<td>'+par+'</td><td><input type="text" value="'+value+'" id="optimizer_'+id+'_cfg_'+par+'" autocomplete="off" onchange="optimizerCfgChange('+id+',\''+par+'\',this.value)"></td><td onclick="optimizerCfgReset('+id+',\''+par+'\')">reset</td>';

                    document.getElementById("optimizer_"+id+"_config_tableheader").parentElement.appendChild(tr);
                }
            }   
        }
    }
}

function lockOptimizerConfiguration(id, lock){
    if(lock){
        if(!document.getElementById("optimizer_"+id).className.includes("cfglocked"))
            document.getElementById("optimizer_"+id).className += " cfglocked";
    } else {
        if(document.getElementById("optimizer_"+id).className.includes("cfglocked"))
            document.getElementById("optimizer_"+id).className = document.getElementById("optimizer_"+id).className.replace(" cfglocked","");        
    }
}




function updateOptimizerResult(id, result, err){   
    if(!err && result){
        if(result.bestPS != null){
            document.getElementById("optimizer_"+id+"_resultBestPS").innerHTML = result.bestPS;
            document.getElementById("optimizer_"+id+"_resultBestPS").parentElement.style.display = "inherit";
        } else {
            document.getElementById("optimizer_"+id+"_resultBestPS").parentElement.style.display = "none";            
        }
        if(result.minimum != null){
            document.getElementById("optimizer_"+id+"_resultMinimum").innerHTML = result.minimum;
            document.getElementById("optimizer_"+id+"_resultMinimum").parentElement.style.display = "inherit";
        } else {
            document.getElementById("optimizer_"+id+"_resultMinimum").parentElement.style.display = "none";            
        }
        
        if(result.exception != null){
            document.getElementById("optimizer_"+id+"_resultException").innerHTML = result.exception;
            document.getElementById("optimizer_"+id+"_resultException").parentElement.style.display = "inherit";
        } else {
            document.getElementById("optimizer_"+id+"_resultException").parentElement.style.display = "none";            
        }
        
        /*if(!document.getElementById("optimizer_compare_troptimizer_"+id))
            document.getElementById("optimizer_"+id+"_compareBtn").style.display = "inherit";*/
        
        document.getElementById("optimizer_"+id+"_result").style.display = "inherit";  
        
    } else {
        optimizerCompareRemove(id);
        //document.getElementById("optimizer_"+id+"_compareBtn").style.display = "none";
        document.getElementById("optimizer_"+id+"_result").style.display = "none";            
    }    
}









function newOptimizerContentDiv(id, algorithm, creator){    
    var div = document.createElement("div");
    div.setAttribute("id", "optimizer_"+id);
    div.setAttribute("class", "optimizer_content manager_content");
    
        var table1 = document.createElement("table");
        table1.setAttribute("class", "manager_content_t1");
        table1.innerHTML = '<tr><td><h3>'+algorithm+'</h3></td><td>'+creator+'</td><td><span id="optimizer_'+id+'_status">initialized</span></td><td><button onclick="optimizerDuplicate('+id+');">Duplicate</button><button onclick="optimizerDelete('+id+');">Delete</button></td></tr>';
            
    div.appendChild(table1);

    
        var div1 = document.createElement("div");

            var table2 = document.createElement("table");
            table2.setAttribute("class", "manager_content_t2");
            table2.innerHTML = '<tr><td><h4>Configuration</h4></td><td><button id="optimizer_'+id+'_copyfromBtn" onclick="optimizerCfgCopyFrom('+id+');">Copy from</button><input type="text" autocomplete="off" value="" id="optimizer_'+id+'_copyfrom"></td></tr>';

        div1.appendChild(table2);

            var table3 = document.createElement("table");
            table3.setAttribute("class", "manager_content_t3");
            var table3a = document.createElement("table");
            table3a.setAttribute("class", "manager_content_t3a");

                var tr31 = document.createElement("tr");
                tr31.setAttribute("id", "optimizer_"+id+"_config_tableheader")
                tr31.innerHTML = '<th>Hyperparameter</th><th>Value</th><th onclick="optimizerCfgResetAll('+id+')">Reset All</th>';
    
                var tr3a1 = document.createElement("tr");
                tr3a1.setAttribute("id", "optimizer_"+id+"_config_solver_tableheader")
                tr3a1.innerHTML = '<th>Solver Hyperparameter</th><th colspan="3">Value</th><th></th>';
                var tr3a2 = document.createElement("tr");
                tr3a2.setAttribute("id", "optimizer_"+id+"_config_solver_add")
                tr3a2.innerHTML += '<th><input type="text" placeholder="name" value="" id="optimizer_'+id+'_cfg_newSHP_name" autocomplete="off"></th><th><input type="text" placeholder="min" value="" id="optimizer_'+id+'_cfg_newSHP_min" autocomplete="off"></th><th>to</th><th><input type="text" placeholder="max" value="" id="optimizer_'+id+'_cfg_newSHP_max" autocomplete="off"></th><th onclick="optimizerCfgAddSHP('+id+');">add</th>';
           

            table3.appendChild(tr31);
            table3a.appendChild(tr3a1);
            table3a.appendChild(tr3a2);
                var pars;

                for(var i=0; i < OptiAlgorithms_JSON.length; i++){
                    if(OptiAlgorithms_JSON[i].algorithm == algorithm){
                        pars = OptiAlgorithms_JSON[i].parameters;
                    }
                }

                for(var i=0; i < pars.length; i++){
                    var par = pars[i].name;
                    
                    if(par == "SHP") break;
                    
                    var tr = document.createElement("tr");
                    
                    if(pars[i].min_default){
                        var min_value = pars[i].min_default;
                        var max_value = pars[i].max_default;

                        tr.innerHTML = '<td>'+par+'</td><td><input type="text" value="'+min_value+'" id="optimizer_'+id+'_cfg_'+par+'_min" autocomplete="off" onchange="optimizerCfgChangeMMPar('+id+',\''+par+'\')"></td><td>to</td><td><input type="text" value="'+max_value+'" id="optimizer_'+id+'_cfg_'+par+'_max" autocomplete="off" onchange="optimizerCfgChangeMMPar('+id+',\''+par+'\')"></td><td></td>';
                        
                        table3a.appendChild(tr);
                    } else {
                        var value = pars[i].default;

                        tr.innerHTML = '<td>'+par+'</td><td><input type="text" value="'+value+'" id="optimizer_'+id+'_cfg_'+par+'" autocomplete="off" onchange="optimizerCfgChange('+id+',\''+par+'\',this.value)"></td><td onclick="optimizerCfgReset('+id+',\''+par+'\')">reset</td>';
                        
                        table3.appendChild(tr);
                    }


                }
    
        div1.appendChild(table3);
        div1.appendChild(table3a);

    div.appendChild(div1);
    
        var div2 = document.createElement("div");
        div2.innerHTML = '<button id="optimizer_'+id+'_startBtn" onclick="optimizerSolve('+id+');">Start</button><button id="optimizer_'+id+'_termBtn" onclick="optimizerTerm('+id+');" style="display: none;">Terminate</button><button id="optimizer_'+id+'_clearBtn" onclick="optimizerClear('+id+');" style="display: none;">Clear</button>';

    div.appendChild(div2);
        
        var div3 = document.createElement("div");
        div3.setAttribute("id", "optimizer_"+id+"_result");
        div3.setAttribute("style", "display: none;");
        div3.innerHTML = '<h4>Result</h4><table class="manager_content_t4"><tr><td>Best Parameterset: </td><td id="optimizer_'+id+'_resultBestPS"></td></tr><tr><td>Minimum: </td><td id="optimizer_'+id+'_resultMinimum"></td></tr><tr><td>Exception: </td><td id="optimizer_'+id+'_resultException"></td></tr></table>';
        //div3.innerHTML += '<button class="optimizer_compareBtn" id="optimizer_'+id+'_compareBtn" onclick="optimizerCompare('+id+');">Compare</button>';
    
    div.appendChild(div3);
    
    
    document.getElementById("optimizer_contents").appendChild(div);
}










/* actions in optimizer detail view */

function optimizerDuplicate(id){
    sendCmds(["om clone "+id], 1000, tab_optimizer_responseHandler);    
}

function optimizerDelete(id){
    if(window.confirm("Delete optimizer?")){
        sendCmds(["om delete "+id], 1000, tab_optimizer_responseHandler); 
    }   
}

function optimizerCfgCopyFrom(id){
    sendCmds(["om config "+id+" -clone "+document.getElementById("optimizer_"+id+"_copyfrom").value], 1000, tab_optimizer_responseHandler);  
    loadOptimizerConfiguration(id);
}

function optimizerCfgChange(id, name, value){
    sendCmds(["om config "+id+" "+name.replace(",",".")+"="+value.replace(",",".")], 1000, tab_optimizer_responseHandler);   
}

function optimizerCfgChangeMMPar(id, name){
    var min_val = document.getElementById("optimizer_"+id+"_cfg_"+name+"_min").value;
    var max_val = document.getElementById("optimizer_"+id+"_cfg_"+name+"_max").value;
    
    optimizerCfgChangeMM(id, name, min_val, max_val);
}

function optimizerCfgChangeMM(id, name, min, max){
    sendCmds(["om config "+id+" "+name.replace(",",".")+"="+min.replace(",",".")+"/"+max.replace(",",".")], 1000, tab_optimizer_responseHandler);   
}

function optimizerCfgResetAll(id){
    if(window.confirm("Reset configuration?"))
        sendCmds(["om config "+id+" -reset"], 1000, tab_optimizer_detail_responseHandler);   
}

function optimizerCfgReset(id, name){
    
    var optimizer = currentOptimizerList_JSON[id];
    
    if(currentOptimizerList_JSON[id].id != id){
         for(var i=0; i < currentOptimizerList_JSON.length; i++){
            if(currentOptimizerList_JSON[i].id == id){
                optimizer = currentOptimizerList_JSON[i];
                break;
            }
        }
    }
    
    var pars;
    
    for(var i=0; i < OptiAlgorithms_JSON.length; i++){
        if(OptiAlgorithms_JSON[i].algorithm == optimizer.algorithm){
            pars = OptiAlgorithms_JSON[i].parameters;
        }
    }
    
    for(var i=0; i < pars.length; i++){
        if(pars[i].name == name){
            if(document.getElementById("optimizer_"+id+"_cfg_"+pars[i].name)){
                if(document.getElementById("optimizer_"+id+"_cfg_"+pars[i].name).value != pars[i].default){
                    optimizerCfgChange(id, pars[i].name, pars[i].default);
                }               
            } else if(document.getElementById("optimizer_"+id+"_cfg_"+pars[i].name+"_min")){
                if(document.getElementById("optimizer_"+id+"_cfg_"+pars[i].name+"_min").value != pars[i].min_default
                  || document.getElementById("optimizer_"+id+"_cfg_"+pars[i].name+"_max").value != pars[i].max_default){
                    optimizerCfgChangeMM(id, pars[i].name, pars[i].min_default, pars[i].max_default);
                }               
            }
                
            break;
        }
    }
}


function optimizerCfgAddSHP(id){
    if(document.getElementById("optimizer_"+id+"_cfg_newSHP_name").value.trim() == "") return;
    var s = document.getElementById("optimizer_"+id+"_cfg_newSHP_name").value.replace(",",".") + "=";
    s += document.getElementById("optimizer_"+id+"_cfg_newSHP_min").value.replace(",",".") + "/";
    s += document.getElementById("optimizer_"+id+"_cfg_newSHP_max").value.replace(",",".");
        
    sendCmds(["om config "+id+" -addSHP "+s], 1000, tab_optimizer_detail_responseHandler);   
    
    document.getElementById("optimizer_"+id+"_cfg_newSHP_name").value = "";
    document.getElementById("optimizer_"+id+"_cfg_newSHP_min").value = "";
    document.getElementById("optimizer_"+id+"_cfg_newSHP_max").value = "";
}



function optimizerCfgRemoveSHP(id, name){
    sendCmds(["om config "+id+" -rmvSHP "+name], 1000, tab_optimizer_detail_responseHandler);      
}

function optimizerSolve(id){
    sendCmds(["om start "+id], 1000, tab_optimizer_responseHandler);  
    loadOptimizerStatus(id);
    if(currentOptimizerList_JSON[id].status == -2){
        openInfo("warn","Optimizer not configured, using default values.");
    }
}

function optimizerTerm(id){
    sendCmds(["om term "+id], 1000, tab_optimizer_responseHandler);   
    loadOptimizerStatus(id)
}

function optimizerClear(id){
    sendCmds(["om clear "+id], 1000, tab_optimizer_responseHandler);  
    loadOptimizerStatus(id)
}






/* add optimizer */

function createOptimizer(){
    var algorithm = document.getElementById("optialgorithm").value;
    var cmd = "om create";
    if(algorithm == "PSOeeg" && easterEggUnlock(0)){
        return;
    }
    if(algorithm != "default"){
        cmd += " "+algorithm;
    }
    sendCmds([cmd], 1000, tab_optimizer_responseHandler);
}


function createOptiAlgorithmSelectHTML(algorithm){
    var option = document.createElement("option");
    option.setAttribute("value", algorithm);
    option.innerHTML = algorithm;
    
    document.getElementById("optialgorithm").lastChild.after(option);
}












/* optimizer compare */

function optimizerCompare(id){
    if(!document.getElementById("optimizer_compare_troptimizer_"+id)){
        var algorithm = "";
        var minimum = document.getElementById("optimizer_"+id+"_resultBestPS").innerHTML;
        
        
        for(var i = 0; i < currentOptimizerList_JSON.length; i++){
            if(currentOptimizerList_JSON[i].id == id){
                algorithm = currentOptimizerList_JSON[i].algorithm;
                break;
            }
        }
        
        
        var td = document.createElement("td");
        
        td.setAttribute("id", "optimizer_compare_troptimizer_"+id);
        td.innerHTML = id+": "+algorithm+" <span onclick='optimizerCompareRemove("+id+")'>&#x2717;</span>";
        document.getElementById("optimizer_compare_troptimizer").appendChild(td);
        
        
        td = document.createElement("td");
        
        td.setAttribute("id", "optimizer_compare_trminimum_"+id);
        td.innerHTML = minimum;
        document.getElementById("optimizer_compare_trminimum").appendChild(td);
        
        
        td = document.createElement("td");
        
        td.setAttribute("id", "optimizer_compare_triter_"+id);
        td.innerHTML = iterations;
        document.getElementById("optimizer_compare_triter").appendChild(td);
        
        
        td = document.createElement("td");
        
        td.setAttribute("id", "optimizer_compare_trfc_"+id);
        td.innerHTML = ffc;
        document.getElementById("optimizer_compare_trfc").appendChild(td);
        
        
    }
    document.getElementById("optimizer_"+id+"_compareBtn").style.display = "none";
}


function optimizerCompareRemove(id){
    if(document.getElementById("optimizer_"+id+"_compareBtn"))
        document.getElementById("optimizer_"+id+"_compareBtn").style.display = "inherit";
    
    if(document.getElementById("optimizer_compare_troptimizer_"+id)){
        document.getElementById("optimizer_compare_troptimizer").removeChild(document.getElementById("optimizer_compare_troptimizer_"+id));
    }
    
    if(document.getElementById("optimizer_compare_trminimum_"+id)){
        document.getElementById("optimizer_compare_trminimum").removeChild(document.getElementById("optimizer_compare_trminimum_"+id));
    }
    
    if(document.getElementById("optimizer_compare_triter_"+id)){
        document.getElementById("optimizer_compare_triter").removeChild(document.getElementById("optimizer_compare_triter_"+id));
    }    
    
    if(document.getElementById("optimizer_compare_trfc_"+id)){
        document.getElementById("optimizer_compare_trfc").removeChild(document.getElementById("optimizer_compare_trfc_"+id));
    }

}