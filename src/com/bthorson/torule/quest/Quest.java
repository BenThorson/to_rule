package com.bthorson.torule.quest;

import com.bthorson.torule.geom.Direction;
import com.bthorson.torule.geom.Point;

import java.util.List;

/**
 * User: Ben Thorson
 * Date: 12/6/12
 * Time: 2:34 PM
 */
public class Quest {

    private String name;
    private String text;
    private List<String> textParams;

    private String givenBy;
    private List<ScriptedSpawn> spawns;

    private String objective;

    private QuestReward reward;
    private int distance;
    private Direction direction;
    private Point position;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getTextParams() {
        return textParams;
    }

    public void setTextParams(List<String> textParams) {
        this.textParams = textParams;
    }

    public String getGivenBy() {
        return givenBy;
    }

    public void setGivenBy(String givenBy) {
        this.givenBy = givenBy;
    }

    public List<ScriptedSpawn> getSpawns() {
        return spawns;
    }

    public void setSpawns(List<ScriptedSpawn> spawns) {
        this.spawns = spawns;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public QuestReward getReward() {
        return reward;
    }

    public void setReward(QuestReward reward) {
        this.reward = reward;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }
}
