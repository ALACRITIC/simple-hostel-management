
$(function () {
    ServiceForm.init();
});

var ServiceForm = {

    init: function () {

        var self = ServiceForm;

        var deleteButton = $("#serviceDeleteButton");
        var resourceId = $("#serviceId").val();
        var token = $("#serviceToken").val();

        deleteButton.click(function(){
            ServiceUtils.showDeleteServiceTypeDialog(resourceId, token);
        });

    }

};

