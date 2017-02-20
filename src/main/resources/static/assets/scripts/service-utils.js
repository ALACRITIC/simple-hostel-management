var ServiceUtils = {
    
    showDeleteServiceDialog: function (serviceId, token) {

        if (!serviceId || !token) {
            throw "This method need an id and a token: id/" + serviceId + " t/" + token;
        }

        $("<div>You will delete this service. Are you sure ?</div>").dialog({
            resizable: false,
            height: "auto",
            width: 400,
            modal: true,
            buttons: {
                "Cancel": function () {
                    $(this).dialog("close");
                },
                "Delete": function () {

                    ServiceUtils.deleteService(serviceId, token);

                    $(this).dialog("close");

                }
            }
        });
    },
    
    deleteService: function (serviceId, token) {
        window.location = UrlTree.getDeleteServiceUrl() + "?id=" + serviceId + "&token=" + token;
    }

};


