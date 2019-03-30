package data;

import business.SubSysDespesas.Categoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class CategoriaDAO implements Map<String, Categoria> {

    @Override
    public int size() {
        int size = -1;
        Connection con = null;
        try {
            con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("SELECT designacao FROM Categoria");
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
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Categoria "
                    + "WHERE designacao = ? ");
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
    public Categoria get(Object key) {
        Categoria c = null;
        Map<String, Double> percents = new TreeMap<>();
        Connection con = null;
        try {
            con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM InquilinoCategoria "
                    + "WHERE categoria = ?");
            ps.setString(1, key.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                percents.put(rs.getString("inquilino"), rs.getDouble("percentagem"));
            }
            c = new Categoria(key.toString(), percents);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return c;
    }

    @Override
    public Categoria put(String key, Categoria value) {
        if (value == null) {
            return null;
        }
        Connection con = null;
        try {
            con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("INSERT INTO Categoria (designacao) "
                    + "VALUES ( ?)");
            ps.setString(1, key);
            ps.executeUpdate();
            /*for (Map.Entry<String, Double> e : value.getPercentagens().entrySet()) {
                ps = con.prepareStatement("INSERT INTO InquilinoCategoria (inquilino, categoria, percentagem) "
                        + "VALUE ( ?, ?, ?)");
                ps.setString(1, e.getKey());
                ps.setString(2, key);
                ps.setDouble(3, e.getValue());
            }
            ps.executeUpdate();*/
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
    public Categoria remove(Object key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void putAll(Map<? extends String, ? extends Categoria> m) {
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
            PreparedStatement ps = con.prepareStatement("SELECT designacao FROM Categoria");
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
    public Collection<Categoria> values() {
        Set<String> categorias = keySet();
        Collection<Categoria> res = new ArrayList<>();
        for (String s : categorias) {
            res.add(get(s));
        }
        return res;
    }

    @Override
    public Set<Entry<String, Categoria>> entrySet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Map<String, Double> getPercentagensCategorias(String inquilino) {
        Map<String, Double> percents = new TreeMap<>();
        Connection con = null;
        try {
            con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM InquilinoCategoria "
                    + "WHERE inquilino = ?");
            ps.setString(1, inquilino);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                percents.put(rs.getString("categoria"), rs.getDouble("percentagem"));
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
        return percents;
    }

    public Boolean existeAssociacao(String inquilino, String categoria) {
        Connection con = null;
        try {
            con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM InquilinoCategoria "
                    + "WHERE inquilino = ? AND categoria = ?");
            ps.setString(1, inquilino);
            ps.setString(2, categoria);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
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
        return false;
    }

    public void associarCategoriaInquilino(String inquilino, String categoria, Double percentagem) {
        Connection con = null;
        try {
            con = Connect.connect();
            if (existeAssociacao(inquilino, categoria) == false) {
                PreparedStatement ps = con.prepareStatement("INSERT INTO InquilinoCategoria (inquilino, categoria, percentagem) "
                        + "VALUE ( ?, ?, ?) ");
                ps.setString(1, inquilino);
                ps.setString(2, categoria);
                ps.setDouble(3, percentagem);
                ps.executeUpdate();
            } else {
                PreparedStatement ps = con.prepareStatement("UPDATE InquilinoCategoria "
                        + "SET percentagem = ? "
                        + "WHERE inquilino = ? AND categoria = ?");
                ps.setDouble(1, percentagem);
                ps.setString(2, inquilino);
                ps.setString(3, categoria);
                ps.executeUpdate();
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
    }

}
