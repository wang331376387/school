<!DOCTYPE html>
<html>
<head>
    <title>信息管理系统</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <script type="text/javascript" src="${ctx}/scripts/jquery/jquery-1.7.1.js"></script>
    <link href="${ctx}/style/authority/basic_layout.css" rel="stylesheet" type="text/css">
    <link href="${ctx}/style/authority/common_style.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${ctx}/scripts/authority/commonAll.js"></script>
    <script type="text/javascript" src="${ctx}/scripts/jquery/jquery-1.4.4.min.js"></script>
    <script src="${ctx}/scripts/My97DatePicker/WdatePicker.js" type="text/javascript" defer="defer"></script>
    <script type="text/javascript" src="${ctx}/scripts/artDialog/artDialog.js?skin=default"></script>
    <script type="text/javascript">
        $(document).ready(function() {
            /*
             * 提交
             */
            $("#submitbutton").click(function() {
                if(validateForm()){
                    $.ajax({
                        type : "POST",
                        url : "saveClass",
                        data : {
                            "sid": $("#sid").val(),
                            "cname": $("#cname").val()
                        },
                        dataType : "json",
                        success : function(data) {
                            // 如果返回数据不为空，更改“班级信息”
                            if (data=="success"){
                                parent.$.fancybox.close();
                            }
                        }
                    });
                }
            });

            /*
             * 取消
             */
            $("#cancelbutton").click(function() {
                /**  关闭弹出iframe  **/
                window.parent.$.fancybox.close();
            });

            var result = 'null';
            if(result =='success'){
                /**  关闭弹出iframe  **/
                window.parent.$.fancybox.close();
            }
        });

        /** 表单验证  **/
        function validateForm(){
            if($("#sid").val()==""){
                art.dialog({icon:'error', title:'友情提示', drag:false, resize:false, content:'请选择学校', ok:true,});
                return false;
            }
            if($("#cname").val()==""){
                art.dialog({icon:'error', title:'友情提示', drag:false, resize:false, content:'填写班级名称', ok:true,});
                return false;
            }
            return true;
        }
    </script>
</head>
<body>
<form id="submitForm" method="post">
    <div id="container">
        <div id="nav_links">
            当前位置：学校管理&nbsp;>&nbsp;<span style="color: #1A5CC6;">新增班级</span>
            <div id="page_close">
                <a href="javascript:parent.$.fancybox.close();">
                    <img src="${ctx}/images/common/page_close.png" width="20" height="20" style="vertical-align: text-top;"/>
                </a>
            </div>
        </div>
        <div class="ui_content">
            <table  cellspacing="0" cellpadding="0" width="100%" align="left" border="0">
                <tr>
                    <td class="ui_text_rt" width="80">学校</td>
                    <td class="ui_text_lt">
                        <select name="sid" id="sid" class="ui_select01">
                            <option value="">--请选择--</option>
                            <#list schools as school>
                                <option value="${school.value}">${school.text}</option>
                            </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td class="ui_text_rt">班级名称</td>
                    <td class="ui_text_lt">
                        <input type="text" id="cname" name="cname"  class="ui_input_txt02"/>
                    </td>
                </tr>

                <tr>
                    <td>&nbsp;</td>
                    <td class="ui_text_lt">
                        &nbsp;<input id="submitbutton" type="button" value="提交" class="ui_input_btn01"/>
                        &nbsp;<input id="cancelbutton" type="button" value="取消" class="ui_input_btn01"/>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</form>
</body>
</html>
