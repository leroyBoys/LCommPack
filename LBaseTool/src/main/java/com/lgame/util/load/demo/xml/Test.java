package com.lgame.util.load.demo.xml;

import com.lgame.util.json.JsonUtil;
import com.lgame.util.load.xml.XmlApi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Administrator on 2017/4/1.
 */
public class Test {
    private final static String path = "D:\\hot2.xml";

    /**
     * @param args
     */
    public static void main(String[] args) {

        create();
       /* XmlLoad xmlLoad = new XmlLoad(path);
        Element element = xmlLoad.searchSingleNode("/root/cmd[@value>2]",xmlLoad.getRoot());
        if(element != null){
            element.setAttribute("news222222222222","22");
        }else {
            System.out.println("===>notfind");
        }
        xmlLoad.save();*/
    }


    public static void create(){
        //创建java对象
        Root hotel=new Root();

        for(int i=0;i<10;i++){
            Root cmd= new Root();
            cmd.setCmd(i*100+2);
            cmd.setNeedLogin(i%2==0);
            cmd.setServerType(i/2);
            cmd.setType(i%2);
            hotel.addCmd(cmd);
        }

        //将java对象转换为XML字符串
        XmlApi.save(hotel,path,true);
        System.out.println("========over");
       System.out.println(JsonUtil.getJsonFromBean(XmlApi.readObjectFromXml(Root.class,path)));
    }

   public static class RoomTypeVO {
        ///@XmlElement(name = "typeid")
        @XmlAttribute(name = "typeid")
        public int getTypeid() {
            return typeid;
        }
        public void setTypeid(int typeid) {
            this.typeid = typeid;
        }
        @XmlElement(name = "typename")
        public String getTypename() {
            return typename;
        }
        public void setTypename(String typename) {
            this.typename = typename;
        }
        @XmlElement(name = "price")
        public String getPrice() {
            return price;
        }
        public void setPrice(String price) {
            this.price = price;
        }
        private int typeid;
        private String typename;
        private String price;

    }

    @XmlRootElement(name = "hotel")
    public static class Hotel {
        @XmlElementWrapper(name = "RoomTypeVOs")
        @XmlElement(name = "RoomTypeVO")
        public List<RoomTypeVO> getRoomTypeVOs() {
            return RoomTypeVOs;
        }
        public void setRoomTypeVOs(List<RoomTypeVO> roomTypeVOs) {
            RoomTypeVOs = roomTypeVOs;
        }
        private List<RoomTypeVO> RoomTypeVOs;

        /////@XmlElement(name = "id")
        @XmlAttribute(name = "id")
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        @XmlElement(name = "name")
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        private int id;
        private String name;
    }
}

