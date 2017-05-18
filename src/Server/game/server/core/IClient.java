package game.server.core;

interface IClient{
    void send(String message);
    String receive();
}
