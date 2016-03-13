package ru.atott.combiq.service.markdown;

import org.apache.commons.lang3.StringUtils;
import org.pegdown.LinkRenderer;
import org.pegdown.PegDownProcessor;
import org.pegdown.ToHtmlSerializer;
import org.pegdown.ast.RootNode;
import org.pegdown.plugins.PegDownPlugins;
import org.pegdown.plugins.ToHtmlSerializerPlugin;
import org.springframework.stereotype.Service;
import ru.atott.combiq.dao.entity.MarkdownContent;
import ru.atott.combiq.service.site.UserContext;

import java.util.List;

import static java.util.Collections.emptyMap;

@Service
public class MarkdownServiceImpl implements MarkdownService {

    @Override
    public String toHtml(UserContext uc, String markdown) {
        return toHtml(markdown, new DefaultToHtmlSerializer(uc));
    }

    @Override
    public String toSimplifiedHtml(UserContext uc, String markdown) {
        return toHtml(markdown, new ToSimplifiedHtmlSerializer(uc));
    }

    @Override
    public String toSimplifiedHtml(UserContext uc, String markdown, int outputLength) {
        String result = toSimplifiedHtml(uc, markdown);

        if (result.length() > outputLength) {
            result = result.substring(0, outputLength) + "...";
        }

        return result;
    }

    @Override
    public MarkdownContent toMarkdownContent(UserContext uc, String markdown) {
        return toMarkdownContent(uc, null, markdown);
    }

    @Override
    public MarkdownContent toMarkdownContent(UserContext uc, String id, String markdown) {
        MarkdownContent content = new MarkdownContent();
        content.setId(id);
        content.setHtml(toHtml(uc, markdown));
        content.setMarkdown(markdown);
        return content;
    }

    private String toHtml(String markdown, ToHtmlSerializer toHtmlSerializer) {
        if (StringUtils.isBlank(markdown)) {
            return "";
        }

        PegDownProcessor processor = new PegDownProcessor(0);
        RootNode rootNode = processor.parseMarkdown(markdown.toCharArray());
        return toHtmlSerializer.toHtml(rootNode);
    }
}
