var UrlTree = {

    ///////////////////////////////

    accommodationAvailablesFeedUrl: null,

    setAccommodationsAvailableFeedUrl: function (url) {
        UrlTree.accommodationAvailablesFeedUrl = url;
    },

    getAccommodationsAvailableFeedUrl: function () {
        UrlTree._checkIfDefined(UrlTree.accommodationAvailablesFeedUrl, "accommodationAvailablesFeedUrl");
        return UrlTree.accommodationAvailablesFeedUrl;
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

    deleteAccommodationUrl: null,

    setDeleteAccommodationUrl: function (url) {
        UrlTree.deleteAccommodationUrl = url;
    },

    getDeleteAccommodationUrl: function () {
        UrlTree._checkIfDefined(UrlTree.deleteAccommodationUrl, "deleteAccommodationUrl");
        return UrlTree.deleteAccommodationUrl;
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

    exportReservationCsvUrl: null,

    setExportReservationCsvUrl: function (url) {
        UrlTree.exportReservationCsvUrl = url;
    },

    getExportReservationCsvUrl: function () {
        UrlTree._checkIfDefined(UrlTree.exportReservationCsvUrl, "exportReservationCsvUrl");
        return UrlTree.exportReservationCsvUrl;
    },  
    
    ///////////////////////////////

    exportServiceCsvUrl: null,

    setExportServiceCsvUrl: function (url) {
        UrlTree.exportServiceCsvUrl = url;
    },

    getExportServiceCsvUrl: function () {
        UrlTree._checkIfDefined(UrlTree.exportServiceCsvUrl, "exportServiceCsvUrl");
        return UrlTree.exportServiceCsvUrl;
    },

    ///////////////////////////////

    deleteServiceTypeUrl: null,

    setDeleteServiceTypeUrl: function (url) {
        UrlTree.deleteServiceTypeUrl = url;
    },

    getDeleteServiceUrl: function () {
        UrlTree._checkIfDefined(UrlTree.deleteServiceTypeUrl, "deleteServiceTypeUrl");
        return UrlTree.deleteServiceTypeUrl;
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

    serviceSearchUrl: null,

    setServiceSearchUrl: function (url) {
        UrlTree.serviceSearchUrl = url;
    },

    getServiceSearchUrl: function () {
        UrlTree._checkIfDefined(UrlTree.serviceSearchUrl, "serviceSearchUrl");
        return UrlTree.serviceSearchUrl;
    }, 
    
    ///////////////////////////////

    mainMenuUrl: null,

    setMainMenuUrl: function (url) {
        UrlTree.mainMenuUrl = url;
    },

    getMainMenuUrl: function () {
        UrlTree._checkIfDefined(UrlTree.mainMenuUrl, "mainMenuUrl");
        return UrlTree.mainMenuUrl;
    },

    ///////////////////////////////

    _checkIfDefined: function (field, label) {
        if (!field) {
            throw "URL field not defined ! Please set field before use it: " + label;
        }
    }

};