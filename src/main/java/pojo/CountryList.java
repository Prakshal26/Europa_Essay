package pojo;

public class CountryList {

    String name;
    String xmlId;
    String altHeading;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getXmlId() {
        return xmlId;
    }

    public void setXmlId(String xmlId) {
        this.xmlId = xmlId;
    }

    public String getAltHeading() {
        return altHeading;
    }

    public void setAltHeading(String altHeading) {

        if (this.altHeading == null) {
            this.altHeading = altHeading;
        } else {
            this.altHeading = this.altHeading + altHeading;
        }
    }
}
