/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.ndt.bolsas.enel.dao.jdbcImpl.storeprocedure;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author Lenovo
 */
public class ObtenerIdStoredProcedure {

    Connection connection;

    public ObtenerIdStoredProcedure(DataSource dataSource) {
        try {
            connection = dataSource.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(ObtenerIdStoredProcedure.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
