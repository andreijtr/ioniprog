
(function() {
    const STATE = {
        ACTIVE : "ACTIVE"
    };
    const loggedUser = JSON.parse(document.getElementById('userConnected').value);
    const validatorPatientForm = validatePatientForm();

    let offset = 0;
    let pageSizeSelect = document.getElementById('pageSizeSelect');

    // modal
    setBirthDatePicker();
    bindCloseModalPatientButtonClickEvent();

    // paging
    initPage();
    bindChangeEventOnPageSizeSelect();

    // MODAL ADD|EDIT PATIENT
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

    function addPatientAjax(jsonObject) {
        $.ajax({
            url: "/patient/add",
            type: 'POST',
            contentType : "application/json",
            data : JSON.stringify(jsonObject)
        }).done(function(data) {
            showSuccessMessage("Pacient introdus cu succes!");
            offset = 0;
            const patientParams = getPatientsPagingParams(STATE.ACTIVE);
            loadPatients(patientParams);
        }).fail(function( jqXHR, textStatus, errorThrown ) {
            showErrorMessage("Ceva nu a functionat. Cod eroare: " + jqXHR.status + ".");
        })
    }

    // PAGING
    function initPage() {
        pageSizeSelect.value = "10";
        const patientParams = getPatientsPagingParams(STATE.ACTIVE);
        loadPatients(patientParams);
    }

    function clearPatientsPaging() { // in callback si la before send cand pui un loading
        document.getElementById('patientCardsRow').textContent = '';
        document.getElementById('pagingButtonsCol').textContent = '';
    }

    function renderPatients(data) {
        const patientsRow = document.getElementById('patientCardsRow');
        data.forEach(patientDoctor => patientsRow.appendChild(createPatientCard(patientDoctor.patientDto)));
    }

    function createPatientCard(patient) {
        const card = document.createElement('div');
        const cardHeader = document.createElement('div');
        const cardBody = document.createElement('div');

        card.classList.add('card', 'card-outline', 'card-primary', 'collapsed-card');
        card.appendChild(cardHeader);
        card.appendChild(cardBody);

        cardHeader.classList.add('card-header');

        const cardTitle = document.createElement('h3');
        cardTitle.classList.add('card-title');
        cardTitle.innerHTML = patient.lastName + ' ' + patient.firstName;
        cardHeader.appendChild(cardTitle);

        const cardTools = createCardTools();
        cardHeader.appendChild(cardTools);

        cardBody.classList.add('card-body');
        cardBody.style.padding = '0';
        cardBody.appendChild(createTable(patient));

        const column = document.createElement('div')
        column.classList.add('col-md-3');
        column.appendChild(card);

        return column;
    }

    function createCardTools() {
        const cardTools = document.createElement('div');
        cardTools.classList.add('card-tools');

        const collapseButton = document.createElement('button');
        collapseButton.classList.add('btn', 'btn-tool');
        collapseButton.type = 'button';
        collapseButton.setAttribute('data-card-widget', 'collapse');

        const iElement = document.createElement('i');
        iElement.classList.add('fas', 'fa-plus');

        collapseButton.appendChild(iElement);
        cardTools.appendChild(collapseButton);

        return cardTools;
    }

    function createTable(patient) {
        const table = document.createElement('table');
        const tbody = document.createElement('tbody');

        table.classList.add('table', 'table-striped', 'table-bordered');
        table.style.fontSize = '1em';
        table.appendChild(tbody);

        tbody.appendChild(createRowTbody('Telefon', patient.phone));
        tbody.appendChild(createRowTbody('Data nasterii', patient.birthdayDate));
        tbody.appendChild(createRowTbody('Descriere', (patient.details != '' ? patient.details : '-')));
        tbody.appendChild(createRowTbody('Status', patient.status));
        tbody.appendChild(createRowActions());

        return table;
    }

    function createRowTbody(valueCol1, valueCol2) {
        let row = document.createElement('tr');
        let col1 = document.createElement('td');
        let col2 = document.createElement('td');

        if (valueCol1 == 'Status') {
            const span = document.createElement('span');
            if (valueCol2 == STATE.ACTIVE) {
                span.classList.add('badge', 'bg-success');
                span.innerHTML = STATE.ACTIVE;
            }
            col2.appendChild(span);
        }
        else {
            col2.innerHTML = valueCol2;
        }
        col1.innerHTML = valueCol1;
        row.appendChild(col1);
        row.appendChild(col2);

        return row;
    }

    function createRowActions() {
        let row = document.createElement('tr');
        let col = document.createElement('td');
        col.setAttribute('colspan', '2');
        row.appendChild(col);

        let editBtn = document.createElement('button');
        let scheduleBtn = document.createElement('button');
        let transferBtn = document.createElement('button');
        let deleteBtn = document.createElement('button');
        let infoBtn = document.createElement('button');

        editBtn.classList.add('btn', 'btn-sm', 'btn-secondary', 'mr-1', 'editPatient');
        scheduleBtn.classList.add('btn', 'btn-sm', 'btn-success','mr-1','schedulePatient');
        transferBtn.classList.add('btn', 'btn-sm', 'btn-warning','mr-1', 'transferPatient');
        deleteBtn.classList.add('btn', 'btn-sm', 'btn-danger','mr-1', 'deletePatient');
        infoBtn.classList.add('btn', 'btn-sm', 'btn-secondary','mr-1', 'showInfoPatient');

        editBtn.innerHTML = 'Editeaza';
        scheduleBtn.innerHTML = 'Programeaza';
        transferBtn.innerHTML = 'Transfera';
        deleteBtn.innerHTML = 'Sterge';
        infoBtn.innerHTML = 'Info';

        col.appendChild(editBtn);
        col.appendChild(scheduleBtn);
        col.appendChild(infoBtn);
        col.appendChild(transferBtn);
        col.appendChild(deleteBtn);

        return row;
    }

    function renderPagingButtons(currentPage, totalPage) {
        let pagingButtonsCol = document.getElementById('pagingButtonsCol');
            pagingButtonsCol.textContent = '';
        let pagination = document.createElement('div');
            pagination.classList.add('pagination');
        pagingButtonsCol.appendChild(pagination);

        for (let i = 1; i <= totalPage; i++) {
            let btn = document.createElement('a');
            btn.innerHTML = i;
            btn.href = "#";
            if (i == currentPage) {
                btn.classList.add('active');
            }
            btn.classList.add('pagingButton');
            pagination.appendChild(btn);
        }
    }

    function bindClickEventPagingButtons() {
        document.querySelectorAll('.pagingButton').forEach(function (btn) {
            btn.addEventListener('click', function () {
                offset = (btn.innerHTML - 1) * pageSizeSelect.value;
                const patientParams = getPatientsPagingParams(STATE.ACTIVE);
                loadPatients(patientParams);
            })
        })
    }

    function bindChangeEventOnPageSizeSelect() {
        pageSizeSelect.addEventListener('change', function() {
            offset = 0;
            const patientParams = getPatientsPagingParams("ACTIVE");
            loadPatients(patientParams);
        })
    }

    function getPatientsPagingParams(state) {
        const patientParams = {
            "offset" : offset,
            "pageSize" : pageSizeSelect.value,
            "doctor" : {
                "idUser" : loggedUser.idUser
            },
            "state" : state
        }

        return patientParams;
    }

    function loadPatients(patientsParams) {
        $.ajax({
            url : "/patient/paging",
            type: "POST",
            data: JSON.stringify(patientsParams),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            beforeSend: function() {
                $('#patientCardsRow').html('<div class="col text-center"><img src="/gif/loading.gif" style="width: 2rem;"></div>');
            }
        }).done(function (data) {
            clearPatientsPaging();
            renderPatients(data.data);
            renderPagingButtons(data.currentPage, data.totalPages);
            bindClickEventPagingButtons();
        }).fail(function( jqXHR, textStatus, errorThrown ) {
            console.log("a esuat requestul. asta e statusul", jqXHR.status);
        });
    }

})();