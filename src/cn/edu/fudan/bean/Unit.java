package cn.edu.fudan.bean;

import com.google.gson.Gson;

/**
 * Created by Dawnwords on 2015/5/15.
 */
public class Unit {
    private int id;
    private String text;
    private boolean children;
    private State state;

    private class State {
        private boolean opened, disabled, selected;

        public State(int lv) {
            this.opened = lv == 0;
            this.disabled = lv < 2;
            this.selected = false;
        }
    }

    public Unit() {
    }

    public Unit(int id, String text, int level) {
        this.id = id;
        this.text = text;
        this.children = level < 3;
        this.state = new State(level);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
