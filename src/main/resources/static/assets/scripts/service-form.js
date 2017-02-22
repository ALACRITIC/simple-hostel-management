
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
        var firstNameTxt = $("#serviceCustomerFirstname");
        var lastNameTxt = $("#serviceCustomerLastname");

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

        firstNameTxt.autocomplete({
            source: function (request, response) {

                CustomerUtils.search(request.term)
                    .then(function (result) {

                        var max = 6;
                        var autoCompleteProposition = [];
                        $.each(result, function (index, element) {
                            var label = element.firstname + " " + element.lastname + " (" + element.phonenumber + ")";
                            autoCompleteProposition.push({
                                label: label,
                                value: element.firstname,
                                __customer: element
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
                var customer = ui.item.__customer;
                lastNameTxt.val(customer.lastname);
                phoneNumberTxt.val(customer.phonenumber);
            }
        });

    }

};

