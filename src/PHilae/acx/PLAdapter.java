/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.acx;

import PHilae.APController;
import java.sql.Connection;
import oracle.ucp.UniversalConnectionPoolException;
import oracle.ucp.UniversalPooledConnection;

/**
 *
 * @author Pecherk
 */
public final class PLAdapter implements AutoCloseable {

    private UniversalPooledConnection pooledConnection;

    public PLAdapter(boolean unlimited) throws UniversalConnectionPoolException {
        setPooledConnection(APController.borrowConnection());
        try {
            if (unlimited) {
                getPooledConnection().registerTimeToLiveConnectionTimeoutCallback(() -> true);
                getPooledConnection().registerAbandonedConnectionTimeoutCallback(() -> true);
            }
        } catch (Exception ex) {
            try {
                close();
            } catch (Exception e) {
                e = null;
            }
            throw ex;
        }
    }

    @Override
    public void close() throws UniversalConnectionPoolException {
        try {
            getPooledConnection().removeTimeToLiveConnectionTimeoutCallback();
            getPooledConnection().removeAbandonedConnectionTimeoutCallback();
        } finally {
            APController.returnConnection(getPooledConnection());
        }
    }

    /**
     * @return the connection
     */
    public Connection getConnection() {
        return (Connection) getPooledConnection().getPhysicalConnection();
    }

    /**
     * @return the pooledConnection
     */
    public UniversalPooledConnection getPooledConnection() {
        return pooledConnection;
    }

    /**
     * @param pooledConnection the pooledConnection to set
     */
    public void setPooledConnection(UniversalPooledConnection pooledConnection) {
        this.pooledConnection = pooledConnection;
    }
}
