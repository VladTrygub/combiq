<#import "../_layout/templates.ftl" as templates />
<#import "../_layout/parts.ftl" as parts />
<#import "../_layout/functions.ftl" as functions />

<#macro userLayout>

    <#assign head>
        <link rel="stylesheet" href="/static/css/user.css?v=${resourceVersion}">
    </#assign>

    <@templates.layoutWithoutSidebar head=head>

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
    </@templates.layoutWithoutSidebar>
</#macro>