$(function () {
    ReservationForm.init();
});

var ReservationForm = {

    errorColor: "rgb(255, 191, 191)",

    init: function () {

        var self = ReservationForm;

        var beginDate = $("#reservationBeginDate");
        var endDate = $("#reservationEndDate");
        var placesTxt = $("#reservationPlaces");
        var phoneNumberTxt = $("#reservationCustomerPhonenumber");
        var deleteButton = $("#reservationDeleteButton");

        var reservationId = $("#reservationId").val();
        var token = $("#reservationToken").val();

        // transform fields in date picker
        beginDate.datepicker({
            dateFormat: "dd/mm/yy",
            onSelect: function (date) {
                beginDate.val(date + " 16:00");
                self.setDateFieldBackground(false);
                self.checkDates();
                self.checkAvailability();
            }
        });

        endDate.datepicker({
            dateFormat: "dd/mm/yy",
            onSelect: function (date) {
                endDate.val(date + " 10:00");
                self.setDateFieldBackground(false);
                self.checkDates();
                self.checkAvailability();
            }
        });

        placesTxt.on('input', function () {
            self.checkAvailability();
        });

        phoneNumberTxt.on('input', function () {
            CustomerUtils.checkPhoneNumber(
                $("#reservationCustomerPhonenumber"),
                $("#reservationCustomerPhonenumberWarning"),
                $("#customerFirstname"),
                $("#customerLastname")
            );
        });

        deleteButton.click(function () {
            ReservationUtils.showDeleteReservationDialog(reservationId, token);
        });

        // special checkbox
        $("#reservationIsPaid").checkboxradio();

        // check availability on click
        $("#checkAvailabilityButton").click(function () {
            self.checkAvailability();
        });

        // first check
        self.checkAvailability();
        
    },

    /**
     * Change background of date field
     * @param error
     */
    setDateFieldBackground: function (error) {

        var self = ReservationForm;

        var beginDate = $("#reservationBeginDate");
        var endDate = $("#reservationEndDate");

        var defaultColor = $(beginDate).css('background');

        endDate.css('background', error ? self.errorColor : defaultColor);
        beginDate.css('background', error ? self.errorColor : defaultColor);
    },

    /**
     * Check if selected dates are valid or change background
     */
    checkDates: function () {

        var self = ReservationForm;

        var beginDate = $("#reservationBeginDate");
        var endDate = $("#reservationEndDate");

        var bd = moment(beginDate.val(), "DD/MM/YYYY");
        var ed = moment(endDate.val(), "DD/MM/YYYY");

        // check if dates are in order
        if (bd._isValid == false || ed._isValid == false || bd.isAfter(ed)) {
            self.setDateFieldBackground(true);
            console.error("Dates are invalid");
        }

    },

    /**
     * Check room available for specified period and change select list content
     */
    checkAvailability: function () {

        var self = ReservationForm;

        var beginDate = $("#reservationBeginDate");
        var endDate = $("#reservationEndDate");
        var roomSelect = $("#reservationAccommodationSelect");
        var placesTxt = $("#reservationPlaces");

        var checkin = beginDate.val();
        var checkout = endDate.val();
        var places = placesTxt.val();

        if (!checkin || !checkout || !places) {
            console.error("Invalid reservation form: " + checkin + " / " + checkout + " / " + places);
            return;
        }

        roomSelect.empty();

        ReservationUtils.getRoomsAvailable(checkin, checkout, places)
            .then(function (result) {

                $.each(result, function (index, element) {

                    var elmt = $("<option/>");
                    elmt.text(element.name);
                    elmt.attr("value", element.id);

                    roomSelect.append(elmt);
                });

                if (!result || result.length == 0) {
                    roomSelect.append("<option>No room available</option>");
                }

                var pval = roomSelect.attr("data-primary-value");
                var plab = roomSelect.attr("data-primary-name");
                if (pval) {

                    // add option if needed
                    if(roomSelect.children("option[id=" + pval + "]").length < 1){
                        var elmt = $("<option/>");
                        elmt.text(plab);
                        elmt.attr("value", pval);
                        roomSelect.prepend(elmt);
                    }

                    roomSelect.val(pval);
                }

            })
            .fail(function () {
                console.error("Fail !");
                roomSelect.append("<option>Error</option>");
            });
    }


};

