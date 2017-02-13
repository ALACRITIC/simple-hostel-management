var ReservationUtils = {

    // fill me first !
    roomAvailablesFeedUrl: null,

    setRoomsAvailableFeedUrl: function (url) {
        var self = ReservationUtils;
        self.roomAvailablesFeedUrl = url;
    },

    getRoomsAvailable: function (start, end, places) {

        if(!places){
            places = 1;
        }
        
        var self = ReservationUtils;
        if (!self.roomAvailablesFeedUrl) {
            throw "Calendar feed url is null !"
        }

        var result = $.Deferred();

        $.ajax({
            url: self.roomAvailablesFeedUrl,
            data: {
                start: start,
                end: end,
                places: places 
            }
        }).done(function (response) {
            result.resolve(response);
        }).fail(function (error) {
            console.error(error);
            result.reject();
        });

        return result.promise();
    }

};


