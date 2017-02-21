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

                var expectedFormat = "YYYY-MM-DD";

                $.ajax({
                    url: UrlTree.getCalendarFeedUrl(),
                    data: {
                        // our hypothetical feed requires UNIX timestamps
                        start: start.format(expectedFormat),
                        end: end.format(expectedFormat)
                    }
                })
                    .done(function (response) {
                        var events = self.distantEventsToFullcalendarEvents(response);
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
                console.log(date);
                ReservationUtils.newReservation(moment(date).format('DD/MM/YYYY'));
            }
        });
    },

    /**
     * Transform distant events to full calendar events format
     * @param arrayOfEvents
     */
    distantEventsToFullcalendarEvents: function (arrayOfEvents) {

        var events = [];
        $.each(arrayOfEvents, function (index, element) {

            var firstname = element.customer.firstname;
            var lastname = element.customer.lastname;
            var startDate = element.begin;
            var color = element.resource ? "rgb(" + element.resource.color + ")" : "#444444";
            var endDate = element.end;

            events.push({
                title: lastname + " " + firstname,
                start: moment(startDate).add(12, 'hours'),
                end: moment(endDate).add(12, 'hours'),
                color: color,
                _reservationId: element.id
            });
        });

        return events;

    }
};


