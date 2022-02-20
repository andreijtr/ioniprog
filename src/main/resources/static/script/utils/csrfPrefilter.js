(function() {
    enableCsrfPrefilter();

    const xsrfToken = $("meta[name='csrf-token']").attr("value");
    const xsrfHeader = $("meta[name='csrf-header']").attr("value");

    const UNSAFE_METHODS = ['POST', 'PUT'];

    function enableCsrfPrefilter() {
        $.ajaxPrefilter( function (options, originalOptions, jqXHR) {
            if (needsProtection(options.type, UNSAFE_METHODS)) {
                jqXHR.setRequestHeader(xsrfHeader, xsrfToken);
            }
        })
    }

    function needsProtection(methodType, unsafeMethods) {
        let needProtection = false;
        unsafeMethods.forEach(function(method) {
            if (methodType.toUpperCase() === method) {
                needProtection = true;
            }
        });
        return needProtection;
    }
})();