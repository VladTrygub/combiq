package ru.atott.combiq.service.question;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.atott.combiq.dao.entity.QuestionnaireEntity;
import ru.atott.combiq.dao.repository.QuestionnaireRepository;
import ru.atott.combiq.service.ServiceException;
import ru.atott.combiq.service.bean.Questionnaire;
import ru.atott.combiq.service.bean.QuestionnaireHead;
import ru.atott.combiq.service.mapper.QuestionnaireHeadMapper;
import ru.atott.combiq.service.search.question.SearchContext;
import ru.atott.combiq.service.search.question.SearchResponse;
import ru.atott.combiq.service.search.question.SearchService;
import ru.atott.combiq.service.markdown.MarkdownService;
import ru.atott.combiq.service.site.UserContext;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

@Service
public class QuestionnaireServiceImpl implements QuestionnaireService {

    @Autowired
    private QuestionnaireRepository questionnaireRepository;

    @Autowired
    private SearchService searchService;

    @Autowired
    private MarkdownService markdownService;

    @Override
    public List<QuestionnaireHead> getQuestionnaires() {
        QuestionnaireHeadMapper<QuestionnaireHead> mapper = new QuestionnaireHeadMapper<>(QuestionnaireHead.class);
        return mapper.toList(questionnaireRepository.findAll());
    }

    @Override
    public QuestionnaireHead getQuestionnaireByLegacyId(String id) {
        QuestionnaireHeadMapper<QuestionnaireHead> mapper = new QuestionnaireHeadMapper<>(QuestionnaireHead.class);
        return mapper.safeMap(questionnaireRepository.findOneByLegacyId(id));
    }

    @Override
    public Questionnaire getQuestionnaire(UserContext uc, String id) {
        QuestionnaireEntity entity = questionnaireRepository.findOne(id);

        if (entity == null) {
            return null;
        }

        SearchContext searchContext = new SearchContext();
        searchContext.setUserContext(uc);
        searchContext.setQuestionIds(entity.getQuestions());
        searchContext.setSize(400);
        SearchResponse questionsSearchResponse = searchService.searchQuestions(searchContext);

        QuestionnaireHeadMapper<Questionnaire> mapper = new QuestionnaireHeadMapper<>(Questionnaire.class);
        Questionnaire questionnaire = mapper.map(entity);
        questionnaire.setQuestions(questionsSearchResponse.getQuestions().getContent());
        questionnaire.setDescription(entity.getDescription());

        return questionnaire;
    }

    @Override
    public Questionnaire exportQuestionnareToPdf(UserContext uc, String id, OutputStream outputStream) {
        Questionnaire questionnaire = getQuestionnaire(uc, id);
        exportQuestionnareToPdf(questionnaire, outputStream);
        return questionnaire;
    }

    @Override
    public void updateQuestionnaireTitle(UserContext uc, String id, String title) {
        QuestionnaireEntity entity = questionnaireRepository.findOne(id);

        if (entity == null) {
            return;
        }

        entity.setTitle(markdownService.toMarkdownContent(uc, title));

        questionnaireRepository.save(entity);
    }

    @Override
    public void exportQuestionnareToPdf(Questionnaire questionnaire, OutputStream outputStream) {
        try {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("questionnaireName", questionnaire.getName());

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(questionnaire.getQuestions());

            InputStream reportStream = this.getClass().getResourceAsStream("/reports/questionnaire.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
