package org.remipassmoilesel.bookme.configuration;

import org.remipassmoilesel.bookme.utils.AbstractDaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by remipassmoilesel on 30/01/17.
 */
@Service
public class UserSettingsService extends AbstractDaoService<UserSettings> {

    private static final Logger logger = LoggerFactory.getLogger(UserSettingsService.class);

    public UserSettingsService(SpringConfiguration configuration) throws IOException {
        super(UserSettings.class, configuration);

        // set default values
        if (getAll(-1l, -1l).size() < 1) {
            create(new UserSettings(UserSettings.HOSTEL_NAME, "Great oak's hostel"));
            create(new UserSettings(UserSettings.TRACKING_CODE, "\n" +
                    "\n" +
                    "<!-- Piwik -->\n" +
                    "<script type=\"text/javascript\">\n" +
                    "  var _paq = _paq || [];\n" +
                    "  // tracker methods like \"setCustomDimension\" should be called before \"trackPageView\"\n" +
                    "  _paq.push(['trackPageView']);\n" +
                    "  _paq.push(['enableLinkTracking']);\n" +
                    "  (function() {\n" +
                    "    var u=\"//vps303506.ovh.net/piwik/\";\n" +
                    "    _paq.push(['setTrackerUrl', u+'piwik.php']);\n" +
                    "    _paq.push(['setSiteId', '11']);\n" +
                    "    var d=document, g=d.createElement('script'), s=d.getElementsByTagName('script')[0];\n" +
                    "    g.type='text/javascript'; g.async=true; g.defer=true; g.src=u+'piwik.js'; s.parentNode.insertBefore(g,s);\n" +
                    "  })();\n" +
                    "</script>\n" +
                    "<noscript><p><img src=\"//vps303506.ovh.net/piwik/piwik.php?idsite=11&rec=1\" style=\"border:0;\" alt=\"\" /></p></noscript>\n" +
                    "<!-- End Piwik Code -->\n" +
                    "\n"));
        }
    }

    public void includeTrackingCode(Model model) throws IOException {
        model.addAttribute("trackingCode", getByKey(UserSettings.TRACKING_CODE).getValue());
    }

    private UserSettings getByKey(String key) throws IOException {
        try {
            return (UserSettings) dao.queryForEq(UserSettings.KEY_FIELD_NAME, key).get(0);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

}
