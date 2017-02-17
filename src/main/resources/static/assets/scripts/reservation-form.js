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
            onSelect: function () {
                self.setDateFieldBackground(false);
                self.checkDates();
                self.checkAvailability();
            }
        });

        endDate.datepicker({
            dateFormat: "dd/mm/yy",
            onSelect: function () {
                self.setDateFieldBackground(false);
                self.checkDates();
                self.checkAvailability();
            }
        });

        placesTxt.on('input', function () {
            self.checkAvailability();
        });

        phoneNumberTxt.on('input', function () {
            self.checkPhoneNumber();
        });

        deleteButton.click(function () {
            ReservationUtils.showDeleteReservationDialog(reservationId, token);
        });

        // first check
        self.checkAvailability();

    },

    checkPhoneNumber: function () {

        var phoneNumberTxt = $("#reservationCustomerPhonenumber");
        var warnZone = $("#reservationCustomerPhonenumberWarning");

        warnZone.empty();

        var pnumber = phoneNumberTxt.val();
        if (!pnumber) {
            console.error("Invalid phone number");
            return;
        }

        $.ajax({
            url: UrlTree.getCustomersJsonFeedUrl(),
            data: {
                phonenumber: pnumber
            }
        })
            .done(function (response) {
                if (response) {
                    var first = response.firstname;
                    var last = response.lastname;
                    var text = "Somebody already have this phone number: <b>" + first + " " + last + "</b>.&nbsp;";
                    text += "If you validate form, customer entry will be updated with new values.&nbsp;";
                    warnZone.html(text);

                    var fillLink = $("<a style='cursor: pointer'>Fill fields with existing values.</a>");
                    fillLink.click(function () {
                        $("#customerFirstname").val(first);
                        $("#customerLastname").val(last);
                    });
                    warnZone.append(fillLink);
                }
            })

            .fail(function (error) {
                console.error(error);
                warnZone.html("Unable to check if phone number already exist");
            });
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
            throw "Dates are invalid"
        }

    },

    /**
     * Check room available for specified period and change select list content
     */
    checkAvailability: function () {

        var self = ReservationForm;

        var beginDate = $("#reservationBeginDate");
        var endDate = $("#reservationEndDate");
        var roomSelect = $("#reservationSharedResourceSelect");
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

            })
            .fail(function () {
                console.error("Fail !");
                roomSelect.append("<option>Error</option>");
            });
    }


};

