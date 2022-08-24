import com.vnightray.springsqlitetest.dao.ProDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringTest1 {

    @Autowired
    ProDao dao;

    @Test
    public void test01(){
        System.out.println(dao.list());
    }
}
