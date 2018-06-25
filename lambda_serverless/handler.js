'use strict';
var AWS = require("aws-sdk");
var moment = require("moment");
var uniqid = require('uniqid');
const aws_key = process.env.AMAZON_KEY;
const aws_secret = process.env.AMAZON_SECRET_KEY;
const aws_region = process.env.AMAZON_REGION;

AWS.config.update({
    region: aws_region,
    accessKeyId: aws_key,
    secretAccessKey: aws_secret
});

var docClient = new AWS.DynamoDB.DocumentClient();
var codepipeline = new AWS.CodePipeline();
var table = "approval_request";

module.exports.snsTrigger = (event, context, callback) =>
{
    var now_date = moment().format("YYYY-MM-DD HH:mm:ss");
    //var parse = JSON.parse(event);
    var statusCode = 200;
    var records = event.Records;
    for (var i = 0; i < records.length; i++) {
        var Sns = records[i].Sns;
        var MessageId = Sns.MessageId;
        var Message = Sns.Message;
        var Timestamp = Sns.Timestamp;
        var Subject = Sns.Subject;
        var parse_message = JSON.parse(Message);
        var params = {
            Item: {
                id: uniqid(),
                Timestamp: Timestamp,
                Message: Message,
                ApprovalStatus: "WAITING_APPROVAL",
                Subject: Subject,
                Token: parse_message.approval.token,
                StageName: parse_message.approval.stageName,
                ActionName: parse_message.approval.actionName,
                PipelineName:parse_message.approval.pipelineName
            },
            TableName: table
        };

        docClient.put(params, function (err, data) {
            if (err) {
                statusCode = 400;
                console.error("Unable to add item. Error JSON:", JSON.stringify(err, null, 2));
            } else {
                console.log(JSON.stringify(event, null, 2));
                console.log(event);
            }
        });
    }

    const response = {
        statusCode: statusCode,
        body: JSON.stringify({
            message: 'Go Serverless v1.0! Your function executed successfully!',
            input: event,
        }),
    };

    callback(null, response);

    // Use this code if you don't use the http event with the LAMBDA-PROXY integration
    // callback(null, { message: 'Go Serverless v1.0! Your function executed successfully!', event });
}
;


module.exports.deployToEcs = (event, context, callback) =>
{
    console.log(event);
    console.log(event.body);
    var parse_json = JSON.parse(event.body);
    console.log(parse_json);

    var approval_params = {
        "pipelineName": parse_json.pipelineName,
        "stageName": parse_json.stageName,
        "actionName": parse_json.actionName,
        "token": parse_json.token,
        "result": {
            "status": parse_json.action,
            "summary": parse_json.message
        }
    };
    var statusCode = 200;
    codepipeline.putApprovalResult(approval_params, function(err, data) {
        if (err){
            console.log(err, err.stack)
        } else {
            var act = "APPROVED";
            if(parse_json.action == "Rejected"){
                act = "REJECTED";
            }
            var params = {
                TableName:table,
                Key:{
                    "id": parse_json.id
                },
                UpdateExpression: "set ApprovalStatus = :r",
                ExpressionAttributeValues:{
                    ":r":act
                }
            };
            docClient.update(params, function(err, data) {
                if (err) {
                    statusCode = 400;
                    console.error("Unable to update item. Error JSON:", JSON.stringify(err, null, 2));
                } else {
                    console.log("UpdateItem succeeded:", JSON.stringify(data, null, 2));
                }
            });
            console.log(data)
        }
    });
    const response = {
        statusCode: statusCode,
        body: JSON.stringify({
            status: "OK",
            input: event,
        }),
    };

    callback(null, response);

    // Use this code if you don't use the http event with the LAMBDA-PROXY integration
    // callback(null, { message: 'Go Serverless v1.0! Your function executed successfully!', event });
}
;
