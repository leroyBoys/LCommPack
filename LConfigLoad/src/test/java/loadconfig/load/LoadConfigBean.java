/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loadconfig.load;


import com.config.ResourceService;
import com.config.ResourceServiceImpl;
import com.lgame.util.json.JsonTool;
import loadconfig.model.RoomSettingTemplateGen;

import java.util.List;

/**
 *
 */
public class LoadConfigBean {
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
        System.out.println(JsonTool.getJsonFromCollection(actList));
    }
}
