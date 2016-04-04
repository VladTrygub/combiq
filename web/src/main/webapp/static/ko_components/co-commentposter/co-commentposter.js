define(['ajax'], function(ajax) {

    function ViewModel(params) {
        this.comment = ko.wrap('');
        this.questionId = params.questionId;
        this.focused = ko.wrap();
        this.posting = ko.wrap();
    }

    ViewModel.prototype.postComment = function() {
        if (!this.comment() || !window.co.userId) {
            return;
        }

        var self = this;

        this.posting(true);

        var json = {
            content: this.comment()
        };
        ajax
            .rest('POST', '/rest/v1/question/' + this.questionId + '/comment', json)
            .done(function() {
                new Dialog({
                    content: 'Ваш комментарий успешно размещен на сайте',
                    closeHandler: function() {
                        location.reload();
                    }
                });
            })
            .always(function() {
                self.posting(false);
            });
    };

    ViewModel.prototype.focus = function() {
        if (!window.co.userId) {
            ko.openDialog('co-login');
        } else {
            this.focused(true);
        }
    };

    return ViewModel;
});