package data;

import business.SubSysUsers.Conta;
import business.SubSysUsers.Inquilino;
import business.SubSysUsers.Movimento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static java.sql.Types.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class InquilinoDAO implements Map<String, Inquilino> {
    
    public void descontar(String user, Double valor) {
        Connection con = null;
        try {
            con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("UPDATE Conta C "
                    + "INNER JOIN Inquilino I ON C.idConta = I.conta "
                    + "SET saldo = saldo - ? "
                    + "WHERE utilizador = ?");
            ps.setDouble(1, valor);
            ps.setString(2, user);
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
    
    public void registarMovimento(String username, Movimento m) {
        int idConta = get(username).getConta().getNrConta();
        String date;
        Connection con = null;
        try {
            con = Connect.connect();
            if (m.getDescricao().equals("Depósito") == true) {
                PreparedStatement ps = con.prepareStatement("INSERT INTO Movimento (descricao, valor, data, conta) "
                    + "VALUE ( ?, ?, ?, ?)");
                ps.setString(1, m.getDescricao());
                ps.setDouble(2, m.getValor());
                SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date = sdf.format(m.getData());
                ps.setString(3, date);
                ps.setInt(4, idConta);
                ps.executeUpdate();
            } else if (m.getDescricao().equals("Pagamento despesa") == true) {
                PreparedStatement ps = con.prepareStatement("INSERT INTO Movimento (descricao, valor, data, conta, despesa) "
                    + "VALUE ( ?, ?, ?, ?, ?)");
                ps.setString(1, m.getDescricao());
                ps.setDouble(2, m.getValor());
                SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date = sdf.format(m.getData());
                ps.setString(3, date);
                ps.setInt(4, idConta);
                ps.setString(5, m.getRefAssociada());
                ps.executeUpdate();
            } else {
                PreparedStatement ps = con.prepareStatement("INSERT INTO Movimento (descricao, valor, data, conta, prestacao) "
                    + "VALUE ( ?, ?, ?, ?, ?)");
                ps.setString(1, m.getDescricao());
                ps.setDouble(2, m.getValor());
                SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date = sdf.format(m.getData());
                ps.setString(3, date);
                ps.setInt(4, idConta);
                ps.setInt(5, Integer.valueOf(m.getRefAssociada()));
                
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

    public Collection<Movimento> getMovimentos(String username) {
        Set<Movimento> movs = new TreeSet<>();
        String desig = null;
        String refAss = null;
        Connection con = null;
        try {
            con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Movimento M "
                    + "INNER JOIN Conta C ON M.conta = C.idConta "
                    + "INNER JOIN Inquilino I ON C.idConta = I.conta "
                    + "WHERE I.utilizador = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                desig = rs.getString("descricao");
                if (desig.equals("Pagamento despesa") == true) {
                    refAss = rs.getString("despesa");
                } else {
                    refAss = Integer.toString(rs.getInt("prestacao"));
                }
                Movimento m = new Movimento(rs.getString("descricao"), rs.getDouble("valor"), rs.getTimestamp("data"), rs.getInt("id"), refAss);
                movs.add(m);
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
        Collection<Movimento> movsOrd = new ArrayList<>(movs);
        return movsOrd;
    }

    public void depositar(String username, Double valor) {
        Connection con = null;
        try {
            con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("UPDATE Conta C "
                    + "INNER JOIN Inquilino I ON C.idConta = I.conta "
                    + "SET saldo = saldo + ? "
                    + "WHERE utilizador = ?");
            ps.setDouble(1, valor);
            ps.setString(2, username);
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
            PreparedStatement ps = con.prepareStatement("SELECT utilizador FROM Inquilino");
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
    public Inquilino get(Object key) {
        Inquilino i = null;
        Conta c = null;
        int idConta = 0;
        Double saldo = 0.0;
        Collection<Movimento> movs = new ArrayList<>();
        Connection con = null;
        try {
            con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("SELECT conta, saldo "
                    + "FROM Inquilino I "
                    + "INNER JOIN Conta C ON I.conta = C.idConta "
                    + "WHERE utilizador = ?");
            ps.setString(1, key.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                idConta = rs.getInt(1);
                saldo = rs.getDouble(2);
            }
            ps = con.prepareStatement("SELECT * FROM Movimento WHERE Conta = ?");
            ps.setInt(1, idConta);
            rs = ps.executeQuery();
            while (rs.next()) {
                String refD = rs.getString("despesa");
                int refP = rs.getInt("prestacao");
                String refAss;
                if (refD == null) {
                    refAss = refD;
                } else {
                    refAss = Integer.toString(refP);
                }
                Movimento m = new Movimento(rs.getString("descricao"), rs.getDouble("valor"), rs.getDate("data"), rs.getInt("id"), refAss);
                movs.add(m);
            }
            c = new Conta(idConta, saldo, movs);
            ps = con.prepareStatement("SELECT * FROM Utilizador U "
                    + "INNER JOIN Inquilino I ON U.username = I.utilizador "
                    + "WHERE U.username = ? ");
            ps.setString(1, key.toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                i = new Inquilino(rs.getString("username"), rs.getString("nome"),
                        rs.getString("password").toCharArray(), rs.getDate("dataEntrada"), rs.getDate("dataSaida"), c);
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
        return i;
    }

    @Override
    public Collection<Inquilino> values() {
        Set<String> users = keySet();
        Collection<Inquilino> res = new ArrayList<>();
        for (String s : users) {
            res.add(get(s));
        }
        return res;
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
                    + "INNER JOIN Inquilino I ON U.username = I.utilizador "
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
    public Inquilino put(String key, Inquilino value) {
        int idConta = 0;
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
            ps = con.prepareStatement("INSERT INTO conta (saldo) "
                    + "VALUES ( ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setDouble(1, value.getConta().getSaldo());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idConta = rs.getInt(1);
            }
            ps = con.prepareStatement("INSERT INTO Inquilino (dataEntrada, conta, utilizador) "
                    + "VALUES ( ?, ?, ?)");
            ps.setDate(1, new java.sql.Date(value.getDataEntrada().getTime()));
            ps.setInt(2, idConta);
            ps.setString(3, key);
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
    public Inquilino remove(Object key) {
        Connection con = null;
        if (containsKey(key) == false) {
            return null;
        }
        try {
            con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("UPDATE Inquilino I SET dataSaida = ? "
                    + "WHERE I.utilizador = ? AND I.dataSaida IS ?");
            ps.setDate(1, new java.sql.Date(System.currentTimeMillis()));
            ps.setString(2, key.toString());
            ps.setNull(3, DATE);
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
    public void putAll(Map<? extends String, ? extends Inquilino> m) {
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
            PreparedStatement ps = con.prepareStatement("SELECT utilizador FROM Inquilino");
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
    public Set<Entry<String, Inquilino>> entrySet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
