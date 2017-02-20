
$(function () {
    ServiceForm.init();
});

var ServiceForm = {

    init: function () {

        var self = ServiceForm;

        var deleteButton = $("#serviceDeleteButton");
        var resourceId = $("#serviceId").val();
        var token = $("#serviceToken").val();
        var phoneNumberTxt = $("#serviceCustomerPhonenumber");
        var execDate = $("#serviceExecutionDate");

        deleteButton.click(function(){
            ServiceUtils.showDeleteServiceDialog(resourceId, token);
        });

        phoneNumberTxt.on('input', function () {
            CustomerUtils.checkPhoneNumber(
                $("#serviceCustomerPhonenumber"),
                $("#serviceCustomerPhonenumberWarning"),
                $("#serviceCustomerFirstname"),
                $("#serviceCustomerLastname")
            );
        });

        execDate.datepicker({
            dateFormat: "dd/mm/yy",
            onSelect: function () {
                
            }
        });
        
        // special checkbox
        $("#serviceIsPaid").checkboxradio();
        $("#serviceIsScheduled").checkboxradio();

    }

};

