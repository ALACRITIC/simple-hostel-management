var UrlTree = {

    ///////////////////////////////

    roomAvailablesFeedUrl: null,

    setResourcesAvailableFeedUrl: function (url) {
        UrlTree.roomAvailablesFeedUrl = url;
    },

    getResourcesAvailableFeedUrl: function () {
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

    deleteResourceUrl: null,

    setDeleteResourceUrl: function (url) {
        UrlTree.deleteResourceUrl = url;
    },

    getDeleteResourceUrl: function () {
        UrlTree._checkIfDefined(UrlTree.deleteResourceUrl, "deleteResourceUrl");
        return UrlTree.deleteResourceUrl;
    },

    ///////////////////////////////

    customersJsonFeedUrl: null,

    setCustomersJsonFeedUrl: function (url) {
        UrlTree.customersJsonFeedUrl = url;
    },

    getCustomersJsonFeedUrl: function () {
        UrlTree._checkIfDefined(UrlTree.customersJsonFeedUrl, "customersJsonFeedUrl");
        return UrlTree.customersJsonFeedUrl;
    },

    ///////////////////////////////

    exportCsvUrl: null,

    setExportCsvUrl: function (url) {
        UrlTree.exportCsvUrl = url;
    },

    getExportCsvUrl: function () {
        UrlTree._checkIfDefined(UrlTree.exportCsvUrl, "exportCsvUrl");
        return UrlTree.exportCsvUrl;
    },

    ///////////////////////////////

    deleteServiceUrl: null,

    setDeleteServiceUrl: function (url) {
        UrlTree.deleteServiceUrl = url;
    },

    getDeleteServiceUrl: function () {
        UrlTree._checkIfDefined(UrlTree.deleteServiceUrl, "deleteServiceUrl");
        return UrlTree.deleteServiceUrl;
    },

    ///////////////////////////////

    reservationSearchUrl: null,

    setReservationSearchUrl: function (url) {
        UrlTree.reservationSearchUrl = url;
    },

    getReservationSearchUrl: function () {
        UrlTree._checkIfDefined(UrlTree.reservationSearchUrl, "reservationSearchUrl");
        return UrlTree.reservationSearchUrl;
    },

    ///////////////////////////////

    _checkIfDefined: function (field, label) {
        if (!field) {
            throw "URL field not defined ! Please set field before use it: " + label;
        }
    }

};