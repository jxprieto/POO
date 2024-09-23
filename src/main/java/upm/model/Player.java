package upm.model;

public class Player {
    private static final Double MIN_SCORE = -999999.0;

    private final String name;
    private double score;

    public Player(String name) {
        this.name = name;
        this.score = 0.0;
    }

    public String getName() {
        return name;
    }

    public double getScore() {
        return score;
    }

    public void setScore(Double score) {
        if (score < MIN_SCORE)
            throw new IllegalArgumentException("Score must be greater than 0");
        this.score = score;
    }
}
