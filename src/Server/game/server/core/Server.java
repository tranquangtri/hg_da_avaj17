package game.server.core;
import game.server.IClientManager;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.util.concurrent.*;

/**
 * Tạo serverSocket, lắng nghe các kết nối của client và thêm vào {@link ClientManager}
 * sau đó gọi RoutineServer để xử lý theo kịch bản gửi nhận dữ liệu
 */
class Server{
    private final int port;

    public Server(int port){
        this.port = port;
    }

    public void start(){
        /* Create and open socket on specific port */
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
        }catch (IOException e){
            System.err.println("\nCannot open and create serversocket !\n");
            throw new RuntimeException(e);
        }

        /* Create time counting task for n seconds*/
        Callable<Boolean> timeCount = ()-> {
                try {
                    TimeUnit.SECONDS.sleep(20);
                } catch (InterruptedException e) {}
                return true;
        };

        /* Create cached thread pool */
        ExecutorService services = Executors.newCachedThreadPool();

        /* Start thread timeCount and acceptClients */
        Future<Boolean> futureTimeCount = services.submit(timeCount);
        services.execute(TaskAcceptClients.fromServerSocket(server));

         /* Wait until timeout and close server socket */
        try {
            futureTimeCount.get();
            if (!server.isClosed());
                 server.close();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            IClientManager clientManager = ClientManager.getInstance();
            Object o = null;
            try {
                o = Class.forName("game.server.RoutineServer").getConstructor(IClientManager.class).newInstance(clientManager);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            if (o instanceof Runnable){
                Runnable routine = (Runnable)o;
                routine.run();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}


