package org.remipassmoilesel.notes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by remipassmoilesel on 10/12/16.
 */
@Controller
public class NotesController {

    @Autowired
    private NotesService service;

    @RequestMapping("note")
    public String displayNote(@RequestParam(value = "name", required = false, defaultValue = "configuration") String name, Model model) {
        model.addAttribute("content", service.getNoteAsHTML(name));
        return "note_display";
    }

}
