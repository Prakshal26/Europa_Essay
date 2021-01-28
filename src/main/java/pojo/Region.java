package pojo;

public class Region {

    String xmlId;
    String name;
    int countryId;
    String altHeading;
    String description;

    public String getXmlId() {
        return xmlId;
    }

    public void setXmlId(String xmlId) {
        this.xmlId = xmlId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getAltHeading() {
        return altHeading;
    }

    public void setAltHeading(String altHeading) {
        if (this.altHeading == null) {
            this.altHeading = altHeading;
        } else {
            this.altHeading = this.altHeading + " " + altHeading;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
