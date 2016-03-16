define(['knockout'], function(ko) {
     function ViewModel(params) {
         this.favorite = ko.wrap(params.favorite);
         this.isUser = ko.wrap(params.isUser);
         this.id = ko.wrap(params.id);
         this.stars = ko.wrap(params.stars);
     }

     ViewModel.prototype.like = function(){
         $.post('/questions/' + this.id() + '/like');
         this.favorite(true);
         this.stars(this.stars() + 1);
     }

     ViewModel.prototype.dislike = function(){
         $.post('/questions/' + this.id() + '/dislike');
         this.favorite(false);
         this.stars(this.stars() - 1);
     }

     ViewModel.prototype.login = function(){
         document.location.href = '/login.do';
     }

     return ViewModel;
});