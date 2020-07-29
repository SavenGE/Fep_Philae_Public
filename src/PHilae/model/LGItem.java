/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.model;

/**
 *
 * @author Pecherk
 */
public final class LGItem implements Comparable<LGItem> {

    private String key;
    private String prefix;
    private Object payload;
    private Long position;

    public LGItem(String key, String prefix, Object callObject, Long position, Integer index) {
        setKey(key);
        setPrefix(prefix);
        setPayload(callObject);
        setPosition((position > 0L ? position : System.currentTimeMillis()) + index);
    }

    /**
     * @return the prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * @param prefix the prefix to set
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * @return the payload
     */
    public Object getPayload() {
        return payload;
    }

    /**
     * @param payload the payload to set
     */
    public void setPayload(Object payload) {
        this.payload = payload;
    }

    /**
     * @return the position
     */
    public Long getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(Long position) {
        this.position = position;
    }

    @Override
    public int compareTo(LGItem o) {
        return getPosition().compareTo(o.getPosition());
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }
}
