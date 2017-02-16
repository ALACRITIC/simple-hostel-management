
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

        deleteButton.click(function(){
            
            $("<div>You will delete this reservation. Are you sure ?</div>").dialog({
                resizable: false,
                height: "auto",
                width: 400,
                modal: true,
                buttons: {
                    "Cancel": function() {
                        $( this ).dialog( "close" );
                    },
                    "Delete": function() {

                        ReservationUtils.deleteReservation(reservationId, token);
                        
                        $(this).dialog( "close" );
                        
                    }
                }
            });
            
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

        var arrival = beginDate.val();
        var departure = endDate.val();
        var places = placesTxt.val();

        if(!arrival || !departure || !places){
            console.error("Invalid reservation form: " + arrival + " / " + departure + " / " + places);
            return;
        }

        roomSelect.empty();

        ReservationUtils.getRoomsAvailable(arrival, departure, places)
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

