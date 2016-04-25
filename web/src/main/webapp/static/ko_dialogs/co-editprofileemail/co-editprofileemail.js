define(['knockout', 'ajax'], function(ko, ajax) {

    function ViewModel(params) {
        this.email = ko.wrap(params.email);
        this.userId = ko.wrap(params.userId);
        this.buttons = [];
    }

    ViewModel.prototype.apply = function() {
        var restUrl = coUtils.getRestUrl('/rest/v1/user/{userId}/profile/email', {
            userId: this.userId()
        });

        var json = {
            email: this.email()
        };

        ajax
            .rest('POST', restUrl, json)
            .done(function() {
                alert('111');
            });
    };

    return ViewModel;
});