package org.remipassmoilesel.notes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by remipassmoilesel on 10/12/16.
 */
@Controller
@RequestMapping("notes/")
public class NotesController {

    private static final Logger logger = LoggerFactory.getLogger(NotesController.class);

    @Autowired
    private NotesService service;

    @RequestMapping("display/{noteId}")
    public String displayNoteHtml(@PathVariable String noteId, Model model) {
        model.addAttribute("currentNote", service.getNote(noteId));
        model.addAttribute("notesList", service.getNotesList());
        return "note_display";
    }

    @RequestMapping(value = "get/json/{noteId}")
    @ResponseBody
    public Note getJson(@PathVariable String noteId) {

        Note note = service.getNote(noteId);

        // if we return an object, it will be converted automatically in JSON (if specified)
        // or in other format according to "Accept" header
        return note;
    }

    //@RequestMapping(value = "get/json/{noteId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    //@ResponseBody
    public String getExplicitlyConvertedJson(@PathVariable String noteId) {

        ObjectMapper mapper = new ObjectMapper();

        Note note = service.getNote(noteId);

        String jsonContent = null;
        try {
            jsonContent = mapper.writeValueAsString(note);
        } catch (JsonProcessingException e) {
            jsonContent = "error";
            logger.error("Error while retrieving note", e);
        }

        return jsonContent;

    }

    @RequestMapping(value = "get/xml/{noteId}", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public Note getXml(@PathVariable String noteId) {
        Note note = service.getNote(noteId);
        return note;
    }


}
