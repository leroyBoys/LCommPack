package test;


import com.lgame.util.comm.ClassIntrospector;
import com.lgame.util.comm.ReflectionTool;
import com.lgame.util.load.demo.xml.Root;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2018/4/12.
 */
public class Test2 {
    public Set<Integer> list;
/*    private int num;
    private Integer nums;
    private Test2 test2;*/

    public static void main(String[] args) throws InterruptedException, NoSuchFieldException {
       // new POIReadData().read("D:\\ww.xls");

        String str="adsf23(dsfsdl)";
        System.out.println(str.replaceAll("(?<=\\()(.*?)(?=\\))", "").replaceAll("(|)",""));

        Test2 test2 = new Test2();
        Field[] fields = test2.getClass().getDeclaredFields();
        for(Field field:fields){
            Type type = field.getType();
            Type type2 = field.getGenericType();

            ParameterizedType types =(ParameterizedType)type2;
            type = types.getOwnerType();
            System.out.println("========getOwnerType:"+type);
            Class cls = (Class) types.getRawType();


            System.out.println(cls.getSimpleName().toLowerCase().equals("set"));
            System.out.println(type2.getTypeName());
            if(field.getName().equals("list")){
                System.out.println(type2.getTypeName().startsWith("List"));
               Class cl= (Class) (types).getActualTypeArguments()[0];
                System.out.println(cl.getName());
            }else {

            }
        }

    }
}
