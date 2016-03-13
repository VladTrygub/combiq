<#import "functions.ftl" as functions />

<#macro contentEditor content url=''>
    <#-- @ftlvariable name="content" type="ru.atott.combiq.dao.entity.MarkdownContent" -->

    <#if functions.hasRoleSaOrContenter()>
        <co-contenteditor params="
                markdown: '${(content.markdown)!?html?js_string}',
                html: '${(content.html)!?html?js_string}',
                url: '${functions.if(url == '', "/content/" + content.id!, url)}'">
        </co-contenteditor>
    <#else>
        ${(content.html)!''}
    </#if>
</#macro>

<#macro questionLevel level class=''>
    <span class="co-level ${class} co-level-${level?lower_case}">
        <a title="Показать все вопросы с ${explainLevel(level)} уровнем" href="/questions/level/${level}">${level}</a>
    </span>
</#macro>

<#function explainLevel level>
    <#switch level>
        <#case "D1"><#return "Junior" />
        <#case "D2"><#return "Middle" />
        <#case "D3"><#return "Senior" />
    </#switch>
    <#return level />
</#function>

<#macro latestCommentList list>
    <#-- @ftlvariable name="lastComment" type="ru.atott.combiq.service.search.comment.LatestComment" -->

    <ul class="co-comments co-comments-latest">
        <#list list as latestComment>
            <li>
                <div class="co-comments-question-title">
                    <span class="co-level-bound">
                        <@questionLevel level=latestComment.question.level class='co-small' />
                    </span><a href="${urlResolver.getQuestionUrl(latestComment.question)}">${latestComment.question.title}</a>
                </div>
                <div class="co-comments-question-comment">
                    ${latestComment.commentSimplifiedHtml?html}
                </div>
            </li>
        </#list>
    </ul>
</#macro>

<#macro latestCommentFeedList>
    <#if latestCommentFeed?? && latestCommentFeed?size != 0>
        <div class="co-comments-latest-feed">
            <h4>Лента обновлений</h4>
            <@latestCommentList list=latestCommentFeed />
        </div>
    </#if>
</#macro>