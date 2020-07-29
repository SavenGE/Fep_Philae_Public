/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.acx;

import PHilae.DBPClient;
import java.io.Serializable;

/**
 *
 * @author Pecherk
 */
public class ATBox implements Serializable {

    private APLog log;
    private final AXFile file = new AXFile();
    private final AXCypher cypher = new AXCypher();

    private final AXWorker worker = new AXWorker(this);
    private final DBPClient client = new DBPClient(this);
    private final AXClient xapi = new AXClient(this);

    public ATBox(APLog log) {
        this.log = log;
    }

    public AXWorker getWorker() {
        return worker;
    }

    public DBPClient getClient() {
        return client;
    }

    /**
     * @param log the logger to set
     */
    public void setLog(APLog log) {
        this.log = log;
    }

    /**
     * @param <T>
     * @param clazz
     * @return the log
     */
    public <T> T getLog(Class<T> clazz) {
        return (T) log;
    }

    /**
     * @return the log
     */
    public APLog getLog() {
        return log;
    }

    public AXCypher getCypher() {
        return cypher;
    }

    public AXFile getFile() {
        return file;
    }

    /**
     * @return the xapi
     */
    public AXClient getXapi() {
        return xapi;
    }
}
