<#import "_layout/templates.ftl" as templates />

<#assign head>
    <link rel="stylesheet" href="/static/css/rest.css?v=${resourceVersion}">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.2.0/styles/default.min.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.2.0/highlight.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.2.0/languages/json.min.js"></script>
    <script>hljs.initHighlightingOnLoad();</script>
</#assign>

<@templates.layoutHtml showFooter=false showHeader=true head=head>
    <#-- @ftlvariable name="documentIndex" type="ru.atott.combiq.rest.doc.RestDocumentIndex" -->

    <div class="container">
        <h1>Combiq REST API</h1>

        <div>
            <h2>Для разработчиков</h2>

            <h3>Ответы REST API</h3>
            <p>
                Для ответов rest-api следует использовать объекты классов из пакета
                <code>ru/atott/combiq/rest/bean</code>. Если подходящего ответа еще нет, то его нужно создать.
                В качестве ответов rest-api нельзя использовать внутренние объекты Combiq.
                Постарайтесь воздержаться от наследования бинов-ответов от других классов из этого
                или другого пакетов.
            </p>
            <p>
                Для преобразования внутренних объектов Combiq в бины для отдачи используйте мапперы
                из пакета <code>ru/atott/combiq/rest/mapper</code>. Если нужного маппера нет его нужно
                создать, наследуйте новый маппер от интерфейса
                <code>ru.atott.combiq.rest.utils.BeanMapper</code>. Предполагается, что для каждого запроса к
                rest-api будут использоваться новые экземпляры мапперов (внутреннее состояние маппера
                может содержать некоторый кэш или данные специфичные для текущего запроса),
                воздержитесь от использования расшаренных экземпляров мапперов, например, через статические поля.
                Если для работы маппера вам нужен доступ к объектам spring контекста используйте для доступа к ним
                свойство <code>ru.atott.combiq.rest.utils.RestContext.applicationContext</code>,
                не пробрасывайте сервиса в маппер через конструктор.
            </p>

            <h3>Документирование</h3>
            <p>
                Каждый публичный метод rest-api нужно документировать. Для этого нужно использовать
                стандартный механизм java-doc. Для того чтобы генератор документации подхватил
                новые rest контроллеры их нужно зарегистрировать
                в классе <code>ru.atott.combiq.web.controller.RestController</code>.
                Пример комментария, который содержит все поддерживаемые java-doc тэги и их пояснения:
            </p>
            <pre><code>/**
* Здесь находится общее описание метода. Здесь можно использовать несложные html тэги. В случае,
*       если в теле запроса может передаваться JSON, нужно явно указать пример запроса с использованием
*       тэга request.body.example. Кроме JSON-а нужно документировать все входные параметры
*       метода.
*
* @request.body.example
*      {@link ru.atott.combiq.rest.request.QuestionRequest#EXAMPLE}
*
* @param page
*      Описание параметра page. Типы параметров здесь явно указывать не нужно.
*           Чтобы показать, что параметр устарел и будет скоро удален используйте аннотацию @deprecated
*           для параметров метода.
*
* @response.200.doc
*      Описание ответа метода в случае успеха (ответ 200).
*
* @response.200.example
*      {@link ru.atott.combiq.rest.bean.QuestionBean#EXAMPLE_LIST}
*
* @response.404.doc
*      Описание ошибки в случае ответа 404.
*
* @deprecated
*      Описание причины по которой этот метод считается устаревшим. Дополнительно к документации используйте
*           аннотацию java.lang.Deprecated для устаревших методов.
*/</code></pre>
        </div>

        <div>
            <h2>Index</h2>

            <ul class="co-rest-index">
                <#list documentIndex.buckets as bucket>
                    <li>
                        <ul class="co-rest-bucket">
                            <#list bucket.services as service>
                                <li>
                                    <a href="#${service.uri}">${service.uri}</a>
                                    <#list service.methods as method>
                                        <span class="co-rest-method-ref">
                                            <a href="#${method.uri}_${method.method}">${method.method}</a>
                                        </span>
                                    </#list>
                                </li>
                            </#list>
                        </ul>
                    </li>
                </#list>
            </ul>
        </div>

        <div>
            <h2>Resources</h2>
            <ul class="co-rest-plainlist">
                <#list documentIndex.buckets as bucket>
                    <li>
                        <ul class="co-rest-plainlist">
                            <#list bucket.services as service>
                                <li id="${service.uri}">
                                    <#list service.methods as method>
                                        <@restMethod method />
                                    </#list>
                                </li>
                            </#list>
                        </ul>
                    </li>
                </#list>
            </ul>
        </div>
    </div>
</@templates.layoutHtml>

<#macro restMethod method>
    <#-- @ftlvariable name="method" type="ru.atott.combiq.rest.doc.RestDocumentMethod" -->
    <div id="${method.uri}_${method.method}" class="co-rest-method">

        <#-- Заголовок метода -->
        <div class="co-rest-method-head">
            <strong>${method.method}</strong> ${method.uri}
        </div>

        <#-- Описание метода -->
        <div>
            ${method.comment!}
        </div>

        <#-- Запрос -->
        <#if (method.parameters?? && method.parameters?size != 0) || method.acceptableRepresentation??>

            <h3>Request</h3>

            <div style="margin-left: 50px;">
                <#-- Параметры запроса -->
                <#if (method.parameters?? && method.parameters?size != 0)>
                    <h4>GET параметры запроса</h4>

                    <table class="table table-bordered" style="table-layout: fixed;">
                        <thead>
                            <tr>
                                <th style="width: 120px;">parameter</th>
                                <th style="width: 120px;">type</th>
                                <th>description</th>
                            </tr>
                        </thead>
                        <tbody>
                            <#list method.parameters as parameter>
                                <tr>
                                    <td>
                                        <span class="co-rest-code">${parameter.name}</span>
                                    </td>
                                    <td style="width: 120px; overflow-x: auto;">
                                        <em>${parameter.type}</em>
                                        <#if parameter.defaultValue??>
                                            <div>
                                                Default:
                                                <span class="co-rest-code">${parameter.defaultValue}</span>
                                            </div>
                                        </#if>
                                    </td>
                                    <td>
                                        ${parameter.description!}
                                    </td>
                                </tr>
                            </#list>
                        </tbody>
                    </table>
                </#if>

                <#-- Тело запроса -->
                <#if method.acceptableRepresentation??>
                    <h4>Пример тела запроса</h4>

                    <pre><code class="json hljs">${method.acceptableRepresentation?html}</code></pre>
                </#if>
            </div>
        </#if>

        <#-- Ответ -->
        <#if method.representations?? && method.representations?size != 0>
            <h3>Response</h3>

            <div style="margin-left: 50px;">
                <div>
                    <#list method.representations as representation>
                        <div>
                            <#if representation.code == "200">
                                <span class="co-rest-status">
                                    <span class="co-rest-status-ok">STATUS 200</span>
                                </span>
                            <#else>
                                <span class="co-rest-status">
                                    <span class="co-rest-status-error">STATUS ${representation.code}</span>
                                </span>
                            </#if>
                            <span>${representation.description!}</span>
                        </div>
                        <#if representation.examples?? && representation.examples?size != 0>
                            <div style="margin-top: 10px;">
                                <#list representation.examples as example>
                                    <div>
                                        <h4>Пример ответа</h4>

                                        <pre><code class="json hljs">${example.value?html}</code></pre>
                                    </div>
                                </#list>
                            </div>
                        </#if>
                    </#list>
                </div>
            </div>
        </#if>
    </div>
</#macro>