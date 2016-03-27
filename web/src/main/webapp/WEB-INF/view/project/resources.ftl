<#import "project-common.ftl" as common>

<@common.projectLayout tab='resources' title='Проектные ресурсы'>

    <div class="container co-project-resources">

        <#-- Gihub -->
        <div class="row">
            <div class="col-md-1">
                <div class="co-project-resource">
                    <img src="/static/images/site/Octocat.png" style="margin-top: 10px;">
                </div>
            </div>
            <div class="col-md-11">
                <h2>Репозиторий кода</h2>
                <ul>
                    <li>
                        <a href="https://github.com/combiq/combiq">https://github.com/combiq/combiq</a> -
                        основной проект, веб-сайт <a href="http://combiq.ru">Combiq.ru</a>.
                    </li>
                    <li>
                        <a href="https://github.com/combiq/android">https://github.com/combiq/android</a> -
                        проект мобильного приложения на платформе android.
                    </li>
                </ul>
            </div>
        </div>

        <div class="row">
            <div class="col-md-1">
                <div class="co-project-resource">
                    <img src="/static/images/site/jira.png">
                </div>
            </div>
            <div class="col-md-11">
                <h2>Трекер задач</h2>
                <ul>
                    <li>
                        <a href="http://jira.combiq.ru/">http://jira.combiq.ru/</a> - трекер задач проекта.
                        Здесь мы планируем наши задачи и общаемся.
                    </li>
                </ul>
            </div>
        </div>

        <#-- Test resources -->
        <div class="row">
            <div class="col-md-1">
                <div class="co-project-resource">
                    <img src="/static/images/image.png">
                </div>
            </div>
            <div class="col-md-11">
                <h2>Стенды для тестирования</h2>
                <ul>
                    <li>
                        <a href="http://uat.combiq.ru/">http://uat.combiq.ru/</a> - стенд для функционального
                        тестирования.
                    </li>
                    <li>
                        <a href="http://mobile.combiq.ru/">http://mobile.combiq.ru/</a> - интеграционный стенд для
                        разработки под мобильные устройства (<a href="http://mobile.combiq.ru/rest/v1">rest-api</a>).
                    </li>
                </ul>
            </div>
        </div>

        <#-- Rest resources -->
        <div class="row">
            <div class="col-md-1">
                <div class="co-project-resource">
                    <img src="/static/images/site/teamcity.png">
                </div>
            </div>
            <div class="col-md-11">
                <h2>Другое</h2>
                <ul>
                    <li>
                        <a href="http://teamcity.combiq.ru/">http://teamcity.combiq.ru/</a> - здесь мы собираем и
                        деплоим наш проект.
                    </li>
                    <li>
                        <a href="http://kibana.combiq.ru/">http://kibana.combiq.ru/</a> - здесь мы собираем
                        логи с наших стендов.
                    </li>
                </ul>
            </div>
        </div>

    </div>

</@common.projectLayout>