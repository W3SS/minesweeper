package sh.w3ss.cm.models;

public class ResultEvent {
    private final boolean won;

    public ResultEvent(boolean won) {
        this.won = won;
    }

    public boolean isWon() {
        return won;
    }
}
