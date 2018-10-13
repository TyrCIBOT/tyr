package org.xstefank.model.json.triggerbuild;

public class BuildJson {

    private String branchName;
    private BuildType buildType;
    private Properties properties;

    public BuildJson(String branchName, String id, String sha, String pull, String branch) {
        this.branchName = branchName;
        buildType = new BuildType(id);
        properties = new Properties(sha, pull, branch);
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public BuildType getBuildType() {
        return buildType;
    }

    public void setBuildType(BuildType buildType) {
        this.buildType = buildType;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
