define(['knockout', 'ajax'], function(ko, ajax) {

    function ViewModel(params) {
        this.width = 720;
        this.align = 'top';
        this.buttons = [];
        this.markdown = ko.wrap(params.commentMarkdown);
        this.commentId = ko.unwrap(params.commentId);
        this.questionId = ko.unwrap(params.questionId);
        this.sticked = true;
        this.posting = ko.wrap(false);
    }

    ViewModel.prototype.saveButtonClick = function() {
        this
            .save()
            .done(function() {
                location.reload();
            });

        return false;
    };

    ViewModel.prototype.save = function() {
        var self = this;

        self.posting(true);

        var json = {
            content: this.markdown()
        };

        return ajax
            .rest('PUT', '/rest/v1/question/' + this.questionId + '/comment/' + this.commentId, json)
            .always(function() {
                self.posting(false);
            });
    };

    return ViewModel;
});