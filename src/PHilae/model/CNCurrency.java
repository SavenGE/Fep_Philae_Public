/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.model;

import PHilae.acx.AXIgnore;
import java.io.Serializable;

/**
 *
 * @author Pecherk
 */
public class CNCurrency implements Serializable, Comparable<CNCurrency> {

    private Long id;
    private String code;
    private String name;
    private Integer points = 0;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the points
     */
    public Integer getPoints() {
        return points;
    }

    /**
     * @param points the points to set
     */
    public void setPoints(Integer points) {
        this.points = points;
    }

    @Override
    public int compareTo(CNCurrency o) {
        return getCode().compareTo(o.getCode());
    }

    @Override
    @AXIgnore
    public String toString() {
        return getCode() + "~" + getName();
    }
}
