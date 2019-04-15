package Entty;

/**
 * 常用备注
 * Created by Administrator on 2017/8/10.
 */

public class Goods_Common_Notes {
    String notes_id;
    String notes;

    public Goods_Common_Notes(String notes_id, String notes) {
        this.notes_id = notes_id;
        this.notes = notes;
    }

    public String getNotes_id() {
        return notes_id;
    }

    public void setNotes_id(String notes_id) {
        this.notes_id = notes_id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
