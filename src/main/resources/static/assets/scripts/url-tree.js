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

    reservationCalendarFeedUrl: null,

    setReservationCalendarFeedUrl: function (url) {
        UrlTree.reservationCalendarFeedUrl = url;
    },

    getReservationCalendarFeedUrl: function () {
        UrlTree._checkIfDefined(UrlTree.reservationCalendarFeedUrl, "reservationCalendarFeedUrl");
        return UrlTree.reservationCalendarFeedUrl;

    },

    ///////////////////////////////

    serviceCalendarFeedUrl: null,

    setServiceCalendarFeedUrl: function (url) {
        UrlTree.serviceCalendarFeedUrl = url;
    },

    getServiceCalendarFeedUrl: function () {
        UrlTree._checkIfDefined(UrlTree.serviceCalendarFeedUrl, "serviceCalendarFeedUrl");
        return UrlTree.serviceCalendarFeedUrl;

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

    showServiceUrl: null,

    setShowServiceUrl: function (url) {
        UrlTree.showServiceUrl = url;
    },

    getShowServiceUrl: function () {
        UrlTree._checkIfDefined(UrlTree.showServiceUrl, "showServiceUrl");
        return UrlTree.showServiceUrl;
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