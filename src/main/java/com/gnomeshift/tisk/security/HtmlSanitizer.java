package com.gnomeshift.tisk.security;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.stereotype.Component;

@Component
public class HtmlSanitizer {
    private static final PolicyFactory POLICY = new HtmlPolicyBuilder()
            // Formatting
            .allowElements("p", "br", "strong", "b", "em", "i", "u", "s", "strike", "span")
            // Headers
            .allowElements("h1", "h2", "h3", "h4", "h5", "h6")
            // Lists
            .allowElements("ul", "ol", "li")
            // Block elements
            .allowElements("blockquote", "pre", "code", "div")
            // Tables
            .allowElements("table", "thead", "tbody", "tr", "th", "td")
            .allowAttributes("colspan", "rowspan").onElements("th", "td")
            // Limited links
            .allowElements("a")
            .allowAttributes("href").onElements("a")
            .allowUrlProtocols("http", "https", "mailto")
            .requireRelNofollowOnLinks()
            // Code highlighting CSS
            .allowAttributes("class").matching(
                    (elementName, attributeName, value) ->
                            String.valueOf(value != null && value.matches("^(language-[\\w-]+|hljs[\\w-]*)$"))
            ).onElements("code", "pre", "span")
            .toFactory();

    public String sanitize(String html) {
        if (html == null || html.isBlank()) {
            return html;
        }
        return POLICY.sanitize(html);
    }
}
