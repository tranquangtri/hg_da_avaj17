package game.server;


class NPCClient implements IClient {

    @Override
    public void send(String message) {

    }

    @Override
    public String receive(){
        return "NPC";
    }
}
