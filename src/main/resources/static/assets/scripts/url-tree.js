var UrlTree = {

    ///////////////////////////////

    roomAvailablesFeedUrl: null,

    setRoomsAvailableFeedUrl: function (url) {
        UrlTree.roomAvailablesFeedUrl = url;
    },

    getRoomsAvailableFeedUrl: function () {
        UrlTree._checkIfDefined(UrlTree.roomAvailablesFeedUrl, "roomAvailablesFeedUrl");
        return UrlTree.roomAvailablesFeedUrl;
    },

    ///////////////////////////////

    calendarFeedUrl: null,

    setCalendarFeedUrl: function (url) {
        UrlTree.calendarFeedUrl = url;
    },

    getCalendarFeedUrl: function () {
        UrlTree._checkIfDefined(UrlTree.calendarFeedUrl, "calendarFeedUrl");
        return UrlTree.calendarFeedUrl;

    },

    ///////////////////////////////

    showReservationUrl: null,

    setShowReservationUrl: function (url) {
        UrlTree.showReservationUrl = url;
    },

    getShowReservationUrl: function () {
        UrlTree._checkIfDefined(UrlTree.showReservationUrl, "showReservationUrl");
        return UrlTree.showReservationUrl;
    },

    ///////////////////////////////

    deleteReservationUrl: null,

    setDeleteReservationUrl: function (url) {
        UrlTree.deleteReservationUrl = url;
    },

    getDeleteReservationUrl: function () {
        UrlTree._checkIfDefined(UrlTree.deleteReservationUrl, "deleteReservationUrl");
        return UrlTree.deleteReservationUrl;
    },

    ///////////////////////////////

    _checkIfDefined: function (field, label) {
        if (!field) {
            throw "URL field not defined ! Please set field before use it: " + label;
        }
    }

};