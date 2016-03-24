define(['knockout'], function(ko) {

     function ViewModel(params) {
         this.like = ko.wrap(params.like);
         this.questionId = ko.wrap(params.questionId);
         this.likeCount = ko.wrap(params.likeCount);
         this.authenticated = ko.wrap(window.co.userId);
     }

     ViewModel.prototype.asked = function() {
         if (!this.authenticated()) {
             ko.openDialog('co-login');
             return;
         }
         $.post('/questions/' + encodeURIComponent(this.questionId()) + '/addcount');
         this.like(true);
         this.likeCount(this.likeCount() + 1);
     };

     return ViewModel;
});