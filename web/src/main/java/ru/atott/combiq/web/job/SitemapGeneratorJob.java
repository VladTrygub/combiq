package ru.atott.combiq.web.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.atott.combiq.service.site.SitemapEntry;
import ru.atott.combiq.service.site.SitemapService;
import ru.atott.combiq.service.site.HostPortUrlResolver;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;

@Component
public class SitemapGeneratorJob {

    @Value("${web.sitemap.location}")
    private String sitemapLocation;

    @Value("${web.sitemap.generate}")
    private boolean generate;

    @Autowired
    private SitemapService sitemapService;

    @Scheduled(cron = "0 1 1 * * ?")
    public void generateSiteMap() throws Exception {
        if (!generate) {
            return;
        }

        forceGenerateSiteMap();
    }

    public void forceGenerateSiteMap() throws Exception {
        HostPortUrlResolver urlResolver = new HostPortUrlResolver();
        File file = new File(sitemapLocation);

        if (!file.exists()) {
            file.createNewFile();
        }

        String sitemapEntriesJson = IOUtils.toString(this.getClass().getResourceAsStream("/ru/atott/combiq/web/sitemap.json"), "utf-8");
        ObjectMapper objectMapper = new ObjectMapper();
        List<SitemapEntry> sitemapEntries = Arrays.asList(objectMapper.readValue(sitemapEntriesJson, SitemapEntry[].class));
        sitemapEntries.forEach(entry -> entry.setUrl(urlResolver.externalize(entry.getUrl())));

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            sitemapService.generateSitemap(outputStream, urlResolver, sitemapEntries);
        }
    }
}
