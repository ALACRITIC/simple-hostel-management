$(function () {
    CustomerBillForm.init();
});

var CustomerBillForm = {

    errorColor: "rgb(255, 191, 191)",

    init: function () {

        var self = CustomerBillForm;

        var validButton = $("#exportHtmlValidButton");
        var clientSearch = $("#exportHtmlClientSeachTextField");
        var customerId = $("#exportHtmlCustomerId");

        // enable auto complete on search field
        clientSearch.autocomplete({
            source: function (request, response) {

                CustomerUtils.search(request.term)
                    .then(function (result) {

                        var max = 6;
                        var autoCompleteProposition = [];
                        $.each(result, function (index, element) {
                            var label = element.firstname + " " + element.lastname + " (" + element.phonenumber + ")";
                            autoCompleteProposition.push({
                                label: label,
                                value: label,
                                __customer_id: element.id
                            });

                            if (index > max) {
                                autoCompleteProposition.push({label: "..."});
                                return false;
                            }
                        });

                        if (result.length < 1) {
                            autoCompleteProposition.push({label: "No customer found"});
                        }

                        response(autoCompleteProposition)
                    })
                    .fail(function () {
                        console.error("Fail !");
                        response(["Error !"]);
                    });
            },

            /**
             * Search for items to bill on select
             * @param event
             * @param ui
             */
            select: function (event, ui) {
                customerId.val(ui.item.__customer_id);
                self.searchItems(ui.item.__customer_id);
            }
        });

        // export on valid button click
        validButton.click(function () {
            self.exportBillHtml();
        });

        // select all non-billed items on button click
        $("#exportHtmlSelectAllDates").click(function () {
            $("#exportHtmlCheckboxesArea input[type=checkbox]").prop('checked', true);
        });

    },

    /**
     * Search for items to bill
     * @param customerId
     */
    searchItems: function (customerId) {

        var self = CustomerBillForm;

        var customerReservationsResult = $("#exportHtmlReservationsResult");
        var customerOldReservationsResult = $("#exportHtmlOldReservationsResult");
        var customerServicesResult = $("#exportHtmlServicesResult");
        var customerOldServicesResult = $("#exportHtmlOldServicesResult");

        customerReservationsResult.empty();
        customerOldReservationsResult.empty();

        customerServicesResult.empty();
        customerOldServicesResult.empty();

        // search for non-billed reservations
        ReservationUtils.searchForCustomer(customerId, false)
            .then(function (response) {
                self.displayReservationResults(response, customerReservationsResult);
            })
            .fail(function (error) {
                console.error(error);
                customerReservationsResult.append("<div>Error, please enter a valid customer name</div>");
            });

        // search for billed reservations
        ReservationUtils.searchForCustomer(customerId, true)
            .then(function (response) {
                self.displayReservationResults(response, customerOldReservationsResult);
            })
            .fail(function (error) {
                console.error(error);
                customerReservationsResult.append("<div>Error, please enter a valid customer name</div>");
            });

        // search for services
        ServiceUtils.searchForCustomer(customerId, false)
            .then(function (response) {
                self.displayServicesResults(response, customerServicesResult);
            })
            .fail(function (error) {
                console.error(error);
                customerServicesResult.append("<div>Error, please enter a valid customer name</div>");
            });

        // search for services
        ServiceUtils.searchForCustomer(customerId, true)
            .then(function (response) {
                self.displayServicesResults(response, customerOldServicesResult);
            })
            .fail(function (error) {
                console.error(error);
                customerServicesResult.append("<div>Error, please enter a valid customer name</div>");
            });

    },

    displayReservationResults: function (results, target) {

        var table = $("<table class='table table-striped'>");

        var thead = $("<thead>");
        thead.append("<tr><td colspan='10' style='font-weight: bolder'>Reservations</td></tr>");
        thead.append("<tr>"
            + "<th></th>"
            + "<th>Begin</th>"
            + "<th>End</th>"
            + "<th>Accommodation</th>"
            + "<th>Price</th>"
            + "</tr>");
        table.append(thead);

        var tbody = $('<tbody>');
        $.each(results, function (index, element) {

            var tr = $("<tr>");
            tr.append('<td><input class="form-check-input" type="checkbox" '
                + 'name="reservationsToExport" value="' + element.id + '"/></td>');
            tr.append('<td>' + moment(element.begin).format('DD/MM/YYYY') + '</td>');
            tr.append('<td>' + moment(element.end).format('DD/MM/YYYY') + '</td>');
            tr.append('<td>' + element.accommodation.name + '</td>');
            tr.append('<td>' + element.totalPrice + '</td>');

            tbody.append(tr);
        });

        if (results.length < 1) {
            tbody.append("<tr><td colspan='10'>No reservations found</td></tr>")
        }

        table.append(tbody);
        target.append(table);
    },

    displayServicesResults: function (results, target) {

        var table = $("<table class='table table-striped'>");

        var thead = $("<thead>");
        thead.append("<tr><td colspan='10' style='font-weight: bolder'>Services</td></tr>");
        thead.append("<tr>"
            + "<th></th>"
            + "<th>Purchase date</th>"
            + "<th>Name</th>"
            + "<th>Price</th>"
            + "</tr>");
        table.append(thead);

        var tbody = $('<tbody>');
        $.each(results, function (index, element) {

            var tr = $("<tr>");
            tr.append('<td><input class="form-check-input" type="checkbox" '
                + 'name="servicesToExport" value="' + element.id + '"/></td>');
            tr.append('<td>' + moment(element.purchaseDate).format('DD/MM/YYYY') + '</td>');
            tr.append('<td>' + element.serviceType.name + '</td>');
            tr.append('<td>' + element.totalPrice + '</td>');

            tbody.append(tr);
        });

        if (results.length < 1) {
            tbody.append("<tr><td colspan='10'>No services found</td></tr>")
        }

        table.append(tbody);
        target.append(table);
    },

    exportBillHtml: function () {

        var errorMessage = "<div>Please select a customer, and then select dates for billing.</div>";
        var datesWarning = $("#exportHtmlDatesWarning");
        var days = $("input[name=reservationsToExport]");

        datesWarning.empty();

        // check if dates are selected
        if (days.length < 1) {
            datesWarning.append(errorMessage);
            return;
        }

        var oneIsChecked = false;
        $.each(days, function (index, element) {
            if ($(this).prop("checked")) {
                oneIsChecked = true;
                return false;
            }
        });

        if (oneIsChecked != true) {
            datesWarning.append(errorMessage);
            return;
        }

        // send form if all is okay
        $("#exportHtmlForm").submit();
    }

};

