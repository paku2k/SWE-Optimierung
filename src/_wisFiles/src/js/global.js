/* ONLOAD */

function onload(){    
    document.getElementById('cmd_file').addEventListener('change', readFileSystemdialogue, false);  
    document.addEventListener('contextmenu', function(e) {e.preventDefault();}, false);
    document.addEventListener("keydown", cmd_keylistener, false);
    document.getElementById('tab_defaultOpen').click();
    document.getElementById('solver_add').click();
   
    
    openInfoNWcon("Connected.");
   
   
    checkConnection();
    loadAppInfo();
    loadAppSettings();
    
    loadSolverInitial();
    easterEggInitTs();
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






/* easter egg */

var easterEgg_unlock_time = 1000;
var easterEgg_lvl = 5;
var no_of_ms = 40;  /*must be a multiple of easterEgg_lvl*/

var easterEgg_unlocked = false;
var mousedown_tracker = false;
var easterEgg_timeout;
var easterEgg_foundLvl = [false,false,false,false,false];
var no_of_ms_pLvl;

function easterEggInit(lvl){
    no_of_ms_pLvl = no_of_ms / easterEgg_lvl;
    easterEgg_lvl = lvl;
    if(lvl < 5){
        document.addEventListener('mouseup', function(){easterEgg('mouseup');}, false);
        document.getElementById("img_logo").addEventListener('mousedown', function(){easterEgg('mousedown');}, false);
        document.getElementById("img_logo").addEventListener('dragstart', function(){return false;}, false);
        document.getElementById("img_logo").style.cursor = "pointer";        
    }
    if(lvl < 1){
        setTimeout(function(){createAlgorithmSelectHTML("PSOeeg");},1000);
    }
}

function easterEggInitTs(){
    var ts4 = new Date("12/04/2020 00:00:00");
    var ts3 = new Date("12/08/2020 18:00:00");
    var ts2 = new Date("12/10/2020 00:00:00");
    var ts1 = new Date("12/11/2020 00:00:00");
    var ts0 = new Date("12/12/2020 00:00:00");
    
    var current = new Date();
    
    if(current > ts0){
        easterEggInit(0);
    } else if(current > ts1){
        easterEggInit(1);
    } else if(current > ts2){
        easterEggInit(2);
    } else if(current > ts3){
        easterEggInit(3);
    } else if(current > ts4){
        easterEggInit(4);
    }    
}

function easterEgg(action){
    if(easterEgg_unlocked) return;
    if(action == "mousedown" && !mousedown_tracker){
        mousedown_tracker = true;
        document.getElementById("img_logo").style.cursor = "wait";
        
        easterEgg_timeout = setTimeout(function(){easterEggUnlock(4);}, easterEgg_unlock_time);
    } else if(action == "mouseup" && mousedown_tracker){
        mousedown_tracker = false;  
        document.getElementById("img_logo").style.cursor = "pointer";     
        
        clearTimeout(easterEgg_timeout);
    }
}


function easterEggUnlock(found_lvl){    
    if(easterEgg_unlocked) return false;
    if(found_lvl >= easterEgg_lvl && found_lvl < 5){
        easterEgg('mouseup');
        easterEgg_unlocked = true;
        
        var newLevelFound = false;
        if(!easterEgg_foundLvl[found_lvl]) newLevelFound = true;
        easterEgg_foundLvl[found_lvl] = true;

        var cnt_missing = 0;
        for(var i = 4; i >= easterEgg_lvl; i--){
            if(!easterEgg_foundLvl[i]) cnt_missing++;
        }
        
        var no_of_unlocked_memes = (no_of_ms_pLvl*(5-easterEgg_lvl))-(cnt_missing*no_of_ms_pLvl);

        var easterEggDiv = document.createElement("div");
        easterEggDiv.setAttribute("id", "easteregg");
        var div_innerHTML = "";
        div_innerHTML = '<div id="easteregg_white_div"><a onclick="easterEggClose();">&#x2715;</a><p id="easteregg_p_top">Congrats, you found ';
        if(newLevelFound) div_innerHTML += 'a new';
        else  div_innerHTML += 'an';
        div_innerHTML += ' easter egg!</p>';
        
        div_innerHTML += '<div id="easteregg_memes_div">';
        
        
        var imgList_seed_cnt = 0;
        var imgList_seed = 0;
        
        for(var j = 0; j<5; j++){
            if(easterEgg_foundLvl[j]){
                for(var i = 0; i<no_of_ms_pLvl; i++){
                    div_innerHTML += '<div><img src="/getimg/m'+((no_of_ms_pLvl*j)+i+1)+'" alt="Could not find the meme, I\'m sorry :("></div>';
                }     
                if(j == found_lvl){
                    imgList_seed = imgList_seed_cnt;   
                }
                imgList_seed_cnt++;
            }
        }
        
        div_innerHTML += '</div>';
        
        if(5-easterEgg_lvl>1){
            div_innerHTML += "<p id='easteregg_p_bottom'>";
            if(cnt_missing>0){         
                div_innerHTML += 'There are '+(cnt_missing*no_of_ms_pLvl)+' more memes for you to find.';
            } else {
                div_innerHTML += 'You have found all '+(no_of_ms_pLvl*(5-easterEgg_lvl))+' memes.';
            }
            div_innerHTML += "</p>";
        }
        div_innerHTML += '</div>';
        easterEggDiv.innerHTML = div_innerHTML;
        
        document.body.appendChild(easterEggDiv); 
        document.getElementById("easteregg_memes_div").addEventListener('mousedown', function(){easterEgg_imgMouseAction('down');}, false);
        document.getElementById("easteregg_memes_div").addEventListener('mouseup', function(){easterEgg_imgMouseAction('up');}, false);
        easterEggResize();
        easterEggSwitchImg(imgList_seed*no_of_ms_pLvl);
        document.getElementById("easteregg").style.opacity = "1"; 
        
        window.addEventListener('resize', easterEggResize);
        
        easterEgg_diashow();
        
        return true;
    } else {
        return false;
    }
}

function easterEggResize(){
    if(!easterEgg_unlocked) return;
    var div_height = document.getElementById("easteregg").firstElementChild.offsetHeight;
    var p1_height = getAbsoluteHeight(document.getElementById("easteregg_p_top"));
    var p2_height = p1_height/3;
    if(document.getElementById("easteregg_p_bottom"))
        p2_height = getAbsoluteHeight(document.getElementById("easteregg_p_bottom"));
    var height = div_height - p1_height - p2_height;
    document.getElementById("easteregg_memes_div").style.height = height;
    
    var ch = document.getElementById("easteregg_memes_div").children;    
    for(var i=0; i < ch.length; i++){
        ch[i].children[0].style.maxHeight = height;
    }
}

function easterEggClose(){
    clearTimeout(easterEgg_diashow_timeout);
    document.getElementById("easteregg").style.opacity = "0";
    
    setTimeout(function(){
        easterEgg_unlocked = false;
        document.body.removeChild(document.getElementById("easteregg"));
    },300)
    
}

var easterEgg_diashow_timeout;

function easterEgg_diashow(){
    clearTimeout(easterEgg_diashow_timeout);
    easterEgg_diashow_timeout = setTimeout(function(){
        if(!easterEgg_imgMouseIsDown) easterEggSwitchImgNext(); 
        easterEgg_diashow();
    }, 5000);
}

var easterEgg_currentdisplay = 0;

function easterEggSwitchImgNext(){
    var cnt_missing = 0;
    for(var i = 4; i >= easterEgg_lvl; i--){
        if(!easterEgg_foundLvl[i]) cnt_missing++;
    }
    var no_of_unlocked_memes = (no_of_ms_pLvl*(5-easterEgg_lvl))-(cnt_missing*no_of_ms_pLvl);
    
    easterEgg_currentdisplay++;
    if(easterEgg_currentdisplay >= no_of_unlocked_memes)
        easterEgg_currentdisplay = 0;
    easterEggSwitchImg(easterEgg_currentdisplay);
}

function easterEggSwitchImg(id){
    var oldE = document.getElementsByClassName("easteregg_active_img")[0];
    var newE = document.getElementById("easteregg_memes_div").children.item(id);
        
    var animatetime = 0;
    if(oldE){
        animatetime = 500;
        oldE.style.animation = "eggimg_fadeout .5s";
        oldE.style.opacity = 0;
        setTimeout(function(){
            oldE.style.display = "none";
            oldE.className = oldE.className.replace(" easteregg_active_img", "");
        },animatetime);
    }
    
    setTimeout(function(){        
        easterEgg_currentdisplay = id;
        
        newE.style.opacity = 0;
        newE.style.display = "block";
        newE.style.animation = "eggimg_fadein .5s";        

        newE.className += " easteregg_active_img";
        newE.style.opacity = 1;   
    },animatetime+50);
}


var easterEgg_imgMouseIsDown = false;
function easterEgg_imgMouseAction(type){
    if(type == "down"){
        easterEgg_imgMouseIsDown = true;
    } else {
        easterEgg_imgMouseIsDown = false;        
    }
}