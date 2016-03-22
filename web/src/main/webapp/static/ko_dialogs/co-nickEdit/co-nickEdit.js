define(['knockout', 'ajax'], function(ko, ajax) {

    function ViewModel(params) {
        this.width = 360;
        this.align = 'top';
        this.buttons = [];
    }

    return ViewModel;
});
