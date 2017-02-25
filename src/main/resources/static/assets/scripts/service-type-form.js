
$(function () {
    ServiceTypeForm.init();
});

var ServiceTypeForm = {

    init: function () {

        var self = ServiceTypeForm;

        var deleteButton = $("#serviceTypeDeleteButton");
        var serviceId = $("#serviceTypeId");
        var token = $("#serviceTypeToken");

        deleteButton.click(function(){
            ServiceUtils.showDeleteServiceTypeDialog(serviceId.val(), token.val());
        });

    }

};

