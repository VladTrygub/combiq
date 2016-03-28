define(['knockout', 'ajax'], function(ko, ajax) {

    function ViewModel(params) {
        this.width = 460;
        this.align = 'top';
        this.buttons = [];
        this.nickName=ko.wrap(params.nickName);
        this.sticked = true;
        this.posting = ko.wrap(false);
    }

    ViewModel.prototype.editButtonClick = function() {

        var url ="/users/"+window.co.userId+"/"+$("#nickEditBox").val();

        this
            .save()
            .done(function() {
                window.location=url;
            });

        return false;
    };

    ViewModel.prototype.save = function() {
        var self = this;

        self.posting(true);

        var json = {
          /*  userId: this.userId,*/
            nickName: this.nickName()
        };

        return ajax
            .rest('POST', '/users/setNickName', json)
            .always(function() {
                self.posting(false);
            });
    };

    return ViewModel;
});
