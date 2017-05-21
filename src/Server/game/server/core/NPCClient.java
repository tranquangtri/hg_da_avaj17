package game.server.core;


import game.server.IClient;

class NPCClient implements IClient {

    @Override
    public void send(String message) {

    }

    @Override
    public String receive(){
        return "NPC";
    }
}
