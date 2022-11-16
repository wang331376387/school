package com.zl.yxt.controller;

import com.zl.yxt.pojo.Comment;
import com.zl.yxt.pojo.Moods;
import com.zl.yxt.pojo.vo.ResultVO;
import com.zl.yxt.service.moodService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/mood")
@Slf4j
@Api(tags = "心情管理")
public class moodController {

    @Autowired
    private moodService moodService;

    @Autowired
    private com.zl.yxt.utils.UpdatePicToHuangban updatePicToHuangban;

    @Value("${uploadpath}")
    public String path;

    //按时间倒叙查询心情信息
    @ApiOperation(value = "查看心情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "登录人编号",required = true),
            @ApiImplicitParam(name = "mid", value = "心情编号，不填查询全部")
    })
    @RequestMapping(value = "/showAll",method = RequestMethod.POST)
    public ResultVO showAllMoods(@RequestParam(defaultValue = "1") Integer pageIndex,
                                 String id,Integer mid){
        if (mid == null){
            ResultVO data = moodService.showAllMoods(pageIndex,id);
            return data;
        }else {
            ResultVO OneData = moodService.showOneMoods(mid,Integer.valueOf(id));
            return OneData;
        }
    }

    /**
     * 创建心情
     * @param mood
     * @return
     */
    @ApiOperation(value = "创建心情")
    @RequestMapping(value = "/creatMood",method = RequestMethod.POST)
    public ResultVO creatMood(@RequestBody Moods mood) {

        ResultVO data = null;
        try {
            data = moodService.creatMood(mood);
            boolean isDelete = updatePicToHuangban.delAllFile(path + File.separator + mood.getUid()); //TODO 删除本地暂存区文件
        } catch (Exception e) {
            e.printStackTrace();
            data = ResultVO.customize(502,null,"图片上传失败");
            boolean isDelete = updatePicToHuangban.delAllFile(path + File.separator + mood.getUid());
        }
        return data;
    }

    /**
     * 清空临时文件夹
     * @return
     */
    @RequestMapping(value = "/clear/{id}",method = RequestMethod.GET)
    public ResultVO clearfiles(@PathVariable("id") String id){
        File fileflor = new File(path + File.separator + id);
        if (!fileflor.exists()) {
            return ResultVO.success(); //如果不存在直接返回
        }
        //存在清空文件夹
        boolean isDelete = updatePicToHuangban.delAllFile(path + File.separator + id);
        if (isDelete){
            return ResultVO.success();
        }else {
            return ResultVO.error();
        }

    }

    /**
     * 临时保存文件到本地
     * @param files
     */
    @ApiOperation(value = "临时保存图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "发布人编号"),
            @ApiImplicitParam(name = "files", value = "多个文件", allowMultiple = true, dataType = "__file")
    })
    @RequestMapping(value = "/saveFiles",method = RequestMethod.POST,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void  saveFile(@RequestParam(value = "files") MultipartFile[] files, String id) throws Exception {

        for (MultipartFile file : files){
            //上传文件存储路径
            String filePath = path;
            //文件夹不存在就创建
            File fileflor = new File(filePath + File.separator + id);
            if (!fileflor.exists()) {
                fileflor.mkdirs();
            }
            if(!file.isEmpty()) {
                //上传文件的全路径
                File tempFile = new File(filePath + File.separator
                        + id + File.separator + file.getOriginalFilename());
                //文件上传
                file.transferTo(tempFile);
            }
        }
    }

    /**
     * 删除临时文件夹指定文件
     * @param file
     * @return
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public ResultVO delete(@RequestParam("file") MultipartFile file,String id){
        String name = file.getOriginalFilename();
        File delFile = new File(path + File.separator + id + File.separator + name);
        delFile.delete();
        return ResultVO.success();
    }

    /**
     * 点赞或者取消
     * @param uid
     * @param mid
     * @param status
     * @return
     */
    @RequestMapping(value = "/toLike/{uid}/{mid}/{status}",method = RequestMethod.GET)
    public ResultVO toLike(@PathVariable("uid")String uid,@PathVariable("mid")String mid,@PathVariable("status") Integer status){

        ResultVO data = moodService.tolike(uid,mid,status);

        return data;
    }

    /**
     * 发布评论
     * @param comment
     * @return
     */
    @RequestMapping(value = "/sendComment",method = RequestMethod.POST)
    public ResultVO sendComment(@RequestBody Comment comment){
        System.err.println(comment);
        moodService.insertComment(comment);
        return ResultVO.success();
    }
}
