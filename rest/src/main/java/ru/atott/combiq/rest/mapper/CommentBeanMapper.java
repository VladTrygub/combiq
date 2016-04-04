package ru.atott.combiq.rest.mapper;

import ru.atott.combiq.dao.entity.QuestionComment;
import ru.atott.combiq.rest.bean.CommentBean;
import ru.atott.combiq.rest.bean.MarkdownContentBean;
import ru.atott.combiq.rest.bean.UserBean;
import ru.atott.combiq.rest.utils.BeanMapper;
import ru.atott.combiq.rest.utils.RestContext;

public class CommentBeanMapper implements BeanMapper<QuestionComment, CommentBean> {

    @Override
    public CommentBean map(RestContext context, QuestionComment source) {
        CommentBean bean = new CommentBean();
        bean.setContent(new MarkdownContentBean(source.getContent()));
        bean.setId(source.getId());
        bean.setEditDate(source.getEditDate());
        bean.setPostDate(source.getPostDate());

        if (source.getUserId() != null) {
            String uri = context.getUrlResolver().getUserUrl(source.getUserId());
            uri = context.getUrlResolver().externalize(uri);
            bean.setAuthor(new UserBean(source.getId(), source.getUserName(), uri));
        }

        if (source.getEditUserId() != null) {
            String uri = context.getUrlResolver().getUserUrl(source.getEditUserId());
            uri = context.getUrlResolver().externalize(uri);
            bean.setAuthor(new UserBean(source.getEditUserId(), source.getEditUserName(), uri));
        }

        return bean;
    }
}
