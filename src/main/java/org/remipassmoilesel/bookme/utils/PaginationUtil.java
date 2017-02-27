package org.remipassmoilesel.bookme.utils;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.ui.Model;

/**
 * Created by remipassmoilesel on 27/02/17.
 */
public class PaginationUtil {

    private Long limit;
    private Long offset;
    private String baseUrl;

    public PaginationUtil(String baseUrl, Long limit, Long offset) {
        this.limit = limit;
        this.offset = offset;
        this.baseUrl = baseUrl;
    }

    public String getNextLink() {
        Long newOffset = offset + limit;
        return getLink(newOffset, limit);
    }

    public String getPreviousLink() {
        Long newOffset = offset - limit;
        if (newOffset < 0) {
            newOffset = 0l;
        }
        return getLink(newOffset, limit);
    }

    private String getLink(Long offset, Long limit) {
        try {
            URIBuilder b = new URIBuilder(baseUrl);
            b.addParameter("limit", String.valueOf(limit));
            b.addParameter("offset", String.valueOf(offset));
            return b.build().toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addNextLink(Model m) {
        m.addAttribute("nextPageLink", getNextLink());
    }

    public void addPreviousLink(Model m) {
        m.addAttribute("previousPageLink", getPreviousLink());
    }

}
