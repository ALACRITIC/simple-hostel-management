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

    public String getNoteAsHTML(String name) {
        String md = null;
        try {
            md = repository.getNote(name);
        } catch (IOException e) {
            md = "# Note indisponible";
            logger.error("Error while retrieving note", e);
        }

        PegDownProcessor processor = new PegDownProcessor();

        return processor.markdownToHtml(md);
    }

    public ArrayList<String> getNotesList() {
        try {
            return repository.getList();
        } catch (IOException e) {
            logger.error("Error while retrieving notes list", e);
            ArrayList<String> list = new ArrayList<String>();
            list.add("Aucune note disponible");
            return list;
        }
    }

}
