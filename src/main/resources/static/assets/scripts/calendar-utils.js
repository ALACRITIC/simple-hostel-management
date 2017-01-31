var CalendarUtils = {

    FEED_URL: "/reservation/json/get",

    createCalendar: function (selector) {

        var self = CalendarUtils;

        $(selector).fullCalendar({
            header: {
                left: 'prev,next today',
                center: 'title',
                right: 'month,agendaWeek,agendaDay,listWeek'
            },
            defaultDate: new Date(),
            navLinks: true, // can click day/week names to navigate views
            editable: true,
            eventLimit: true, // allow "more" link when too many events
            events: function (start, end, timezone, callback) {

                var expectedFormat = "YYYY-MM-DD";

                $.ajax({
                    url: self.FEED_URL,
                    data: {
                        // our hypothetical feed requires UNIX timestamps
                        start: start.format(expectedFormat),
                        end: end.format(expectedFormat)
                    },
                    success: function (response) {
                        var events = self.distantEventsToFullcalendarEvents(response);
                        callback(events);
                    }
                });
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

            console.log(element);
            console.log(index);

            var firstname = element.customer.firstname;
            var lastname = element.customer.lastname;
            var startDate = element.arrival;
            var endDate = element.departure;

            events.push({
                title: lastname + " " + firstname,
                start: startDate,
                start: endDate
            });
        });

        return events;

    }
};


