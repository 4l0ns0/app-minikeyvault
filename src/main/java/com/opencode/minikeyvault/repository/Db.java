package com.opencode.minikeyvault.repository;

import com.opencode.minikeyvault.utils.ResourceManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import lombok.extern.slf4j.Slf4j;
import org.h2.tools.RunScript;

/**
 * class: DB. <br/>
 * 
 * @author Henry Navarro <br/>
 *         <br/>
 *         <u>Cambios</u>:<br/>
 *         <ul>
 *         <li>2021-08-27 Creación del proyecto.</li>
 *         </ul>
 * @version 1.0
 */
@Slf4j
public class Db {

    private static final String DRIVER_NAME = "jdbc:h2";
    private static final String BD_NAME = "keystore";
    private static final String BD_FILENAME = "keystore.mv.db";

    private Db() {
        throw new IllegalStateException(Db.class.getName());
    }

    /**
     * Metodo que devuelve una nueva instancia de conexión a la base de datos.
     * 
     * @param onlyIfExist Si es true, únicamente se conectará a la bd si esta existe y 
     *     devolvera una excepción en caso esto no se cumpla. Caso contrario, si es 
     *     false, si la bd no existe, intentará crear el archivo de base de datos.
     * @return instancia de conexión.
     * @throws SQLException excepción en caso ocurriera un error de conexión.
     */
    private static Connection getNewInstance(boolean onlyIfExist) throws SQLException {
        return DriverManager.getConnection(
                DRIVER_NAME + ":./" + BD_NAME + ";IFEXISTS=" + (onlyIfExist ? "TRUE" : "FALSE"), 
                "kstoreUser", "s3cr3t-P@ss#K3yv4ult"); // TODO manejar desde el properties
    }
    
    /**
     * Metodo para inicializar la base de datos.
     * 
     * <p>Este metodo solo debe ser llamado cuando se desea inicializar la base de datos,
     * es decir, se crea el archivo de base de datos y genera dentro todos los componentes
     * necesarios. Si este metodo se ejecutara sobre una base de datos existente, devolveria
     * un error.
     */
    public static void init() {

        if (new File(BD_FILENAME).exists()) {
            log.debug("La base de datos ya existe, no es necesario inicializarla.");
        } else {
            Connection cn = null;

            try (InputStreamReader reader = new InputStreamReader(ResourceManager
                    .getScriptFile("bd-init.sql"), StandardCharsets.UTF_8)) {
                cn = getNewInstance(false);
                RunScript.execute(cn, reader);
            } catch (SQLException | IOException e) {
                log.error("Error al iniciar la bd: {}", e.getMessage());
            } finally {
                Db.close(cn);
            }
        }

    }

    /**
     * Metodo para obtener una nueva instancia de Connection.
     * 
     * @return nueva instancia de Connection. Null en caso de errror.
     */
    public static Connection getConnection() {

        try {
            return getNewInstance(true);
        } catch (SQLException e) {
            log.error("Error al generar el connection: {}", e.getMessage());
        }

        return null;
    }

    /**
     * Metodo para obtener una nueva instancia de PreparedStatement.
     * 
     * @param sql query que ejecutará el statement
     * @return nueva instancia de PreparedStatement. Null en caso de errror.
     */
    public static PreparedStatement getStatement(String sql) {

        Connection cn = Db.getConnection();

        if (cn != null) {
            try {
                return cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            } catch (SQLException e) {
                log.error("Error al generar el statement: {}", e.getMessage());
            }
        }

        return null;
    }

    /**
     * Cierra el Connection recibido.
     * 
     * @param cn Connection a cerrar.
     */
    public static void close(Connection cn) {
        close(cn, null, null, null);
    }

    /**
     * Cierra el PreparedStatement recibido.
     * 
     * @param ps PreparedStatement a cerrar.
     * @param closeAll true si se desea cerrar tambien el Connection relacionado. 
     *     Caso contrario, false.
     */
    public static void close(PreparedStatement ps, boolean closeAll) {

        if (closeAll) {
            Connection cn = null;

            try {
                cn = ps.getConnection();
            } catch (SQLException e) {
                log.debug("Error al recuperar el Connection a través "
                        + "del PreparedStatement: {}", e.getMessage());
            }

            close(cn, null, ps, null);
        } else {
            close(null, null, ps, null);
        }

    }
    
