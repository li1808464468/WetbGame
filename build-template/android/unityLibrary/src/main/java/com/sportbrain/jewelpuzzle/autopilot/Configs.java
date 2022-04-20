package com.sportbrain.jewelpuzzle.autopilot;

import java.util.List;

public class Configs {
    private String ESID;
    private String switchflag;
    private Remove remove;
    private Combo combo;
    private Special_gold special_gold;
    private Special_blue_select special_blue_select;
    private Special_blue_split special_blue_split;
    private Bestscore_ingame bestscore_ingame;
    private Bestscore_roundover bestscore_roundover;
    public void setESID(String ESID) {
        this.ESID = ESID;
    }
    public String getESID() {
        return ESID;
    }

    public void setSwitchflag(String switchflag) {
        this.switchflag = switchflag;
    }
    public String getSwitchflag() {
        return switchflag;
    }

    public void setRemove(Remove remove) {
        this.remove = remove;
    }
    public Remove getRemove() {
        return remove;
    }

    public void setCombo(Combo combo) {
        this.combo = combo;
    }
    public Combo getCombo() {
        return combo;
    }

    public void setSpecial_gold(Special_gold special_gold) {
        this.special_gold = special_gold;
    }
    public Special_gold getSpecial_gold() {
        return special_gold;
    }

    public void setSpecial_blue_select(Special_blue_select special_blue_select) {
        this.special_blue_select = special_blue_select;
    }
    public Special_blue_select getSpecial_blue_select() {
        return special_blue_select;
    }

    public void setSpecial_blue_split(Special_blue_split special_blue_split) {
        this.special_blue_split = special_blue_split;
    }
    public Special_blue_split getSpecial_blue_split() {
        return special_blue_split;
    }

    public void setBestscore_ingame(Bestscore_ingame bestscore_ingame) {
        this.bestscore_ingame = bestscore_ingame;
    }
    public Bestscore_ingame getBestscore_ingame() {
        return bestscore_ingame;
    }

    public void setBestscore_roundover(Bestscore_roundover bestscore_roundover) {
        this.bestscore_roundover = bestscore_roundover;
    }
    public Bestscore_roundover getBestscore_roundover() {
        return bestscore_roundover;
    }

}
