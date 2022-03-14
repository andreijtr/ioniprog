
(function() {
    enableTooltip();
    setToastrOptions();

    function enableTooltip() {
        $('[data-toggle="tooltip"]').tooltip()
    }

    function setToastrOptions() {
        toastr.options = {
            "closeButton": true,
            "debug": false,
            "newestOnTop": false,
            "progressBar": false,
            "positionClass": "toast-top-right",
            "preventDuplicates": false,
            "onclick": null,
            "showDuration": "300",
            "hideDuration": "300",
            "timeOut": "2000",
            "showEasing": "swing",
            "hideEasing": "linear",
            "showMethod": "fadeIn",
            "hideMethod": "fadeOut"
        }
    }

})();

// GLOBAL FUNCTIONS
function loadFragment(url) {
    $('#page-content').load(url);
}

function showSuccessMessage(message) {
    toastr["success"](message);
}

function showErrorMessage(message) {
    toastr["error"](message);
}
