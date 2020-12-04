var REFRESH_RATE_SOLVER = 1000;


/* TAB_SOLVER switching tabs */
var current_solver = "";

function switch_solver(e, tabname) {
    var i, tabcontent, tablinks;


    //get all elements with class="solver_content" and remove the class "active"
    tablinks = document.getElementsByClassName("solver_content");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }

    //get all elements with class="solver_list_elem" and remove the class "active"
    tablinks = document.getElementsByClassName("solver_list_elem");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
    //remove class "active" from solver_add button (label)
    document.getElementById("solver_add").className = document.getElementById("solver_add").className.replace(" active", "");

    //show the current tab, and add an "active" class to the button that opened the tab
    document.getElementById(tabname).className += " active";
    e.currentTarget.className += " active";
    
    current_solver = tabname;
    loadSolverDetail_updater();
}





/* initial data load */

var Algorithms_JSON;

function loadSolverInitial(){
    checkSolverList();
    loadSolverDetail_updater();
    
    sendCmds(["sm lsalgo -json"], 1000, tab_solver_responseHandler);
}



/* loading data LIST */
var currentSolverList_JSON;

function checkSolverList(){    
    sendCmds(["sm list -json"], 1000, tab_solver_responseHandler);
    setTimeout(checkSolverList, REFRESH_RATE_SOLVER);
}

