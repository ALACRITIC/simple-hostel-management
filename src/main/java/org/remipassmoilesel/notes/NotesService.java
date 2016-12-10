package org.remipassmoilesel.notes;

import org.pegdown.PegDownProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by remipassmoilesel on 10/12/16.
 */
@Service
public class NotesService {

    @Autowired
    private NotesRepository repository;

    public String getNoteAsHTML(String name) {
        String md = null;
        try {
            md = repository.getNote(name);
        } catch (IOException e) {
            md = "# Note indisponible";
        }

        PegDownProcessor processor = new PegDownProcessor();

        return processor.markdownToHtml(md);
    }

}
