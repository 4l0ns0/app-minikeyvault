package com.opencode.minikeyvault.repository.impl;

import com.opencode.minikeyvault.entity.UserKey;
import com.opencode.minikeyvault.repository.UserKeyRepository;
import com.opencode.minikeyvault.repository.db.Datasource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class UserKeyRepositoryImpl implements UserKeyRepository {

    @Override
    public UserKey get(int userKeyId) {

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
            e.printStackTrace();
        } finally {
            Datasource.close(rs, true);
        }

        return userKey;
    }

    @Override
    public List<UserKey> list(String filter) {

        List<UserKey> lst = null;

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

            lst = new ArrayList<>();

            while (rs.next()) {
                lst.add(new UserKey(rs.getInt(COL_USERKEY_ID),
                        rs.getString(COL_APPLICATION),
                        rs.getString(COL_DESCRIPTION),
                        rs.getString(COL_USERNAME),
                        rs.getString(COL_PASSWORD)));
            }
        } catch (SQLException e) {
//            log.error("Ocurrieron errores al recuperar los registros "
//                    + "desde la BD: {}" + e.getMessage());
            e.printStackTrace();
        } finally {
            Datasource.close(rs, true);
        }

        return lst;
    }

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
//            log.error("Ocurrieron errores al insertar el "
//                    + "registro en la BD: {}", e.getMessage());
            e.printStackTrace();
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
//            log.error("Ocurrieron errores al actualizar el registro "
//                    + "en la BD: {}", e.getMessage());
            e.printStackTrace();
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
//            log.error("Ocurrieron errores al actualizar el registro "
//                    + "en la BD: {}", e.getMessage());
            e.printStackTrace();
        } finally {
            Datasource.close(ps, true);
        }

        return result == 1;
    }

}
