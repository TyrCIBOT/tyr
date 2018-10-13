package org.xstefank.model.json.triggerbuild;

import java.util.ArrayList;
import java.util.List;

public class Properties {

    List<Property> property;

    public Properties(String sha, String pull, String branch) {
        property = new ArrayList<>();
        property.add(new Property("hash", sha));
        property.add(new Property("pull", pull));
        property.add(new Property("branch", branch));
    }

    public List<Property> getProperty() {
        return property;
    }

    public void setProperty(List<Property> property) {
        this.property = property;
    }
}
