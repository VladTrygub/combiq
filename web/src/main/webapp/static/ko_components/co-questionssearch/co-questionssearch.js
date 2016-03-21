define(['knockout', 'ajax'], function(ko, ajax) {
    function ViewModel(params) {
        this.title = ko.wrap(params.title)
        this.preloader = ko.wrap();
        this.sizeDescr = ko.wrap();
        this.questions = ko.wrap();
        this.findAll = ko.wrap();
        this.size = params.size != undefined ? params.size : '';
        this.dsl = params.dsl !== undefined ? params.dsl : '';
    };
    ViewModel.prototype.init = function() {
        var self = this;
        $.get('/questions/search/json',
                {
                    q: self.dsl,
                    size: self.size
                },
                function(data) {
                    self.preloader().hide();
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
                });
    };
    return ViewModel;
});
