<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <script type="text/javascript" src="${ctx}/scripts/jquery/jquery-1.7.1.js"></script>
    <link href="${ctx}/style/authority/basic_layout.css" rel="stylesheet" type="text/css">
    <link href="${ctx}/style/authority/common_style.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${ctx}/scripts/authority/commonAll.js"></script>
    <script type="text/javascript" src="${ctx}/scripts/fancybox/jquery.fancybox-1.3.4.js"></script>
    <script type="text/javascript" src="${ctx}/scripts/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
    <link rel="stylesheet" type="text/css" href="${ctx}/style/authority/jquery.fancybox-1.3.4.css" media="screen"></link>
    <script type="text/javascript" src="${ctx}/scripts/artDialog/artDialog.js?skin=default"></script>
    <title>信息管理系统</title>
    <script type="text/javascript">
        $(document).ready(function () {
            /** 新增   **/
            $("#addBtn").fancybox({
                'href': '/admin/user_edit',
                'width': 733,
                'height': 530,
                'type': 'iframe',
                'hideOnOverlayClick': false,
                'showCloseButton': false,
                'onClosed': function () {
                    window.location.href = '/admin/findUsers';
                }
            });
            $("#school").val(${param.sid!});
            getFyDhListByFyXqCode();
            $("#role").val('${param.role!}');
            $("#phone").val('${param.phone!}');
            $("#xm").val('${param.name!}');
            $("#myclass").val(${param.cid!});
        });
        /** 用户角色   **/
        var userRole = '';

        /** 查询用户  **/
        function search() {
            // $("#submitForm").submit();
            $("#submitForm").attr("action", "/admin/findUsers").submit();
        }

        /** 新增   **/
        function add() {
            $("#submitForm").attr("action", "/xngzf/archives/luruFangyuan.action").submit();
        }

        /** 清空认证 **/
        function del(id) {
            // 非空判断
            if (id == '') return;
            if (confirm("您确定要清空认证吗？")) {
                $.ajax({
                    type : "POST",
                    url : "ClearCertification",
                    data : {
                        "id": id
                    },
                    dataType : "json",
                    success : function(data) {
                        // 如果返回数据不为空，更改“班级信息”
                        if (data=="success"){
                            alert("清空成功");
                        }
                    }
                });
            }
        }

        /** 批量删除 **/
        function batchDel() {
            if ($("input[name='IDCheck']:checked").size() <= 0) {
                art.dialog({icon: 'error', title: '友情提示', drag: false, resize: false, content: '至少选择一条', ok: true,});
                return;
            }
            // 1）取出用户选中的checkbox放入字符串传给后台,form提交
            var allIDCheck = "";
            $("input[name='IDCheck']:checked").each(function (index, domEle) {
                allIDCheck += $(domEle).val() + ",";
            });
            // 截掉最后一个","
            if (allIDCheck.length > 0) {
                allIDCheck = allIDCheck.substring(0, allIDCheck.length - 1);
                // 赋给隐藏域
                $("#allIDCheck").val(allIDCheck);
                if (confirm("您确定要批量清空这些记录吗？")) {
                    // 提交form
                    console.log("编号：",allIDCheck)
                }
            }
        }

        /** 普通跳转，上下页 **/
        function jumpNormalPage(page) {
            $("#pageIndex").val(page);
            $("#submitForm").attr("action", "/admin/findUsers").submit();
        }

        /** 输入页跳转 **/
        function jumpInputPage() {
            // 如果“跳转页数”不为空
            if ($("#jumpNumTxt").val() != '') {
                var pageNum = parseInt($("#jumpNumTxt").val());
                // 如果跳转页数在不合理范围内，则置为1
                if (pageNum < 1 | pageNum > ${userPage.total}) {
                    art.dialog({
                        icon: 'error',
                        title: '友情提示',
                        drag: false,
                        resize: false,
                        content: '请输入合适的页数',
                        ok: true,
                    });
                    $("#jumpNumTxt").val(null);
                    return;
                }
                jumpNormalPage(pageNum);
            } else {
                // “跳转页数”为空
                art.dialog({
                    icon: 'error',
                    title: '友情提示',
                    drag: false,
                    resize: false,
                    content: '请输入合适的页数',
                    ok: true,
                });
                // jumpNormalPage(pageNum);
            }
        }
    </script>
    <style>
        .alt td {
            background: black !important;
        }
    </style>
