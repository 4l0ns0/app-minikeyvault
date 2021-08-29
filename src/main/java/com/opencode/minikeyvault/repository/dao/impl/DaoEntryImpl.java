package com.opencode.minikeyvault.repository.dao.impl;

import com.opencode.minikeyvault.model.Entry;
import com.opencode.minikeyvault.repository.Db;
import com.opencode.minikeyvault.repository.dao.DaoEntry;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/** class: DaoEntryImpl. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-27 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
@Slf4j
public class DaoEntryImpl implements DaoEntry {

    private static final String COL_ID = "id";
    private static final String COL_APPLICATION = "application";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";

    /**
     * Devuelve todos los registros de la tabla desde la base de datos.
     */
    @Override
    public List<Entry> getAll() {

        List<Entry> lst = new ArrayList<>();

        ResultSet rs = null;

        try {
            rs = Db.getStatement("select * from data")
                    .executeQuery();

            while (rs.next()) {
                lst.add(new Entry(rs.getInt(COL_ID),
                        rs.getString(COL_APPLICATION),
                        rs.getString(COL_DESCRIPTION),
                        rs.getString(COL_USERNAME),
                        rs.getString(COL_PASSWORD)));
            }
        } catch (SQLException e) {
            log.error("Ocurrieron errores al recuperar los registros "
                    + "desde la BD: {}" + e.getMessage());
        } finally {
            Db.close(rs, true);
        }

        return lst;
    }

    /**
     * Devuelve el registro cuyo id se se indica como parametro.
     */
    @Override
    public Entry getOne(int entryId) {

        Entry entry = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = Db.getStatement("select * from data where id = ?");
            ps.setInt(1, entryId);

            rs = ps.executeQuery();

            while (rs.next()) {
                entry = new Entry(rs.getInt(COL_ID), rs.getString(COL_APPLICATION), 
                        rs.getString(COL_DESCRIPTION), rs.getString(COL_USERNAME), 
                        rs.getString(COL_PASSWORD));
            }
        } catch (SQLException e) {
            log.error("Ocurrieron errores al recuperar el registro "
                    + "desde la BD: {}", e.getMessage());
        } finally {
            Db.close(rs, true);
        }

        return entry;
    }

    /**
     * Inserta el registro en la base de datos.
     */
    @Override
    public Entry insert(Entry entry) {

        PreparedStatement ps = null;
        int result = 0;

        try {
            ps = Db.getStatement("insert into data (application, description, "
                    + "username, password) values (?, ?, ?, ?)");
            ps.setString(1, entry.getApplication());
            ps.setString(2, entry.getDescription());
            ps.setString(3, entry.getUserName());
            ps.setString(4, entry.getPassword());

            result = ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();

            if (keys.next() && result != 0) {
                entry.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            log.error("Ocurrieron errores al insertar el registro "
                    + "en la BD: {}", e.getMessage());
        } finally {
            Db.close(ps, true);
        }

        return result == 1 ? entry : null;
    }

    @Override
    public Entry update(Entry entry) {

        PreparedStatement ps = null;
        int result = 0;

        try {
            ps = Db.getStatement("update data set application = ?, description = ?, "
                    + "username = ?, password = ? where id = ?");
            ps.setString(1, entry.getApplication());
            ps.setString(2, entry.getDescription());
            ps.setString(3, entry.getUserName());
            ps.setString(4, entry.getPassword());
            ps.setInt(5, entry.getId());

            result = ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Ocurrieron errores al actualizar el registro "
                    + "en la BD: {}", e.getMessage());
        } finally {
            Db.close(ps, true);
        }

        return result == 1 ? entry : null;
    }

    @Override
    public boolean delete(int recordId) {

        PreparedStatement ps = null;
        int result = 0;

        try {
            ps = Db.getStatement("delete from data where id = ?");
            ps.setInt(1, recordId);

            result = ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Ocurrieron errores al actualizar el registro "
                    + "en la BD: {}", e.getMessage());
        } finally {
            Db.close(ps, true);
        }

        return result == 1;
    }

}
