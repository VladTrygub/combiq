define(['knockout'], function(ko) {

     function ViewModel(params) {
         this.asked = ko.wrap(params.asked);
         this.questionId = ko.wrap(params.questionId);
         this.askedCount = ko.wrap(params.askedCount);
         this.authenticated = ko.wrap(window.co.userId);
     }

     ViewModel.prototype.ask = function() {
         if (!this.authenticated()) {
             ko.openDialog('co-login');
             return;
         }
         $.post('/questions/' + encodeURIComponent(this.questionId()) + '/asked');
         this.asked(true);
         this.askedCount(this.askedCount() + 1);
     };

     return ViewModel;
});