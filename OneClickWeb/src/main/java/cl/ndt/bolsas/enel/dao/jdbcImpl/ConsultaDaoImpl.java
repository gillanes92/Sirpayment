/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.ndt.bolsas.enel.dao.jdbcImpl;

import cl.ndt.bolsas.enel.dao.ConsultaDao;
import cl.ndt.bolsas.enel.dao.jdbcImpl.storeprocedure.ObtenerIdStoredProcedure;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;


/**
 *
 * @author Lenovo
 */
public class ConsultaDaoImpl implements ConsultaDao {
    
    private DataSource dataSource;
    
    public void setDataSource(DataSource dataSource) {
    	this.dataSource = dataSource;
    }
    
    
}
