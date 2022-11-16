<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link href="${ctx}/style/authority/basic_layout.css" rel="stylesheet" type="text/css">
    <link href="${ctx}/style/authority/common_style.css" rel="stylesheet" type="text/css">
    <link href="${ctx}/style/authority/zTreeStyle.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${ctx}/scripts/jquery/jquery-1.4.4.min.js"></script>
    <script type="text/javascript" src="${ctx}/scripts/zTree/jquery.ztree.core-3.2.js"></script>
    <script type="text/javascript" src="${ctx}/scripts/fancybox/jquery.fancybox-1.3.4.js"></script>
    <script type="text/javascript" src="${ctx}/scripts/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
    <link rel="stylesheet" type="text/css" href="${ctx}/style/authority/jquery.fancybox-1.3.4.css" media="screen"/></link>
    <script type="text/javascript" src="${ctx}/scripts/artDialog/artDialog.js?skin=default"></script>
    <title>信息管理系统</title>
    <script type="text/javascript">

        /** ----------------leftMenu zTree部分开始----------------  **/
        var zTree;
        var setting = {
            view : {
                dblClickExpand : false,
                showLine : true,
                selectedMulti : false,
                expandSpeed : ($.browser.msie && parseInt($.browser.version) <= 6) ? ""
                    : "fast"
            },
            data : {
                key : {
                    name : "weiduName"
                },
                simpleData : {
                    enable : true,
                    idKey : "weiduID",
                    pIdKey : "parentID",
                    rootPId : ""
                }
            },
            callback : {
                beforeClick : beforeClick,
                onClick : onClick
            }
        };

        $(document).ready(function() {
            getTree();
        });
        function getTree(){
            var trees = ${trees};
            $.fn.zTree.init($("#tree"), setting, trees);
            zTree = $.fn.zTree.getZTreeObj("tree");
            // 默认展开所有节点
            zTree.expandAll(true);
        }
        function beforeClick(treeId, treeNode) {
            var check = (treeNode && treeNode.isParent && treeNode.weiduGrade != 2 && treeNode.weiduGrade != 0 );
            if (!check) {
                art.dialog({icon:'error', title:'友情提示', drag:false, resize:false, content:'请选择有班级的学校展开', ok:true,});
                return false;
            }
            return true;
        }

        /** 左键单击 **/
        function onClick(e, treeId, treeNode) {
            var fyXqCode = treeNode.getParentNode().weiduID;
            var fyDhCode = treeNode.weiduID;
            $("#sid").val(treeNode.weiduID);
            <#--$("#container").load("${ctx}/admin/getClassesBySid/"+treeNode.weiduID);-->
            $("#submitForm").submit();
        }
        /** ----------------leftMenu  zTree部分开始----------------  **/
        function deleteById(id) {
            if (confirm("您确定删除班级吗？")) {
                $.ajax({
                    type : "GET",
                    url : "deleteClassById/"+id,
                    success : function(data) {
                        // 如果返回数据不为空，更改“班级信息”
                        if (data=="success"){
                            $("#submitForm").submit();
                        }
                    }
                });
            }
        }
    </script>

    <script type="text/javascript">
        $(document).ready(function(){
            $("#addSchool").fancybox({
                'href': '/admin/school_add',
                'width': 500,
                'height': 300,
                'type': 'iframe',
                'hideOnOverlayClick': false,
                'showCloseButton': false,
                'onClosed': function () {
                    window.location.reload();
                }
            });
            $("#addClass").fancybox({
                'href': '/admin/class_add',
                'width': 500,
                'height': 300,
                'type': 'iframe',
                'hideOnOverlayClick': false,
                'showCloseButton': false,
                'onClosed': function () {
                    window.location.reload();
                }
            });
        });
    </script>
    <style type="text/css">
        #sider{
            position: absolute;
            top: 0;
            left: 25px;
            bottom: 0px;
            width: 260px;
            border: 1px solid #DEDFDF;
        }

        #main{
            position: absolute;
            top: 0;
            left: 285px;
            right: 0px;
            bottom: 0px;
            border: 1px solid #DEDFDF;
            overflow: auto;
        }
        /*border: 1px solid #EDEDED;*/
        #box_border {
            height: 50px;
            line-height: 50px;
        }
        #fang_type {
            list-style-type: none;
        }
        #fang_type li{
            width: 80px;
            height: 22px;
            line-height: 22px;
            color: #FFF;
            display: inline-block;
        }

        .fy_table_td{
            color: #fff;
        }

        .fang_th{
            background: #044599 no-repeat;
            text-align: center;
            border-left: 1px solid #02397F;
            border-right: 1px solid #02397F;
            border-bottom: 1px solid #02397F;
            border-top: 1px solid #02397F;
            letter-spacing: 2px;
            text-transform: uppercase;
            font-size: 14px;
            color: #fff;
            height: 37px;
        }
    </style>
</head>
<body>
<form id="submitForm" action="${ctx}/admin/getAllSchoolAndClass" method="post">
    <input type="hidden" id="sid" name="sid" value="${sid}" />
    <div id="container">
        <div id="sider">
            <ul id="tree" class="ztree"></ul>
        </div>
        <div id="main">
            <div id="box_border">
                <div style="width:50%;float:left;text-align: left">
                    <span><P style="color: green">当前学校：${scname}</P></span>
                </div>
                <div style="width:50%;float:left;text-align: right">
                    <button id="addSchool" class="ui_input_btn03">+添加学校</button>
                    <button id="addClass" class="ui_input_btn03">+添加班级</button>
                </div>
            </div>
            <table class="unit-the-table table" cellspacing="0" cellpadding="0" width="100%" align="center" border="0">
                <thead id="unit-thead">
                    <tr>
                        <th width="20" class="fang_th">序号</th>
                        <th width="50" class="fang_th">班级</th>
                        <th width="50" class="fang_th">操作</th>
                    </tr>
                </thead>
                <tbody>
                    <#list classes as cla>
                        <tr>
                            <td style="color:#1853A1;">${cla_index+1}</td>

                            <td width="40">${cla.cname}</td>

                            <td width="40">
                                <a href="javascript:void(0);" onclick="deleteById(${cla.id})" class="fy_table_td" style="color:red;">删除</a>
                            </td>
                        </tr>
                    </#list>
                </tbody>
            </table>
        </div>
    </div>
</form>
</body>
</html>
