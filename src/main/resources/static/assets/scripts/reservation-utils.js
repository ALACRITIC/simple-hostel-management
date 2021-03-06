var ReservationUtils = {

    getRoomsAvailable: function (start, end, places) {

        if (!places) {
            places = 1;
        }

        var self = ReservationUtils;
        var result = $.Deferred();

        $.ajax({
            url: UrlTree.getAccommodationsAvailableFeedUrl(),
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


    newReservation: function (date, accommodation) {

        if (!date) {
            throw "Date is mandatory"
        }

        var url = UrlTree.getShowReservationUrl() + "?date=" + date;
        if (accommodation) {
            url += "&accommodation=" + accommodation;
        }

        window.location = url;
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
    },

    searchForCustomer: function (customerId, paid, orderAscent) {

        var defer = $.Deferred();

        if (!customerId) {
            throw "Customer id is null";
        }

        var data = {customerId: customerId};
        if (typeof paid !== "undefined") {
            data.paid = paid;
        }
        if (typeof orderAscent !== "undefined") {
            data.ascent = orderAscent;
        }

        $.ajax({
            url: UrlTree.getReservationSearchUrl(),
            data: data
        })
            .done(function (response) {
                var result = [];
                $.each(response, function (index, element) {
                    result.push(element);
                });

                defer.resolve(result);
            })

            .fail(function (error) {
                console.error(error);
                defer.reject();
            });

        return defer.promise();

    }

};



