package com.campusverse.municipal.dto;

public class ValidateApplicationDto {
    private boolean complete;
    private String comment;

    public boolean isComplete() { return complete; }
    public void setComplete(boolean complete) { this.complete = complete; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
