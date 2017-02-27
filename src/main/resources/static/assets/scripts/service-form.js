$(function () {
    ServiceForm.init();
});

var ServiceForm = {

    init: function () {

        var self = ServiceForm;

        var deleteButton = $("#serviceDeleteButton");
        var serviceType = $("#serviceType");
        var totalPrice = $("#totalPrice");
        var serviceId = $("#serviceId");
        var token = $("#serviceToken").val();
        var phoneNumberTxt = $("#serviceCustomerPhonenumber");
        var execDate = $("#serviceExecutionDate");
        var firstNameTxt = $("#serviceCustomerFirstname");
        var lastNameTxt = $("#serviceCustomerLastname");
        var customerIdField = $("#customerId");
        var cancelButton = $("#serviceCancelButton");

        cancelButton.click(function () {
            window.location = UrlTree.getMainMenuUrl();
        });

        deleteButton.click(function () {
            ServiceUtils.showDeleteServiceDialog(serviceId.val(), token);
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
            onSelect: function (date) {
                execDate.val(date + " 10:00");
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
                customerIdField.val(customer.id);
            }
        });

        serviceType.change(function () {
            self.updatePrice();
        });

        self.updatePrice();
    },

    updatePrice: function () {

        var serviceType = $("#serviceType");
        var totalPrice = $("#totalPrice");

        totalPrice.val(serviceType.find(":selected").data("servicePrice"));

    }

};

