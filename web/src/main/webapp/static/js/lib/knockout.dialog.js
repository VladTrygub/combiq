(function() {
    function Dialog(options) {
        this.originalOptions = options;

        var defaults = {
            content: null,
            buttons: ['OK'],
            buttonHandler: function(dialog, button) {},
            closeHandler: function(dialog) {},
            width: null,
            positioned: true,
            closeable: true,
            onShow: function(dialog) {},
            primaryButtonDefaultClass: '',
            buttonDefaultClass: '',
            align: '',   // top, center
            modal: true, // Если true - уже открытые диалоги будут закрыты, за исключением диалогов
                         // у которых выставлен флаг sticked.
            sticked: false
        };

        options = options || {};
        if (typeof (options) == 'string') {
            options = {
                content: options
            };
        }

        this.options = $.extend({}, defaults, options);
        this.show();
        this.resultDeferred = new $.Deferred();
        this.resultPromise = this.resultDeferred.promise();
        this.originalOptions.dialog = this;
    }

    Dialog.openedInstances = [];

    Dialog.prototype.createButtons = function() {
        var self = this;
        var buttons = this.options.buttons;
        if (!buttons) return undefined;

        var buttonsParentNode;
        var buttonNodes = buttons.map(this.createButton.bind(this));
        if (buttonNodes && buttonNodes.length > 0) {
            buttonsParentNode = $('<div class="co-dialog-buttons" />');
            var allButtonsWithoutCustomClasses = buttonNodes.every(function(buttonNode) {
                return !buttonNode.attr('class');
            });
            buttonNodes.forEach(function(buttonNode, index) {
                if (allButtonsWithoutCustomClasses) {
                    // Применить дефалтовую расцветку. Для первой кнопки primary стиль.
                    if (index == 0) {
                        buttonNode.addClass(self.options.primaryButtonDefaultClass);
                    } else {
                        buttonNode.addClass(self.options.buttonDefaultClass);
                    }
                }
                buttonsParentNode.append(buttonNode);
            });
        }
        return buttonsParentNode;
    };

    Dialog.prototype.createButton = function(button) {
        var self = this;
        var node;

        if (!button) {
            return undefined;
        } else if (typeof(button) == 'string') {
            node = $('<button />');
            node.text(button);
        } else if (button instanceof jQuery) {
            node = button;
        } else if (typeof(button) == 'object') {
            node = $('<button />');
            if (typeof(button.content) == 'string') {
                node.text(button.content);
            } else {
                node.append(button.content);
            }
            node.addClass(button.className);
        }

        $(node).click(function() {
            var buttonHandler = self.options.buttonHandler.bind(self.originalOptions);
            if (buttonHandler(self, button) !== false) {
                self.close();
            }
        });

        return node;
    };

    Dialog.prototype.showOverlay = function(zIndex) {
        var self = this;
        this.overlay = $('<div class="co-dialog-overlay" />');
        this.overlay.css({
            opacity: '0.5',
            position: 'fixed',
            top: '0',
            right: '0',
            bottom: '0',
            left: '0',
            'z-index': zIndex
        });
        $(document.body).append(this.overlay);
        this.overlay.click(function() {self.close();});
    };

    Dialog.prototype.result = function(result) {
        this.resultDeferred.resolve(result);
    };

    Dialog.prototype.close = function() {
        var self = this;
        this.options.closeHandler(this);
        Dialog.openedInstances = Dialog.openedInstances.filter(function(dialog) {return dialog != self;});

        this.popup.fadeOut(200, $.proxy(this.popup.remove, this.popup));
        this.overlay.fadeOut(200, $.proxy(this.overlay.remove, this.overlay));
    };

    Dialog.prototype.getMargin = function() {
        this.popup.css({'margin-left': '-' + this.popup.outerWidth() / 2 + 'px'});
    };

    Dialog.prototype.reposition = function(){
        var scrolled = $(document).scrollTop();
        var winH = $(window).height();
        var popH = this.popup.outerHeight();
        var displacement = (popH < winH)? scrolled + winH/2 - popH/2 : scrolled + 10;
        switch (this.options.align) {
            case 'top': {
                displacement = scrolled + 100;
                break;
            }
        }
        this.popup.css({'position': 'absolute', 'top': displacement + 'px'});
        this.getMargin();
    };

    Dialog.zIndexStep = 10;
    Dialog.zIndexCurrentValue = 1000;

    Dialog.prototype.show = function(){
        if (this.options.modal) {
            Dialog.openedInstances.reverse()
                .filter(function(dialog) {
                    return !dialog.options.sticked;
                })
                .forEach(function(dialog) {
                    dialog.close();
                });
        }
        Dialog.openedInstances.push(this);

        Dialog.zIndexCurrentValue += Dialog.zIndexStep;
        this.showOverlay(Dialog.zIndexCurrentValue);
        this.showBody(Dialog.zIndexCurrentValue + 5);
    };

    Dialog.prototype.showBody = function(zIndex) {
        var self = this;

        this.body = $('<div class="co-dialog-body"></div>');
        this.body.append(this.options.content);

        this.popup = $('<div class="co-dialog"></div>')
            .css({
                'z-index': zIndex
            });
        this.popup.append(this.body);
        this.popup.append(this.createButtons());
        if (this.options.closeable) {
            var closeButton = $('<button type="button" class="close" aria-label="Закрыть"><span aria-hidden="true">&times;</span></button>');
            this.popup.append(closeButton);
            closeButton.click(function() { self.close(); })
        }

        if (this.options.width !== null) {
            this.body.width(this.options.width);
        }

        $(document.body).append(this.popup);

        if (this.options.positioned) {
            this.reposition();
        } else {
            this.getMargin();
            switch (this.options.align) {
                case 'center': {
                    var screenHeight = $(window).height();
                    var height = this.popup.height();
                    var top = Math.max((screenHeight - height) / 2 - 100, 100);
                    this.popup.css('top', top);
                    break;
                }
            }
        }

        self.options.onShow(this);
    };


    if (!ko.openDialog) {
        var styles = {
            'co-questionposter': 'css'
        };

        ko.openDialog = function (dialogName, dialogParams) {
            var deferred = new $.Deferred();

            var templateFile = 'text!ko_dialogs/' + dialogName + '/' + dialogName + '.html';
            var viewModelFile = require.toUrl('ko_dialogs/' + dialogName + '/' + dialogName + '.js');

            var deps = [templateFile, viewModelFile];

            if (styles[dialogName]) {
                deps.push('requirejs.css!ko_dialogs/' + dialogName + '/' + dialogName + '.css');
            }

            require(deps, function (template, ViewModel) {
                dialogParams = dialogParams || {};
                var viewModel = new ViewModel(dialogParams);
                viewModel.content = $('<div />').append(template).get()[0];
                ko.applyBindings(viewModel, viewModel.content);
                viewModel.content = $(viewModel.content).contents();
                viewModel.name = (viewModel.name || '') + ' ' + dialogName;
                var dialog = new Dialog(viewModel);
                dialog.resultPromise.done(function (result) {
                    deferred.resolve(result);
                });
            });

            return deferred.promise();
        }
    }

    window.Dialog = Dialog;

    $(document).keydown(function(e) {
        if (e.keyCode == 27) {
            (Dialog.openedInstances || [])
                .forEach(function(dialog) {
                    dialog.close();
                });
        }
    });
})();