function tab_solver_responseHandler(){   
    //console.log("tab_solver: \n"+this.responseText);
    var response = JSON.parse(this.responseText);
    
    if(response.cmd_ans){
        var cmd_ans = response.cmd_ans;
        
        for(var i=0; i < cmd_ans.length; i++){
            if(cmd_ans[i].cmd == "sm list -json"){
                // create/change list of solvers
                if(cmd_ans[i].ans.solvers){
                    var solvers = cmd_ans[i].ans.solvers;
                    
                    currentSolverList_JSON = solvers;
                    
                    for(var j=0; j < solvers.length; j++){
                        createOrChangeSolverListHTML(solvers[j]);
                    }
                } 
            } else if(cmd_ans[i].cmd == "sm lsalgo -json"){
                // create list of algorithms in add solver div
                if(cmd_ans[i].ans.algorithms){
                    var algorithms = cmd_ans[i].ans.algorithms;
                    
                    Algorithms_JSON = algorithms;
                    
                    for(var j=0; j < algorithms.length; j++){
                        createAlgorithmSelectHTML(algorithms[j].algorithm);
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



function createOrChangeSolverListHTML(solver){
    if(solver.deleted == true){
        if(document.getElementById("solverBtn_"+solver.id)){ //exists in list
            var btn = document.getElementById("solverBtn_"+solver.id);
            if(btn.className.includes("active")){
                if(btn.nextElementSibling && btn.nextElementSibling !== document.getElementById('solver_add')){
                    btn.nextElementSibling.click();
                } else if(btn.previousElementSibling){
                    btn.previousElementSibling.click();                
                } else {
                    document.getElementById('solver_add').click();                       
                }             
            }
            btn.className = "solver_list_elem deleted";
            btn.removeAttribute("onclick");
            btn.removeAttribute("style");
            
            if(!showDelSolvers) btn.parentElement.removeChild(btn);
            
            if(document.getElementById("solver_"+solver.id))
                document.getElementById("solver_"+solver.id).innerHTML = "";   
            
            solverCompareRemove(solver.id);           
            
        } else if(showDelSolvers) { //does not exist in list
            var button = document.createElement("button");
            button.setAttribute("class", "solver_list_elem deleted");
            button.setAttribute("id", "solverBtn_"+solver.id);
            button.innerHTML = solver.id;
            if(solver.id > 0){
                document.getElementById("solverBtn_"+(solver.id-1)).after(button);
            } else {
                document.getElementById("solver_list").insertBefore(button, document.getElementById("solver_list").firstChild);
            }
        }
        
    } else {        
        if(!document.getElementById("solverBtn_"+solver.id)){ //does not exist in list
            var button = document.createElement("button");
            button.setAttribute("class", "solver_list_elem");
            button.setAttribute("onclick", "switch_solver(event, 'solver_"+solver.id+"')");
            button.setAttribute("id", "solverBtn_"+solver.id);
            button.innerHTML = solver.id+": "+solver.algorithm;
            if(solver.id > 0){
                var i = solver.id - 1;
                while(!document.getElementById("solverBtn_"+i) && i >= 0){
                    i--;
                }
                if(i < 0){     
                    document.getElementById("solver_list").insertBefore(button, document.getElementById("solver_list").firstChild);
                } else {
                    document.getElementById("solverBtn_"+i).after(button);                    
                }
            } else {
                document.getElementById("solver_list").insertBefore(button, document.getElementById("solver_list").firstChild);
            }
            
            newSolverContentDiv(solver.id, solver.algorithm);
        }
        
        
        //update status color
        
        var btn = document.getElementById("solverBtn_"+solver.id);
        btn.removeAttribute("style");
        
        var was_active = false;
        
        if(solver.status == -2){
            //initialized
            was_active = btn.className.includes("active");
            
            btn.className = "solver_list_elem";
            btn.className += " initialized"
            if(was_active) btn.className += " active";
            
        } else if(solver.status == -1){
            //configured
            was_active = btn.className.includes("active");
            
            btn.className = "solver_list_elem";
            btn.className += " configured"
            if(was_active) btn.className += " active";
            
        } else if(solver.status >= 0 && solver.status <= 100){
            //running
            was_active = btn.className.includes("active");
            
            btn.className = "solver_list_elem";
            if(was_active) btn.className += " active";
            
            btn.style = "background-image: linear-gradient(to right, green 0%, green "+solver.status+"%, #eee "+solver.status+"%, #eee 100%);";   
            
        } else if(solver.status == 101){
            //result ready
            was_active = btn.className.includes("active");
            
            btn.className = "solver_list_elem";
            btn.className += " finished"
            if(was_active) btn.className += " active";
            
        } else if(solver.status == 102){
            //terminated
            was_active = btn.className.includes("active");
            
            btn.className = "solver_list_elem";
            btn.className += " error"
            if(was_active) btn.className += " active";
            
        } else if(solver.status == 103){
            //error
            was_active = btn.className.includes("active");
            
            btn.className = "solver_list_elem";
            btn.className += " error"
            if(was_active) btn.className += " active";
            
        }
    }
}


var showDelSolvers = true;
function setShowDelSolvers(bool){
    showDelSolvers = bool;
}






/* loading data SOLVER DETAIL VIEW */

var load_sd_timeout;
var current_solver_detail = "";

function loadSolverDetail_updater(){
    if(current_solver != "add_solver"){
        var id = current_solver.replace("solver_","");
        loadSolverStatus(id);    
        if(current_solver_detail != current_solver){
            current_solver_detail = current_solver;
            loadSolverConfiguration(id);
            loadSolverResult(id);
        }
    }
    
    clearTimeout(load_sd_timeout);
    load_sd_timeout = setTimeout(loadSolverDetail_updater, REFRESH_RATE_SOLVER);
}




function loadSolverStatus(id){
    sendCmds(["sm status "+id], 1000, tab_solver_detail_responseHandler);     
}

function loadSolverConfiguration(id){
    sendCmds(["sm config "+id+" -get -json"], 1000, tab_solver_detail_responseHandler); 
}

function loadSolverResult(id){
    sendCmds(["sm result "+id+" -json"], 1000, tab_solver_detail_responseHandler);       
}



function tab_solver_detail_responseHandler(){   
    //console.log("tab_solver_detail: \n"+this.responseText);
    var response = JSON.parse(this.responseText);
    
    if(response.cmd_ans){
        var cmd_ans = response.cmd_ans;
        
        for(var i=0; i < cmd_ans.length; i++){
            if(cmd_ans[i].cmd.includes("sm config") && cmd_ans[i].cmd.includes("-get -json")){
                // update configuration values
                var id = cmd_ans[i].cmd.replace("sm config ","").replace(" -get -json","");
                updateSolverConfiguration(id, cmd_ans[i].ans.cfg, cmd_ans[i].err)
                
            } else if(cmd_ans[i].cmd.includes("sm config") && cmd_ans[i].cmd.includes("-reset")){
                // info successful reset
                if(cmd_ans[i].ans){
                    openInfoAdvanced("suc", cmd_ans[i].ans, 3000);
                } else if(cmd_ans[i].err){
                    openInfo("err", cmd_ans[i].err);
                }
                
            } else if(cmd_ans[i].cmd.includes("sm status")){
                // update status
                var id = cmd_ans[i].cmd.replace("sm status ","");
                var stat ="";
                if(cmd_ans[i].ans) stat = cmd_ans[i].ans.replace("Status: ","");
                updateSolverStatus(id, stat, cmd_ans[i].err);
                                
            } else if(cmd_ans[i].cmd.includes("sm result")){
                // update status
                var id = cmd_ans[i].cmd.replace("sm result ","").replace(" -json","");
                updateSolverResult(id, cmd_ans[i].ans, cmd_ans[i].err)
                
            } else if(cmd_ans[i].err){
                openInfo("err", cmd_ans[i].err);
            }
            
        }
    } else {
        openInfo("err", "Could not read data from WebInterfaceServer.");
    }
}







function updateSolverStatus(id, status, err){
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
    
    document.getElementById("solver_"+id+"_status").innerHTML = statusstring;
    
    if( ((status == 101 || status == 103) && document.getElementById("solver_"+id+"_result").style.display == "none") ||
        ((status != 101 && status != 103) && document.getElementById("solver_"+id+"_result").style.display != "none") )
    {
        loadSolverResult(id);        
    }
        
    
    if(status < 0){        
        document.getElementById("solver_"+id+"_solveBtn").style.display = "block"; 
        document.getElementById("solver_"+id+"_termBtn").style.display = "none"; 
        document.getElementById("solver_"+id+"_clearBtn").style.display = "none";  
        document.getElementById("solver_"+id+"_copyfromBtn").style.display = "inherit";
        document.getElementById("solver_"+id+"_copyfrom").style.display = "inherit";
        
        lockSolverConfiguration(id, false);
        loadSolverConfiguration(id);        
        
    } else if(status >= 0 && status <= 100){        
        document.getElementById("solver_"+id+"_solveBtn").style.display = "none"; 
        document.getElementById("solver_"+id+"_termBtn").style.display = "block"; 
        document.getElementById("solver_"+id+"_clearBtn").style.display = "none"; 
        document.getElementById("solver_"+id+"_copyfromBtn").style.display = "none";
        document.getElementById("solver_"+id+"_copyfrom").style.display = "none";
        
        lockSolverConfiguration(id, true);
        
    } else if(status > 100){        
        document.getElementById("solver_"+id+"_solveBtn").style.display = "none";
        document.getElementById("solver_"+id+"_termBtn").style.display = "none"; 
        document.getElementById("solver_"+id+"_clearBtn").style.display = "block";  
        document.getElementById("solver_"+id+"_copyfromBtn").style.display = "none";
        document.getElementById("solver_"+id+"_copyfrom").style.display = "none";  
        
        lockSolverConfiguration(id, true);
             
    }
}




function updateSolverConfiguration(id, array, err){        
    if(!err && array){
        for(var i=0; i < array.length; i++){            
            if(document.getElementById("solver_"+id+"_cfg_"+array[i].key)){
                if(!(document.getElementById("solver_"+id+"_cfg_"+array[i].key)===document.activeElement))
                    document.getElementById("solver_"+id+"_cfg_"+array[i].key).value = array[i].value;
            }
                
            //else
                //create new
        }
    }
}

function lockSolverConfiguration(id, lock){
    if(lock){
        if(!document.getElementById("solver_"+id).className.includes("cfglocked"))
            document.getElementById("solver_"+id).className += " cfglocked";
    } else {
        if(document.getElementById("solver_"+id).className.includes("cfglocked"))
            document.getElementById("solver_"+id).className = document.getElementById("solver_"+id).className.replace(" cfglocked","");        
    }
}




function updateSolverResult(id, result, err){   
    if(!err && result){
        if(result.value != null){
            document.getElementById("solver_"+id+"_resultMin").innerHTML = result.value;
            document.getElementById("solver_"+id+"_resultMin").parentElement.style.display = "inherit";
        } else {
            document.getElementById("solver_"+id+"_resultMin").parentElement.style.display = "none";            
        }
        if(result.particle != null){
            document.getElementById("solver_"+id+"_resultPos").innerHTML = result.particle;
            document.getElementById("solver_"+id+"_resultPos").parentElement.style.display = "inherit";
        } else {
            document.getElementById("solver_"+id+"_resultPos").parentElement.style.display = "none";            
        }
        if(result.iterations != null){
            document.getElementById("solver_"+id+"_resultIter").innerHTML = result.iterations;
            document.getElementById("solver_"+id+"_resultIter").parentElement.style.display = "inherit";
        } else {
            document.getElementById("solver_"+id+"_resultIter").parentElement.style.display = "none";            
        }
        if(result.ffCounter != null){
            document.getElementById("solver_"+id+"_resultFC").innerHTML = result.ffCounter;
            document.getElementById("solver_"+id+"_resultFC").parentElement.style.display = "inherit";
        } else {
            document.getElementById("solver_"+id+"_resultFC").parentElement.style.display = "none";            
        }
        if(result.exception != null){
            document.getElementById("solver_"+id+"_resultException").innerHTML = result.exception;
            document.getElementById("solver_"+id+"_resultException").parentElement.style.display = "inherit";
        } else {
            document.getElementById("solver_"+id+"_resultException").parentElement.style.display = "none";            
        }
        
        if(!document.getElementById("solver_compare_trsolver_"+id))
            document.getElementById("solver_"+id+"_compareBtn").style.display = "inherit";
        
        document.getElementById("solver_"+id+"_result").style.display = "inherit";  
        
    } else {
        solverCompareRemove(id);
        document.getElementById("solver_"+id+"_compareBtn").style.display = "none";
        document.getElementById("solver_"+id+"_result").style.display = "none";            
    }    
}









function newSolverContentDiv(id, algorithm){
    var div = document.createElement("div");
    div.setAttribute("id", "solver_"+id);
    div.setAttribute("class", "solver_content");
    
        var table1 = document.createElement("table");
        table1.setAttribute("class", "solver_content_t1");
        table1.innerHTML = '<tr><td><h3>'+algorithm+'</h3></td><td><span id="solver_'+id+'_status">initialized</span></td><td><button onclick="solverDuplicate('+id+');">Duplicate</button><button onclick="solverDelete('+id+');">Delete</button></td></tr>';
            
    div.appendChild(table1);

    
        var div1 = document.createElement("div");

            var table2 = document.createElement("table");
            table2.setAttribute("class", "solver_content_t2");
            table2.innerHTML = '<tr><td><h4>Configuration</h4></td><td><button id="solver_'+id+'_copyfromBtn" onclick="solverCfgCopyFrom('+id+');">Copy from</button><input type="text" value="" id="solver_'+id+'_copyfrom"></td></tr>';

        div1.appendChild(table2);

            var table3 = document.createElement("table");
            table3.setAttribute("class", "solver_content_t3");

                var tr31 = document.createElement("tr");
                tr31.setAttribute("id", "solver_"+id+"_config_tableheader")
                tr31.innerHTML = '<th>Hyperparameter</th><th>Value</th><th onclick="solverCfgResetAll('+id+')">Reset All</th></tr>';

            table3.appendChild(tr31);
                var pars;

                for(var i=0; i < Algorithms_JSON.length; i++){
                    if(Algorithms_JSON[i].algorithm == algorithm){
                        pars = Algorithms_JSON[i].parameters;
                    }
                }

                for(var i=0; i < pars.length; i++){
                    var par = pars[i].name;
                    var value = pars[i].default;

                    var tr3x = document.createElement("tr");
                    tr3x.innerHTML = '<td>'+par+'</td><td><input type="text" value="'+value+'" id="solver_'+id+'_cfg_'+par+'" autocomplete="off" onchange="solverCfgChange('+id+',\''+par+'\',this.value)"></td><td onclick="solverCfgReset('+id+',\''+par+'\')">reset</td>';


                    table3.appendChild(tr3x);
                }
    
        div1.appendChild(table3);

    div.appendChild(div1);
    
        var div2 = document.createElement("div");
        div2.innerHTML = '<button id="solver_'+id+'_solveBtn" onclick="solverSolve('+id+');">Solve</button><button id="solver_'+id+'_termBtn" onclick="solverTerm('+id+');" style="display: none;">Terminate</button><button id="solver_'+id+'_clearBtn" onclick="solverClear('+id+');" style="display: none;">Clear</button>';

    div.appendChild(div2);
        
        var div3 = document.createElement("div");
        div3.setAttribute("id", "solver_"+id+"_result");
        div3.setAttribute("style", "display: none;");
        div3.innerHTML = '<h4>Result</h4><table class="solver_content_t4"><tr><td>Minimum: </td><td id="solver_'+id+'_resultMin"></td></tr><tr><td>Position: </td><td id="solver_'+id+'_resultPos"></td></tr><tr><td>Iterations: </td><td id="solver_'+id+'_resultIter"></td></tr><tr><td>Function Calls: </td><td id="solver_'+id+'_resultFC"></td></tr><tr><td>Exception: </td><td id="solver_'+id+'_resultException"></td></tr></table><button class="solver_compareBtn" id="solver_'+id+'_compareBtn" onclick="solverCompare('+id+');">Compare</button>';
    
    div.appendChild(div3);
    
    
    document.getElementById("solver_contents").appendChild(div);
}










/* actions in solver detail view */

function solverDuplicate(id){
    sendCmds(["sm clone "+id], 1000, tab_solver_responseHandler);    
}

function solverDelete(id){
    if(window.confirm("Delete solver?")){
        sendCmds(["sm delete "+id], 1000, tab_solver_responseHandler); 
    }   
}

function solverCfgCopyFrom(id){
    sendCmds(["sm config "+id+" -clone "+document.getElementById("solver_"+id+"_copyfrom").value], 1000, tab_solver_responseHandler);  
    loadSolverConfiguration(id);
}

function solverCfgChange(id, name, value){
    sendCmds(["sm config "+id+" "+name+"="+value], 1000, tab_solver_responseHandler);   
}

/*
function solverCfgSave(id){
    var cfgstring = "";
    
    var solver = currentSolverList_JSON[id];
    
   if(currentSolverList_JSON[id].id != id){
         for(var i=0; i < currentSolverList_JSON.length; i++){
            if(currentSolverList_JSON[i].id == id){
                solver = currentSolverList_JSON[i];
                break;
            }
        }
    }
    
    
    var pars;
    
    for(var i=0; i < Algorithms_JSON.length; i++){
        if(Algorithms_JSON[i].algorithm == solver.algorithm){
            pars = Algorithms_JSON[i].parameters;
        }
    }
    
    for(var i=0; i < pars.length; i++){
        if(document.getElementById("solver_"+id+"_cfg_"+pars[i].name)){
            cfgstring += pars[i].name +"="+ document.getElementById("solver_"+id+"_cfg_"+pars[i].name).value +", ";
        }
    }
    
    sendCmds(["sm config "+id+" "+cfgstring], 2000, tab_solver_responseHandler);   
}*/

function solverCfgResetAll(id){
    if(window.confirm("Reset configuration?"))
        sendCmds(["sm config "+id+" -reset"], 1000, tab_solver_detail_responseHandler);   
}

function solverCfgReset(id, name){
    
    var solver = currentSolverList_JSON[id];
    
    if(currentSolverList_JSON[id].id != id){
         for(var i=0; i < currentSolverList_JSON.length; i++){
            if(currentSolverList_JSON[i].id == id){
                solver = currentSolverList_JSON[i];
                break;
            }
        }
    }
    
    var pars;
    
    for(var i=0; i < Algorithms_JSON.length; i++){
        if(Algorithms_JSON[i].algorithm == solver.algorithm){
            pars = Algorithms_JSON[i].parameters;
        }
    }
    
    for(var i=0; i < pars.length; i++){
        if(pars[i].name == name){
            if(document.getElementById("solver_"+id+"_cfg_"+pars[i].name).value != pars[i].default){
                solverCfgChange(id, pars[i].name, pars[i].default);
            }
                
            break;
        }
    }
}

function solverSolve(id){
    sendCmds(["sm solve "+id], 1000, tab_solver_responseHandler);  
    loadSolverStatus(id);
    if(currentSolverList_JSON[id].status == -2){
        openInfo("warn","Solver not configured, using default values.");
    }
}

function solverTerm(id){
    sendCmds(["sm term "+id], 1000, tab_solver_responseHandler);   
    loadSolverStatus(id)
}

function solverClear(id){
    sendCmds(["sm clear "+id], 1000, tab_solver_responseHandler);  
    loadSolverStatus(id)
}






/* add solver */

function createSolver(){
    var algorithm = document.getElementById("algorithm").value;
    var cmd = "sm create";
    if(algorithm == "PSOeeg" && easterEggUnlock(0)){
        return;
    }
    if(algorithm != "default"){
        cmd += " "+algorithm;
    }
    sendCmds([cmd], 1000, tab_solver_responseHandler);
}


function createAlgorithmSelectHTML(algorithm){
    var option = document.createElement("option");
    option.setAttribute("value", algorithm);
    option.innerHTML = algorithm;
    
    document.getElementById("algorithm").lastChild.after(option);
}












/* solver compare */

function solverCompare(id){
    if(!document.getElementById("solver_compare_trsolver_"+id)){
        var algorithm = "";
        var minimum = document.getElementById("solver_"+id+"_resultMin").innerHTML;
        var iterations = document.getElementById("solver_"+id+"_resultIter").innerHTML;
        var ffc = document.getElementById("solver_"+id+"_resultFC").innerHTML;
        
        
        for(var i = 0; i < currentSolverList_JSON.length; i++){
            if(currentSolverList_JSON[i].id == id){
                algorithm = currentSolverList_JSON[i].algorithm;
                break;
            }
        }
        
        
        var td = document.createElement("td");
        
        td.setAttribute("id", "solver_compare_trsolver_"+id);
        td.innerHTML = id+": "+algorithm+" <span onclick='solverCompareRemove("+id+")'>&#x2717;</span>";
        document.getElementById("solver_compare_trsolver").appendChild(td);
        
        
        td = document.createElement("td");
        
        td.setAttribute("id", "solver_compare_trminimum_"+id);
        td.innerHTML = minimum;
        document.getElementById("solver_compare_trminimum").appendChild(td);
        
        
        td = document.createElement("td");
        
        td.setAttribute("id", "solver_compare_triter_"+id);
        td.innerHTML = iterations;
        document.getElementById("solver_compare_triter").appendChild(td);
        
        
        td = document.createElement("td");
        
        td.setAttribute("id", "solver_compare_trfc_"+id);
        td.innerHTML = ffc;
        document.getElementById("solver_compare_trfc").appendChild(td);
        
        
    }
    document.getElementById("solver_"+id+"_compareBtn").style.display = "none";
}


function solverCompareRemove(id){
    if(document.getElementById("solver_"+id+"_compareBtn"))
        document.getElementById("solver_"+id+"_compareBtn").style.display = "inherit";
    
    if(document.getElementById("solver_compare_trsolver_"+id)){
        document.getElementById("solver_compare_trsolver").removeChild(document.getElementById("solver_compare_trsolver_"+id));
    }
    
    if(document.getElementById("solver_compare_trminimum_"+id)){
        document.getElementById("solver_compare_trminimum").removeChild(document.getElementById("solver_compare_trminimum_"+id));
    }
    
    if(document.getElementById("solver_compare_triter_"+id)){
        document.getElementById("solver_compare_triter").removeChild(document.getElementById("solver_compare_triter_"+id));
    }    
    
    if(document.getElementById("solver_compare_trfc_"+id)){
        document.getElementById("solver_compare_trfc").removeChild(document.getElementById("solver_compare_trfc_"+id));
    }

}