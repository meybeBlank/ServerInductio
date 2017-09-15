package test;

public class Main {
    public static void main(String[] args) {
//        MyObservable myObservable = new MyObservable();
//        ConnectionObserver connectionObserver = new ConnectionObserver("数据库AAA");
//        ConnectionObserver connectionObserver1 = new ConnectionObserver("数据库BBB");
//
//        // 添加到观察者
//        myObservable.addObserver(connectionObserver);
//        myObservable.addObserver(connectionObserver1);
//
//        // 提交
//        myObservable.commit();
//
//        // 移除一个观察者
//        myObservable.deleteObserver(connectionObserver);
//
//        myObservable.rollback();
//        myObservable.close();

        String sql = "UPDATE FUN_USERS SET USER_NAME = ? WHERE USER_MOBILE = ?";
        DBHelper.getInstance().update(DBHelper.DB_USER_NAME,sql,null);
    }
}
