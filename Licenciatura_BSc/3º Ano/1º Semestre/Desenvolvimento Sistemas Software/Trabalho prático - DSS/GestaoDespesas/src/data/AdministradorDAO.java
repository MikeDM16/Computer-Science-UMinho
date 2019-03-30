package data;

import business.SubSysUsers.Administrador;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class AdministradorDAO implements Map<String, Administrador> {

    public void update(String username, String nome, char[] password) {
        Connection con = null;
        try {
            con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("UPDATE Utilizador U "
                    + "SET nome = ?, password = ?"
                    + "WHERE username = ?");
            ps.setString(1, nome);
            ps.setString(2, new String(password));
            ps.setString(3, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getAdminAtivo() {
        Connection con = null;
        String r = null;
        try {
            con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("SELECT utilizador FROM Administrador "
                    + "WHERE estado = ?");
            ps.setBoolean(1, true);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                r = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return r;
    }

    public char[] getPassword(String username) {
        char[] pass = null;
        Connection con = null;
        try {
            con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("SELECT password FROM Utilizador U "
                    + "WHERE U.username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                pass = rs.getString(1).toCharArray();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return pass;
    }

    @Override
    public int size() {
        int size = -1;
        Connection con = null;
        try {
            con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("SELECT utilizador FROM Administrador");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return size;
    }

    @Override
    public boolean isEmpty() {
        return (size() == 0);
    }

    @Override
    public boolean containsKey(Object key) {
        boolean result = false;
        Connection con = null;
        try {
            con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Utilizador U "
                    + "INNER JOIN Administrador A ON U.username = A.utilizador "
                    + "WHERE U.username = ? ");
            ps.setString(1, key.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Administrador get(Object key) {
        Administrador a = null;
        Connection con = null;
        try {
            con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Utilizador U "
                    + "INNER JOIN Administrador A ON U.username = A.utilizador "
                    + "WHERE U.username = ?");
            ps.setString(1, key.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                a = new Administrador(rs.getString("username"), rs.getString("nome"), rs.getString("password").toCharArray(), rs.getBoolean("estado"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return a;
    }

    @Override
    public Administrador put(String key, Administrador value) {
        if (value == null) {
            return null;
        }
        Connection con = null;
        try {
            con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("INSERT INTO Utilizador (username, nome, password) "
                    + "VALUES ( ?, ?, ?)");
            ps.setString(1, key);
            ps.setString(2, value.getNome());
            ps.setString(3, new String(value.getPassword()));
            ps.executeUpdate();
            ps = con.prepareStatement("INSERT INTO Administrador (utilizador, estado) "
                    + "VALUES ( ?, ?)");
            ps.setString(1, key);
            ps.setBoolean(2, value.getEstado());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    @Override
    public Administrador remove(Object key) {
        Connection con = null;
        if (containsKey(key) == false) {
            return null;
        }
        try {
            con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("UPDATE Administrador SET estado = ?");
            ps.setBoolean(1, false);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return get(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Administrador> m) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<String> keySet() {
        Set<String> keys = new TreeSet<>();
        Connection con = null;
        try {
            con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("SELECT utilizador FROM Administrador");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                keys.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return keys;
    }

    @Override
    public Collection<Administrador> values() {
        Set<String> users = keySet();
        Collection<Administrador> res = new ArrayList<>();
        for (String s : users) {
            res.add(get(s));
        }
        return res;
    }

    @Override
    public Set<Entry<String, Administrador>> entrySet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
