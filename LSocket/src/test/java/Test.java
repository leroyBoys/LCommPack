import com.FirstProbuff;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.protobuftest.protobuf.PersonProbuf;
import com.protobuftest.protobuf.Student;

import java.util.Arrays;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by Administrator on 2017/4/2.
 */
public class Test {
    public static void main(String[] args) throws InvalidProtocolBufferException {
    /*    ConcurrentMap map = new ConcurrentSkipListMap();

        for(int i = 0;i<100000;i++){

        }*/

/*        SocketConfig serverConfig = SocketConfig.getInstance();
        System.out.println(JsonTool.bean2json(serverConfig));*/
        PersonProbuf.Person.Builder peron = PersonProbuf.Person.newBuilder();
        peron.setEmail("1wlvxiaohui");
        peron.setName("tome");
        peron.setId(1);
        PersonProbuf.Person.PhoneNumber.Builder phoneNumber = PersonProbuf.Person.PhoneNumber.newBuilder();

        Student.Student22.Builder stat = Student.Student22.newBuilder();
        stat.setQuery("tommmm");

        byte[] bytes = peron.build().toByteArray();
        PersonProbuf.Person personss = new FirstProbuff().getObject(bytes);
        System.out.println("===========>"+personss.toString());
        out(peron);
        out(stat);
        out(personss.toBuilder());


    }

    public static void out(com.google.protobuf.GeneratedMessage.Builder builder){
        System.out.println(builder.toString());
    }

}
