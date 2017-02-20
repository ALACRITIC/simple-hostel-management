
$(function () {
    ServiceTypeForm.init();
});

var ServiceTypeForm = {

    init: function () {

        var self = ServiceTypeForm;

        var deleteButton = $("#serviceTypeDeleteButton");
        var resourceId = $("#serviceTypeId").val();
        var token = $("#serviceTypeToken").val();

        deleteButton.click(function(){
            ServiceUtils.showDeleteServiceTypeDialog(resourceId, token);
        });

    }

};

