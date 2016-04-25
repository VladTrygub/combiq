<#import "user-common.ftl" as common />
<#import "../_layout/templates.ftl" as templates />
<#import "../_layout/parts.ftl" as parts />
<#import "../_layout/functions.ftl" as functions />

<@common.userLayout>

    <div>
        <div class="co-userphoto pull-right">
            <img src="${headAvatarUrl}">
        </div>
        <div>
            <h1>${userName}</h1>
            <#if userRegisterDate??>
                <div>
                    Зарегистрировался ${userRegisterDate?string["dd MMMM yyyy г."]}
                </div>
            </#if>
            <#if nick??>
            <div>
                 Ник: ${nick}  <a href="" onclick="ko.openDialog('co-editnick'); return false;">Изменить</a>
            </div>
            <#else>
                <a href="" onclick="ko.openDialog('co-editnick'); return false;">Выберите себе Ник</a>
            </#if>
            <co-questionssearch params="
                title: 'Избранные вопросы',
                dsl: 'favorite:true',
                size: 5">
            </co-questionssearch>
        </div>
    </div>
        </div>
    </div>

    <div>
        <label for="email">Email:</label>
        <input readonly type="text" value="${email!?html}">

        <button onclick="
                ko.openDialog('co-editprofileemail', {
                    email: '${email!?js_string}',
                    userId: '${profileUserId?js_string}'
                })">
            Изменить email
        </button>
    </div>

    <co-questionssearch params="
        title: 'Избранные вопросы',
        dsl: 'favorite:true',
        size: 5">
    </co-questionssearch>

</@common.userLayout>