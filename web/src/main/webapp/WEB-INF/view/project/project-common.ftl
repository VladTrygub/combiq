<#import "../_layout/templates.ftl" as templates />
<#import "../_layout/functions.ftl" as functions />

<#macro projectLayout tab='about' title='О проекте'>
    <@templates.layoutBody
        chapter="about"
        subTitle="О проекте combiq.ru">

    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <h1>${title}</h1>
                <div class="subtitle">О проекте Combiq.ru</div>
            </div>
        </div>
    </div>

    <div class="container" style="margin-bottom: 60px;">
        <ul class="nav nav-tabs">
            <li class="${functions.if(tab == 'about', 'active')}">
                <a href="/project">О проекте</a>
            </li>
            <li class="${functions.if(tab == 'resources', 'active')}">
                <a href="/project/resources">Проектные ресурсы</a>
            </li>
            <#--<li class="${functions.if(tab == 'wtf', 'active')}">
                <a href="/project/wtf">WTF</a>
            </li>-->
        </ul>
    </div>

    <#nested />

    </@templates.layoutBody>
</#macro>

<#macro projectWtfLayout>
    <@projectLayout>
        <#nested />
    </@projectLayout>
</#macro>