(function() {
    const loggedUser = JSON.parse(document.getElementById('userConnected').value);

    const STATE = {
        ACTIVE : "ACTIVE",
        DELETED : "DELETED"
    };

    loadDoctorLocation();

    function loadDoctorLocation () {
        $.ajax({
            url : "/location/user",
            type: "GET",
            data: {
                "idDoctor" : loggedUser.idUser
            },
            beforeSend: function() {
                $('#locationCardsRow').html('<div class="col text-center"><img src="/gif/loading.gif" style="width: 2rem;"></div>');
            }
        }).done(function(data) {
            renderLocations(data);
        })
    }

    function renderLocations(data) {
        const parentElement = document.getElementById('locationCardsRow');
        parentElement.innerHTML = '';
        for (let i = 0; i < data.length; i++) {
            const callout = document.createElement('div');
            const paragraph = document.createElement('p');
            const badge = document.createElement('span');
            const h5 = document.createElement('h5');
            const iElem = document.createElement('i');

            iElem.classList.add('fas', 'fa-map-marker-alt', 'mr-1');
            h5.appendChild(iElem);
            h5.appendChild(document.createTextNode(data[i].locationDto.name));

            if (data[i].state == STATE.ACTIVE) {
                callout.classList.add('callout', 'callout-success');
                badge.classList.add('badge', 'badge-success');
                badge.innerText = 'Activ';
                paragraph.appendChild(badge);
            }
            else if (data[i].state == STATE.DELETED) {
                callout.classList.add('callout', 'callout-danger');
                badge.classList.add('badge', 'badge-danger');
                badge.innerText = 'Sters';
                paragraph.appendChild(badge);
            }
            callout.appendChild(h5);
            callout.appendChild(paragraph);
            parentElement.appendChild(callout);
        }
    }
})();