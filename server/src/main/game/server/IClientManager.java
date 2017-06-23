package game.server;
public interface IClientManager{
    public void sendAll(String message);

    /**
     * Gửi chuỗi <tt>message</tt> đến một client xác định
     * @param client là thứ tự của client (0-3)
     * @param message là một chuối cần gửi đến client
     * @implNote phương thức này sẽ không kiểm tra client có nằm trong các client
     * hiện quản lý bởi bởi IClientManager, trách nhiệm kiểm tra sẽ do user chịu trách nhiệm
     * <pre>
     * {@code if (index < clientManager.getCount())
     * clientManager.send(index, "Hello!" );
     * }
     * </pre>
     */
    public void send(int client, String message);
    public int getCount();
    public String receive(int index);
    public void add(IClient client);
    public void remove(int index);
    public void set(int index, IClient client);
}