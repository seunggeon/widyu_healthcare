package com.widyu.healthcare.core.domain.domain.v1;

public enum GoalType {
    MEDICATION("MEDICATION"),
    WALKING("WALKING"),
    GENERAL("GENERAL");

    private String goalType;
    GoalType(String goalType){this.goalType = goalType;}
    public String getGoalType(){return goalType;}
}