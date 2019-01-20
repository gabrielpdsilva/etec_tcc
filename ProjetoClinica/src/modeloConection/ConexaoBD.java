package modeloConection;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ConexaoBD {
    
    public Statement stm;                               //Prepara e realiza pesquisa no banco de dados
    public ResultSet rs;                                //Armazena o resultado da pesquisa
    private String driver = "org.postgresql.Driver";    //driver identifica nosso serviço de banco de dados
    private String caminho = "jdbc:postgresql://localhost:5432/projetoclinica";     //caminho vai dizer onde está alocado o banco de dados
    private String usuario = "postgres";                             //usuario da instalação do banco de dados, postgres é o nome padrão
    private String senha = "admin";                               //senha do banco de dados
    public Connection con;                              //realiza conexão com banco de dados
    
    public void conexao(){
        try {
            System.setProperty("jdbc.Drivers", ""); //responsavel por setar propriedade do driver de conexao
            con = DriverManager.getConnection(caminho, usuario, senha);
         //   JOptionPane.showMessageDialog(null, "Banco de dados conectado com sucesso.");
        } catch (SQLException ex) {
            Logger.getLogger(ConexaoBD.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Não foi possível conectar-se ao banco de dados:\n"+ex.getMessage()); //ex pega a mensagem de erro que tá dando e exibirá ao lado
        }
    }
    
    public void executaSql(String sql){
        try {
            stm = con.createStatement(rs.TYPE_SCROLL_INSENSITIVE, rs.CONCUR_READ_ONLY);
            rs = stm.executeQuery(sql);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na função executaSql\n"+ex.getMessage());
        }
        
    }
    
    public void desconecta(){
        try {
            con.close();
          //  JOptionPane.showMessageDialog(null, "Banco de dados desconectado com sucesso.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Não foi possível desconectar-se do banco de dados.\n"+ex.getMessage());
        }
    }
    
}
