var CustomerUtils = {

    errorColor: "rgb(255, 191, 191)",

    checkPhoneNumber: function (phoneTextField, warnZone, firstNameTextField, lastNameTextField) {

        warnZone.empty();

        var pnumber = phoneTextField.val();
        if (!pnumber) {
            console.error("Invalid phone number");
            return;
        }

        $.ajax({
            url: UrlTree.getCustomersJsonFeedUrl(),
            data: {
                phonenumber: pnumber
            }
        })
            .done(function (response) {
                if (response) {
                    var first = response.firstname;
                    var last = response.lastname;
                    var text = "Somebody already have this phone number: <b>" + first + " " + last + "</b>.&nbsp;";
                    text += "If you validate form, customer entry will be updated with new values.&nbsp;";
                    warnZone.html(text);

                    var fillLink = $("<a style='cursor: pointer'>Fill fields with existing values.</a>");
                    fillLink.click(function () {
                        if (firstNameTextField) {
                            firstNameTextField.val(first);
                        }
                        if (lastNameTextField) {
                            lastNameTextField.val(last);
                        }
                    });
                    warnZone.append(fillLink);
                }
            })

            .fail(function (error) {
                console.error(error);
                warnZone.html("Unable to check if phone number already exist");
            });
    }

};

