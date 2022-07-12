(function() {
    let calendarEl = document.getElementById('calendarAppointments');
    let calendar = new FullCalendar.Calendar(calendarEl, {
        locale: 'ro',
        initialView: 'dayGridMonth',
        headerToolbar: {
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek,timeGridDay'
        },
        themeSystem: 'bootstrap4',
        contentHeight: 'auto',
        eventTimeFormat: {
            hour: '2-digit',
            minute: '2-digit',
            omitZeroMinute: false,
            hour12: false
        },
        dayMaxEventRows: 3,
        slotMinTime: '07:00:00',
        slotMaxTime: '22:00:00',
        allDaySlot: false,
        slotLabelFormat: {
            hour: 'numeric',
            minute: '2-digit',
            omitZeroMinute: false,
            hour12: false
        },
        dateClick: function(info) {
            calendar.changeView('timeGridDay', info.date);
        },
        eventDidMount : function(arg) {
            if (arg.event.extendedProps.patientDto != null && arg.event.extendedProps.locationDto != null) {
                const event = arg.event;
                arg.el.
                    querySelector('.fc-event-title')
                    .innerHTML = `<i class="fas fa-solid fa-user" style="color: green"></i> ${event.extendedProps.patientDto.lastName} ${event.extendedProps.patientDto.firstName.charAt(0).toUpperCase()}. 
                                  <i class="fas fa-fw fa-map-marker-alt" style="color: #b73867;"></i>${event.extendedProps.locationDto.name}`;
            }
        },
        eventClick: function(info) {
            const event = info.event;
            console.log(event);
            showModalViewAppointment();
        },
        // events: [
        //     {
        //         id     : 1,
        //         // title  : 'Pac. Tudose',
        //         start  : '2022-05-10 09:00',
        //         end    : '2022-05-10 10:00',
        //         extendedProps : {
        //             patientDto : {
        //                 idPatient : '3',
        //                 firstName : 'Marius',
        //                 lastName  : 'Tudose',
        //                 phone     : '0789123431'
        //             },
        //             doctorDto : {
        //                 idDoctor : '1',
        //                 firstName : 'Ioana',
        //                 lastName  : 'Jitaru',
        //                 phone     : '0761092710'
        //             },
        //             locationDto : {
        //                 idLocation : 1,
        //                 name       : 'Aident'
        //             }
        //         }
        //     },
        //     {
        //         id     : 2,
        //         // title  : 'Pac. Cristi',
        //         start  : '2022-05-04 09:00',
        //         end    : '2022-05-04 11:00',
        //         extendedProps : {
        //             patientDto : {
        //                 idPatient : '3',
        //                 firstName : 'Marius',
        //                 lastName  : 'Cristi',
        //                 phone     : '0789123431'
        //             },
        //             doctorDto : {
        //                 idDoctor : '1',
        //                 firstName : 'Ioana',
        //                 lastName  : 'Jitaru',
        //                 phone     : '0761092710'
        //             },
        //             locationDto : {
        //                 idLocation : 1,
        //                 name       : 'Bolta'
        //             }
        //         }
        //     },
        //     {
        //         id     : 3,
        //         // title  : 'Pac. Miruna',
        //         start  : '2022-05-09T12:30',
        //         end    : '2022-05-09T13:30',
        //         extendedProps : {
        //             patientDto : {
        //                 idPatient : '3',
        //                 firstName : 'Marius',
        //                 lastName  : 'Miruna',
        //                 phone     : '0789123431'
        //             },
        //             doctorDto : {
        //                 idDoctor : '1',
        //                 firstName : 'Ioana',
        //                 lastName  : 'Jitaru',
        //                 phone     : '0761092710'
        //             },
        //             locationDto : {
        //                 idLocation : 1,
        //                 name       : 'Zorilor'
        //             }
        //         }
        //     },
        //     {
        //         id     : 4,
        //         // title  : 'Pac. Crinul',
        //         start  : '2022-05-09T10:30',
        //         end    : '2022-05-09T11:30',
        //         extendedProps : {
        //             patientDto : {
        //                 idPatient : '3',
        //                 firstName : 'Marius',
        //                 lastName  : 'Crinuletttule',
        //                 phone     : '0789123431'
        //             },
        //             doctorDto : {
        //                 idDoctor : '1',
        //                 firstName : 'Ioana',
        //                 lastName  : 'Jitaru',
        //                 phone     : '0761092710'
        //             },
        //             locationDto : {
        //                 idLocation : 1,
        //                 name       : 'Aident'
        //             }
        //         }
        //     },
        //     {
        //         id     : 5,
        //         title  : 'Pac. Micuta',
        //         start  : '2022-05-09T13:30',
        //         end    : '2022-05-09T14:30',
        //         extendedProps : {
        //             patientDto : {
        //                 idPatient : '3',
        //                 firstName : 'Marius',
        //                 lastName  : 'Micuta',
        //                 phone     : '0789123431'
        //             },
        //             doctorDto : {
        //                 idDoctor : '1',
        //                 firstName : 'Ioana',
        //                 lastName  : 'Jitaru',
        //                 phone     : '0761092710'
        //             },
        //             locationDto : {
        //                 idLocation : 1,
        //                 name       : 'Bolta'
        //             }
        //         }
        //     },
        //     {
        //         id     : 6,
        //         title  : 'Pac. Calin',
        //         start  : '2022-05-09T15:00',
        //         end    : '2022-05-09T17:30',
        //         extendedProps : {
        //             patientDto : {
        //                 idPatient : '3',
        //                 firstName : 'Marius',
        //                 lastName  : 'Calin',
        //                 phone     : '0789123431'
        //             },
        //             doctorDto : {
        //                 idDoctor : '1',
        //                 firstName : 'Ioana',
        //                 lastName  : 'Jitaru',
        //                 phone     : '0761092710'
        //             },
        //             locationDto : {
        //                 idLocation : 1,
        //                 name       : 'Aident'
        //             }
        //         }
        //     }
        // ]
        eventSources: [
            {
                url: '/appointment/test',
                method: 'GET',
                failure: function() {
                    alert('there was an error while fetching events!');
                }
            }
        ]
    });

    function showModalViewAppointment() {
        $('#modalViewAppoinment').modal('show');
    }
    function hideModalViewAppointment() {
        $('#modalViewAppoinment').modal('hide');
    }

    function bindClickEventOnCloseModalViewAppointment() {
        $('.closeModalViewAppointment').on('click', function() {
           //clear data in modal
           hideModalViewAppointment();
        });
    }

    function bindClickEventOnAddAppointment() {
        document.getElementById('addAppointment')
                .addEventListener('click',
                                function() {
                                            document.getElementById('rowAddAppointmentButton').style.display = 'none';
                                            document.getElementById('rowAppointmentForm').style.display = 'inline';
                                            window.scrollTo(0, document.body.scrollHeight);
                                        })
    }
    function bindClickEventOnCloseFormAppAppointment() {
        document.querySelector('.closeFormAddAppointment')
                .addEventListener('click',
                                function() {
                                            // clear form input
                                            document.getElementById('rowAppointmentForm').style.display = 'none';
                                            document.getElementById('rowAddAppointmentButton').style.display = 'inline';
                                            window.scrollTo(0, document.body.scrollHeight);
                                        })
    }

    calendar.render();
    bindClickEventOnCloseModalViewAppointment();
    bindClickEventOnAddAppointment();
    bindClickEventOnCloseFormAppAppointment();
})();