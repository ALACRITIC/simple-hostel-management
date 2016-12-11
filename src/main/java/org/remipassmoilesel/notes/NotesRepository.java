package org.remipassmoilesel.notes;

import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by remipassmoilesel on 10/12/16.
 */
@Repository
public class NotesRepository {

    //private static final PathROOT = Paths.get("classes").resolve("notes");
    private static final Path ROOT = Paths.get("notes");
    private static final String FILE_EXT = ".md";

    public Note getNote(String name) throws IOException {
        Path notePath = ROOT.resolve(name + FILE_EXT);
        String mdContent = new String(Files.readAllBytes(notePath));
        return new Note(name, mdContent, null);
    }

    public ArrayList<String> getList() throws IOException {

        Iterator<Path> stream = Files.newDirectoryStream(ROOT).iterator();
        ArrayList<String> rslt = new ArrayList<>();
        while (stream.hasNext()) {
            String name = stream.next().getFileName().toString();
            rslt.add(name.substring(0, name.length() - FILE_EXT.length()));
        }

        return rslt;
    }

}
