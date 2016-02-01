<#import "../templates.ftl" as templates />
<#import "admin-common.ftl" as common />

<#assign sidebar>
    <@common.sidebar activeMenuItem='dashboard' />
</#assign>

<@templates.layoutWithSidebar
        chapter='admin'
        subTitle='Общая информация о системе'
        pageTitle='Dashboard'
        sidebar=sidebar
        mainContainerClass='co-rightbordered'>

    <ul>
        <li>
            Общее число зарегистрированных пользователей:
            <strong>${countAllRegisteredUsers}</strong>
        </li>
        <li>
            Число зарегистрированных за последние 2 недели пользователей:
            <strong>${countLastRegisteredUsers}</strong>
        </li>
        <li>
            Список из 10 последних зарегистрировавшихся пользователей:
            <ol>
                <#list lastRegisteredUsers as user>
                    <#--@ftlvariable name="user" type="ru.atott.combiq.service.bean.User"-->
                    <li>
                        ${user.login}, ${user.type}, ${user.name}
                        <#if user.registerDate??>
                            , ${user.registerDate?string["dd.MM.yyyy"]}
                        </#if>
                    </li>
                </#list>
            </ol>
        </li>
    </ul>

</@templates.layoutWithSidebar>