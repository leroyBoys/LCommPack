/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsocket.loadconfig.load;


import com.config.ResourceService;
import com.config.ResourceServiceImpl;
import com.lgame.util.json.JsonTool;
import com.lsocket.loadconfig.model.Item;
import com.lsocket.loadconfig.model.RoomSettingTemplateGen;

import java.util.List;

/**
 *
 */
public class LoadConfigBean {
    private ResourceService resourceService;

    public void loadAll() {
        resourceService = ResourceServiceImpl.getInstance("D:\\360极速浏览器下载\\棋牌_update\\qpgame\\t\\");
        loadActivity();
    }

    /**
     * 活动列表
     */
    public void loadActivity() {
        List<RoomSettingTemplateGen> actList = (List<RoomSettingTemplateGen>) resourceService.listAll(RoomSettingTemplateGen.class);
        System.out.println(JsonTool.getJsonFromCollection(actList));
    }
}
