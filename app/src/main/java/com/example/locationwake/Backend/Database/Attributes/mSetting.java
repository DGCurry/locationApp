package com.example.locationwake.Backend.Database.Attributes;

public class mSetting implements AttributeInterface {
    private String setting;

    public mSetting(String setting) {
        this.setting = setting;
    }

    /**
     * Return the setting information as String
     * @return setting setting the user has added
     *                SLT: silent
     *                VBR: vibrate
     *                SND: sound
     */
    public String getSetting() {
        return setting;
    }

    /**
     * Add the setting information as String
     * @return setting setting the user has added
     *                SLT: silent
     *                VBR: vibrate
     *                SND: sound
     */
    public void setSetting(String setting) {
        this.setting = setting;
    }

    @Override
    public int getType() {
        return AttributeInterface.SETTING_TYPE;
    }

    @Override
    public boolean isValid() {
        return (setting.equals("VBR") || setting.equals("SLT") || setting.equals("SND"));
    }
}
