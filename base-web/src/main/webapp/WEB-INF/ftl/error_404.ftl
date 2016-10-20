<!DOCTYPE html>
<html lang="en">
<head>
    <title>页面未找到</title>
<#include "common/head-meta.ftl">
</head>

<body>
<#include "common/header.ftl">
<!-- Page container -->
<div class="page-container">

    <!-- Page content -->
    <div class="page-content">
	<#include "common/sidebar.ftl">
        <!-- Error wrapper -->
        <div class="container-fluid text-center">
            <h1 class="error-title">500</h1>
            <h6 class="text-semibold content-group">页面不见咯，您可以反馈给我们哦！</h6>

            <div class="row">
                <div class="col-lg-4 col-lg-offset-4 col-sm-6 col-sm-offset-3">
                    <form action="#" class="main-search">
                        <div class="input-group content-group">
                            <input type="text" class="form-control input-lg" placeholder="反馈内容">

                            <div class="input-group-btn">
                                <button type="submit" class="btn bg-slate-600 btn-icon btn-lg">提交</button>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-sm-6">
                                <a href="${request.contextPath}" class="btn btn-primary btn-block content-group"><i class="icon-circle-left2 position-left"></i>回到首页</a>
                            </div>

                            <div class="col-sm-6">
                                <a href="" class="btn btn-default btn-block content-group"><i class="fa fa-refresh position-left"></i>刷新试试</a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <!-- /error wrapper -->

        <!-- Content area -->
        <div class="content">
		<#include "common/footer.ftl">
        </div>
        <!-- /content area -->


    </div>
    <!-- /page content -->

</div>
<!-- /page container -->

</body>

<#include "common/scripts.ftl">
<#--<script type="text/javascript" src="${request.contextPath}/static/assets/js/pages/dashboard.js"></script>-->

</html>
