package com.andrey_derma.approvalapp.model;


import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "approval_request")

public class ApprovalRequestDO {
    private String _id;
    private String _message;
    private String _status;
    private String _subject;
    private String _timestamp;

    private String _actionName;
    private String _actionStagename;
    private String _actionPipelineName;
    private String _token;

    @DynamoDBHashKey(attributeName = "id")
    @DynamoDBAttribute(attributeName = "id")
    public String getId() {
        return _id;
    }

    public void setId(final String _id) {
        this._id = _id;
    }


    @DynamoDBAttribute(attributeName = "ApprovalStatus")
    public String getStatus() {
        return _status;
    }

    public void setStatus(final String _status) {
        this._status = _status;
    }


    @DynamoDBAttribute(attributeName = "Message")
    public String get_message() {
        return _message;
    }

    public void set_message(String _message) {
        this._message = _message;
    }

    @DynamoDBAttribute(attributeName = "Subject")
    public String get_subject() {
        return _subject;
    }

    public void set_subject(String _subject) {
        this._subject = _subject;
    }

    @DynamoDBAttribute(attributeName = "Timestamp")
    public String get_timestamp() {
        return _timestamp;
    }

    public void set_timestamp(String _timestamp) {
        this._timestamp = _timestamp;
    }

    @DynamoDBAttribute(attributeName = "ActionName")
    public String get_actionName() {
        return _actionName;
    }

    public void set_actionName(String _actionName) {
        this._actionName = _actionName;
    }

    @DynamoDBAttribute(attributeName = "StageName")
    public String get_actionStagename() {
        return _actionStagename;
    }

    public void set_actionStagename(String _actionStagename) {
        this._actionStagename = _actionStagename;
    }

    @DynamoDBAttribute(attributeName = "PipelineName")
    public String get_actionPipelineName() {
        return _actionPipelineName;
    }

    public void set_actionPipelineName(String _actionPipelineName) {
        this._actionPipelineName = _actionPipelineName;
    }

    @DynamoDBAttribute(attributeName = "Token")
    public String get_token() {
        return _token;
    }

    public void set_token(String _token) {
        this._token = _token;
    }
}