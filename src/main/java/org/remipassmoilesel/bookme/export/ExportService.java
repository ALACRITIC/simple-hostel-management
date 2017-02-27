package org.remipassmoilesel.bookme.export;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.remipassmoilesel.bookme.configuration.SpringConfiguration;
import org.remipassmoilesel.bookme.reservations.Reservation;
import org.remipassmoilesel.bookme.reservations.ReservationService;
import org.remipassmoilesel.bookme.services.MerchantService;
import org.remipassmoilesel.bookme.services.MerchantServiceService;
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

    @Autowired
    private MerchantServiceService merchantServiceService;

    public ExportService() throws IOException {
        if (Files.isDirectory(SpringConfiguration.TEMP_DIRECTORY) == false) {
            Files.createDirectories(SpringConfiguration.TEMP_DIRECTORY);
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
            headers.add("ACCOMMODATION-NAME");
            headers.add("ACCOMMODATION-PRICE-PER-DAY");
            headers.add("RESERVATION-TOTAL-PRICE");
            headers.add("FIRST-NAME");
            headers.add("LAST-NAME");
            headers.add("PHONE-NUMBER");
            headers.add("COMMENT");
            headers.add("PAID");

            // print headers
            for (String s : headers) {
                csvFilePrinter.print(s);
            }
            csvFilePrinter.println();

            // print data entries
            for (Reservation entry : reservations) {

                csvFilePrinter.print(entry.getBegin());
                csvFilePrinter.print(entry.getEnd());
                csvFilePrinter.print(entry.getAccommodation().getName());
                csvFilePrinter.print(entry.getAccommodation().getPricePerDay());
                csvFilePrinter.print(entry.getTotalPrice());
                csvFilePrinter.print(entry.getCustomer().getFirstname());
                csvFilePrinter.print(entry.getCustomer().getLastname());
                csvFilePrinter.print(entry.getCustomer().getPhonenumber());
                csvFilePrinter.print(entry.getComment());
                csvFilePrinter.print(entry.isPaid());

                // end line
                csvFilePrinter.println();

            }

            return tempFile;
        }
    }

    public File exportServicesCsv(Date begin, Date end) throws IOException {

        File tempFile = createTempFile();
        try (FileWriter writer = new FileWriter(tempFile);
             CSVPrinter csvFilePrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {

            List<MerchantService> services = merchantServiceService.getByInterval(begin, end, true);

            // get all headers from list
            ArrayList<String> headers = new ArrayList<>();
            headers.add("PURCHASE-DATE");
            headers.add("SERVICE-NAME");
            headers.add("SERVICE-COMMENT");
            headers.add("SERVICE-PRICE");
            headers.add("TOTAL-PRICE");
            headers.add("FIRST-NAME");
            headers.add("LAST-NAME");
            headers.add("PHONE-NUMBER");
            headers.add("COMMENT");
            headers.add("PAID");

            // print headers
            for (String s : headers) {
                csvFilePrinter.print(s);
            }
            csvFilePrinter.println();

            // print data entries
            for (MerchantService entry : services) {

                csvFilePrinter.print(entry.getPurchaseDate());
                csvFilePrinter.print(entry.getServiceType().getName());
                csvFilePrinter.print(entry.getServiceType().getComment());
                csvFilePrinter.print(entry.getServiceType().getPrice());
                csvFilePrinter.print(entry.getTotalPrice());
                csvFilePrinter.print(entry.getCustomer().getFirstname());
                csvFilePrinter.print(entry.getCustomer().getLastname());
                csvFilePrinter.print(entry.getCustomer().getPhonenumber());
                csvFilePrinter.print(entry.isScheduled());
                csvFilePrinter.print(entry.getExecutionDate());
                csvFilePrinter.print(entry.getComment());
                csvFilePrinter.print(entry.isPaid());

                // end line
                csvFilePrinter.println();

            }

            return tempFile;
        }
    }

    private File createTempFile() throws IOException {
        return File.createTempFile("export", ".tmp", SpringConfiguration.TEMP_DIRECTORY.toFile());
    }


}
