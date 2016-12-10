package org.remipassmoilesel.notes;

import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by remipassmoilesel on 10/12/16.
 */
@Repository
public class NotesRepository {

    //private static final PathROOT = Paths.get("classes").resolve("notes");
    private static final Path ROOT = Paths.get("notes");

    public String getNote(String name) throws IOException {
        Path note = ROOT.resolve(name + ".md");
        return new String(Files.readAllBytes(note));
    }

}
