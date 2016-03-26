package ru.atott.combiq.data.commands;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;
import ru.atott.combiq.dao.es.IndexService;
import ru.atott.combiq.data.service.DeleteIndecesService;
import ru.atott.combiq.data.service.ListIndecesService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@Component
public class IndecesCommands implements CommandMarker {

    @Autowired
    private IndexService indexService;

    @Autowired
    private ListIndecesService listIndecesService;

    @Autowired
    private DeleteIndecesService deleteIndecesService;

    @CliCommand("delete index")
    public void deleteIndex(
            @CliOption(key = "domain", mandatory = true) String domain,
            @CliOption(key = "version", mandatory = true) Long version) {
        deleteIndecesService.deleteIndex(domain, version);
    }

    @CliCommand("list indeces")
    public String list(@CliOption(key = "domain", mandatory = true) String domain) {
        return StringUtils.join(listIndecesService.list(domain), ",");
    }

    @CliCommand(value = "create indeces")
    public String create() throws InterruptedException, ExecutionException, IOException {
        ArrayList<String> indeces = Lists.newArrayList(
                indexService.createQuestionIndex(),
                indexService.createPersonalIndex(),
                indexService.createSystemIndex(),
                indexService.createSiteIndex());
        return StringUtils.join(indeces.toArray(), ",") + ". Done.";
    }

    @CliCommand(value = "update indeces")
    public String update() throws InterruptedException, ExecutionException, IOException {
        ArrayList<String> indeces = Lists.newArrayList(
                indexService.updateQuestionMapping(),
                indexService.updatePersonalMapping(),
                indexService.updateSiteMapping());
        return StringUtils.join(indeces.toArray(), ",") + ". Done.";
    }

    @CliCommand(value = "create index system")
    public String createSystemIndex() throws InterruptedException, ExecutionException, IOException {
        return indexService.createSiteIndex();
    }

    @CliCommand(value = "create index site")
    public String createSiteIndex() throws InterruptedException, ExecutionException, IOException {
        return indexService.createSiteIndex();
    }

    @CliCommand(value = "update index site")
    public String updateSiteIndex() throws InterruptedException, ExecutionException, IOException {
        return indexService.updateSiteMapping();
    }

    @CliCommand(value = "create index personal")
    public String createPersonalIndex() throws InterruptedException, ExecutionException, IOException {
        return indexService.createPersonalIndex();
    }

    @CliCommand(value = "update index personal")
    public String updatePersonalIndex() throws InterruptedException, ExecutionException, IOException {
        return indexService.updatePersonalMapping();
    }

    @CliCommand(value = "create index question")
    public String createQuestionIndex() throws InterruptedException, ExecutionException, IOException {
        return indexService.createQuestionIndex();
    }

    @CliCommand(value = "update index question")
    public String updateQuestionIndex() throws InterruptedException, ExecutionException, IOException {
        return indexService.updateQuestionMapping();
    }
}
