$(function () {
    AdministrationPage.init();
});

var AdministrationPage = {

    errorColor: "rgb(255, 191, 191)",

    init: function () {

        var self = AdministrationPage;

        var beginDate = $("#exportBeginDate");
        var endDate = $("#exportEndDate");
        var validButton = $("#exportValidButton");
        
        // transform fields in date picker
        beginDate.datepicker({
            dateFormat: "dd/mm/yy",
            onSelect: function () {
                self.setDateFieldBackground(false);
                self.checkDates();
            }
        });

        endDate.datepicker({
            dateFormat: "dd/mm/yy",
            onSelect: function () {
                self.setDateFieldBackground(false);
                self.checkDates();
            }
        });

        validButton.click(function () {
            self.exportCsv();
        });

    },

    exportCsv: function () {

        var beginDate = $("#exportBeginDate");
        var endDate = $("#exportEndDate");

        document.location = UrlTree.getExportCsvUrl() + "?begin=" + beginDate.val() + "&end=" + endDate.val();
    },

    /**
     * Change background of date field
     * @param error
     */
    setDateFieldBackground: function (error) {

        var self = AdministrationPage;

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

        var self = AdministrationPage;

        var beginDate = $("#exportBeginDate");
        var endDate = $("#exportEndDate");

        var bd = moment(beginDate.val(), "DD/MM/YYYY");
        var ed = moment(endDate.val(), "DD/MM/YYYY");

        // check if dates are in order
        if (bd._isValid == false || ed._isValid == false || bd.isAfter(ed)) {
            self.setDateFieldBackground(true);
            console.error("Dates are invalid");
        }

    }

};

