package ru.atott.combiq.service.site;

import java.io.OutputStream;
import java.util.List;

public interface SitemapService {

    void generateSitemap(OutputStream outputStream, UrlResolver urlResolver, List<SitemapEntry> staticEntries);
}
