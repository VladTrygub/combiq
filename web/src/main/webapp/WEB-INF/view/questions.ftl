<#import "templates.ftl" as templates />

<#assign head>
    <link rel="import" href="/static/elements/co-question.html">
</#assign>

<@templates.layoutWithSidebar head=head>
    <ul>
        <#list questions as question>
            <li>
                <co-question>${question.title}</co-question>
            </li>
        </#list>
    </ul>
    <@templates.paging paging=paging />
</@templates.layoutWithSidebar>