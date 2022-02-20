
(function() {
    const loggedUser = JSON.parse(document.getElementById('userConnected').value);
    const validatorPatientForm = validatePatientForm();

    // patient modal
    setBirthDatePicker();
    bindCloseModalPatientButtonClickEvent();

    // MODAL
    function bindCloseModalPatientButtonClickEvent() {
        $('.closeModalPatient').on('click', function() {
            closeModalPatient();
        })
    }

    function closeModalPatient() {
        $('#idPatientInput').val('');
        $('#firstnameInput').val('');
        $('#lastnameInput').val('');
        $('#phoneInput').val('');
        $('#detailsInput').val('');
        $("#birthDatePicker").find('input').val('');
        $('#statusInput').val('');

        $('#modalPatient').modal('hide');
        validatorPatientForm.resetForm();
    }

    function setBirthDatePicker() {
        $('#birthDatePicker').datetimepicker({
            locale : 'ro',
            format : 'DD.MM.yyyy'
        });
    }

    function validatePatientForm() {
        const validator = $('#formAddPatient').validate({
            rules: {
                firstnameInput: 'required',
                lastnameInput: 'required',
                phoneInput: {
                    required: true,
                    minlength: 10,
                    maxlength: 10,
                    digits: true
                },
                birthDatePickerName: 'required'
            },
            messages: {
                firstnameInput: 'Acest camp este obligatoriu',
                lastnameInput: 'Acest camp este obligatoriu',
                phoneInput: 'Campul are 10 caractere, doar numere',
                birthDatePickerName: 'Acest camp este obligatoriu'
            },
            submitHandler: function(form, event) {
                event.preventDefault();
                if($('#idPatientInput').val() == '') {
                    let jsonObject = createJsonFromPatientForm();
                    jsonObject['status'] = 'ACTIVE';
                    addPatientAjax(jsonObject);
                }

                closeModalPatient();
            }
        });
        return validator;
    }

    function createJsonFromPatientForm() {
        const jsonObject = {
            "idPatient" : $('#idPatientInput').val(),
            "firstName" : $('#firstnameInput').val(),
            "lastName" : $('#lastnameInput').val(),
            "phone" : $('#phoneInput').val(),
            "birthdayDate" : $("#birthDatePicker").find('input').val(),
            "details" : $('#detailsInput').val(),
            "status" : $('#statusInput').val(),
            "doctorResponsible" : {
                "idDoctor" : loggedUser.idUser
            }
        }
        return jsonObject;
    }

    // AJAX
    function addPatientAjax(jsonObject) {
        $.ajax({
            url: "/patient/add",
            type: 'POST',
            contentType : "application/json",
            data : JSON.stringify(jsonObject)
        }).done(function(data) {
            console.log("a functionat requestul. asteas sunt datele", data);
        }).fail(function( jqXHR, textStatus, errorThrown ) {
            console.log("a esuat requestul. asta e statusul", jqXHR.status);
        })
    }
})();