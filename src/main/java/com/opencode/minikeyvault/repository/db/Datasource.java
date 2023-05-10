package com.opencode.minikeyvault.repository.db;

import com.opencode.minikeyvault.utils.Constants;
import com.opencode.minikeyvault.utils.ResourceManager;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.tools.RunScript;

/**
 * class: Datasource. <br/>
 * @author Henry Navarro <br/>
 *         <br/>
 *         <u>Cambios</u>:<br/>
 *         <ul>
 *         <li>2021-08-27 Creación del proyecto.</li>
 *         </ul>
 * @version 1.0
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Datasource {

    private static JdbcConnectionPool connectionPool;

    /**
     * Método para inicializar (crear) la base de datos.
     *
     * @param username usuario de la base de datos.
     * @param password clave de la base de datos.
     * @return detalle del error (en caso de error), caso contrario null.
     */
    public static String init(String username, String password) {

        String error = null;

        try (Connection cn = getConnectionFromPool(username, password, false);
             InputStreamReader reader = new InputStreamReader(
                     ResourceManager.getScriptFile("bd-init.sql"),
                     StandardCharsets.UTF_8)) {
            RunScript.execute(cn, reader);

            return null;
        } catch (SQLException | IOException e) {
            connectionPool = null;
            error = e.getMessage();
            e.printStackTrace();
        }

        return error;
    }

    /**
     * Método para aperturar la base de datos.
     *
     * @param username usuario de la base de datos.
     * @param password clave de la base de datos.
     * @return detalle del error (en caso de error), caso contrario null.
     */
    public static String open(String username, String password) {

        String error = null;

        try (Connection cn = getConnectionFromPool(username, password, true)) {
            if (cn != null) {
                return null;
            }
        } catch (SQLException e) {
            connectionPool = null;

            if (e.getErrorCode() == 28000) {
                error = "Usuario o clave incorrecta.";
            } else {
                error = e.getMessage();
            }

            e.printStackTrace();
        }

        return error;
    }

    /**
     * Metodo que crea el pool de conexiones y devuelve una instancia de conexión
     * a la base de datos.
     *
     * @param username usuario de la base de datos.
     * @param password clave de la base de datos.
     * @param onlyIfExist Si es true, únicamente se conectará a la bd si esta existe y 
     *     devolvera una excepción en caso esto no se cumpla. Caso contrario, si es 
     *     false y la bd no existe, intentará crear el archivo de base de datos.
     * @return instancia de conexión.
     * @throws SQLException excepción en caso ocurriera un error.
     */
    private static Connection getConnectionFromPool(
            String username, String password, boolean onlyIfExist) throws SQLException {

        if (connectionPool == null) {
            connectionPool = JdbcConnectionPool.create(
                    Constants.DB_DRIVER + ":./" + Constants.DB_NAME
                    + ";IFEXISTS=" + (onlyIfExist ? "TRUE" : "FALSE") 
                    + ";IGNORECASE=TRUE", username, password);
        }

        return connectionPool.getConnection();
    }

    /**
     * Metodo para generar backups de la base de datos.
     */
    public static void generateBackup() {

        try (PreparedStatement ps = getStatement("SCRIPT TO 'backup/backup.sql'")) {
            if (ps != null) {
                ps.execute();
            } // XXX añadir logica para manejar los distintos tipos de backup
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Metodo para obtener una nueva instancia de Connection.
     * 
     * @return nueva instancia de Connection. Null en caso de error.
     */
    public static Connection getConnection() {

        try {
            return connectionPool != null
                    ? connectionPool.getConnection() : null;
        } catch (SQLException e) {
            log.error("Error al generar el connection: {}", e.getMessage());
        }

        return null;
    }

    /**
     * Metodo para obtener una nueva instancia de PreparedStatement.
     * 
     * @param sql query que ejecutará el statement
     * @return nueva instancia de PreparedStatement. Null en caso de error.
     */
    public static PreparedStatement getStatement(String sql) {

        Connection cn = Datasource.getConnection();

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
     * @param closeAll true si se desea cerrar también el Connection relacionado.
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
     * @param closeAll true si se desea cerrar también el Connection relacionado.
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
     * @param closeAll true si se desea cerrar también el Statement y el
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
