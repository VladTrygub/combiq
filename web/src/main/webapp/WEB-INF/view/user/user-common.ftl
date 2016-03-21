<#import "../_layout/templates.ftl" as templates />
<#import "../_layout/parts.ftl" as parts />
<#import "../_layout/functions.ftl" as functions />

<#macro userLayout>

    <#assign head>
        <link rel="stylesheet" href="/static/css/user.css?v=${resourceVersion}">
    </#assign>
    <#assign sidebar>
        <co-questionssearch params="title: 'Добавленные в избранное', dsl: 'favorite:true'"></co-questionssearch>
    </#assign>
    <@templates.layoutWithSidebar head=head sidebar=sidebar>

        <div class="co-userpage">

            <ul class="nav nav-tabs co-userpage-tabs">
                <li class="active">
                    <a href="#">Профиль</a>
                </li>
            </ul>

            <div class="co-userpage-content">
                <#nested />
            </div>
        </div>
    </@templates.layoutWithSidebar>

</#macro>