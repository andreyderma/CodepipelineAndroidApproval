# DroidOps App
Folow this tutorial to setup CI/CD pipeline with manual approval using android.
 

#How to run?
Clone or download this repository to your local machine.

## lambda_serverless
Change directory to **lambda_serverless** and deploy to your AWS using this following command  

`sls deploy --aws_key YOUR_AWS_KEY --aws_secret_key YOUR_AWS_SECRET --aws_region YOUR_AWS_REGION 
`

Don't forget to install serverless framework!

## Android project
Open **ApprovalApp** project using Android Studio and create AWS mobilehub.
Please follow this tutorial how to setup AWS mobile hub:
https://docs.aws.amazon.com/aws-mobile/latest/developerguide/getting-started.html
https://docs.aws.amazon.com/aws-mobile/latest/developerguide/add-aws-mobile-nosql-database.html

Download **awsconfiguration.json** and move to **/app/src/main/res/raw/** 
Rebuild Android studio project and run.
