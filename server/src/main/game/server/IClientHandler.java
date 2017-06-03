package game.server;

public interface IClientHandler {
    void run();
    void setClientManager(IClientManager clientManager);
}
