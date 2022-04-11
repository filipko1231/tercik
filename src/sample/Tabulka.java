package sample;

public class Tabulka {
    String meno;
    String strely;
    String skore;

    public Tabulka(String meno, String strely, String skore) {
        this.meno = meno;
        this.strely = strely;
        this.skore = skore;
    }

    public String getMeno() {
        return meno;
    }

    public void setMeno(String meno) {
        this.meno = meno;
    }

    public String getStrely() {
        return strely;
    }

    public void setStrely(String strely) {
        this.strely = strely;
    }

    public String getSkore() {
        return skore;
    }

    public void setSkore(String skore) {
        this.skore = skore;
    }
}
