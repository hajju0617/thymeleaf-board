package com.project.thymeleafboard.common;

import lombok.RequiredArgsConstructor;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Markdown {
    private final Parser parser;
    private final HtmlRenderer htmlRenderer;

    public String parseMarkdown(String markdown) {
        Node documentNode = parser.parse(markdown);
        return Jsoup.clean(htmlRenderer.render(documentNode), Safelist.basicWithImages());
    }
}
