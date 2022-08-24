package com.vnightray.springsqlitetest.controller;

import com.vnightray.springsqlitetest.dao.ProDao;
import com.vnightray.springsqlitetest.domain.ProEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.ws.soap.Addressing;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/test")
public class ProController {

    private static Logger logger = LoggerFactory.getLogger(ProController.class);
    @Autowired
    private ProDao dao;

    @RequestMapping("/pro")
    @ResponseBody
    public String search(){
        List<ProEntity> list = dao.list();
        return list.toString();
    }

    @RequestMapping("/first/{id}")
    @ResponseBody
    public String findById(@PathVariable("id") String id){
        ProEntity entity = dao.findById(Integer.parseInt(id));
        if (entity == null) return "";
        logger.info(entity.toString());
        return entity.toString();
    }

    @RequestMapping("/add")
    @ResponseBody
    public String addProEntity(){
        ProEntity entity = new ProEntity();
        entity.setId(8);
        entity.setDatabaseKey("secondary");
        entity.setDatabaseUrl("http://localhost");
        entity.setCreateTime(LocalDateTime.now());
        logger.info(entity.toString());
        int i = dao.add(entity);
        logger.info("Added entity: " + i);
        return entity.toString();
    }

}
