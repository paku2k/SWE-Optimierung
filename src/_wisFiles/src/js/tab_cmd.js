/* TAB CMD load and save */

var drag_active = false;
function cmdInputDragOver(e){
    e.preventDefault();  
    if(!drag_active){
        drag_active = true;
        document.getElementById("cmd_input").style.transition = "0.1s";
        document.getElementById("cmd_input").className = "ondragover";
    }
}

function cmdInputDragLeave(e){
    e.preventDefault();  
    if(drag_active){
        drag_active = false;
        document.getElementById("cmd_input").className = "ondragleave";
        setTimeout(function(){document.getElementById("cmd_input").style.transition = "0s";},100)
    }
}


function cmdInputDrop(e){
    if(drag_active){
        drag_active = false;
        document.getElementById("cmd_input").className = "ondragleave";
        setTimeout(function(){document.getElementById("cmd_input").style.transition = "0s";},100)
    }
    
    e.preventDefault();
    
    if (e.dataTransfer.items) {
        // Use DataTransferItemList interface to access the file(s)
        for (var i = 0; i < e.dataTransfer.items.length; i++) {
            // If dropped items aren't files, reject them
            if (e.dataTransfer.items[i].kind === 'file') {
                fileReaderCmdInput( e.dataTransfer.items[i].getAsFile());
            }
        }
    } else {
        // Use DataTransfer interface to access the file(s)
        for (var i = 0; i < e.dataTransfer.files.length; i++) {
            fileReaderCmdInput( e.dataTransfer.files[i]);
        }
    }  
}


function fileReaderCmdInput(file){
    if(file.name.substr(file.name.length - 5).indexOf(".mocl") > -1 
    || file.name.substr(file.name.length - 5).indexOf(".MOCL") > -1
    || file.name.substr(file.name.length - 5).indexOf(".txt") > -1
    || file.name.substr(file.name.length - 5).indexOf(".TXT") > -1){
        var reader = new FileReader();
        reader.onload = function(e) {
            var contents = e.target.result;
            addToCmdInput("/********************************************** \n FILE: "+file.name+" \n**********************************************/");
            addToCmdInput(contents);
        };
        reader.readAsText(file);        
    } else {
        openInfo("warn", "Only .mocl or .txt files as input!");
    }
}


function readFileSystemdialogue(e) {
    for (var i = 0; i < e.target.files.length; i++) {
        if (!e.target.files[i]) {
           continue;
        }
        fileReaderCmdInput( e.target.files[i]);
    }
}

function  addToCmdInput(contents) {
    var element = document.getElementById('cmd_input');
    if(element.value != ""){
        element.value += "\n\n";
    }
    element.value += contents;
}



function cmd_keylistener(e) {
    if ((window.navigator.platform.match("Mac") ? e.metaKey : e.ctrlKey) && e.shiftKey && e.keyCode == 83) { //ctrl + shift + S
        e.preventDefault();
        
        if(current_tab == "tab_cmd"){
            saveAsWithFilename();
        }
    } else if ((window.navigator.platform.match("Mac") ? e.metaKey : e.ctrlKey)  && e.keyCode == 83) { //ctrl + S
        e.preventDefault();
        
        if(current_tab == "tab_cmd"){
            saveAsMoclFile("");
        }
    } else if ((window.navigator.platform.match("Mac") ? e.metaKey : e.ctrlKey)  && e.keyCode == 90) { //ctrl + Z
        if(current_tab == "tab_cmd"){
            if(revertClr_cmd_input()) e.preventDefault();
        }  
    } else if ((window.navigator.platform.match("Mac") ? e.metaKey : e.ctrlKey)  && e.keyCode == 69) { //ctrl + E
        if(current_tab == "tab_cmd"){
            e.preventDefault();
            
            cmdInputExecute();
        }            
    } 
}



function saveAsWithFilename(){
    var textData = document.getElementById('cmd_input').value;     
    if(textData.trim() == "") return;
    
    var filename = window.prompt("Enter the filename: ", "");    
    if(filename != null)
        saveAsMoclFile(filename);
}




function saveAsMoclFile(filename){    
    var textData = document.getElementById('cmd_input').value; 
    
    if(textData.trim() == "") return;
                
    document.getElementById("cmd_input").style.transition = "0.1s";
    document.getElementById("cmd_input").className = "onsave";

    setTimeout(function(){
        document.getElementById("cmd_input").className = "ondragleave";
        setTimeout(function(){document.getElementById("cmd_input").style.transition = "0s";},100);                
    },100);
    
    var date = new Date();
    if(filename == ""){
        filename = "mocmdlist_"+date.getFullYear()+(date.getMonth()+1)+date.getDate()+"_"+(date.getHours() < 10 ? "0" : "")+date.getHours()+(date.getMinutes() < 10 ? "0" : "")+date.getMinutes(); 
    }
    filename += ".mocl";
        
    var blob = new Blob([textData], {type: "text/plain;charset=utf-8"});
    saveAs(blob, filename);
}



