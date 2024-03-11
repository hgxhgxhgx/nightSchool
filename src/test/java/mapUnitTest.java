import com.tencent.wxcloudrun.utils.MapUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class mapUnitTest {

    @Test
    public void testDistance(){
        String dis = MapUtils.calDistance("(40.11,116.42)", "39.97,116.32");
        System.out.println(dis);
    }

}
