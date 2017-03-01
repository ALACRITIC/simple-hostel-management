$(function () {
    AdministrationPage.init();
});

var AdministrationPage = {

    errorColor: "rgb(255, 191, 191)",

    init: function () {

        var self = AdministrationPage;

        var reservationBeginDate = $("#exportReservationBeginDate");
        var reservationEndDate = $("#exportReservationEndDate");
        var reservationValidButton = $("#exportReservationValidButton");
        var changeLangButton = $("#changeLangButton");
        var changeLangSelect = $("#changeLangSelect");

        // transform fields in date picker
        reservationBeginDate.datepicker({
            dateFormat: "dd/mm/yy",
            onSelect: function () {
                self.setDateFieldBackground(false, reservationBeginDate, reservationEndDate);
                self.checkDates(reservationBeginDate, reservationEndDate);
            }
        });

        reservationEndDate.datepicker({
            dateFormat: "dd/mm/yy",
            onSelect: function () {
                self.setDateFieldBackground(false, reservationBeginDate, reservationEndDate);
                self.checkDates(reservationBeginDate, reservationEndDate);
            }
        });

        reservationValidButton.click(function () {
            self.exportReservationsCsv();
        });

        var serviceBeginDate = $("#exportServiceBeginDate");
        var serviceEndDate = $("#exportServiceEndDate");
        var serviceValidButton = $("#exportServiceValidButton");

        // transform fields in date picker
        serviceBeginDate.datepicker({
            dateFormat: "dd/mm/yy",
            onSelect: function () {
                self.setDateFieldBackground(false, serviceBeginDate, serviceEndDate);
                self.checkDates(serviceBeginDate, serviceEndDate);
            }
        });

        serviceEndDate.datepicker({
            dateFormat: "dd/mm/yy",
            onSelect: function () {
                self.setDateFieldBackground(false, serviceBeginDate, serviceEndDate);
                self.checkDates(serviceBeginDate, serviceEndDate);
            }
        });

        serviceValidButton.click(function () {
            self.exportServicesCsv();
        });

        changeLangButton.click(function () {
            window.location = UrlTree.getAdminPageUrl() + "?lang=" + changeLangSelect.val();
        });

    },

    exportReservationsCsv: function () {

        var beginDate = $("#exportReservationBeginDate");
        var endDate = $("#exportReservationEndDate");

        document.location = UrlTree.getExportReservationCsvUrl() + "?begin=" + beginDate.val() + "&end=" + endDate.val();
    },

    exportServicesCsv: function () {

        var beginDate = $("#exportServiceBeginDate");
        var endDate = $("#exportServiceEndDate");

        document.location = UrlTree.getExportServiceCsvUrl() + "?begin=" + beginDate.val() + "&end=" + endDate.val();
    },

    /**
     * Change background of date field
     * @param error
     */
    setDateFieldBackground: function (error, beginDate, endDate) {

        var self = AdministrationPage;

        var defaultColor = $(beginDate).css('background');

        endDate.css('background', error ? self.errorColor : defaultColor);
        beginDate.css('background', error ? self.errorColor : defaultColor);
    },

    /**
     * Check if selected dates are valid or change background
     */
    checkDates: function (beginDate, endDate) {

        var self = AdministrationPage;

        var bd = moment(beginDate.val(), "DD/MM/YYYY");
        var ed = moment(endDate.val(), "DD/MM/YYYY");

        // check if dates are in order
        if (bd._isValid == false || ed._isValid == false || bd.isAfter(ed)) {
            self.setDateFieldBackground(true, beginDate, endDate);
            console.error("Dates are invalid");
        }

    }

};

