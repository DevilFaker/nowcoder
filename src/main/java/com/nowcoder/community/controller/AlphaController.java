package com.nowcoder.community.controller;

import com.nowcoder.community.dao.service.AlphaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/alpha")
public class AlphaController {
    @Autowired
    AlphaService alphaService;

    @RequestMapping("/hello")
    @ResponseBody
    public String sayhallo(){
        return "hello,world";
    }

    @RequestMapping("/data")
    @ResponseBody
    public String getData(){
        return alphaService.find();
    }

    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response){
        System.out.print(request.getMethod());
        System.out.println(request.getServletPath());
        Enumeration<String> enumeration=request.getHeaderNames();
        while (enumeration.hasMoreElements()){
            String name=enumeration.nextElement();
            String value=request.getHeader(name);
            System.out.println(name+":"+value);
        }
        System.out.println("code:"+request.getParameter("code"));
        //返回响应数据
        response.setContentType("text/html;charset=utf-8");
        try{
            PrintWriter writer=response.getWriter();
            writer.write("<h1>牛客网<h1>");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //Get 请求
    //查询所有学生/student?current=1&limit=20

    @RequestMapping(path="/student",method = RequestMethod.GET)
    @ResponseBody
    public String getStudents(
            @RequestParam(name="current",required =false,defaultValue = "1")int current,
            @RequestParam(name="limit",required =false,defaultValue = "20")int limit){
        System.out.println(""+current+" "+limit);
        return "some students";
    }

    ///student/123
    @RequestMapping(path="/student/{id}",method = RequestMethod.GET)
    @ResponseBody
    public String student(@PathVariable("id")int id){
        System.out.println(id);
        return "a student";
    }

    ///community/alpha/saveName
    @RequestMapping(path = "/saveName",method = RequestMethod.POST)
    @ResponseBody
    public String saveName(String name){
        System.out.println(name);
        return "success";
    }

    @RequestMapping(path = "/teacher",method = RequestMethod.GET)
    public ModelAndView getTeacher(){
        ModelAndView mav=new ModelAndView();
        mav.addObject("name","liTeng");
        mav.setViewName("/demo/view");
        return mav;
    }

    @RequestMapping(path = "/school",method = RequestMethod.GET)
    public String getSchool(Model model){
        model.addAttribute("name","liteng");
        return "/demo/view";
    }

    //响应json数据
    @RequestMapping(path = "/emp",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getEmp(){
        Map<String,Object> emp=new HashMap<>();
        emp.put("name","liteng");
        emp.put("age",23);
        emp.put("salary",13000);
        return emp;
    }
}

