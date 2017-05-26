package game.server;

public interface IClient{
    void send(String message);
    String receive();
}
