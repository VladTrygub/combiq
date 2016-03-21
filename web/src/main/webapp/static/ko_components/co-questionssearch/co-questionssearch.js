define(['knockout', 'ajax'], function(ko, ajax) {

    function ViewModel(params) {
        this.title = ko.wrap(params.title);
        this.preloader = ko.wrap();
        this.sizeDescr = ko.wrap();
        this.questions = ko.wrap();
        this.findAll = ko.wrap();
        this.size = params.size || 10;
        this.dsl = params.dsl || '';

        this.search();
    }

    ViewModel.prototype.search = function() {
        var self = this;

        var params = {
            q: this.dsl,
            size: this.size
        };

        ajax
            .rest('GET', '/questions/search', params)
            .done(function(data) {
                var totalSize = data.totalElements;
                var size = self.size.length > 0 ? self.size : 10;
                size = size < totalSize ? size : totalSize;
                self.sizeDescr('Первые ' + size + ' из ' +
                    '<a title="Искать все вопросы" href="/questions/search'+
                    (self.dsl.length > 0 ? '?q='+self.dsl : '') +
                    '">'+ totalSize +'</a>' +
                    ' вопросов');
                self.findAll("<a href = '/questions/search"+
                    (self.dsl.length > 0 ? "?q="+self.dsl : "")
                    +"'>Искать все вопросы</a>");
                self.questions(data.content);
            })
            .always(function() {
                self.preloader().hide();
            });
    };
    return ViewModel;
});
