/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.load.demo.json;


import com.lgame.util.json.JsonTool;
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
        List<Item> actList = (List<Item>) resourceService.listAll(Item.class);
        System.out.println(JsonTool.getJsonFromCollection(actList));
    }
}
