package game.server;

import java.io.IOException;

interface IClient{
    void send(String message);
    String receive();
}
