package simemens;

/**
 * @author bo.zhou
 * @date 2019/12/12
 */
public class Send {

    public static void main(String[] args) {
        try {
            System.out.println(args[0] + " " + args[1]);
            S7Client s7Client = new S7Client(args[0], args[1]);
            s7Client.initConnection();
            System.out.println("connection success to " + args[1] + " on port 102");
            s7Client.writeBoolean(args[2], true);
            System.out.println(s7Client.readBoolean(args[2]));
            s7Client.writeBoolean(args[2], false);
            System.out.println(s7Client.readBoolean(args[2]));
            s7Client.close();
            System.out.println("connection closed ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
