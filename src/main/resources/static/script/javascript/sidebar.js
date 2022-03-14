(function() {
    const loggedUser = JSON.parse(document.getElementById('userConnected').value);

    setUserNames();

    function setUserNames() {
        $('.userNames a').html(loggedUser['firstName'] + ' ' + loggedUser['lastName']);
    }
})();

