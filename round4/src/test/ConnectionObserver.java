package test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;

/**
 * 观察者
 *
 * @author fengzhen
 * @version v1.0, 2017/9/5 17:15
 */
public class ConnectionObserver implements Observer {

    private Connection connection;
    private boolean autoCommit = true;

    public ConnectionObserver(Connection connection) {
        this.connection = connection;
        try {
            autoCommit = connection.getAutoCommit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        try {
            switch (o.toString()) {
                case "commit":
                    if (!connection.isClosed() && !autoCommit) {
                        connection.commit();
                        System.out.println(connection.toString() + "   提交");
                    }
                    break;
                case "rollback":
                    if (!connection.isClosed() && !autoCommit) {
                        connection.rollback();
                        System.out.println(connection.toString() + "   回滚");
                    }
                    break;
                case "close":
                    if (!connection.isClosed()) {
                        connection.close();
                        System.out.println(connection.toString() + "    关闭");
                    }
                    break;
                case "auto":
                    autoCommit = true;
                    break;
                case "unauto":
                    autoCommit = false;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
