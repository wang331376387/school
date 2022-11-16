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
				checkPhoneSubmit();
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

	/** 检测电话是否存在并提交form  **/
	function checkPhoneSubmit(){
		var phone = $("#phone").val();
		if(phone!=""){
			$.ajax({
				type:"POST",
				url:"checkPhoneExists",
				data:{
					"phone":phone
				},
				success:function(data){
					// 如果返回数据不为空，更改“房源信息”
					if(data=="fail"){
						 art.dialog({icon:'error', title:'友情提示', drag:false, resize:false, content:'电话号码已经注册，\n请更换', ok:true,});
						 $("#phone").css("background", "#EEE");
						 $("#phone").focus();
						 return false;
					}else{
						console.log("成功了")
						$("#submitForm").attr("action", "/admin/createRoot").submit();
						parent.$.fancybox.close();
					}
				}
			});
		}
		return true;
	}

	/** 表单验证  **/
	function validateForm(){
		if($("#phone").val()==""){
			art.dialog({icon:'error', title:'友情提示', drag:false, resize:false, content:'填写电话号码', ok:true,});
			return false;
		}
		if($("#name").val()==""){
			art.dialog({icon:'error', title:'友情提示', drag:false, resize:false, content:'填写姓名', ok:true,});
			return false;
		}
		if($("#username").val()==""){
			art.dialog({icon:'error', title:'友情提示', drag:false, resize:false, content:'填写昵称', ok:true,});
			return false;
		}
		return true;
	}
</script>
</head>
<body>
<form id="submitForm" name="submitForm" method="post">
	<div id="container">
		<div id="nav_links">
			当前位置：用户列表&nbsp;>&nbsp;<span style="color: #1A5CC6;">新增管理员</span>
			<div id="page_close">
				<a href="javascript:parent.$.fancybox.close();">
					<img src="${ctx}/images/common/page_close.png" width="20" height="20" style="vertical-align: text-top;"/>
				</a>
			</div>
		</div>
		<div class="ui_content">
			<table  cellspacing="0" cellpadding="0" width="100%" align="left" border="0">
				<tr>
					<td class="ui_text_rt" width="80">性别</td>
					<td class="ui_text_lt">
						<select name="sex" id="sex" class="ui_select01">
							<option value="1">男</option>
							<option value="0">女</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="ui_text_rt">姓名</td>
					<td class="ui_text_lt">
						<input type="text" id="name" name="name"  class="ui_input_txt02"/>
					</td>
				</tr>
				<tr>
					<td class="ui_text_rt">昵称</td>
					<td class="ui_text_lt">
						<input type="text" id="username" name="username" class="ui_input_txt02"/>
					</td>
				</tr>
				<tr>
					<td class="ui_text_rt">电话号码</td>
					<td class="ui_text_lt">
						<input type="text" id="phone" name="phone" class="ui_input_txt02"/>
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
