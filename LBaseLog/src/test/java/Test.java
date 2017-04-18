import com.lgame.util.comm.SplitConstant;
import com.lgame.util.time.DateTimeTool;
import com.logger.log.SystemLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2017/4/3.
 */
public class Test {
    public static final Logger logger = LoggerFactory.getLogger(Test.class);
    public static void main(String[] args) {
        try {
            int i = 0;
            System.out.println(i/i);
            logger.debug(SplitConstant.DELIMITER_ARGS+"=======>test"+ DateTimeTool.getCurrentDateStr());

        }catch (Exception e){
            SystemLogger.error(Test.class,e);
        }
    }
}
