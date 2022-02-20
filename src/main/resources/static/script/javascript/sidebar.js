(function() {
    const loggedUser = JSON.parse(document.getElementById('userConnected').value);

    setUserNames();

    function setUserNames() {
        $('.userNames a').html(loggedUser['firstName'] + ' ' + loggedUser['lastName']);
    }
})();

function loadFragment(url) {
    $('#page-content').load(url);
}
