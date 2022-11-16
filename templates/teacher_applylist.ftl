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

        /** 查询用户  **/
        function search() {
            // $("#submitForm").submit();
            $("#submitForm").attr("action", "/admin/getAllTeacherApply").submit();
        }

        /** 普通跳转，上下页 **/
        function jumpNormalPage(page) {
            $("#pageIndex").val(page);
            $("#submitForm").attr("action", "/admin/getAllTeacherApply").submit();
        }

        /** 接收申请 **/
        function acceptApply(id,uid) {
            // 非空判断
            if (id == '') return;
            if (confirm("您确定同意吗？")) {
                $.ajax({
                    type : "GET",
                    url : "acceptApply/"+id+"/"+uid,
                    success : function(data) {
                        if (data==20000){
                            window.location.reload();
                        }
                    }
                });
            }
        }

        /** 输入页跳转 **/
        function jumpInputPage() {
            // 如果“跳转页数”不为空
            if ($("#jumpNumTxt").val() != '') {
                var pageNum = parseInt($("#jumpNumTxt").val());
                // 如果跳转页数在不合理范围内，则置为1
                if (pageNum < 1 | pageNum > ${applyPage.total}) {
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
                        <select value="${sid}" name="sid" id="school" class="ui_select01">
                            <option value="">--请选择--</option>
                            <#list schools as school>
                                <option value="${school.value}">${school.text}</option>
                            </#list>
                        </select>

                        <input type="hidden" id="pageIndex" name="pageIndex" />
                    </div>
                    <div id="box_bottom">
                        <input type="button" value="查询" class="ui_input_btn01" onclick="search();"/>
                    </div>
                </div>
            </div>
        </div>
        <div class="ui_content">
            <div class="ui_tb">
                <table class="table" cellspacing="0" cellpadding="0" width="100%" align="center" border="0">
                    <tr>
                        <th>姓名</th>
                        <th>性别</th>
                        <th>学校</th>
                        <th>班级</th>
                        <th>身份</th>
                        <th>操作</th>
                    </tr>
                    <#list applyPage.list as user>
                        <tr>
                            <td>${user.name!}</td>
                            <td><#if user.sex??>
                                    <#if user.sex==1>
                                        男
                                    <#else >
                                        女
                                    </#if>
                                </#if></td>
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
                            <td>
                                <a style="color: blue" href="javascript:acceptApply(${user.id},${user.uid});">同意</a>
                            </td>
                        </tr>
                    </#list>

                </table>
            </div>
            <div class="ui_tb_h30">
                <div class="ui_flt" style="height: 30px; line-height: 30px;">
                    共有
                    <span class="ui_txt_bold04">${applyPage.total}</span>
                    条记录，当前第
                    <span class="ui_txt_bold04">${applyPage.pageNum}
						/
						${applyPage.pages}</span>
                    页
                </div>
                <div class="ui_frt">
                    <!--    如果是第一页，则只显示下一页、尾页 -->

                    <input type="button" value="首页" class="ui_input_btn01"
                           onclick="jumpNormalPage(1);"/>
                    <#if applyPage.prePage==0>
                        <input type="button" value="下一页" class="ui_input_btn01"
                               onclick="jumpNormalPage(${applyPage.nextPage});"/>
                    <#elseif applyPage.nextPage==0>
                        <input type="button" value="上一页" class="ui_input_btn01"
                               onclick="jumpNormalPage(${applyPage.prePage});"/>
                    </#if>

                    <#if applyPage.prePage gt 0 && applyPage.nextPage gt 0>
                        <input type="button" value="上一页" class="ui_input_btn01"
                               onclick="jumpNormalPage(${applyPage.prePage});"/>
                        <input type="button" value="下一页" class="ui_input_btn01"
                               onclick="jumpNormalPage(${applyPage.nextPage});"/>
                    </#if>

                    <input type="button" value="尾页" class="ui_input_btn01"
                           onclick="jumpNormalPage(${applyPage.pages});"/>

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
