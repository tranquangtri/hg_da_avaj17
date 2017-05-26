package game.client.core;

class Commands {
    public enum Mode{Console, Gui};
    private final Mode mode;
    public Commands(Mode mode){
        this.mode = mode;
    }
    public void excute(ICommand command){
        if (mode.equals(Mode.Console))
            command.excuteConsoleMode();
        else
            command.excuteGuiMode();
    }
}