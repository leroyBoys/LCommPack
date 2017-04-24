/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lsocket.loadconfig.model;

import com.config.annotation.Id;
import com.config.annotation.Resource;

/**
 *
 * @author lxh
 */
@Resource(suffix="xml",sheetName = "")
public class Item {
    @Id
    private int id;
    private String name;

    private String description;
    private int turn_item;//可转换的道具id

    private int turn_item_Time;//转换后道具的有效期(分钟)

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTurn_item() {
        return turn_item;
    }

    public void setTurn_item(int turn_item) {
        this.turn_item = turn_item;
    }

    public int getTurn_item_Time() {
        return turn_item_Time;
    }

    public void setTurn_item_Time(int turn_item_Time) {
        this.turn_item_Time = turn_item_Time;
    }



}
