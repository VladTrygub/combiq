package ru.atott.combiq.data.service;

import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.atott.combiq.dao.Types;
import ru.atott.combiq.dao.es.IndexService;
import ru.atott.combiq.dao.es.NameVersionDomainResolver;
import ru.atott.combiq.data.service.ImportService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Service
public class ImportServiceImpl implements ImportService {

    @Autowired
    private Client client;

    @Autowired
    private NameVersionDomainResolver domainResolver;

    @Autowired
    private IndexService indexService;

    @Override
    public String importQuestionnareOds(String filename, String questionnaireName) throws Exception {
        SpreadsheetDocument document = SpreadsheetDocument.loadDocument(new File(filename));
        Table sheet = document.getSheetByIndex(0);

        List<String> questions = new ArrayList<>();

        for (int i = 1; i < sheet.getRowList().size(); i++) {
            Row row = sheet.getRowList().get(i);

            String question = getCellStringValue(row, 0);

            if (StringUtils.isBlank(question)) {
                break;
            }

            String levelString = getCellStringValue(row, 1);
            long level = Long.valueOf(StringUtils.replace(levelString, "D", ""));
            String tagsString = getCellStringValue(row, 2);
            List<String> tags = Collections.emptyList();
            if (StringUtils.isNotBlank(tagsString)) {
                tags = Lists.newArrayList(StringUtils.split(tagsString, ", "));
            }
            String tip = StringUtils.defaultIfBlank(getCellStringValue(row, 3), null);


            XContentBuilder builder = jsonBuilder()
                    .startObject()
                        .field("title", question)
                        .field("level", level)
                        .field("tags", tags)
                        .field("tip", tip)
                    .endObject();

            IndexResponse indexResponse = client
                    .prepareIndex(domainResolver.resolveQuestionIndex(), Types.question)
                    .setSource(builder)
                    .execute()
                    .actionGet();

            questions.add(indexResponse.getId());
        }

        XContentBuilder questionnaireBuilder = jsonBuilder()
                .startObject()
                    .field("name", questionnaireName)
                    .field("questions", questions)
                .endObject();

        IndexResponse indexResponse = client
                .prepareIndex(domainResolver.resolveQuestionIndex(), Types.questionnaire)
                .setSource(questionnaireBuilder)
                .execute()
                .actionGet();

        return indexResponse.getId();
    }

    @Override
    public String importJdk8ClassesDictionary() throws IOException {
        String questionIndex = domainResolver.resolveQuestionIndex();

        List<String> classes = IOUtils.readLines(this.getClass().getResourceAsStream("/data/jdk8classes.txt"));
        Map<String, List<String>> normalizedClasses = classes.stream()
                .flatMap(className -> {
                    String simpleClassName = StringUtils.substringAfterLast(className, ".");
                    return Stream.<ImmutablePair<String, String>>of(
                            new ImmutablePair<>(className, className),
                            new ImmutablePair<>(simpleClassName, className));
                })
                .collect(groupingBy(ImmutablePair::getLeft, mapping(ImmutablePair::getRight, toList())));

        normalizedClasses.keySet().forEach(className -> {
            List<String> classNames = normalizedClasses.get(className);

            try {
                XContentBuilder request = jsonBuilder()
                        .startObject()
                            .field("classNames", classNames)
                        .endObject();

                client
                        .prepareIndex(questionIndex, Types.jdk8classes, className)
                        .setSource(request)
                        .execute()
                        .actionGet();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        indexService.refreshIndexByIndexName(questionIndex);

        return questionIndex;
    }

    private String getCellStringValue(Row row, int index) {
        if (row.getCellByIndex(index) == null) {
            return null;
        }
        return row.getCellByIndex(index).getStringValue();
    }
}
