/* INFO BANNER */

var timeout;

function openInfo(type, text){
    openInfoAdvanced(type, text, 5000)
}

function openInfoAdvanced(type, text, ontime){
    if(document.getElementById("info").style.display!="none"){
        closeInfo();
        setTimeout(function(){ openInfo(type,text) }, 200);
        return;
    }
    
    setInfoColor(type);
    document.getElementById("info_text").innerHTML = text;
    document.getElementById("info").style.animationName = "fadeInfoIn";
    document.getElementById("info").style.animationDuration = "0.2s";
    document.getElementById("info").style.display = "block";   
    
    clearTimeout(timeout);
    
    if(ontime > 0) timeout = setTimeout(function(){ closeInfo(); }, ontime); 
}

function closeInfo(){
    document.getElementById("info").style.animationName = "fadeInfoOut";
    document.getElementById("info").style.animationDuration = "0.2s";
    
    var elem = document.getElementById("info");
    elem.parentNode.replaceChild(elem.cloneNode(true), elem);
    
    setTimeout(function(){ document.getElementById("info").style.display = "none"; }, 150);    
    
    infonwerr_open = false;
    infonwcon_open = false;
}

function setInfoColor(type){
    if(type=="suc" || type=="warn" || type=="err"){
        document.getElementById("info").className = type;
        document.getElementById("info_close_x").className = "white";
        if(type=="warn"){            
            document.getElementById("info_close_x").className = "grey";
        }
    } else {
        document.getElementById("info").className = "err";        
    }    
}


