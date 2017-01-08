package io.github.solomkinmv.glossary.persistence.model;

/**
 * Created by max on 28.12.16.
 */
public class Foo {
    private Long id;
    private String value;

    public Foo(Long id, String value) {
        this.id = id;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
