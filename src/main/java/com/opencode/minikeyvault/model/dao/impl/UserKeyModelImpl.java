package com.opencode.minikeyvault.model.dao.impl;

import com.opencode.minikeyvault.model.dao.UserKeyModel;
import com.opencode.minikeyvault.model.db.Datasource;
import com.opencode.minikeyvault.model.entity.UserKey;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/** 
 * class: UserKeyModelImpl. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-27 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
@Slf4j
public class UserKeyModelImpl implements UserKeyModel {

    private static final String COL_USERKEY_ID = "userkey_id";
    private static final String COL_APPLICATION = "application";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";

    /**
     * Devuelve todos los registros de la tabla desde la base de datos.
     */
    @Override
    public List<UserKey> getAll(String filter) {

        List<UserKey> lst = new ArrayList<>();

        StringBuilder sql = new StringBuilder()
            .append("select * from userkey ")
            .append((filter != null) ? "where application like ? " : "")
            .append("order by application");

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = Datasource.getStatement(sql.toString());

            if (filter != null) {
                ps.setString(1, "%" + filter + "%");
            }

            rs = ps.executeQuery();

            while (rs.next()) {
                lst.add(new UserKey(rs.getInt(COL_USERKEY_ID),
                        rs.getString(COL_APPLICATION),
                        rs.getString(COL_DESCRIPTION),
                        rs.getString(COL_USERNAME),
                        rs.getString(COL_PASSWORD)));
            }
        } catch (SQLException e) {
            log.error("Ocurrieron errores al recuperar los registros "
                    + "desde la BD: {}" + e.getMessage());
        } finally {
            Datasource.close(rs, true);
        }

        return lst;
    }

    /**
     * Devuelve el registro cuyo id se indica como parametro.
     */
    @Override
    public UserKey getOne(int userKeyId) {

        UserKey userKey = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = Datasource.getStatement("select * from userkey where userkey_id = ?");
            ps.setInt(1, userKeyId);

            rs = ps.executeQuery();

            while (rs.next()) {
                userKey = new UserKey(rs.getInt(COL_USERKEY_ID), rs.getString(COL_APPLICATION), 
                        rs.getString(COL_DESCRIPTION), rs.getString(COL_USERNAME), 
                        rs.getString(COL_PASSWORD));
            }
        } catch (SQLException e) {
            log.error("Ocurrieron errores al recuperar el registro "
                    + "desde la BD: {}", e.getMessage());
        } finally {
            Datasource.close(rs, true);
        }

        return userKey;
    }

    /**
     * Inserta el registro en la base de datos.
     */
    @Override
    public UserKey insert(UserKey userKey) {

        int result = 0;

        PreparedStatement ps = null;

        try {
            ps = Datasource.getStatement("insert into userkey (application, description, "
                    + "username, password) values (?, ?, ?, ?)");
            ps.setString(1, userKey.getApplication());
            ps.setString(2, userKey.getDescription());
            ps.setString(3, userKey.getUserName());
            ps.setString(4, userKey.getPassword());

            result = ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();

            if (keys.next() && result != 0) {
                userKey.setUserkeyId(keys.getInt(1));
            }
        } catch (SQLException e) {
            log.error("Ocurrieron errores al insertar el "
                    + "registro en la BD: {}", e.getMessage());
        } finally {
            Datasource.close(ps, true);
        }

        return result == 1 ? userKey : null;
    }

    @Override
    public UserKey update(UserKey userKey) {

        int result = 0;

        PreparedStatement ps = null;

        try {
            ps = Datasource.getStatement("update userkey set application = ?, description = ?, "
                    + "username = ?, password = ? where userkey_id = ?");
            ps.setString(1, userKey.getApplication());
            ps.setString(2, userKey.getDescription());
            ps.setString(3, userKey.getUserName());
            ps.setString(4, userKey.getPassword());
            ps.setInt(5, userKey.getUserkeyId());

            result = ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Ocurrieron errores al actualizar el registro "
                    + "en la BD: {}", e.getMessage());
        } finally {
            Datasource.close(ps, true);
        }

        return result == 1 ? userKey : null;
    }

    @Override
    public boolean delete(int userKeyId) {

        int result = 0;

        PreparedStatement ps = null;

        try {
            ps = Datasource.getStatement("delete from userkey where userkey_id = ?");
            ps.setInt(1, userKeyId);

            result = ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Ocurrieron errores al actualizar el registro "
                    + "en la BD: {}", e.getMessage());
        } finally {
            Datasource.close(ps, true);
        }

        return result == 1;
    }

}
