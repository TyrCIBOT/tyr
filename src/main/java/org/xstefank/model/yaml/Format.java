package org.xstefank.model.yaml;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Format {

    private Pattern title;
    private Pattern commit;
    private SkipPatterns skipPatterns;
    private Description description;
    private List<String> additional;
    private Map<String, String> commands;

    public SkipPatterns getSkipPatterns() {
        return skipPatterns;
    }

    public void setSkipPatterns(SkipPatterns skipPatterns) {
        this.skipPatterns = skipPatterns;
    }

    public Pattern getTitle() {
        return title;
    }

    public void setTitle(Pattern title) {
        this.title = title;
    }

    public Pattern getCommit() {
        return commit;
    }

    public void setCommit(Pattern commit) {
        this.commit = commit;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public List<String> getAdditional() {
        return additional;
    }

    public void setAdditional(List<String> additional) {
        this.additional = additional;
    }

    public Map<String, String> getCommands() {
        return commands;
    }

    public void setCommands(Map<String, String> commands) {
        this.commands = commands;
    }
}
