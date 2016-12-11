package org.remipassmoilesel.notes;

import org.pegdown.PegDownProcessor;
import org.remipassmoilesel.UpdateFilesListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by remipassmoilesel on 10/12/16.
 */
@Service
public class NotesService {

    private static final Logger logger = LoggerFactory.getLogger(UpdateFilesListener.class);

    @Autowired
    private NotesRepository repository;

    public Note getNote(String name) {

        Note note = null;
        try {
            note = repository.getNote(name);
        } catch (IOException e) {
            note = new Note("unavailable", "# Note indisponible", null);
            logger.error("Error while retrieving note", e);
        }

        PegDownProcessor processor = new PegDownProcessor();
        note.setHtml(processor.markdownToHtml(note.getMarkdown()));

        return note;
    }

    public ArrayList<String> getNotesList() {
        try {
            return repository.getList();
        } catch (IOException e) {
            logger.error("Error while retrieving notes list", e);
            ArrayList<String> list = new ArrayList<>();
            list.add("Aucune note disponible");
            return list;
        }
    }

}
