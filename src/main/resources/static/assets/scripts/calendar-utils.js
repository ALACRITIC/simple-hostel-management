var CalendarUtils = {

    // fill me first !
    FEED_URL: null,

    setFeedUrl: function (url) {
        CalendarUtils.FEED_URL = url;
    },

    createCalendar: function (selector) {

        var self = CalendarUtils;

        if (!self.FEED_URL) {
            throw "Calendar feed url is null";
        }

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


