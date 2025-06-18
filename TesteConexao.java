package sistema;

import java.sql.Connection;

public class TesteConexao {
    public static void main(String[] args) {
        try {
            Connection conn = ConexaoDB.getConnection();
            System.out.println("✅ Conectado com sucesso ao banco de dados!");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
