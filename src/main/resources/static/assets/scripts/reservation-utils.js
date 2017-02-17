var ReservationUtils = {

    getRoomsAvailable: function (start, end, places) {

        if (!places) {
            places = 1;
        }

        var self = ReservationUtils;
        var result = $.Deferred();

        $.ajax({
            url: UrlTree.getResourcesAvailableFeedUrl(),
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
     * Show a confirmation dialog and delete specified reservation if needed
     * @param reservationId
     * @param token
     */
    showDeleteReservationDialog: function (reservationId, token) {

        if (!reservationId || !token) {
            throw "This method need an id and a token: id/" + reservationId + " t/" + token;
        }

        $("<div>You will delete this reservation. Are you sure ?</div>").dialog({
            resizable: false,
            height: "auto",
            width: 400,
            modal: true,
            buttons: {
                "Cancel": function () {
                    $(this).dialog("close");
                },
                "Delete": function () {

                    ReservationUtils.deleteReservation(reservationId, token);

                    $(this).dialog("close");

                }
            }
        });
    },

    /**
     * Change current location and delete reservation with specified ID
     * @param id
     */
    deleteReservation: function (reservationId, token) {
        window.location = UrlTree.getDeleteReservationUrl() + "?id=" + reservationId + "&token=" + token;
    }

};



