package com.zl.yxt.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
/**
 * 存储查询到的心情
 * 所以携带作者信息
 * 以及配图
 */
@ApiModel(description="心情实体")
public class Moods {
    @ApiModelProperty(value = "心情编号", hidden = true)
    public Integer id; //心情编号

    @ApiModelProperty(value = "作者编号", name = "uid", required = true, example = "2")
    public Integer uid; //作者编号
    //喜欢个数
    @ApiModelProperty(value = "点赞数量", hidden = true)
    public Integer likenum;
    //评论个数
    @ApiModelProperty(value = "评论个数", hidden = true)
    public Integer commentnum;
    //作者姓名
    @ApiModelProperty(value = "作者姓名", hidden = true)
    public String name;
    //作者昵称
    @ApiModelProperty(value = "作者昵称", hidden = true)
    public String username;
    //头像
    @ApiModelProperty(value = "头像", hidden = true)
    public String photo;
    //时间
    @ApiModelProperty(value = "时间", hidden = true)
    public Date createtime;
    //内容
    @ApiModelProperty(value = "心情内容", name = "content", required = true, example = "心情内容：哈哈哈")
    public String content;
    //配图
    @ApiModelProperty(value = "配图", hidden = true)
    public String[] picture;
    //评论
    @ApiModelProperty(value = "评论", hidden = true)
    public List<Comment> comments;
    //是否被喜欢
    @ApiModelProperty(value = "是否被喜欢", hidden = true)
    public Integer isLike = 0;
}
