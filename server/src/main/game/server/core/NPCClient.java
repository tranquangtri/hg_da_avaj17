package game.server.core;


import game.server.IClient;

class NPCClient implements IClient {
    private String messageReceived = "";
    @Override
    public void send(String message) {
        this.messageReceived = message;
    }

    @Override
    public String receive(){
        return "NPC";
    }
}
