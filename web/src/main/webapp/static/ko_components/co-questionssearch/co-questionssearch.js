define(['knockout', 'ajax'], function(ko, ajax) {

    function ViewModel(params) {
        this.title = ko.wrap(params.title);
        this.pageSize = params.size || 10;
        this.dsl = params.dsl || '';

        this.preloader = ko.wrap();
        this.sizeDescr = ko.wrap();
        this.searchAll = ko.wrap();

        this.questions = ko.wrap();
        this.size = ko.wrap();
        this.totalSize = ko.wrap();

        this.search();
    }

    ViewModel.prototype.search = function() {
        var self = this;

        var params = {
            dsl: this.dsl,
            pageSize: this.pageSize
        };

        ajax
            .rest('GET', '/rest/v1/question', params)
            .done(function(data) {
                var totalSize = data.totalElements;
                var size = self.size.length > 0 ? self.size : 10;
                size = size < totalSize ? size : totalSize;
                self.size(size);
                self.totalSize(totalSize);
                self.sizeDescr().show();
                self.questions(data.questions);
                self.searchAll().show();
            })
            .always(function() {
                self.preloader().hide();
            });
    };
    return ViewModel;
});
