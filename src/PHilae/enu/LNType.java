/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.enu;

/**
 *
 * @author Pecherk
 */
public enum LNType {
    One(0),
    Two(1),
    Three(2),
    None(3);
    private Integer id;

    private LNType(Integer id) {
        setId(id);
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }
}
