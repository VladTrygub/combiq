<#import "user-common.ftl" as common />
<#import "../_layout/templates.ftl" as templates />
<#import "../_layout/parts.ftl" as parts />
<#import "../_layout/functions.ftl" as functions />

<@common.userLayout>

    <div class="row">
        <div class="col-md-3">
            <div class="co-userphoto pull-right">
                <img src="${headAvatarUrl}">
            </div>
        </div>
        <div class="col-md-9">
            <h1>${userName}</h1>
            <#if userRegisterDate??>
                <div>
                    Зарегистрировался ${userRegisterDate?string["dd MMM yyyy г."]}
                </div>
            </#if>
            <div>
                 Nick ${nickName}  <a onclick="ko.openDialog('co-nickEdit'); return false;">Изменить</a>
            </div>
        </div>
    </div>


</@common.userLayout>