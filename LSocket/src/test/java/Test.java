import com.FirstProbuff;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lgame.util.comm.Tools;
import com.lgame.util.file.FileTool;
import com.protobuftest.protobuf.PersonProbuf;
import com.protobuftest.protobuf.Student;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by Administrator on 2017/4/2.
 */
public class Test {
    public static void main(String[] args) throws IOException {
    /*    ConcurrentMap map = new ConcurrentSkipListMap();

        for(int i = 0;i<100000;i++){

        }*/

/*        SocketConfig serverConfig = SocketConfig.getInstance();
        System.out.println(JsonTool.bean2json(serverConfig));*/
        PersonProbuf.Person.Builder peron = PersonProbuf.Person.newBuilder();
        peron.setEmail("tom222张三李四张三李四张三李四张三李四22222222222222222");
        peron.setName("tom张三李四张三李四张三李四张三李四张三李四张三李四张三李四张三李四张三李四张三李四张三李四张三李四张三李四张三李四张三李四张三李四张三李四张三李四张三李四张三李四张三李四张三李四e");
        peron.setId(1);
        PersonProbuf.Person.PhoneNumber.Builder phoneNumber = PersonProbuf.Person.PhoneNumber.newBuilder();

        Student.Student22.Builder stat = Student.Student22.newBuilder();
        stat.setQuery("tommmm");
        byte[] bytes = peron.build().toByteArray();
       // System.out.println("===>"+Arrays.toString(bytes));

        List<byte[]> bysList = new ArrayList<>();
        String path = "D:\\Dev\\Dev\\data.log";

        bysList.add(bytes);
        Tools.writeBytes(path,bysList);

        bysList = Tools.readBytes(path);

        for(byte[] by:bysList){
          //  System.out.println("===>"+Arrays.toString(by));
            PersonProbuf.Person personss = new FirstProbuff().getObject(by);
            out(personss.toBuilder());
            System.out.println(personss.getName());
        }
     /*   PersonProbuf.Person personss = new FirstProbuff().getObject(bytes);
       // System.out.println("===========>"+personss.toString());
        out(peron);
        out(stat);
        out(personss.toBuilder());*/


    }

    public static void out(com.google.protobuf.GeneratedMessage.Builder builder){
        System.out.println(builder.toString());
    }

}
