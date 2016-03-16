define(['knockout'], function(ko) {

     function ViewModel(params) {
         this.favorite = ko.wrap(params.favorite);
         this.questionId = ko.wrap(params.questionId);
         this.stars = ko.wrap(params.stars);
         this.authenticated = ko.wrap(window.co.userId);
     }

     ViewModel.prototype.like = function() {
         if (!this.authenticated()) {
             ko.openDialog('co-login');
             return;
         }
         $.post('/questions/' + encodeURIComponent(this.questionId()) + '/like');
         this.favorite(true);
         this.stars(this.stars() + 1);
     };

     ViewModel.prototype.dislike = function() {
         if (!this.authenticated()) {
             ko.openDialog('co-login');
             return;
         }
         $.post('/questions/' + encodeURIComponent(this.questionId()) + '/dislike');
         this.favorite(false);
         this.stars(this.stars() - 1);
     };


     return ViewModel;
});