</head>
<body>
<form id="submitForm" name="submitForm" method="post">
    <input type="hidden" name="allIDCheck" value="" id="allIDCheck"/>
    <input type="hidden" name="fangyuanEntity.fyXqName" value="" id="fyXqName"/>
    <div id="container">
        <div class="ui_content">
            <div class="ui_text_indent">
                <div id="box_border">
                    <div id="box_top">搜索</div>
                    <div id="box_center">
                        学校
                        <select name="sid" id="school" class="ui_select01"
                                onchange="getFyDhListByFyXqCode();">
                            <option value="">--请选择--</option>
                            <#list schools as school>
                                <option value="${school.value}">${school.text}</option>
                            </#list>
                        </select>

                        班级
                        <select name="cid" id="myclass" class="ui_select01">
                            <option value="">--请选择--</option>
                        </select>
                        身份
                        <select name="role" id="role" class="ui_select01">
                            <option value="">--请选择--</option>
                            <option value="root">管理员</option>
                            <option value="teacher">教师</option>
                            <option value="student">学生</option>
                        </select>

                        电话&nbsp;&nbsp;<input type="text" id="phone" name="phone"
                                             class="ui_input_txt02"/>
                        姓名&nbsp;&nbsp;<input type="text" id="xm" name="name"
                                             class="ui_input_txt02"/>
                        <input type="hidden" id="pageIndex" name="pageIndex" />
                    </div>
                    <div id="box_bottom">
                        <input type="button" value="查询" class="ui_input_btn01" onclick="search();"/>
                        <input type="button" value="新增管理员" class="ui_input_btn01" id="addBtn"/>
                        <input type="button" value="清空认证" class="ui_input_btn01" onclick="batchDel();"/>
                    </div>
                </div>
            </div>
        </div>
        <div class="ui_content">
            <div class="ui_tb">
                <table class="table" cellspacing="0" cellpadding="0" width="100%" align="center" border="0">
                    <tr>
                        <th width="30"><input type="checkbox" id="all" onclick="selectOrClearAllCheckbox(this);"/>
                        </th>
                        <th>姓名</th>
                        <th>电话</th>
                        <th>学校</th>
                        <th>班级</th>
                        <th>身份</th>
                        <th>昵称</th>
                        <th>性别</th>
                        <th>操作</th>
                    </tr>
                    <#list userPage.list as user>
                        <tr>
                            <td><input type="checkbox" name="IDCheck" value="${user.id}" class="acb"/></td>
                            <td>${user.name!}</td>
                            <td>${user.phone!}</td>
                            <td>${user.scname!}</td>
                            <td>
                                <#if user.cname??>
                                    <#assign names = user.cname?split(",")  />
                                    <#assign number =  names?size  />
                                    <#if number gt 0>
                                        <#list names as name>
                                            ${name!}<br>
                                        </#list>
                                    <#else >
                                        ${user.cname}
                                    </#if>
                                </#if>
                            </td>
                            <td>${user.role!}</td>
                            <td>${user.username!}</td>
                            <td>
                                <#if user.sex??>
                                    <#if user.sex==0>
                                        男
                                        <#else >
                                        女
                                    </#if>
                                </#if>
                            </td>
                            <td>
<#--                                <a href="${ctx}/admin/user_edit?id=14458579642011" class="edit">编辑</a>-->
                                <a style="color: blue" href="javascript:del(${user.id});">清空认证</a>
                            </td>
                        </tr>
                    </#list>

                </table>
            </div>
            <div class="ui_tb_h30">
                <div class="ui_flt" style="height: 30px; line-height: 30px;">
                    共有
                    <span class="ui_txt_bold04">${userPage.total}</span>
                    条记录，当前第
                    <span class="ui_txt_bold04">${userPage.pageNum}
						/
						${userPage.pages}</span>
                    页
                </div>
                <div class="ui_frt">
                    <!--    如果是第一页，则只显示下一页、尾页 -->

                    <input type="button" value="首页" class="ui_input_btn01"
                           onclick="jumpNormalPage(1);"/>
                    <#if userPage.prePage==0>
                        <input type="button" value="下一页" class="ui_input_btn01"
                               onclick="jumpNormalPage(${userPage.nextPage});"/>
                        <#elseif userPage.nextPage==0>
                            <input type="button" value="上一页" class="ui_input_btn01"
                                   onclick="jumpNormalPage(${userPage.prePage});"/>
                    </#if>

                    <#if userPage.prePage gt 0 && userPage.nextPage gt 0>
                        <input type="button" value="上一页" class="ui_input_btn01"
                                   onclick="jumpNormalPage(${userPage.prePage});"/>
                        <input type="button" value="下一页" class="ui_input_btn01"
                               onclick="jumpNormalPage(${userPage.nextPage});"/>
                    </#if>

                    <input type="button" value="尾页" class="ui_input_btn01"
                           onclick="jumpNormalPage(${userPage.pages});"/>

                    <!--     如果是最后一页，则只显示首页、上一页 -->

                    转到第<input type="text" id="jumpNumTxt" class="ui_input_txt01"/>页
                    <input type="button" class="ui_input_btn01" value="跳转" onclick="jumpInputPage();"/>
                </div>
            </div>
        </div>
    </div>
</form>
</body>
</html>
