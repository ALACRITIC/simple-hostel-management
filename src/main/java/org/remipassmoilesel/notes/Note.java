package org.remipassmoilesel.notes;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by remipassmoilesel on 11/12/16.
 */
@XmlRootElement(name = "note")
public class Note {

    private String markdown;

    private String html;

    private String name;

    public Note(){

    }

    public Note(String name, String markdown, String html) {
        this.name = name;
        this.html = html;
        this.markdown = markdown;
    }

    public String getMarkdown() {
        return markdown;
    }

    public void setMarkdown(String markdown) {
        this.markdown = markdown;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
