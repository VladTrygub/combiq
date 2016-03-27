<#import 'functions.ftl' as functions>

<#macro topMenu chapter=''>
    <header>
        <div class="co-menu-top">
            <div class="container">
                <div class="row">
                    <div class="col-md-2">
                        <a class="co-topmenu-mainer" href="/" title="Combiq.ru - Всё, что может потребоваться для подготовки к Java собеседованию" style="line-height: 48px;">
                            <img style="margin-bottom: -8px;" src="/static/images/site/flat-logo-64.png?v=2" />
                        </a>
                    </div>
                    <div class="col-md-7">
                        <ul class="co-menu-top-items">
                            <li class="${functions.if(chapter == 'questions', 'active')}">
                                <a href="/questions">Вопросы</a>
                            </li>
                            <li class="${functions.if(chapter == 'interview', 'active')}">
                                <a href="/interview">Собеседование</a>
                            </li>
                            <li class="${functions.if(chapter == 'job', 'active')}">
                                <a href="/job">Работа</a>
                            </li>
                            <li class="${functions.if(chapter == 'about', 'active')}">
                                <a href="/project" title="О проекте Combiq.ru">
                                    <img style="margin-top: -24px; margin-bottom: -10px;" src="/static/images/site/OpenSource.png" alt="О проекте Combiq.ru">
                                </a>
                            </li>
                            <#if functions.hasRole("sa") || functions.hasRole("contenter")>
                            <li class="${functions.if(chapter == 'admin', 'active')}">
                                <a href="/admin">Админка</a>
                            </li>
                            </#if>
                        </ul>
                    </div>
                    <div class="col-md-3">
                        <ul class="co-menu-top-items pull-right">
                            <#if user??>
                                <li class="co-auth">
                                    <div class="btn-group co-profile-button">
                                        <button type="button" class="dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                            <a class="co-inline" href="${urlResolver.getUserUrl(user.id,user.nickName)}">
                                                <#if user.headAvatarUrl??>
                                                    <img style="margin-top: -20px; margin-bottom: -15px;" width="46" height="46" src="${user.headAvatarUrl!}">
                                                </#if>
                                            </a>
                                            <span class="caret"></span>
                                        </button>
                                        <ul class="dropdown-menu pull-right">
                                            <li>
                                                <a href="${urlResolver.getUserUrl(user.id,user.nickName)}">Мой профиль</a>
                                            </li>
                                            <li role="separator" class="divider"></li>
                                            <li>
                                                <a href="/logout.do">Выйти</a>
                                            </li>
                                        </ul>
                                    </div>
                                </li>
                            <#else>
                                <li class="co-auth">
                                    <a onclick="ko.openDialog('co-login'); return false;" href="/login.do">Войти</a>
                                </li>
                            </#if>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </header>
</#macro>