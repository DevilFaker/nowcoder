package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.dao.service.UserService;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import io.micrometer.core.instrument.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping(path = "/user")
public class UserController {
    private static final Logger logger=LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.domain}")
    private String domainPath;

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @LoginRequired
    @RequestMapping(path = "/setting",method = RequestMethod.GET)
    public String getSettingPage(){
        return "/site/setting";
    }

    @LoginRequired
    @RequestMapping(path="/upload",method=RequestMethod.POST)
    public String uploadHeader(MultipartFile multipartFile, Model model){
        if(multipartFile==null){
            model.addAttribute("error","无效路径");
            return "/site/setting";
        }

        String fileName=multipartFile.getOriginalFilename();
        String suffix=fileName.substring(fileName.lastIndexOf(".")+1);
        if(StringUtils.isBlank(suffix)){
            model.addAttribute("error","图片后缀不正确");
            return "/site/setting";
        }

        fileName=CommunityUtil.generateUUID()+suffix;
        File dest=new File(uploadPath+"/"+fileName);
        try {
            multipartFile.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败"+e.getMessage());
            throw new RuntimeException("上传文件失败，服务器发生异常");
        }

        User user=hostHolder.getUser();
        String headerUrl=domainPath+contextPath+"/user/header/"+ fileName;
        userService.updateHeader(user.getId(),headerUrl);

        return "redirect:/index";
    }

    @RequestMapping(path = "/header/{filename}",method =RequestMethod.GET)
    public void getHeader(@PathVariable("filename") String fileName, HttpServletResponse response){
        fileName=uploadPath+"/"+fileName;
        String suffix=fileName.substring(fileName.lastIndexOf(".")+1);
        response.setContentType("image/"+suffix);
        try(
                OutputStream os= response.getOutputStream();
                FileInputStream fis=new FileInputStream(fileName);
        ) {

            byte[] buffer=new byte[1024];
            int b=0;
            while((b=fis.read(buffer))!=-1){
                os.write(buffer,0,b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败"+e.getMessage());
        }


    }

}