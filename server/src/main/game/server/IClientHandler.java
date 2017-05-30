package game.server;

/**
 * Created by tranq on 5/29/2017.
 */
public interface IClientHandler {
    void run();
    void setClientManager(IClientManager clientManager);
}
