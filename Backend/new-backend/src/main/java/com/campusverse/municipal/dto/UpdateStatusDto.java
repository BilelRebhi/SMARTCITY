package com.campusverse.municipal.dto;

import com.campusverse.municipal.model.RequestStatus;

public class UpdateStatusDto {
    private RequestStatus status;
    private String comment;

    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
