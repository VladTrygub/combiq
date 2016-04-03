<#import "../_layout/templates.ftl" as templates />
<#import "../_layout/functions.ftl" as functions />

<#macro projectLayout tab='about' title='О проекте'>
    <@templates.layoutBody
        chapter="about"
        subTitle="О проекте combiq.ru">

    <div class="co-toolbox text-center" style="background-color: #BBDEFB">
        <div class="container">
            <span class="glyphicon glyphicon-bullhorn" aria-hidden="true"></span>
            Мы запустили форум для разработчиков Combiq.ru - <a href="http://forum.combiq.ru">http://forum.combiq.ru</a>, присоединяйтесь!
        </div>
    </div>


    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <h1>${title}</h1>
                <div class="subtitle">О проекте Combiq.ru</div>
            </div>
        </div>
    </div>

    <div class="container" style="margin-bottom: 40px;">
        <ul class="nav nav-tabs">
            <li class="${functions.if(tab == 'about', 'active')}">
                <a href="/project">О проекте</a>
            </li>
            <li class="${functions.if(tab == 'resources', 'active')}">
                <a href="/project/resources">Проектные ресурсы</a>
            </li>
            <li class="${functions.if(tab == 'wtf', 'active')}">
                <a href="/project/wtf">Быстрый старт</a>
            </li>
        </ul>
    </div>

    <#nested />

    </@templates.layoutBody>
</#macro>

<#macro projectWtfLayout title='' wtfTab='site'>
    <@projectLayout tab='wtf' title=title>
        <div class="container">
            <div class="row">
                <div class="col-md-9 col-sm-9">
                    <#nested />
                </div>
                <div class="col-md-3 col-sm-3">
                    <ul class="nav nav-pills nav-stacked" style="margin-bottom: 20px;">
                        <li role="presentation" class="${functions.if(wtfTab == 'site', 'active')}">
                            <a href="/project/wtf">Сайт Combiq.ru</a>
                        </li>
                        <li role="presentation" class="${functions.if(wtfTab == 'android', 'active')}">
                            <a href="/project/wtf/android">Android приложение</a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </@projectLayout>
</#macro>