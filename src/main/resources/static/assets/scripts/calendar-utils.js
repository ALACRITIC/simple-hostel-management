var CalendarUtils = {

    createReservationCalendar: function (selector) {

        var self = CalendarUtils;

        $(selector).fullCalendar({
            header: {
                left: 'prev,next today',
                center: 'title',
                right: 'month,listWeek'
            },
            defaultDate: new Date(),
            navLinks: true, // can click day/week names to navigate views
            editable: true,
            eventLimit: true, // allow "more" link when too many events
            events: function (start, end, timezone, callback) {

                var expectedFormat = "DD/MM/YYYY HH:mm";

                $.ajax({
                    url: UrlTree.getReservationCalendarFeedUrl(),
                    data: {
                        // our hypothetical feed requires UNIX timestamps
                        start: start.format(expectedFormat),
                        end: end.format(expectedFormat)
                    }
                })
                    .done(function (response) {
                        var events = self.reservationsToFullcalendarEvents(response);
                        callback(events);
                    })

                    .fail(function (error) {
                        console.error(error)
                    });
            },

            /**
             * Open reservation on event
             * @param calEvent
             * @param jsEvent
             * @param view
             */
            eventClick: function (calEvent, jsEvent, view) {
                ReservationUtils.showReservation(calEvent._reservationId);
            },

            /**
             * Create reservation on event
             * @param date
             * @param jsEvent
             * @param view
             * @param resourceObj
             */
            dayClick: function (date, jsEvent, view, resourceObj) {
                ReservationUtils.newReservation(moment(date).format('DD/MM/YYYY'));
            }
        });
    },

    /**
     * Transform distant events to full calendar events format
     * @param arrayOfEvents
     */
    reservationsToFullcalendarEvents: function (arrayOfEvents) {

        var events = [];
        $.each(arrayOfEvents, function (index, element) {

            var firstname = element.customer.firstname;
            var lastname = element.customer.lastname;
            var startDate = element.begin;
            var color = element.accommodation ? "rgb(" + element.accommodation.color + ")" : "#444444";
            var endDate = element.end;

            events.push({
                title: lastname + " " + firstname,
                start: moment(startDate),
                end: moment(endDate),
                color: color,
                _reservationId: element.id
            });
        });

        return events;

    },

    createServiceCalendar: function (selector) {

        var self = CalendarUtils;

        $(selector).fullCalendar({
            header: {
                left: 'prev,next today',
                center: 'title',
                right: 'month,listWeek'
            },
            defaultDate: new Date(),
            navLinks: true, // can click day/week names to navigate views
            editable: true,
            eventLimit: true, // allow "more" link when too many events
            events: function (start, end, timezone, callback) {

                var expectedFormat = "YYYY-MM-DD";

                $.ajax({
                    url: UrlTree.getServiceCalendarFeedUrl(),
                    data: {
                        // our hypothetical feed requires UNIX timestamps
                        start: start.format(expectedFormat),
                        end: end.format(expectedFormat)
                    }
                })
                    .done(function (response) {
                        var events = self.servicesToFullcalendarEvents(response);
                        callback(events);
                    })

                    .fail(function (error) {
                        console.error(error)
                    });
            },

            /**
             * Open reservation on event
             * @param calEvent
             * @param jsEvent
             * @param view
             */
            eventClick: function (calEvent, jsEvent, view) {
                ServiceUtils.showService(calEvent._serviceId);
            },

            /**
             * Create reservation on event
             * @param date
             * @param jsEvent
             * @param view
             * @param resourceObj
             */
            dayClick: function (date, jsEvent, view, resourceObj) {
                ServiceUtils.newService(moment(date).format('DD/MM/YYYY'));
            }
        });
    },

    servicesToFullcalendarEvents: function (arrayOfEvents) {

        var events = [];
        $.each(arrayOfEvents, function (index, element) {

            var firstname = element.customer.firstname;
            var lastname = element.customer.lastname;
            var execDate = element.executionDate;
            var color = element.serviceType ? "rgb(" + element.serviceType.color + ")" : "#444444";

            events.push({
                title: lastname + " " + firstname,
                start: moment(execDate),
                end: moment(execDate),
                color: color,
                _serviceId: element.id
            });
        });

        return events;

    },

    createAccommodationCalendar: function (selector, accommodationSelect) {

        var self = CalendarUtils;

        $(selector).fullCalendar({
            header: {
                left: 'prev,next today',
                center: 'title',
                right: 'month,listWeek'
            },
            defaultDate: new Date(),
            navLinks: true, // can click day/week names to navigate views
            editable: true,
            eventLimit: true, // allow "more" link when too many events
            events: function (start, end, timezone, callback) {

                var expectedFormat = "DD/MM/YYYY HH:mm";

                $.ajax({
                    url: UrlTree.getReservationCalendarFeedUrl(),
                    data: {
                        // our hypothetical feed requires UNIX timestamps
                        start: start.format(expectedFormat),
                        end: end.format(expectedFormat),

                        // unique id of resource
                        accommodation: $(accommodationSelect).val()
                    }
                })
                    .done(function (response) {
                        var events = self.reservationsToFullcalendarEvents(response);
                        callback(events);
                    })

                    .fail(function (error) {
                        console.error(error)
                    });
            },

            /**
             * Open reservation on event
             * @param calEvent
             * @param jsEvent
             * @param view
             */
            eventClick: function (calEvent, jsEvent, view) {
                ReservationUtils.showReservation(calEvent._reservationId);
            },

            /**
             * Create reservation on event
             * @param date
             * @param jsEvent
             * @param view
             * @param resourceObj
             */
            dayClick: function (date, jsEvent, view, resourceObj) {
                ReservationUtils.newReservation(moment(date).format('DD/MM/YYYY'), $(accommodationSelect).val());
            }
        });
    }
};


