package com.example.lvtn_app.Model;

public class Issue {
    private int id_Issue;
    private String issueName;
    private String issueProjectType;
    private String issueDecription;
    private String issueType;
    private String issueStartDate;
    private String issuePriority;
    private String issueAssignee;
    private String issueEstimateTime;
    private String issueCreator;
    private int issueProjectID;
    private String issueFinishDate;

    public Issue(int id_Issue, String issueName, String issueProjectType, String issueDecription, String issueType, String issueStartDate, String issuePriority, String issueAssignee, String issueEstimateTime, String issueCreator, int issueProjectID, String issueFinishDate) {
        this.id_Issue = id_Issue;
        this.issueName = issueName;
        this.issueProjectType = issueProjectType;
        this.issueDecription = issueDecription;
        this.issueType = issueType;
        this.issueStartDate = issueStartDate;
        this.issuePriority = issuePriority;
        this.issueAssignee = issueAssignee;
        this.issueEstimateTime = issueEstimateTime;
        this.issueCreator = issueCreator;
        this.issueProjectID = issueProjectID;
        this.issueFinishDate = issueFinishDate;
    }

    public Issue(int id_Issue, String issueName, String issueDecription, String issueStartDate, String issueCreator) {
        this.id_Issue = id_Issue;
        this.issueName = issueName;
        this.issueDecription = issueDecription;
        this.issueStartDate = issueStartDate;
        this.issueCreator = issueCreator;
    }

    public int getId_Issue() {
        return id_Issue;
    }

    public void setId_Issue(int id_Issue) {
        this.id_Issue = id_Issue;
    }

    public String getIssueName() {
        return issueName;
    }

    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }

    public String getIssueProjectType() {
        return issueProjectType;
    }

    public void setIssueProjectType(String issueProjectType) {
        this.issueProjectType = issueProjectType;
    }

    public String getIssueDecription() {
        return issueDecription;
    }

    public void setIssueDecription(String issueDecription) {
        this.issueDecription = issueDecription;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getIssueStartDate() {
        return issueStartDate;
    }

    public void setIssueStartDate(String issueStartDate) {
        this.issueStartDate = issueStartDate;
    }

    public String getIssuePriority() {
        return issuePriority;
    }

    public void setIssuePriority(String issuePriority) {
        this.issuePriority = issuePriority;
    }

    public String getIssueAssignee() {
        return issueAssignee;
    }

    public void setIssueAssignee(String issueAssignee) {
        this.issueAssignee = issueAssignee;
    }

    public String getIssueEstimateTime() {
        return issueEstimateTime;
    }

    public void setIssueEstimateTime(String issueEstimateTime) {
        this.issueEstimateTime = issueEstimateTime;
    }

    public String getIssueCreator() {
        return issueCreator;
    }

    public void setIssueCreator(String issueCreator) {
        this.issueCreator = issueCreator;
    }

    public int getIssueProjectID() {
        return issueProjectID;
    }

    public void setIssueProjectID(int issueProjectID) {
        this.issueProjectID = issueProjectID;
    }

    public String getIssueFinishDate() {
        return issueFinishDate;
    }

    public void setIssueFinishDate(String issueFinishDate) {
        this.issueFinishDate = issueFinishDate;
    }
}
