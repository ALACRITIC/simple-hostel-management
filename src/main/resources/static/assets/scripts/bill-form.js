$(function () {
    BillForm.init();
});

var BillForm = {

    errorColor: "rgb(255, 191, 191)",

    init: function () {

        var self = BillForm;

        var validButton = $("#exportValidButton");
        var clientSearch = $("#clientSeachTextField");
        var customerId = $("#customerId");

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

        var customerDatesResult = $("#customerSearchResultDates");

        customerDatesResult.empty();

        ReservationUtils.searchDatesForCustomer(customerId)
            .then(function (response) {

                console.log("response");
                console.log(response);

                $.each(response, function (index, element) {
                    var chk = $("<label>", {"class": "form-check-label"})
                        .html("&nbsp;&nbsp;" + moment(element.reservationDate).format('DD/MM/YYYY'))
                        .prepend('<input class="form-check-input" type="checkbox">', {
                            name: 'chk' + index,
                            value: element.id
                        });

                    customerDatesResult.append($("<li>").append(chk));
                });
            })
            .fail(function (error) {
                customerDatesResult.append("<div>Please enter a valid customer name</div>");
            });

    },

    exportBillHtml: function () {

        var beginDate = $("#exportBeginDate");
        var endDate = $("#exportEndDate");

        document.location = UrlTree.getExportCsvUrl() + "?begin=" + beginDate.val() + "&end=" + endDate.val();
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

