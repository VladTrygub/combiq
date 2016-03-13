package ru.atott.combiq.service.markdown;

import ru.atott.combiq.dao.entity.MarkdownContent;
import ru.atott.combiq.service.site.UserContext;

public interface MarkdownService {

    String toHtml(UserContext uc, String markdown);

    String toSimplifiedHtml(UserContext uc, String markdown);

    String toSimplifiedHtml(UserContext uc, String markdown, int outputLength);

    MarkdownContent toMarkdownContent(UserContext uc, String markdown);

    MarkdownContent toMarkdownContent(UserContext uc, String id, String markdown);
}
