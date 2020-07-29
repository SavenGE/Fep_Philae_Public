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
public final class CBNode implements Comparable<CBNode> {

    private String contextUrl;
    private Boolean online = Boolean.FALSE;

    public CBNode(String contextUrl) {
        setContextUrl(contextUrl);
    }

    public CBNode(String contextUrl, Boolean online) {
        setContextUrl(contextUrl);
        setOnline(online);
    }

    /**
     * @return the contextUrl
     */
    public String getContextUrl() {
        return contextUrl;
    }

    /**
     * @param contextUrl the contextUrl to set
     */
    public void setContextUrl(String contextUrl) {
        this.contextUrl = contextUrl;
    }

    /**
     * @return the online
     */
    public Boolean isOnline() {
        return online;
    }

    /**
     * @param online the online to set
     */
    public void setOnline(Boolean online) {
        this.online = online;
    }

    @Override
    public int compareTo(CBNode o) {
        return isOnline().compareTo(o.isOnline());
    }
}
