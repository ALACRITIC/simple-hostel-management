package org.remipassmoilesel.notes;

import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by remipassmoilesel on 10/12/16.
 */
@Repository
public class NotesRepository {

    //private static final PathROOT = Paths.get("classes").resolve("notes");
    private static final Path ROOT = Paths.get("notes");
    private static final String FILE_EXT = ".md";

    public String getNote(String name) throws IOException {
        Path note = ROOT.resolve(name + FILE_EXT);
        return new String(Files.readAllBytes(note));
    }

    public ArrayList<String> getList() throws IOException {

        Iterator<Path> stream = Files.newDirectoryStream(ROOT).iterator();
        ArrayList<String> rslt = new ArrayList<String>();
        while (stream.hasNext()) {
            String name = stream.next().getFileName().toString();
            rslt.add(name.substring(0, name.length() - FILE_EXT.length()));
        }

        return rslt;
    }

}
