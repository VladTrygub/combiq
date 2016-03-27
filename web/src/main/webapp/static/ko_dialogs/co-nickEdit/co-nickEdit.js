define([], function() {

    function ViewModel(params) {
        this.width = 460;
        this.align = 'top';
        this.buttons = [];
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
            commentId: this.commentId,
            content: this.markdown()
        };

        return ajax
            .rest('POST', '/questions/' + this.questionId + '/comment', json)
            .always(function() {
                self.posting(false);
            });
    };

    return ViewModel;
});
