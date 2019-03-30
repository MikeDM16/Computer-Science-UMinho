package data;

import business.SubSysDespesas.Categoria;
import business.SubSysDespesas.Despesa;
import business.SubSysDespesas.Extraordinaria;
import business.SubSysDespesas.Prestacao;
import business.SubSysDespesas.Recorrente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class DespesaDAO implements Map<String, Despesa> {

    public Collection<Prestacao> getPrestacao(String user) {
        Collection<Prestacao> prestacoes = new TreeSet<>();
        Connection con = null;
        try {
            con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Prestacao "
                    + "WHERE inquilino = ? ");
            ps.setString(1, user);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                prestacoes.add(new Prestacao(rs.getDouble("valor"), rs.getInt("nrSeq"),
                        rs.getBoolean("estado"), rs.getInt("idPrestacao")));
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
        return prestacoes;
    }

    public void pagarDespesa(Despesa d) {
        Connection con = null;
        try {
            con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("UPDATE Despesa "
                    + "SET estado = ? "
                    + "WHERE ref = ?");
            ps.setBoolean(1, true);
            ps.setString(2, d.getRef());
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

    public void pagarDespesa(String inquilino, Extraordinaria d) {
        Connection con = null;
        try {
            con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("UPDATE InquilinoExtraordinária "
                    + "SET estado = ? "
                    + "WHERE despesa = ? AND inquilino = ?");
            ps.setBoolean(1, true);
            ps.setString(2, d.getRef());
            ps.setString(3, inquilino);
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
    
    public void pagarPrestacao(Prestacao p, String user) {
        Connection con = null;
        try {
            con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("UPDATE Prestacao "
                    + "SET estado = ? "
                    + "WHERE idPrestacao = ?");
            ps.setBoolean(1, true);
            ps.setInt(2, p.getId());
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

    public Boolean inquilinoPagou(String inquilino, Despesa d) {
        Connection con = null;
        if (d instanceof Recorrente) {
            return inquilinoPagouRecorrente(inquilino, (Recorrente) d);
        } else {
            return inquilinoPagouExtraordinaria(inquilino, (Extraordinaria) d);
        }
    }

    private Boolean inquilinoPagouRecorrente(String inquilino, Recorrente d) {
        Connection con = null;
        try {
            con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Movimento M "
                    + "INNER JOIN Conta C ON M.conta = C.idConta "
                    + "INNER JOIN Inquilino I ON C.idConta = I.conta "
                    + "WHERE I.utilizador = ? AND M.despesa = ? ");
            ps.setString(1, inquilino);
            ps.setString(2, d.getRef());
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

    private Boolean inquilinoPagouExtraordinaria(String inquilino, Extraordinaria d) {
        Boolean result = false;
        Connection con = null;
        try {
            con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM InquilinoExtraordinária "
                    + "WHERE inquilino = ? AND despesa = ? ");
            ps.setString(1, inquilino);
            ps.setString(2, d.getRef());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getBoolean("estado");
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
    
    public boolean inquilinoPagou(String user, Prestacao p) {
        Connection con = null;
        Boolean result = false;
        try {
            con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("SELECT estado FROM Prestacao "
                    + "WHERE idPrestacao = ?");
            ps.setInt(1, p.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getBoolean(1);
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

    public Categoria getCategoria(String despesa) {
        Categoria c = null;
        Map<String, Double> percents = new TreeMap<>();
        Connection con = null;
        try {
            con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM InquilinoCategoria "
                    + "WHERE categoria = ?");
            ps.setString(1, despesa);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                percents.put(rs.getString("inquilino"), rs.getDouble("percentagem"));
            }
            c = new Categoria(despesa, percents);
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
    public int size() {
        int size = -1;
        Connection con = null;
        try {
            con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("SELECT ref FROM Despesa");
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
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Despesa "
                    + "WHERE ref = ? ");
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
    public Despesa get(Object key) {
        Despesa d = null;
        Map<String, Double> percents = new TreeMap<>();
        Map<String, Boolean> estados = new TreeMap<>();
        Connection con = null;
        try {
            con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Recorrente R "
                    + "INNER JOIN Despesa D ON R.despesa = D.ref "
                    + "WHERE R.despesa = ?");
            ps.setString(1, key.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String ref = rs.getString("ref");
                Date dataLimite = rs.getDate("dataLimite");
                Date dataEmissao = rs.getDate("dataEmissao");
                Double preco = rs.getDouble("preco");
                Boolean estado = rs.getBoolean("estado");
                String categoria = rs.getString("categoria");
                d = new Recorrente(ref, dataLimite, dataEmissao, preco, estado, getCategoria(categoria));
            } else {
                ps = con.prepareStatement("SELECT * FROM Extraordinária E "
                        + "INNER JOIN Despesa D ON E.despesa = D.ref "
                        + "WHERE E.despesa = ?");
                ps.setString(1, key.toString());
                rs = ps.executeQuery();
                if (rs.next()) {
                    String ref = rs.getString("ref");
                    Date dataLimite = rs.getDate("dataLimite");
                    Date dataEmissao = rs.getDate("dataEmissao");
                    Double preco = rs.getDouble("preco");
                    Boolean estado = rs.getBoolean("estado");
                    String designacao = rs.getString("designacao");
                    ps = con.prepareStatement("SELECT * FROM InquilinoExtraordinária "
                            + "WHERE despesa = ?");
                    ps.setString(1, key.toString());
                    ResultSet rs2 = ps.executeQuery();
                    while (rs2.next()) {
                        percents.put(rs2.getString("inquilino"), rs2.getDouble("percentagem"));
                        estados.put(rs2.getString("inquilino"), rs2.getBoolean("estado"));
                    }
                    d = new Extraordinaria(ref, dataLimite, dataEmissao, preco, estado, designacao,
                            percents, estados, getPrestacoes(ref.toString()));
                }
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
        return d;
    }

    public Map<String, Set<Prestacao>> getPrestacoes(String key) {
        Map<String, Set<Prestacao>> prestacoes = new TreeMap<>();
        Connection con = null;
        try {
            con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Prestacao P "
                    + "INNER JOIN Extraordinária E ON P.despesa = E.despesa "
                    + "INNER JOIN InquilinoExtraordinária IE ON E.despesa = IE.despesa "
                    + "WHERE P.despesa = ? ");
            ps.setString(1, key.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Prestacao p = new Prestacao(rs.getDouble("valor"), rs.getInt("nrSeq"),
                        rs.getBoolean("estado"), rs.getInt("idPrestacao"));
                String inquilino = rs.getString("inquilino");
                if (prestacoes.containsKey(inquilino) == false) {
                    Set<Prestacao> prstInquilino = new TreeSet<>();
                    prstInquilino.add(p);
                    prestacoes.put(inquilino, prstInquilino);
                } else {
                    prestacoes.get(inquilino).add(p);
                }
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
        return prestacoes;
    }

    @Override
    public Despesa put(String key, Despesa value) {
        if (value == null) {
            return null;
        }
        Connection con = null;
        try {
            con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("INSERT INTO Despesa (ref, dataLimite, dataEmissao, preco, estado) "
                    + "VALUE ( ?, ?, ?, ?, ?)");
            ps.setString(1, key);
            ps.setDate(2, new java.sql.Date(value.getDataLimite().getTime()));
            ps.setDate(3, new java.sql.Date(value.getDataEmissao().getTime()));
            ps.setDouble(4, value.getPreco());
            ps.setBoolean(5, value.getEstado());
            ps.executeUpdate();
            if (value instanceof Extraordinaria) {
                ps = con.prepareStatement("INSERT INTO Extraordinária (designacao, despesa) "
                        + "VALUES ( ?, ?)");
                ps.setString(1, ((Extraordinaria) value).getDesignacao());
                ps.setString(2, key);
                ps.executeUpdate();
                for (Map.Entry<String, Double> e : ((Extraordinaria) value).getPercents().entrySet()) {
                    ps = con.prepareStatement("INSERT INTO InquilinoExtraordinária (inquilino, despesa, percentagem, estado) "
                            + "VALUE ( ?, ?, ?, ?)");
                    ps.setString(1, e.getKey());
                    ps.setString(2, key);
                    ps.setDouble(3, e.getValue());
                    ps.setBoolean(4, ((Extraordinaria) value).getEstados().get(e.getKey()));
                    ps.executeUpdate();
                }
                for (Map.Entry<String, Set<Prestacao>> e : ((Extraordinaria) value).getPrestacoes().entrySet()) {
                    for (Prestacao p : e.getValue()) {
                        ps = con.prepareStatement("INSERT INTO Prestacao (valor, nrSeq, estado, despesa, inquilino) "
                                + "VALUE ( ?, ?, ?, ?, ?)");
                        ps.setDouble(1, p.getValor());
                        ps.setInt(2, p.getNrSeq());
                        ps.setBoolean(3, p.getEstado());
                        ps.setString(4, key);
                        ps.setString(5, e.getKey());
                        ps.executeUpdate();
                    }
                }
            } else {
                ps = con.prepareStatement("INSERT INTO Recorrente (categoria, despesa) "
                        + "VALUES ( ?, ?)");
                ps.setString(1, ((Recorrente) value).getCategoria().getDesignacao());
                ps.setString(2, key);
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
        return value;
    }

    @Override
    public Despesa remove(Object key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void putAll(Map<? extends String, ? extends Despesa> m) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<String> keySet() {
        Set<String> despesas = new TreeSet<>();
        Connection con = null;
        try {
            con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Despesa ");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                despesas.add(rs.getString("ref"));
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
        return despesas;
    }

    @Override
    public Collection<Despesa> values() {
        Set<String> despesas = keySet();
        Collection<Despesa> res = new ArrayList<>();
        for (String s : despesas) {
            res.add(get(s));
        }
        return res;
    }

    @Override
    public Set<Entry<String, Despesa>> entrySet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<Despesa> getDespesas(String user) {
        Collection<Despesa> despesas = new ArrayList<>();
        Connection con = null;
        try {
            con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM InquilinoExtraordinária "
                    + "WHERE inquilino = ?");
            ps.setString(1, user);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                despesas.add(get(rs.getString("despesa")));
            }
            ps = con.prepareStatement("SELECT * FROM InquilinoCategoria I "
                    + "INNER JOIN Categoria C ON I.categoria = C.designacao "
                    + "INNER JOIN Recorrente R ON C.designacao = R.categoria "
                    + "WHERE I.inquilino = ?");
            ps.setString(1, user);
            rs = ps.executeQuery();
            while (rs.next()) {
                despesas.add(get(rs.getString("despesa")));
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
        return despesas;
    }

    public Despesa getDespesaPrestacao(Prestacao p) {
        Despesa d = null;
        Connection con = null;
        try {
            con = Connect.connect();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Prestacao "
                    + "WHERE idPrestacao = ?");
            ps.setInt(1, p.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                d = get(rs.getString("despesa"));
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
        return d;
    }
}
