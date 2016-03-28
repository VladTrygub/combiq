<#import "_layout/templates.ftl" as templates />

<@templates.layoutHtml showFooter=false showHeader=false gtmPageName='login'>
    <div class="container" style="min-height: 400px;">
        <div class="row row-centered">
            <div class="col-md-4"></div>
            <div class="col-md-4">
                <div class="co-login co-login-bordered">
                    <div class="co-login-logo">
                        <a href="/"><img src="/static/images/site/flat-logo-64.png" ></a>
                    </div>

                    <h1>Войти на Combiq.ru</h1>

                    <div class="text-center">
                        <#if githubEnable>
                        <p>
                            <a class="btn btn-default" href="https://github.com/login/oauth/authorize?client_id=${githubClientId}&scope=user&state=${githubClientState}:">
                                через <b>github.com</b>
                            </a>
                        </p>
                        </#if>
                        <#if (vkEnable || facebookEnable) && githubEnable>
                        <p class="co-login-delimiter">
                            - или -
                        </p>
                        </#if>
                        <#if vkEnable || facebookEnable>
                        <p>
                            <#if vkEnable>
                            <#assign vkCallbackUrl = urlResolver.externalize("/login/callback/vk.do") />
                            <a class="btn btn-default" href="https://oauth.vk.com/authorize?client_id=${vkClientId}&display=page&response_type=code&state=${vkClientState}:&redirect_uri=${vkCallbackUrl?url}">
                                через <b>vk.com</b>
                            </a>
                            </#if>

                            <#if facebookEnable>
                            <#assign facebookCallbackUrl = urlResolver.externalize("/login/callback/facebook.do") />
                            <a class="btn btn-default" href="https://www.facebook.com/dialog/oauth?client_id=${facebookClientId}&scope=public_profile&redirect_uri=${facebookCallbackUrl?url}&state=${facebookClientState}:">
                                через <b>fb.com</b>
                            </a>
                            </#if>
                        </p>
                        </#if>
                        <#if stackexchangeEnable && (vkEnable || facebookEnable || githubEnable)>
                        <p class="co-login-delimiter">
                            - или -
                        </p>
                        </#if>
                        <#if stackexchangeEnable>
                        <p>
                            <#assign stackexchangeCallbackUrl = urlResolver.externalize("/login/callback/stackexchange.do") />
                            <a class="btn btn-default" href="https://stackexchange.com/oauth?client_id=${stackexchangeClientId}&scope=no_expiry&redirect_uri=${stackexchangeCallbackUrl?url}&state=${stackexchangeClientState}:">
                                через <b>stackoverflow.com</b>
                            </a>
                        </p>
                        </#if>
                    </div>
                    <div class="co-small" style="margin-bottom: -30px; text-align: left; padding-left: 18px; padding-top: 34px;">
                        <a href="/">← Вернуться на главную страницу</a>
                    </div>
                </div>
            </div>
            <div class="col-md-4"></div>
        </div>
    </div>
</@templates.layoutHtml>