
$(function () {
    ResourceForm.init();
});

var ResourceForm = {

    errorColor: "rgb(255, 191, 191)",

    init: function () {

        var self = ResourceForm;

        var deleteButton = $("#resourceDeleteButton");
        var resourceId = $("#resourceId").val();
        var token = $("#resourceToken").val();

        deleteButton.click(function(){
            ResourceUtils.showDeleteResourceDialog(resourceId, token);
        });

    }

};

