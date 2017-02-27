package org.remipassmoilesel.bookme.controllers;

import org.apache.commons.io.IOUtils;
import org.remipassmoilesel.bookme.Mappings;
import org.remipassmoilesel.bookme.Templates;
import org.remipassmoilesel.bookme.customers.Customer;
import org.remipassmoilesel.bookme.customers.CustomerService;
import org.remipassmoilesel.bookme.export.ExportService;
import org.remipassmoilesel.bookme.reservations.ReservationService;
import org.remipassmoilesel.bookme.services.MerchantServiceService;
import org.remipassmoilesel.bookme.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Date;

/**
 * Created by remipassmoilesel on 17/02/17.
 */
@Controller
public class AdministrationController {

    @Autowired
    private ExportService exportService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private MerchantServiceService merchantServiceService;

    @RequestMapping(value = Mappings.ADMINISTRATION_ROOT, method = RequestMethod.GET)
    public String showAdminPage(Model model) throws Exception {

        Mappings.includeMappings(model);
        return Templates.ADMINISTRATION;
    }

    @RequestMapping(value = Mappings.ADMINISTRATION_EXPORT_RESERVATIONS_CSV, method = RequestMethod.GET)
    public void exportReservationsCsv(
            @RequestParam("begin") String begin,
            @RequestParam("end") String end,
            HttpServletResponse response) throws Exception {

        Date beginDate = Utils.stringToDate(begin);
        Date endDate = Utils.stringToDate(end);

        response.setContentType("text/csv");
        response.setHeader("Content-disposition", "attachment; filename=\"export.csv\"");

        File tempFile = exportService.exportReserationsCsv(beginDate, endDate);

        try (ServletOutputStream out = response.getOutputStream();
             InputStream in = Files.newInputStream(tempFile.toPath())) {
            IOUtils.copy(in, out);
        }

        Files.delete(tempFile.toPath());

    }

    @RequestMapping(value = Mappings.ADMINISTRATION_EXPORT_SERVICES_CSV, method = RequestMethod.GET)
    public void exportServicesCsv(
            @RequestParam("begin") String begin,
            @RequestParam("end") String end,
            HttpServletResponse response) throws Exception {

        Date beginDate = Utils.stringToDate(begin);
        Date endDate = Utils.stringToDate(end);

        response.setContentType("text/csv");
        response.setHeader("Content-disposition", "attachment; filename=\"export.csv\"");

        File tempFile = exportService.exportServicesCsv(beginDate, endDate);

        try (ServletOutputStream out = response.getOutputStream();
             InputStream in = Files.newInputStream(tempFile.toPath())) {
            IOUtils.copy(in, out);
        }

        Files.delete(tempFile.toPath());

    }


}
