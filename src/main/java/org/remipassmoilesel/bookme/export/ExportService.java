package org.remipassmoilesel.bookme.export;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.remipassmoilesel.bookme.configuration.CustomConfiguration;
import org.remipassmoilesel.bookme.customers.Customer;
import org.remipassmoilesel.bookme.reservations.Reservation;
import org.remipassmoilesel.bookme.reservations.ReservationService;
import org.remipassmoilesel.bookme.sharedresources.SharedResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by remipassmoilesel on 16/02/17.
 */
@Service
public class ExportService {

    @Autowired
    private ReservationService reservationService;

    public ExportService() throws IOException {
        if (Files.isDirectory(CustomConfiguration.TEMP_DIRECTORY) == false) {
            Files.createDirectories(CustomConfiguration.TEMP_DIRECTORY);
        }
    }

    public File exportReserationsCsv(Date begin, Date end) throws IOException {

        File tempFile = createTempFile();
        try (FileWriter writer = new FileWriter(tempFile);
             CSVPrinter csvFilePrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {

            List<Reservation> reservations = reservationService.getByInterval(begin, end, true);

            // get all headers from list
            ArrayList<String> headers = new ArrayList<>();
            headers.add("CHECK-IN");
            headers.add("CHECK-OUT");
            headers.add("RESOURCE-NAME");
            headers.add("FIRST-NAME");
            headers.add("LAST-NAME");
            headers.add("PHONE-NUMBER");
            headers.add("COMMENT");

            // print headers
            for(String s : headers){
                csvFilePrinter.print(s);
            }
            csvFilePrinter.println();

            // print data entries
            for (Reservation entry : reservations) {

                csvFilePrinter.print(entry.getBegin());
                csvFilePrinter.print(entry.getEnd());
                csvFilePrinter.print(entry.getResource().getName());
                csvFilePrinter.print(entry.getCustomer().getFirstname());
                csvFilePrinter.print(entry.getCustomer().getLastname());
                csvFilePrinter.print(entry.getCustomer().getPhonenumber());
                csvFilePrinter.print(entry.getComment());

                // end line
                csvFilePrinter.println();

            }

            return tempFile;
        }
    }

    private File createTempFile() throws IOException {
        return File.createTempFile("export", ".tmp", CustomConfiguration.TEMP_DIRECTORY.toFile());
    }

}
