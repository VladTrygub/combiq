<#import "_layout/templates.ftl" as templates />
<#import "_layout/parts.ftl" as parts />
<#import "_layout/functions.ftl" as functions />

<#assign sidebar>

    <#if functions.hasRole('sa') || functions.hasRole('contenter')>
        <div>
            <button class="btn btn-primary" onclick="ko.openDialog('co-questionposter');">
                Добавить новый вопрос
            </button>
        </div>
    </#if>

    <div>
        <h4>Интересное</h4>
        <ol class="list-unstyled co-question-aside-tips">
            <li>
                <div class="row">
                    <div class="col-md-1">
                        <span class="glyphicon glyphicon-link" aria-hidden="true"></span>
                    </div>
                    <div class="col-md-10">
                        Freak вопросы, которые вам могут задать на собеседовании.
                        <a href="/questions/tagged/freak">Будьте осторожны →</a>
                    </div>
                </div>
            </li>
        </ol>
    </div>

    <div>
        <h4>Популярное</h4>
        <ul class="co-popularTags">
           <#list popularTags as tag>
            <li>
                <a  class="co-tag" href="/questions/tagged/${tag.value}">
                   ${tag.value}
                </a>
                   <#if tag.docCount??>
                       <span class="co-tag-counter">
                           &nbsp;× ${tag.docCount}
                       </span>
                   </#if>
            </li>
           </#list>
        </ul>
        <p>
            <a href="/tags">Смотреть все тэги →</a>
        </p>
    </div>
</#assign>

<#assign pageTitle>
    <#if questionsCatalog>
        Вопросы
    </#if>
</#assign>

<@templates.layoutWithSidebar head=head dsl=dsl sidebar=sidebar chapter='questions' subTitle=subTitle!'' pageTitle=pageTitle>
    <form action="/questions/search" method="get" id="searchForm">
        <div class="co-search">
            <div class="row">
                <div class="col-md-10">
                    <input id="searchBox" autofocus placeholder="Поисковый запрос" autocomplete="off" type="text" name="q" value="${dsl!}"/>
                </div>
                <div class="col-md-2">
                    <button type="submit" class="pull-right">
                        ENTER
                        <img src="/static/images/down_right-16.png">
                    </button>
                </div>
            </div>
        </div>
        <div class="co-search-help-tip">
            <co-onlywithcomments params="enabled: ${searchOnlyWithComments?c}"></co-onlywithcomments>
            <a class="pull-right" href="https://github.com/atott/combiq/wiki/%D0%9F%D0%BE%D0%B8%D1%81%D0%BA">
                Вы можете задавать гибкие условия поиска, например, по тэгам или уровню <span class="co-arrow">→</span>
            </a>
        </div>
    </form>

    <#-- Advices -->
    <#if showMatrixCompetenceAdvice?? && showMatrixCompetenceAdvice!false>
        <div class="co-search-advice">
            <span class="glyphicon glyphicon-link" aria-hidden="true"></span>
            Ознакомьтесь с <a href="/education/competence">матрицей компетенций Java разработчиков</a>, чтобы
            определить уровень знаний.
        </div>
    </#if>
    <ul class="co-questions">
    <#if questions?size==0>К сожалению по такому запросу ничего не найдено
        <#else>Нашлось ${utils.pluralizeQuestionLabel(questionsCount)}
    </#if>
        <#list questions as question>
            <li>
              <div class="co-component-question">
                    <div class="co-component-question-level-bound">
                        <div class="co-component-question-level">
                            <div>
                                <@parts.questionLevel question.level />
                            </div>
                            <div class="co-component-question-level-desc">уровень</div>
                        </div>
                    </div>
                    <div class="co-component-question-content">
                        <div class="co-component-question-text">
                            <a href="${urlResolver.getQuestionUrl(question, 'index=' + (paging.from + question_index) + '&dsl=' + dsl?url)}">${question.title}</a>
                        </div>
                        <div>
                            <ul class="co-component-question-tags">
                                <#list question.tags as tag>
                                  <li>
                                     <a class="co-tag" href="/questions/tagged/${tag}">
                                        ${tag}
                                     </a>
                                   </li>
                                </#list>
                                <#if question.comments?size != 0>
                                    <li>
                                        <a class="co-search-comment" href="${urlResolver.getQuestionCommentsUrl(question, 'index=' + (paging.from + question_index) + '&dsl=' + dsl?url)}" title="Перейти к комментариям вопроса">
                                            <span class="glyphicon glyphicon-comment"></span>
                                            ${question.comments?size}
                                        </a>
                                    </li>
                                </#if>
                            </ul>
                        </div>
                    </div>
              </div>
            </li>
        </#list>
    </ul>
    <@templates.paging paging=paging />
</@templates.layoutWithSidebar>