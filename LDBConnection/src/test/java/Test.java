import com.mysql.impl.SqlPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Administrator on 2017/4/2.
 */
public class Test {


    public void go() throws SQLException {
        String sql = "select * from `tbl_player`";
        SqlPool sqlPool = new SqlPool(SqlPool.DataSourceType.Hikari);


        Connection con = sqlPool.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
       // ps.executeUpdate();
        ResultSet rs =  ps.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getInt("id"));
            System.out.println(rs.getString("name"));
        }
        ps.close();
        con.close();
    }

    @org.junit.Test
    public void thres(){


    }
}
