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
            eventClick: function (calEvent, jsEvent, view) {
                ReservationUtils.showReservation(calEvent._reservationId);
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
            var endDate = element.end;

            console.log("startDate");
            console.log(startDate);

            events.push({
                title: lastname + " " + firstname,
                start: moment(startDate).add(12, 'hours'),
                end: moment(endDate).add(12, 'hours'),
                _reservationId: element.id
            });
        });

        return events;

    }
};


