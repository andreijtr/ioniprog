
(function() {
    enableTooltip();

    function getPatient(id) {
        $.ajax({
            url: "/patient",
            type: "GET",
            data: {idPatient : id}
        }).done(function (data) {
            console.log(data);
        });
    }

    function enableTooltip() {
        $('[data-toggle="tooltip"]').tooltip()
    }

})();
