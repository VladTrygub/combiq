define(['knockout', 'ajax'], function(ko, ajax) {

    function ViewModel(params) {
        this.title = ko.wrap(params.title);
        this.pageSize = params.size || 10;
        this.dsl = params.dsl || '';
        this.loading = ko.wrap(true);
        this.questions = ko.wrap([]);
        this.size = ko.wrap();
        this.totalSize = ko.wrap();

        this.search();
    }

    ViewModel.prototype.search = function() {
        var self = this;

        this.loading(true);

        var params = {
            dsl: this.dsl,
            pageSize: this.pageSize
        };

        ajax
            .rest('GET', '/rest/v1/question', params)
            .done(function(data) {
                var totalSize = data.total;
                self.size((data.questions && data.questions.length) || 0);
                self.totalSize(totalSize);
                self.questions(data.questions);
            })
            .always(function() {
                self.loading(false);
            });
    };
    return ViewModel;
});
