function loadFile(url, timeout, callback) {
    var args = Array.prototype.slice.call(arguments, 3);
    var xhr = new XMLHttpRequest();
    xhr.ontimeout = function () {
    console.error("The request for " + url + " timed out.");
    };
    xhr.onload = function() {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
            callback.apply(xhr, args);
        } else {
        console.error(xhr.statusText);
        }
    }
};
    xhr.open("GET", url, true);
xhr.timeout = timeout;
    xhr.send(null);
        }
    function showMessage (message) {
        window.alert(message + this.responseText);
        }