var ResourceUtils = {

    /**
     * Show a confirmation dialog and delete specified resource if needed
     * @param reservationId
     * @param token
     */
    showDeleteResourceDialog: function (resourceId, token) {

        if (!resourceId || !token) {
            throw "This method need an id and a token: id/" + resourceId + " t/" + token;
        }

        $("<div>You will delete this resource. Are you sure ?</div>").dialog({
            resizable: false,
            height: "auto",
            width: 400,
            modal: true,
            buttons: {
                "Cancel": function () {
                    $(this).dialog("close");
                },
                "Delete": function () {

                    ResourceUtils.deleteResource(resourceId, token);

                    $(this).dialog("close");

                }
            }
        });
    },

    /**
     * Change current location and delete reservation with specified ID
     * @param id
     */
    deleteResource: function (resourceId, token) {
        window.location = UrlTree.getDeleteResourceUrl() + "?id=" + resourceId + "&token=" + token;
    }

};



