package test;

import com.lgame.util.load.xml.JaxbUtil;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/1.
 */
public class XMLTest {
    /**
     * @param args
     */
    public static void main(String[] args) {

        //创建java对象

        Hotel hotel=new Hotel();
        hotel.setId(1);
        hotel.setName("name1");

        RoomTypeVO t1=new RoomTypeVO();
        t1.setPrice("20");
        t1.setTypeid(1);
        t1.setTypename("typename1");

        RoomTypeVO t2=new RoomTypeVO();
        t2.setPrice("30");
        t2.setTypeid(2);
        t2.setTypename("typename2");


        List<RoomTypeVO> list=new ArrayList<>();
        list.add(t1);
        list.add(t2);
        hotel.setRoomTypeVOs(list);

        //将java对象转换为XML字符串
        JaxbUtil requestBinder = new JaxbUtil(Hotel.class,
                JaxbUtil.CollectionWrapper.class);
        String retXml = requestBinder.toXml(hotel, "utf-8");
        System.out.println("xml:"+retXml);

        //将xml字符串转换为java对象
        JaxbUtil resultBinder = new JaxbUtil(Hotel.class,
                JaxbUtil.CollectionWrapper.class);
        Hotel hotelObj = resultBinder.fromXml(retXml);

        System.out.println("hotelid:"+hotelObj.getId());
        System.out.println("hotelname:"+hotelObj.getName());
        for(RoomTypeVO roomTypeVO:hotelObj.getRoomTypeVOs())
        {
            System.out.println("Typeid:"+roomTypeVO.getTypeid());
            System.out.println("Typename:"+roomTypeVO.getTypename());
        }


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

