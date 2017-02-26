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
        var selectAccommodation = $("#reservationAccommodationSelect");
        var checkPriceButton = $("#reservationCheckTotalPriceButton");

        // transform fields in date picker
        beginDate.datepicker({
            dateFormat: "dd/mm/yy",
            onSelect: function (date) {
                beginDate.val(date + " 16:00");
                self.setDateFieldBackground(false);
                self.checkDates();
                self.checkAvailability(function () {
                    self.computeTotalPrice();
                });

            }
        });

        endDate.datepicker({
            dateFormat: "dd/mm/yy",
            onSelect: function (date) {
                endDate.val(date + " 10:00");
                self.setDateFieldBackground(false);
                self.checkDates();
                self.checkAvailability(function () {
                    self.computeTotalPrice();
                });
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

        checkPriceButton.click(function () {
            self.computeTotalPrice();
        });

        // special checkbox
        $("#reservationIsPaid").checkboxradio();

        // check availability on click
        $("#checkAvailabilityButton").click(function () {
            self.checkAvailability();
            self.computeTotalPrice();
        });

        selectAccommodation.change(function () {
            self.computeTotalPrice();
        });

        // first check
        self.checkAvailability(function(){
            // Do not compute price here
            //self.computeTotalPrice();
        });

    },

    computeTotalPrice: function () {

        var totalPriceTxt = $("#reservationTotalPriceTxt");
        var totalPriceLabel = $("#reservationTotalPriceLabel");
        var beginDate = $("#reservationBeginDate");
        var endDate = $("#reservationEndDate");
        var selectAccommodation = $("#reservationAccommodationSelect");

        totalPriceTxt.val("");
        totalPriceLabel.html("");

        // here we divide be 18 instead of 24 because checkin is at 1600 and checkout at 1000
        var nights = Math.floor(moment(endDate.val(), "DD/MM/YYYY hh:mm")
                .diff(moment(beginDate.val(), "DD/MM/YYYY hh:mm"), 'hours') / 18);
        var pricePerDay = selectAccommodation.find(":selected").attr("data-price");

        if (!pricePerDay) {
            throw "Invalid price";
        }

        var totalPrice = nights * pricePerDay;
        var roundedPrice = Math.round(totalPrice * 100) / 100;
        totalPriceTxt.val(roundedPrice);
        totalPriceLabel.html(nights + " nights, " + pricePerDay + " per night");

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

        var bd = moment(beginDate.val(), "DD/MM/YYYY HH:mm");
        var ed = moment(endDate.val(), "DD/MM/YYYY HH:mm");

        // check if dates are in order
        if (bd._isValid == false || ed._isValid == false || bd.isAfter(ed)) {
            self.setDateFieldBackground(true);
            console.error("Dates are invalid");
        }

    },

    /**
     * Check room available for specified period and change select list content
     */
    checkAvailability: function (onSuccess) {

        var self = ReservationForm;

        var beginDate = $("#reservationBeginDate");
        var endDate = $("#reservationEndDate");
        var accommSelect = $("#reservationAccommodationSelect");
        var placesTxt = $("#reservationPlaces");

        var checkin = beginDate.val();
        var checkout = endDate.val();
        var places = placesTxt.val();

        if (!checkin || !checkout || !places) {
            console.error("Invalid reservation form: " + checkin + " / " + checkout + " / " + places);
            return;
        }

        accommSelect.empty();

        ReservationUtils.getRoomsAvailable(checkin, checkout, places)
            .then(function (result) {

                $.each(result, function (index, element) {

                    var elmt = $("<option/>");
                    elmt.text(element.name);
                    elmt.attr("data-price", element.pricePerDay);
                    elmt.attr("value", element.id);

                    accommSelect.append(elmt);
                });

                if (!result || result.length == 0) {
                    accommSelect.append("<option>No accommodation available</option>");
                }

                // add primary selected option if needed
                var pval = accommSelect.attr("data-primary-value");
                var plab = accommSelect.attr("data-primary-name");
                var pprice = accommSelect.attr("data-primary-price");
                if (pval) {
                    if (accommSelect.children("option[id=" + pval + "]").length < 1) {
                        var elmt = $("<option/>");
                        elmt.text(plab);
                        elmt.attr("value", pval);
                        elmt.attr("data-price", pprice);
                        accommSelect.prepend(elmt);
                    }
                    accommSelect.val(pval);
                }

                if (onSuccess) {
                    try {
                        onSuccess();
                    } catch (e) {
                        console.error(e);
                    }
                }

            })
            .fail(function () {
                console.error("Fail !");
                accommSelect.append("<option>Error</option>");
            });
    }


};

