var ServiceUtils = {

    showDeleteServiceTypeDialog: function (serviceId, token) {

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

                    ServiceUtils.deleteServiceType(serviceId, token);

                    $(this).dialog("close");

                }
            }
        });
    },

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

    searchForCustomer: function (customerId) {

        var defer = $.Deferred();

        if (!customerId) {
            throw "Customer id is null";
        }

        $.ajax({
            url: UrlTree.getServiceSearchUrl(),
            data: {
                customerId: customerId
            }
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

    },

    deleteServiceType: function (serviceId, token) {
        window.location = UrlTree.getDeleteServiceTypeUrl() + "?id=" + serviceId + "&token=" + token;
    },

    deleteService: function (serviceId, token) {
        window.location = UrlTree.getDeleteServiceUrl() + "?id=" + serviceId + "&token=" + token;
    },

    newService: function (date) {
        window.location = UrlTree.getShowServiceUrl() + "?date=" + date;
    },

    showService: function (serviceId) {
        window.location = UrlTree.getShowServiceUrl() + "?id=" + serviceId;
    }

};



