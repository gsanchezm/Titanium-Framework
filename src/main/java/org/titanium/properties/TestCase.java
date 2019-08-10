package org.titanium.properties;

public class TestCase extends Test {
    private String RunMode;
    private String DDTMode;
    private String TCKey;

    public int getTCKey(){
        Double key = new Double(TCKey);
        System.out.println(key.intValue());
        return key.intValue();
    }

    public void setTCKey(String sTCKey){
        this.TCKey = sTCKey;
    }

    public String getRunMode() {
        return this.RunMode;
    }

    public void setRunMode(String sRunMode) {
        this.RunMode = sRunMode;
    }

    public String getDDTMode() {
        return this.DDTMode;
    }

    public void setDDTMode(String sDDTMode) {
        this.DDTMode = sDDTMode;
    }
}
