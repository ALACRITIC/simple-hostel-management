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
                if (response && response[0]) {
                    var first = response[0].firstname;
                    var last = response[0].lastname;

                    // check if informations are not the same
                    if(firstNameTextField.val() !== first || lastNameTextField.val() !== last) {

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
                            warnZone.empty();
                        });
                        warnZone.append(fillLink);
                    }
                }
            })

            .fail(function (error) {
                console.error(error);
                warnZone.html("Unable to check if phone number already exist");
            });
    },

    search: function (term) {

        var defer = $.Deferred();

        if (!term) {
            throw "Invalid terms !";
           
        }

        $.ajax({
            url: UrlTree.getCustomersJsonFeedUrl(),
            data: {
                term: term
            }
        })
            .done(function (response) {
                var result = [];
                $.each(response, function (index, element) {
                    result.push(element);
                });

                defer.resolve(result);
            })

            .fail(function (error) {
                console.error(error);
                defer.reject();
            });

        return defer.promise();
    }

};

