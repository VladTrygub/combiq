<#import "project-common.ftl" as common>
<#import "../_layout/parts.ftl" as parts />

<@common.projectLayout>

    <div class="container">
        <div class="row">
            <div class="col-md-8">
                <p style="font-size: 18px;">
                    Combiq.ru - это проект с открытым исходным кодом,
                    цель которого собрать в одном месте всю полезную информацию
                    для Java программистов, которые готовятся к собеседованию
                    на новое место работы.
                </p>
            </div>
        </div>
    </div>

    <div class="container">
        <div class="row">
            <div class="col-md-8">
                <@parts.contentEditor content=pageContent />
            </div>
        </div>
    </div>
</@common.projectLayout>