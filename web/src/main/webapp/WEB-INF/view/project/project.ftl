<#import "project-common.ftl" as common>

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
                <h2>Для кого наш проект?</h2>
                <p>
                    Combiq.ru, скорее, информационный проект, нежели технологический/программный продукт.
                    Однако, проект, в основном, разрабатывается Java программистами. Поэтому
                    стек технологий и способ реализации тех или иных фич интересен и важен.
                </p>
                <p>
                    У нас нет строгой ролевой модели тимлид/разработчик, у нас мало
                    контроля за качеством кода, у нас нечеткое планирование новых фич и багфикса.
                    Если вас это пугает, возможно, вам стоит поискать другой проект ;(.
                    Если же вы решите присоединиться к нашему проекту, то, скорее всего,
                    ваш рабочий код увидит свет на бою, а в команде вам никто не будет
                    указывать что, когда, и каким способом делать. Занимайтесь тем, что интересно :).
                </p>
            </div>
        </div>
        <div class="row">
            <div class="col-md-8">
                <h2>Я решил. Что делать?</h2>
                <p>
                    Попробуйте сначала развернуть проект локально, используйте для этого
                    инструкцию <a href="https://github.com/combiq/combiq/wiki/%D0%A0%D0%B0%D0%B7%D1%80%D0%B0%D0%B1%D0%BE%D1%82%D0%BA%D0%B0">Быстрый старт разработки</a>.
                    Если у вас возникли проблемы попробуйте задать вопрос в комментариях к
                    задаче <a href="http://jira.combiq.ru/browse/CO-14">CO-14</a>.
                </p>
                <p>
                    Выберите в нашем трекере задач понравившуюся (или создайте новую) и попробуйте
                    реализовать её. Вы можете взять в работу любую задачу в статусе TO DO
                    ни на кого не назначенную. Если вы новичок в проекте, то лучше брать задачи
                    с типом Task (обычно они более конкретные чем Story) и меткой small
                    (<a href="http://jira.combiq.ru/issues/?filter=10001&jql=issuetype%20%3D%20Task%20AND%20resolution%20%3D%20Unresolved%20AND%20labels%20%3D%20small">вот фильтр</a>).
                </p>
                <p>
                    Попробуйте реализовать задачу так, как вы это поняли. Постановка задачи
                    не ограничивает вас, постановка задает направление, вы можете реализовать
                    больше или меньше функционала чем это запрошено.
                    Если вы хотите получить больше деталей по постановке (или изменить ее),
                    задавайте вопросы в комментариях к задаче.
                </p>
                <p>
                    Когда задача будет готова и протестирована локально, сделайте pull request
                    в мастер ветку проекта.
                </p>
                <p>
                    В случае любых вопросов, или, может даже, предложений, пишите на почту
                    atott@yandex.ru, предметно и подробно обсудим. Антон. :)
                </p>
            </div>
        </div>

    </div>
</@common.projectLayout>