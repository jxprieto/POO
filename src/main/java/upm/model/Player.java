package upm.model;

public class Player {
    private static final Double MIN_SCORE = -999999.0;

    private final String name;
    private final String username;
    private double score;

    public Player(String name, String username) {
        this.name = name;
        this.username = username;
        this.score = 0.0;
    }

    public Player(String name, String username, double score) {
        this.name = name;
        this.username = username;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public double getScore() {
        return score;
    }
    public String getUsername() {
        return username;
    }

    public void setScore(Double score) {
        if (score < MIN_SCORE)
            throw new IllegalArgumentException("Score must be greater than 0");
        this.score = score;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", score=" + score +
                '}';
    }
}
