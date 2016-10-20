/**
 * Created by doublez on 16/4/22.
 */

var YoYoPageUtil = {};

//以下四个参数需要通过loadData方法的param参数传递进来
YoYoPageUtil.total = undefined;//总数据条数
YoYoPageUtil.dataCount = undefined;//当前页面数据条数
YoYoPageUtil.pageFooterId = undefined;//保存分页栏的标签id
YoYoPageUtil.requestUrl = undefined;//请求路径的url
YoYoPageUtil.dataCallBack = undefined;
YoYoPageUtil.queryData = undefined;//查询参数

YoYoPageUtil.currentPage = 1;//当前页码
YoYoPageUtil.pageSize = 10;//默认
YoYoPageUtil.dataArray = undefined;
YoYoPageUtil.totalPage = undefined;
YoYoPageUtil.nextpage = undefined;
YoYoPageUtil.prePage = undefined;
YoYoPageUtil.param = {};
YoYoPageUtil.nextParam = {};
YoYoPageUtil.preParam = {};
YoYoPageUtil.toPageParam = {};


YoYoPageUtil.getFooterContent = function(){
    if(YoYoPageUtil.total == undefined) {
        alert('error:请在param的回调函数中给YoYoPageUtil.total参数赋值');
        return;
    }
    if(YoYoPageUtil.dataArray == undefined) {
        alert('error:请在param的回调函数中给YoYoPageUtil.dataArray参数赋值');
        return;
    }
    if(YoYoPageUtil.dataCount == undefined) {
        alert('error:请在param的回调函数中给YoYoPageUtil.dataCount参数赋值');
        return;
    }
    YoYoPageUtil.totalPage = Math.ceil(YoYoPageUtil.total / YoYoPageUtil.pageSize);
    var content = '<div class="col-xs-12 col-md-12 text-center">当前第'+YoYoPageUtil.currentPage+'页,共'
        +YoYoPageUtil.totalPage+'页，本页有'+YoYoPageUtil.dataArray.length+'条数据，共'+YoYoPageUtil.total+'条数据。' +
        '跳转到&nbsp;<input style="width:40px;" id="toPageNum" value="'+YoYoPageUtil.currentPage+'" type="number" max="'+YoYoPageUtil.totalPage+'"/>&nbsp;页 <button class="btn btn-primary btn-small" id="toPage">确定</button> </div>'+
        '<ul class="pager">' +
        '<li id="first" class=""><a id="hrefFirst" href="">首页</a></li>';
    if(YoYoPageUtil.currentPage > 1) {
        YoYoPageUtil.preParam = YoYoPageUtil.param;
        YoYoPageUtil.preParam.page = YoYoPageUtil.currentPage - 1;
        content += '<li class=""><a id="previousPage" href="javascript:YoYoPageUtil.loadData(YoYoPageUtil.preParam)"><span aria-hidden="true">&larr;</span> 前一页</a></li>';
    }
    if (YoYoPageUtil.currentPage < YoYoPageUtil.totalPage) {
        YoYoPageUtil.nextParam = YoYoPageUtil.param;
        YoYoPageUtil.nextParam.page = YoYoPageUtil.currentPage + 1;
        content += '<li class=""><a id="nextPage" href="javascript:YoYoPageUtil.loadData(YoYoPageUtil.nextParam)">后一页 <span aria-hidden="true">&rarr;</span></a></li>';
    }
    content += '</ul>';
    return content;
}

YoYoPageUtil.loadData = function (param) {
    YoYoPageUtil.param = param;
    if (param == undefined) {
        alert('param是必填字段');
        return;
    }
    if (YoYoPageUtil.queryData == undefined) {
        var queryData = {};
        YoYoPageUtil.queryData = queryData;
    }
    YoYoPageUtil.pageFooterId = param.pageFooterId;;//保存分页栏的标签id
    YoYoPageUtil.requestUrl = param.requestUrl;;//请求路径的url
    YoYoPageUtil.dataCallBack = param.dataCallBack;
    YoYoPageUtil.currentPage = Number(param.page);

    if (YoYoPageUtil.pageFooterId == undefined || YoYoPageUtil.requestUrl == undefined) {
        alert('参数错误');
        return;
    }

    if(!_.isFunction(YoYoPageUtil.dataCallBack)){
        alert('回调参数错误');
    }

    if (param.page != undefined) {
        YoYoPageUtil.page = param.page;
    }
    if (param.pageSize != undefined) {
        YoYoPageUtil.pageSize = param.pageSize;
    }
    $('#loading').html('正在加载...');
    YoYoPageUtil.queryData.page = YoYoPageUtil.param.page;
    YoYoPageUtil.queryData.pageSize = YoYoPageUtil.param.pageSize;
    $.getJSON(
        YoYoPageUtil.requestUrl,
        YoYoPageUtil.queryData,
        function (retJson) {
            if (retJson.errno == 0) {
                //成功
                YoYoPageUtil.dataCallBack(retJson.data);
                var $pageFooter = $('#'+YoYoPageUtil.pageFooterId);
                $pageFooter.html(YoYoPageUtil.getFooterContent());
                $('#toPage').click(function(){
                    var toPageNum = $("#toPageNum").val();
                    if(_.isEmpty(toPageNum)) {
                        toastr.error('请输入页码', '', {
                            positionClass:'toast-top-center',
                            timeOut: 1000,
                        });
                        return;
                    }
                    if(toPageNum <= 0 || toPageNum > YoYoPageUtil.totalPage) {
                        toastr.error('请输入1~'+YoYoPageUtil.totalPage+'页码', '', {
                            positionClass:'toast-top-center',
                            timeOut: 1000,
                        });
                        return;
                    }
                    YoYoPageUtil.toPageParam = YoYoPageUtil.param;
                    YoYoPageUtil.toPageParam.page = toPageNum;
                    YoYoPageUtil.loadData(YoYoPageUtil.toPageParam);
                })
                $('#loading').html('');
            } else {
                alertify.alert(retJson.errmsg);
            }
        }
    )
}