var cmd_input_clearedValue = "";
var cmd_input_revertClr_timeframe = false;
var cmd_input_clr_timeframe;
function clr_cmd_input(){
    if(document.getElementById("cmd_input").value == ""){
        if(cmd_input_revertClr_timeframe) revertClr_cmd_input();
        return;
    }
    
    cmd_input_clearedValue = document.getElementById("cmd_input").value;
    document.getElementById("cmd_input").value = "";
        
                
    document.getElementById("cmd_input").style.transition = "0.1s";
    document.getElementById("cmd_input").className = "onclear";

    setTimeout(function(){
        document.getElementById("cmd_input").className = "ondragleave";
        setTimeout(function(){document.getElementById("cmd_input").style.transition = "0s";},100);                
    },100);
    
    document.getElementById("cmd_clr").innerHTML = "&#x2b6f;";
    cmd_input_revertClr_timeframe = true;
    
    clearTimeout(cmd_input_clr_timeframe);
    cmd_input_clr_timeframe = setTimeout(function(){
        document.getElementById("cmd_clr").innerHTML = "&#x2612;";
        cmd_input_revertClr_timeframe = false;
    },10000);     
}

function revertClr_cmd_input(){
    if(document.getElementById("cmd_input").value == ""){
        document.getElementById("cmd_input").value = cmd_input_clearedValue;
        document.getElementById("cmd_clr").innerHTML = "&#x2612;";
        cmd_input_revertClr_timeframe = false;
        return true;
    }
    return false;
}

function cmd_input_changed(){
    if(cmd_input_revertClr_timeframe && document.getElementById("cmd_input").value != ""){
        document.getElementById("cmd_clr").innerHTML = "&#x2612;";
        cmd_input_revertClr_timeframe = false;        
    }
}












/* TAB CMD execute */

function cmdInputExecute(){
    var cmd_string = document.getElementById("cmd_input").value;
    cmd_string = cmd_string.trim(); //delete emtpy space at beginning and end   
    cmd_string = cmd_string.replace(/\r/,"\n").replace(/\t/," ");
    cmd_string += "\n";
    
    var string_helper1;
    var string_helper2;
    
    //delete comments between //* */
    while(cmd_string.indexOf("/*") > -1){
        string_helper1 = cmd_string.substring(0, cmd_string.indexOf("/*"));
        string_helper2 = cmd_string.substring(cmd_string.indexOf("/*")+2,cmd_string.length);
        
        if(string_helper2.indexOf("*/") < 0) {
            cmd_string = string_helper1;
        } else {
            cmd_string = string_helper1 + string_helper2.substring(string_helper2.indexOf("*/")+2,string_helper2.length);       
        }    
    }
    
    //delete rest in line after // comments
    while(cmd_string.indexOf("//") > -1){
        string_helper1 = cmd_string.substring(0, cmd_string.indexOf("//"));
        string_helper2 = cmd_string.substring(cmd_string.indexOf("//"),cmd_string.length);
        
        cmd_string = string_helper1 + string_helper2.substring(string_helper2.indexOf("\n"),string_helper2.length);
    }
    
    sendCmds(cmd_string.split(/\n/), 2000, tab_cmd_responseHandler);
}





function tab_cmd_responseHandler() {    
    //console.log("tab_cmd: \n"+this.responseText);
    var response = JSON.parse(this.responseText);
    
    if(response.cmd_ans){
        var cmd_ans = response.cmd_ans;
        
        for(var i=0; i < cmd_ans.length; i++){
            var id = i;
            if(cmd_ans[id].id != i){
                id = 0;
                while(cmd_ans[id].id != i){
                    if(id >= cmd_ans.length){
                        openInfo("err", "Could not find all cmd responses.");        
                        return;
                    }
                    id++;
                }
            }
            
            appendCmdAnswer(cmd_ans[id].cmd, cmd_ans[id].ans, cmd_ans[id].err);   
            
            
            
        }
    } else {
        openInfo("err", "Could not read data from WebInterfaceServer.");
    }
}


var cmd_id_counter = 0;

function appendCmdAnswer(cmd, ans, err){   
    var tr = document.createElement("tr");
    var td_id = document.createElement("td");
    var td_cmd = document.createElement("td");
    var td_ans = document.createElement("td");
    
    td_id.innerHTML = cmd_id_counter++;
    td_cmd.innerHTML = cmd;
    if(ans){
        if(isJson(ans)) ans = JSON.stringify(ans);
        td_ans.innerHTML = ans.replaceAll("\n","<br>").replaceAll("\r","<br>"); 
    } else if(err){
        td_ans.innerHTML = err.replaceAll("\n","<br>").replaceAll("\r","<br>"); 
        td_ans.className = "err_msg";
    } else {
        td_ans.innerHTML = "unknown err (no response message)";
        td_ans.className = "err_msg";
    }
    
    tr.appendChild(td_id);
    tr.appendChild(td_cmd);
    tr.appendChild(td_ans);
    
    document.getElementById("tab_cmd_history_tableheader").after(tr);
    
    if(!document.getElementsByClassName("clearhistory")[0].className.includes("active")){
        document.getElementsByClassName("clearhistory")[0].className = document.getElementsByClassName("clearhistory")[0].className + " active";
    }
}

function clearCmdHistory(){
    while(document.getElementById("tab_cmd_history_tableheader").nextSibling){
        document.getElementById("tab_cmd_history_tableheader").nextSibling.remove();        
    }
    cmd_id_counter = 0;
    document.getElementsByClassName("clearhistory")[0].className = document.getElementsByClassName("clearhistory")[0].className.replace(" active", "");
}

