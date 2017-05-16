package game.server;

class NormalConfiguration implements IConfiguration {
    private int port;
    public NormalConfiguration(int port){
        this.port = port;
    }
    @Override
    public int getPort() {
        return port;
    }
}
