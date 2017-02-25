var AccommodationUtils = {

    /**
     * Show a confirmation dialog and delete specified accommodation if needed
     * @param reservationId
     * @param token
     */
    showDeleteAccommodationDialog: function (accommodationId, token) {

        if (!accommodationId || !token) {
            throw "This method need an id and a token: id/" + accommodationId + " t/" + token;
        }

        $("<div>You will delete this accommodation. Are you sure ?</div>").dialog({
            resizable: false,
            height: "auto",
            width: 400,
            modal: true,
            buttons: {
                "Cancel": function () {
                    $(this).dialog("close");
                },
                "Delete": function () {

                    AccommodationUtils.deleteAccommodation(accommodationId, token);

                    $(this).dialog("close");

                }
            }
        });
    },

    /**
     * Change current location and delete reservation with specified ID
     * @param id
     */
    deleteAccommodation: function (accommodationId, token) {
        window.location = UrlTree.getDeleteAccommodationUrl() + "?id=" + accommodationId + "&token=" + token;
    }

};