    /**
     * Cierra el Statement recibido.
     * 
     * @param st Statement a cerrar.
     * @param closeAll true si se desea cerrar tambien el Connection relacionado. 
     *     Caso contrario, false.
     */
    public static void close(Statement st, boolean closeAll) {

        if (closeAll) {
            Connection cn = null;

            try {
                cn = st.getConnection();
            } catch (SQLException e) {
                log.debug("Error al recuperar el Connection a través "
                        + "del Statement: {}", e.getMessage());
            }

            close(cn, st, null, null);
        } else {
            close(null, st, null, null);
        }

    }
    
    /**
     * Cierra el ResultSet recibido.
     * 
     * @param rs ResultSet a cerrar.
     * @param closeAll true si se desea cerrar tambien el Statement y el 
     *     Connection relacionado. Caso contrario, false.
     */
    public static void close(ResultSet rs, boolean closeAll) {

        if (closeAll) {
            Connection cn = null;
            Statement st = null;

            try {
                st = rs.getStatement();
                cn = st.getConnection();
            } catch (SQLException e) {
                log.debug("Error al recuperar el Connection y Statement a través "
                        + "del resultset: {}", e.getMessage());
            }

            close(cn, st, null, rs);
        } else {
            close(null, null, null, rs);
        }

    }
    
    /**
     * Cierra el Connection y Statement recibidos.
     * 
     * @param cn Connection a cerrar.
     * @param st Statement a cerrar.
     */
    public static void close(Connection cn, Statement st) {
        close(cn, st, null, null);
    }
    
    /**
     * Cierra el Connection y PreparedStatement recibidos.
     * 
     * @param cn Connection a cerrar.
     * @param ps Statement a cerrar.
     */
    public static void close(Connection cn, PreparedStatement ps) {
        close(cn, null, ps, null);
    }

    /**
     * Cierra el Statement y ResultSet recibidos.
     * 
     * @param st Statement a cerrar.
     * @param rs ResultSet a cerrar.
     */
    public static void close(Statement st, ResultSet rs) {
        close(null, st, null, rs);
    }

    /**
     * Cierra el PreparedStatement y ResultSet recibidos.
     * 
     * @param ps PreparedStatement a cerrar.
     * @param rs ResultSet a cerrar.
     */
    public static void close(PreparedStatement ps, ResultSet rs) {
        close(null, ps, null, rs);
    }

    /**
     * Cierra el Connection, PreparedStatement y ResultSet recibidos.
     * 
     * @param cn Connection a cerrar.
     * @param ps PreparedStatement a cerrar.
     * @param rs ResultSet a cerrar.
     */
    public static void close(Connection cn, PreparedStatement ps, ResultSet rs) {
        close(cn, ps, null, rs);
    }

    /**
     * Cierra el Connection, Statement y ResultSet recibidos.
     * 
     * @param cn Connection a cerrar.
     * @param st Statement a cerrar.
     * @param rs ResultSet a cerrar.
     */
    public static void close(Connection cn, Statement st, ResultSet rs) {
        close(cn, st, null, rs);
    }

    /**
     * Cierra todos los objetos de conexión a base de datos.
     * 
     * @param cn Connection a cerrar.
     * @param st Statement a cerrar.
     * @param ps PreparedStatement a cerrar.
     * @param rs ResultSet a cerrar.
     */
    private static void close(Connection cn, Statement st, 
            PreparedStatement ps, ResultSet rs) {
        
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            log.debug("Error al cerrar el Resultset {}", e.getMessage());
        }

        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException e) {
            log.debug("Error al cerrar el Statement {}", e.getMessage());
        }
        
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            log.debug("Error al cerrar el PreparedStatement {}", e.getMessage());
        }
        
        try {
            if (cn != null) {
                cn.close();
            }
        } catch (SQLException e) {
            log.debug("Error al cerrar el Connection {}", e.getMessage());
        }
        
    }
    
}
