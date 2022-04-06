
(function() {
    const STATE = {
        ACTIVE : "ACTIVE",
        DELETED : "DELETED"
    };
    const loggedUser = JSON.parse(document.getElementById('userConnected').value);
    const validatorPatientForm = validatePatientForm();

    let offset = 0;
    let pageSizeSelect = document.getElementById('pageSizeSelect');

    // modal
    setBirthDatePicker();
    bindClickEventOnAddPatientButton();
    bindClickEventOnCloseModalPatientButton();

    // paging
    initPage();
    bindChangeEventOnPageSizeSelect();

    // MODAL ADD|EDIT PATIENT
    function bindClickEventOnAddPatientButton() {
        $('#addPatient').on('click', function() {
            $('#modalPatient .modal-title').html('Pacient nou');
            $('#modalPatient').modal('show');
        });
    }
    function bindClickEventOnCloseModalPatientButton() {
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
        $('#versionInput').val('');

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
                else {
                    let jsonObject = createJsonFromPatientForm();
                    delete jsonObject.doctorResponsible;
                    updatePatientAjax(jsonObject);
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
            "version" : $('#versionInput').val(),
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
        data.forEach(patientDoctor => patientsRow.appendChild(createPatientCard(patientDoctor)));
    }

    function createPatientCard(patientDoctor) {
        const patient = patientDoctor.patientDto;
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
        cardBody.appendChild(createTable(patientDoctor));

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

    function createTable(patientDoctor) {
        const patient = patientDoctor.patientDto;
        const table = document.createElement('table');
        const tbody = document.createElement('tbody');

        table.classList.add('table', 'table-striped', 'table-bordered');
        table.style.fontSize = '1em';
        table.appendChild(tbody);

        tbody.appendChild(createRowTbody('Telefon', patient.phone));
        tbody.appendChild(createRowTbody('Data nasterii', patient.birthdayDate));
        tbody.appendChild(createRowTbody('Descriere', (patient.details != '' ? patient.details : '-')));
        tbody.appendChild(createRowTbody('Status', patient.status));
        tbody.appendChild(createRowActions(JSON.stringify(patientDoctor)));

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

    function createRowActions(jsonPatientDoctor) {
        let row = document.createElement('tr');
        let col = document.createElement('td');
        col.setAttribute('colspan', '2');
        row.appendChild(col);

        let inputJson = document.createElement('input');
        let editBtn = document.createElement('button');
        let scheduleBtn = document.createElement('button');
        let transferBtn = document.createElement('button');
        let deleteBtn = document.createElement('button');
        let infoBtn = document.createElement('button');

        inputJson.style.display = 'none';
        editBtn.classList.add('btn', 'btn-sm', 'btn-secondary', 'mr-1', 'editPatient');
        scheduleBtn.classList.add('btn', 'btn-sm', 'btn-success','mr-1','schedulePatient');
        transferBtn.classList.add('btn', 'btn-sm', 'btn-warning','mr-1', 'transferPatient');
        deleteBtn.classList.add('btn', 'btn-sm', 'btn-danger','mr-1', 'deletePatient');
        infoBtn.classList.add('btn', 'btn-sm', 'btn-secondary','mr-1', 'showInfoPatient');

        inputJson.setAttribute('type', 'text');
        inputJson.setAttribute('value' , jsonPatientDoctor);
        editBtn.innerHTML = 'Editeaza';
        scheduleBtn.innerHTML = 'Programeaza';
        transferBtn.innerHTML = 'Transfera';
        deleteBtn.innerHTML = 'Sterge';
        infoBtn.innerHTML = 'Info';

        col.appendChild(inputJson);
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
            bindClickEventEditButton();
            bindClickEventOnDeleteButton();
            bindClickEventOnInfoButton();
            bindClickEventOnBackToPatientPanelBtn();
        }).fail(function( jqXHR, textStatus, errorThrown ) {
            console.log("a esuat requestul. asta e statusul", jqXHR.status);
        });
    }

    // EDIT
    function bindClickEventEditButton() {
        document.querySelectorAll('.editPatient').forEach(function (btn) {
            btn.addEventListener('click', function() {
                const stringPatientDoctor = $(btn).siblings('input').val();
                const patient = JSON.parse(stringPatientDoctor).patientDto;

                $('#idPatientInput').val(patient.idPatient);
                $('#firstnameInput').val(patient.firstName);
                $('#lastnameInput').val(patient.lastName);
                $('#phoneInput').val(patient.phone);
                $("#birthDatePicker").find('input').val(patient.birthdayDate);
                $('#detailsInput').val(patient.details);
                $('#statusInput').val(patient.status);
                $('#versionInput').val(patient.version);

                $('#modalPatient .modal-title').html('Editeaza pacientul ' + patient.lastName + ' ' + patient.firstName);
                $('#modalPatient').modal('show');
            })
        })
    }

    function updatePatientAjax(jsonObject) {
        $.ajax({
            url: "/patient/update",
            type: 'PUT',
            contentType : "application/json",
            data : JSON.stringify(jsonObject)
        }).done(function(data) {
            showSuccessMessage("Pacient a fost modificat cu succes!");
            const patientParams = getPatientsPagingParams(STATE.ACTIVE);
            loadPatients(patientParams);
        }).fail(function( jqXHR, textStatus, errorThrown ) {
            showErrorMessage(jqXHR.responseText);
            const patientParams = getPatientsPagingParams(STATE.ACTIVE);
            loadPatients(patientParams);
        })
    }

    // DELETE
    function bindClickEventOnDeleteButton() {
        document.querySelectorAll('.deletePatient').forEach(function (btn) {
            btn.addEventListener('click', function() {
                const stringPatientDoctor = $(btn).siblings('input').val();
                const patientDoctor = JSON.parse(stringPatientDoctor);

                bootbox.confirm({
                    size: 'small',
                    message: 'Stergeti pacientul ' + patientDoctor.patientDto.lastName + ' ' + patientDoctor.patientDto.firstName + '?',
                    callback: function (confirmed) {
                        if (confirmed) {
                            deletePatientAjax(patientDoctor.idPatientDoctor);
                        }
                    }
                })
            })
        })
    }

    function deletePatientAjax(idPatientDoctor) {
        // this function updates state of PatientDoctor to deleted. Patient entity isn't change
        // reason is that a patient can belong to many users, so a user can delete only his PatientDoctor binding
        $.ajax({
            url: "/patient/deletePatientDoctor",
            type: 'PUT',
            data : {'idPatientDoctor' : idPatientDoctor}
        }).done(function() {
            showSuccessMessage("Pacientul a fost sters cu succes!");
            offset = 0;
            const patientParams = getPatientsPagingParams(STATE.ACTIVE);
            loadPatients(patientParams);
        }).fail(function( jqXHR, textStatus, errorThrown ) {
            showErrorMessage(jqXHR.responseText);
            const patientParams = getPatientsPagingParams(STATE.ACTIVE);
            loadPatients(patientParams);
        })
    }

    // INFO SECTION
    function showSection(show, hide) {
        $('#' + hide).slideUp(function() {
            $('#' + show).slideDown();
        })
    }

    function bindClickEventOnInfoButton() {
        document.querySelectorAll('.showInfoPatient').forEach(function(btn) {
            btn.addEventListener('click', function() {
                const stringPatientDoctor = $(btn).siblings('input').val();
                const patientDoctor = JSON.parse(stringPatientDoctor);
                document.getElementById('titleInfoSection').innerHTML = `Informatii pentru pacientul ${patientDoctor.patientDto.lastName} ${patientDoctor.patientDto.firstName} `;
                loadPatientAudit(patientDoctor.patientDto.idPatient);
                loadPatientDoctors(patientDoctor.patientDto.idPatient);
                showSection('infoSection', 'patientSection');
            })
        })
    }

    function bindClickEventOnBackToPatientPanelBtn() {
        document.getElementById("backToPatientPanelBtn")
                .addEventListener('click', function() {
                    showSection('patientSection', 'infoSection');
                });
    }

    // INFO AUDIT - TIMELINE
    function loadPatientAudit(idPatient) {
        $.ajax({
            url : "/patient/audit?idPatient=" + idPatient,
            type: "GET",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            beforeSend: function() {
                $('#timelinePatient').html('<div class="col text-center"><img src="/gif/loading.gif" style="width: 2rem;"></div>');
            }
        }).done(function(data) {
            renderAudit(data);
        })
    }

    function renderAudit(auditList) {
        const timeline = document.getElementById('timelinePatient');
        timeline.innerHTML = '';

        for (let i = 0; i < auditList.length; i++) {
            timeline.appendChild(createDateLabel(auditList[i].auditDto));

            switch (auditList[i].auditDto.actionType) {
                case 'INSERT':
                    timeline.appendChild(createInsertTimeline(auditList[i].auditDto));
                    break;
                case 'UPDATE':
                    timeline.appendChild(createUpdateTimeline(auditList[i].auditDto));
                    break;
            }
        }
    }

    function createDateLabel(audit) {
        const timeLabel = document.createElement('div');
        timeLabel.classList.add('time-label');

        const spanTimelabel = document.createElement('span');
        spanTimelabel.innerHTML = audit.createdOn.split(' ')[0];
        switch (audit.actionType) {
            case 'INSERT':
                spanTimelabel.classList.add('bg-success');
                break;
            case 'UPDATE':
                spanTimelabel.classList.add('bg-warning');
                break;
            default:
                spanTimelabel.classList.add('bg-info');
        }
        timeLabel.appendChild(spanTimelabel);

        return timeLabel;
    }

    function createInsertTimeline(audit) {
        const divItem = document.createElement('div');
        const iElement = document.createElement('i');
        iElement.classList.add('fas', 'fa-solid', 'fa-plus', 'bg-success');

        divItem.appendChild(iElement);

        const timelineItem = document.createElement('div');
        timelineItem.classList.add('timeline-item');

        divItem.appendChild(timelineItem);

        const time = document.createElement('span');
        time.classList.add('time');
        const timePicture = document.createElement('i');
        timePicture.classList.add('fas', 'fa-clock');

        time.appendChild(timePicture);
        time.innerHTML += ' ' + audit.createdOn.split(' ')[1].substring(0, 5);

        timelineItem.appendChild(time);

        const timelineHeader = document.createElement('h3');
        timelineHeader.classList.add('timeline-header');
        timelineHeader.innerHTML = `<a href="#"> ${audit.createdBy.firstName} ${audit.createdBy.lastName}</a> a introdus pacientul`;
        timelineItem.appendChild(timelineHeader);

        return divItem;
    }

    function createUpdateTimeline(audit) {
        const divItem = document.createElement('div');
        const iElement = document.createElement('i');
        iElement.classList.add('fas', 'fa-solid', 'fa-pen', 'bg-warning');

        divItem.appendChild(iElement);

        const timelineItem = document.createElement('div');
        timelineItem.classList.add('timeline-item');

        divItem.appendChild(timelineItem);

        const time = document.createElement('span');
        time.classList.add('time');

        const timePicture = document.createElement('i');
        timePicture.classList.add('fas', 'fa-clock');

        time.appendChild(timePicture);
        time.innerHTML += ' ' + audit.createdOn.split(' ')[1].substring(0, 5);

        timelineItem.appendChild(time);

        const timelineHeader = document.createElement('h3');
        timelineHeader.classList.add('timeline-header');
        timelineHeader.innerHTML = `<a href="#"> ${audit.createdBy.firstName} ${audit.createdBy.lastName}</a> a modificat datele pacientului`;
        timelineItem.appendChild(timelineHeader);

        const timelineBody = document.createElement('h3');
        timelineBody.classList.add('timeline-body');
        timelineBody.style.overflowX = 'auto';
        const changesTable = createChangesTable(audit.changes);
        timelineBody.appendChild(changesTable);
        timelineItem.appendChild(timelineBody);

        return divItem;
    }

    function createChangesTable(changes) {
        const table = document.createElement('table');
        const thead = document.createElement('thead');
        const tbody = document.createElement('tbody');

        table.classList.add('table', 'table-striped', 'table-bordered');
        table.style.fontSize = '15px';

        table.appendChild(thead);
        table.appendChild(tbody);

        let col1 = document.createElement('th');
        col1.innerHTML = 'Campul';
        let col2 = document.createElement('th');
        col2.innerHTML = 'Valoare noua';
        let col3 = document.createElement('th');
        col3.innerHTML = 'Valoare veche';

        thead.appendChild(col1);
        thead.appendChild(col2);
        thead.appendChild(col3);

        for (let i = 0; i < changes.length; i++) {
            const row = document.createElement('tr');

            let col = document.createElement('td');
            col.innerHTML = changes[i].columnName;
            row.appendChild(col);

            col = document.createElement('td');
            col.innerHTML = changes[i].newValue;
            row.appendChild(col);

            col = document.createElement('td');
            col.innerHTML = changes[i].oldValue;
            row.appendChild(col);

            tbody.appendChild(row);
        }

        return table;
    }

    // INFO DOCTORS
    function loadPatientDoctors (idPatient) {
        $.ajax({
            url : "/patient/get",
            type: "POST",
            data : JSON.stringify({"idPatient" : idPatient, "orderByDoctor" : true}),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            beforeSend: function() {
                $('#infoPatientDoctors').html('<div class="col text-center"><img src="/gif/loading.gif" style="width: 2rem;"></div>');
            }
        }).done(function(data) {
            renderPatientDoctors(data);
        })
    }

    function renderPatientDoctors(data) {
        const parentElement = document.getElementById('infoPatientDoctors');
        parentElement.innerHTML = '';
        for (let i = 0; i < data.length; i++) {
            const callout = document.createElement('div');
            const paragraph = document.createElement('p');
            const badge = document.createElement('span');
            const h5 = document.createElement('h5');
            h5.innerHTML = `Dr. ${data[i].doctorDto.lastName} ${data[i].doctorDto.firstName}`;

            if (data[i].state == STATE.ACTIVE) {
                callout.classList.add('callout', 'callout-success');
                badge.classList.add('badge', 'badge-success');
                paragraph.appendChild(badge);
                paragraph.innerText = 'Activ';
            }
            else if (data[i].state == STATE.DELETED) {
                callout.classList.add('callout', 'callout-danger');
                badge.classList.add('badge', 'badge-danger');
                paragraph.appendChild(badge);
                paragraph.innerText = 'Sters';
            }
            callout.appendChild(h5);
            callout.appendChild(paragraph);
            parentElement.appendChild(callout);
        }
    }
})();