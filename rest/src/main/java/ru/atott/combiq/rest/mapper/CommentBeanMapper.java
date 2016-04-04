package ru.atott.combiq.rest.mapper;

import ru.atott.combiq.dao.entity.QuestionComment;
import ru.atott.combiq.rest.bean.CommentBean;
import ru.atott.combiq.rest.bean.MarkdownContentBean;
import ru.atott.combiq.rest.utils.BeanMapper;
import ru.atott.combiq.rest.utils.RestContext;

public class CommentBeanMapper implements BeanMapper<QuestionComment, CommentBean> {

    @Override
    public CommentBean map(RestContext restContext, QuestionComment source) {
        CommentBean bean = new CommentBean();
        MarkdownContentBean markdownContentBean = new MarkdownContentBean();
        markdownContentBean.setHtml(source.getContent().getHtml());
        markdownContentBean.setMarkdown(source.getContent().getMarkdown());
        bean.setMarkdown(markdownContentBean);
        bean.setId(source.getId());
        bean.setUserId(source.getUserId());
        bean.setUserName(bean.getUserName());
        bean.setEditDate(source.getEditDate());
        bean.setPostDate(source.getPostDate());
        bean.setEditUserId(source.getEditUserId());
        bean.setEditUserName(source.getEditUserName());
        return bean;
    }
}
