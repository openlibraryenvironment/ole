(function(){
    angular.module('ngLoadingSpinner', ['angularSpinner'])
    .directive('usSpinner',   ['$http', '$rootScope' ,function ($http, $rootScope){
        return {
            link: function (scope, elm, attrs)
            {
                $rootScope.spinnerActive = false;
                scope.isLoading = function () {
                    return $http.pendingRequests.length > 0;
                };

                scope.$watch(scope.isLoading, function (loading)
                {
                    $rootScope.spinnerActive = loading;
                    if(loading){
                        $('body').append('<div id="mask"></div>');
                        $('#mask').fadeIn(300);
                        $('body').scrollTop(0);
                        $(window.parent).scrollTop(0);
                        elm.removeClass('ng-hide');
                    }else{
                        $('#mask').fadeOut(300);
                        elm.addClass('ng-hide');
                    }
                });
            }
        };

    }]);
}).call(this);