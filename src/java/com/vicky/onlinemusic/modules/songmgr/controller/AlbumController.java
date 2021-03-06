/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vicky.onlinemusic.modules.songmgr.controller;

import com.vicky.common.controller.MyEntityController;
import com.vicky.common.finalpackage.Final;
import com.vicky.common.utils.DealFile.WebFileUtils;
import com.vicky.common.utils.service.BaseService;
import com.vicky.common.utils.statusmsg.StatusMsg;
import com.vicky.onlinemusic.modules.songmgr.entity.Album;
import com.vicky.onlinemusic.modules.songmgr.service.AlbumService;
import com.vicky.onlinemusic.modules.songmgr.utils.Manager;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Vicky
 */
@Controller
@RequestMapping("album")
public class AlbumController extends MyEntityController<Album, String> {

    @Autowired
    private AlbumService albumService;

    @Override
    protected BaseService<Album, String> getBaseService() {
        return this.albumService;
    }

    @Override
    @RequestMapping("getPageData")
    @ResponseBody
    public Map<String, Object> getPageData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return super.getPageData(request, response); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @RequestMapping("getById")
    @ResponseBody
    public StatusMsg getById(HttpServletRequest request, HttpServletResponse response, String primaryKey) {
        return super.getById(request, response, primaryKey); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @RequestMapping("deleteById")
    @ResponseBody
    public StatusMsg deleteById(HttpServletRequest request, HttpServletResponse response, String primaryKey) throws Exception {
        Album album = this.albumService.selectByPrimaryKey(primaryKey);
        WebFileUtils.deleteFile(album.getCoverAbsolutePath());
        return super.deleteById(request, response, primaryKey); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping("saveOrUpdate")
    @ResponseBody
    public StatusMsg saveOrUpdate(Album t) throws Exception {
        Manager manager = super.getManager();
        Part cover = request.getPart("cover");
        if (cover != null && cover.getSize() > 0) {
            String[] strings = WebFileUtils.savePublicFileAtLocalServer(
                    cover, Final.WEB_ROOT_PATH, Final.IMAGE_PATH, manager.getUsername());
            t.setCoverAbsolutePath(strings[0]);
            t.setCoverRelativePath(Final.HOST_ADDRESS+strings[1]);
        }
        t.setCreateTime(new Date());
        if (t.getAlbumId()!= null) {
            this.albumService.updateSelective(t);
        } else {
            this.albumService.save(t);
        }
        return super.simpleBuildSuccessMsg("保存专辑成功!", t);
    }

}
