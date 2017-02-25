
$(function () {
    AccommodationForm.init();
});

var AccommodationForm = {

    errorColor: "rgb(255, 191, 191)",

    init: function () {

        var self = AccommodationForm;

        var deleteButton = $("#accommodationDeleteButton");
        var accommodationId = $("#accommodationId");
        var token = $("#accommodationToken");

        deleteButton.click(function(){
            AccommodationUtils.showDeleteAccommodationDialog(accommodationId.val(), token.val());
        });

    }

};

