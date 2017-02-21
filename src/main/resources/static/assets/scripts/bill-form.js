$(function () {
    BillForm.init();
});

var BillForm = {

    errorColor: "rgb(255, 191, 191)",

    init: function () {

        var self = BillForm;

        var validButton = $("#exportHtmlValidButton");
        var clientSearch = $("#exportHtmlClientSeachTextField");
        var customerId = $("#exportHtmlCustomerId");

        clientSearch.autocomplete({
            //source: [ "c++", "java", "php", "coldfusion", "javascript", "asp", "ruby" ]
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
            select: function (event, ui) {
                customerId.val(ui.item.__customer_id);
                self.searchDates(ui.item.__customer_id);
            }
        });

        validButton.click(function () {
            self.exportBillHtml();
        });

    },

    searchDates: function (customerId) {

        var customerDatesResult = $("#exportHtmlDatesSearchResult");

        customerDatesResult.empty();

        // search or reservations
        ReservationUtils.searchForCustomer(customerId)
            .then(function (response) {

                customerDatesResult.append($("<div>").html("Reservations"));

                $.each(response, function (index, element) {

                    var input = $('<input class="form-check-input" type="checkbox" name="reservationsToExport" value="' + element.id + '"/>');
                    var label = $('<label class="form-check-label"></label>')
                        .html("&nbsp;&nbsp;" + moment(element.reservationDate).format('DD/MM/YYYY'));

                    customerDatesResult.append($("<div>").append(label.prepend(input)));
                });
            })
            .fail(function (error) {
                customerDatesResult.append("<div>Please enter a valid customer name</div>");
            });

        // TODO search and add services too

    },

    exportBillHtml: function () {

        var errorMessage = "<div>Please select a customer, and then select dates for billing.</div>";
        var datesWarning = $("#exportHtmldatesWarning");
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
    },

    /**
     * Change background of date field
     * @param error
     */
    setDateFieldBackground: function (error) {

        var self = BillForm;

        var beginDate = $("#exportBeginDate");
        var endDate = $("#exportEndDate");

        var defaultColor = $(beginDate).css('background');

        endDate.css('background', error ? self.errorColor : defaultColor);
        beginDate.css('background', error ? self.errorColor : defaultColor);
    },

    /**
     * Check if selected dates are valid or change background
     */
    checkDates: function () {

        var self = BillForm;

        var beginDate = $("#exportBeginDate");
        var endDate = $("#exportEndDate");

        var bd = moment(beginDate.val(), "DD/MM/YYYY");
        var ed = moment(endDate.val(), "DD/MM/YYYY");

        // check if dates are in order
        if (bd._isValid == false || ed._isValid == false || bd.isAfter(ed)) {
            self.setDateFieldBackground(true);
            throw "Dates are invalid"
        }

    }

};

