/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.load.demo.excel;


import com.lgame.util.json.JsonUtil;
import com.lgame.util.load.ResourceService;
import com.lgame.util.load.ResourceServiceImpl;

import java.util.List;

/**
 *
 */
public class Test {
    private ResourceService resourceService;

    public void loadAll() {
        resourceService = ResourceServiceImpl.getInstance("resources");
        loadActivity();
    }

    /**
     * 活动列表
     */
    public void loadActivity() {
        List<RoomSettingTemplateGen> actList = (List<RoomSettingTemplateGen>) resourceService.listAll(RoomSettingTemplateGen.class);
        System.out.println(JsonUtil.getJsonFromBean(actList));
    }
}
