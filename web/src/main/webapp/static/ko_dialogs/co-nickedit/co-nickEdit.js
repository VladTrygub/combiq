define(['knockout', 'ajax', 'knockoutValidation'], function(ko, ajax, validation) {

    function ViewModel(params) {
        this.width = 460;
        this.align = 'top';
        this.buttons = [];
        this.nick = ko.wrap(params.nick);
        this.sticked = true;
        this.posting = ko.wrap(false);
        this.nick = ko.observable().extend({
            required: {params: true, message: 'Ник не может быть пустым'},
            minLength: {params: 1, message: 'Минимум 1 символ'},
            maxLength: {params: 40, message: 'Максимум 40 символов'},
            pattern: {params: '^[A-Za-z0-9]{1}[A-Za-z0-9 ]{0,39}$',
                      message: 'Ник должен состоять только из латинских букв и цифр'}
        });
    }

    ViewModel.prototype.editButtonClick = function() {

        var url = "/users/" + window.co.userId + "/" + this.nick().trim();
        if(this.nick.isValid()) {
            this
                .save()
                .done(function () {
                    window.location = url;
                });
            return false;
        }
        return false;
    };

    ViewModel.prototype.save = function() {
        var self = this;
        self.posting(true);
        var json = {
            nick: this.nick().trim()
        };

        return ajax
            .rest('POST', '/users/setNick', json)
            .always(function() {
                self.posting(false);
            });
    };

    return ViewModel;
});
