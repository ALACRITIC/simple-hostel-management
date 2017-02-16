package org.remipassmoilesel.bookme.export;

import org.apache.commons.io.IOUtils;
import org.remipassmoilesel.bookme.Mappings;
import org.remipassmoilesel.bookme.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
 * Created by remipassmoilesel on 16/02/17.
 */
@Controller
public class ExportController {

    @Autowired
    private ExportService exportService;

    @RequestMapping(value = Mappings.EXPORT_RESERVATIONS, method = RequestMethod.GET)
    public void exportReservations(
            @RequestParam("begin") String begin,
            @RequestParam("end") String end,
            HttpServletResponse response) throws Exception {

        Date beginDate = Utils.stringToDate(begin);
        Date endDate = Utils.stringToDate(end);

        response.setContentType("text/csv");
        response.setHeader("Content-disposition", "attachment; filename=\"export.csv\"");

        File tempFile = exportService.export(beginDate, endDate);

        try (ServletOutputStream out = response.getOutputStream();
             InputStream in = Files.newInputStream(tempFile.toPath())) {
            IOUtils.copy(in, out);
        }

        Files.delete(tempFile.toPath());

    }

}
