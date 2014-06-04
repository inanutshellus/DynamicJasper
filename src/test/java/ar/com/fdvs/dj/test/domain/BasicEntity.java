package ar.com.fdvs.dj.test.domain;

import java.util.Collection;

public class BasicEntity {
    private String name;
    private Collection stuff;

    public BasicEntity(String name) {
        this.name = name;
    }

    public BasicEntity(String name, Collection stuff) {
        this.name = name;
        this.stuff = stuff;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection getStuff() {
        return stuff;
    }

    public void setStuff(Collection stuff) {
        this.stuff = stuff;
    }
}
