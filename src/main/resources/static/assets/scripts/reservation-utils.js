var ReservationUtils = {

    getRoomsAvailable: function (start, end, places) {

        if (!places) {
            places = 1;
        }

        var self = ReservationUtils;
        var result = $.Deferred();

        $.ajax({
            url: UrlTree.getRoomsAvailableFeedUrl(),
            data: {
                start: start,
                end: end,
                places: places
            }
        }).done(function (response) {
            result.resolve(response);
        }).fail(function (error) {
            console.error(error);
            result.reject();
        });

        return result.promise();
    },

    /**
     * Change current location and show reservation with specified ID
     * @param id
     */
    showReservation: function (reservationId) {
        window.location = UrlTree.getShowReservationUrl() + "?id=" + reservationId;
    },
    
    /**
     * Change current location and delete reservation with specified ID
     * @param id
     */
    deleteReservation: function (reservationId, token) {
        window.location = UrlTree.getDeleteReservationUrl() + "?id=" + reservationId + "&token=" + token;
    }

};



