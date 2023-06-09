package com.edu.seckill.controller;

import com.edu.seckill.pojo.User;
import com.edu.seckill.service.GoodsService;
import com.edu.seckill.service.UserService;
import com.edu.seckill.vo.GoodsVo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author vercen
 * @version 1.0
 * @date 2023/4/22 14:41
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Resource
    private UserService userService;
    @Resource
    private GoodsService goodsService;
    @Resource
    private RedisTemplate redisTemplate;
    //手动渲染
    @Resource
    private ThymeleafViewResolver thymeleafViewResolver;
    //跳转到商品列表页
    @RequestMapping(value = "/toList1")
    public String toList(Model model, @CookieValue("userTicket") String ticket, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        //如果cookie 没有生成
        if (!StringUtils.hasText(ticket)) {
            return "login";
        }
        User user = userService.getUserByCookie(ticket, httpServletRequest, httpServletResponse);
        //User user = (User) session.getAttribute(ticket);


        if (user == null) {
            return "login";
        }
        List<GoodsVo> goodsVo = goodsService.findGoodsvo();
//        for (GoodsVo vo : goodsVo) {
//            System.out.println(vo);
//            System.out.println(vo.getGoodsName());
//        }

        model.addAttribute("user", user);
        model.addAttribute("goodsList",goodsVo);
        return "goodsList";
    }

    //使用自定义解析器封装user
    @RequestMapping(value = "/toList2")
    public String toList2(Model model,User user) {

        if (user == null) {
            return "login";
        }
        List<GoodsVo> goodsVo = goodsService.findGoodsvo();
        model.addAttribute("user", user);
        model.addAttribute("goodsList",goodsVo);
        return "goodsList";
    }

    //使用自定义解析器封装user
    //页面缓存
    @RequestMapping(value = "/toList",produces = "text/html;charset=utf-8")
    @ResponseBody//使用了redis 缓存页面需要添加
    public String toList3(Model model,User user,HttpServletRequest request,HttpServletResponse response) {

        if (user == null) {
            return "login";
        }
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html =(String) valueOperations.get("goodsList");

        if (StringUtils.hasText(html)){
            return html;
        }

        List<GoodsVo> goodsVo = goodsService.findGoodsvo();
        model.addAttribute("user", user);
        model.addAttribute("goodsList",goodsVo);

        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList", webContext);
        if (StringUtils.hasText(html)) {
        //每60s 更新一次redis 页面缓存, 即60s 后, 该页面缓存失效, Redis 会清除该页面缓存
            valueOperations.set("goodsList", html, 60, TimeUnit.SECONDS);
        }
        return html;
    }



    @RequestMapping(value = "/toDetail/{goodsId}",produces = "text/html;charset=utf-8")
    @ResponseBody//使用了redis 缓存页面需要添加
    public String toDetail(Model model,User user,@PathVariable Long goodsId,HttpServletRequest request,HttpServletResponse response){
        if (user == null) {
            return "login";
        }

        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsDetail:" + goodsId);
        if (StringUtils.hasText(html)) {
            return html;
        }

        model.addAttribute("user", user);
        //============处理秒杀倒计时和状态start ==============
        GoodsVo findgoodbyid = goodsService.findgoodbyid(goodsId);
        model.addAttribute("goods",findgoodbyid);
        Date startDate = findgoodbyid.getStartDate();
        Date endDate = findgoodbyid.getEndDate();
        Date nowDate = new Date();
        //秒杀状态
        int secKillStatus = 0;
        //秒杀倒计时
        int remainSeconds = 0;
        if (nowDate.before(startDate)) {
        //秒杀还没有开始,计算还有多少秒开始秒杀
            remainSeconds = (int) ((startDate.getTime() - nowDate.getTime()) / 1000);
        } else if (nowDate.after(endDate)) {
        //秒杀结束
            secKillStatus = 2;
            remainSeconds = -1;
        } else {
        //秒杀进行中
            secKillStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("secKillStatus", secKillStatus);
        model.addAttribute("remainSeconds", remainSeconds);

//        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
//        html = thymeleafViewResolver.getTemplateEngine().process("goodsList", webContext);
//        if (StringUtils.hasText(html)) {
//            //每60s 更新一次redis 页面缓存, 即60s 后, 该页面缓存失效, Redis 会清除该页面缓存
//            valueOperations.set("goodsList", html, 60, TimeUnit.SECONDS);
//        }
//        return html;



        //如果为null，手动渲染，存入redis 中
        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail", webContext);
        if (StringUtils.hasText(html)) {
        //设置每60s 更新一次缓存, 即60s 后, 该页面缓存失效, Redis 会清除该页面缓存
            valueOperations.set("goodsDetail:" + goodsId, html, 60, TimeUnit.SECONDS);
        }
        return html;

//        return "goodsDetail";
    }

}
