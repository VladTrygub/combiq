<#import "../_layout/templates.ftl" as templates />
<#import "admin-common.ftl" as common />

<#assign sidebar>
    <@common.sidebar activeMenuItem='users' />
</#assign>

<@templates.layoutWithSidebar
        chapter='admin'
        subTitle='Зарегистрированные пользователи системы'
        pageTitle='Пользователи'
        sidebar=sidebar
        mainContainerClass='co-rightbordered'>

    <table class="table table-bordered">
        <thead>
            <tr>
                <th>Логин</th>
                <th>Имя</th>
                <th>Дата регистрации</th>
                <th>Роли</th>
            </tr>
        </thead>
        <tbody>
            <#list registeredUsers as user>
                <#-- @ftlvariable name="user" type="ru.atott.combiq.service.bean.User" -->

                <tr>
                    <td>
                        <div>
                            <strong>${user.type}/${user.login!}</strong>
                        </div>
                        <div>
                            <span class="co-small">
                                id: ${user.id}
                            </span>
                        </div>
                    </td>
                    <td>
                        ${user.name!}
                    </td>
                    <td>
                        <#if user.registerDate??>
                            ${user.registerDate?string["dd.MM.yyyy"]}
                        </#if>
                    </td>
                    <td>
                        <#if user.roles??>
                            <#list user.roles as role>
                            ${role}
                                <#if role_has_next>
                                    ,
                                </#if>
                            </#list>
                        </#if>
                    </td>
                </tr>
            </#list>
        </tbody>
    </table>

    <@templates.paging paging=paging />
</@templates.layoutWithSidebar>