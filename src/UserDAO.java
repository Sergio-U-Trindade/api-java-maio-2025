import java.sql.*;
import java.util.*;
import java.util.regex.Pattern;

public class UserDAO {

    // Método para validar o formato do e-mail usando regex
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (email == null) return false;
        return pattern.matcher(email).matches();
    }

    public static void init() throws Exception {
        Connection conn = DB.connect();
        Statement stmt = conn.createStatement();
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL,
                email TEXT NOT NULL,
                senha TEXT NOT NULL,
                cidade TEXT,
                idade INTEGER
            );
        """;
        stmt.execute(sql);
        conn.close();
    }

    public static void save(User user) throws Exception {
        // Primeiro verifica se o e-mail é válido
        if (!isValidEmail(user.email)) {
            throw new IllegalArgumentException("E-mail inválido! Por favor, digite um e-mail válido.");
        }

        // Verifica se o e-mail já está cadastrado
        Connection conn = DB.connect();
        String checkSql = "SELECT COUNT(*) FROM users WHERE email = ?";
        PreparedStatement checkStmt = conn.prepareStatement(checkSql);
        checkStmt.setString(1, user.email);
        ResultSet rs = checkStmt.executeQuery();

        if (rs.next() && rs.getInt(1) > 0) {
            conn.close();
            throw new IllegalArgumentException("Este e-mail já está cadastrado!");
        }

        // Se passar nas validações, prossegue com o cadastro
        String sql = "INSERT INTO users (nome, email, senha, cidade, idade) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, user.nome);
        stmt.setString(2, user.email);
        stmt.setString(3, user.senha);
        stmt.setString(4, user.cidade);
        stmt.setInt(5, user.idade);
        stmt.execute();
        conn.close();
    }

    public static List<User> findAll() throws Exception {
        List<User> list = new ArrayList<>();
        Connection conn = DB.connect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM users");
        while (rs.next()) {
            list.add(new User(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("senha"),
                    rs.getString("cidade"),
                    rs.getInt("idade")
            ));
        }
        conn.close();
        return list;
    }

    public static User findById(int id) throws Exception {
        Connection conn = DB.connect();
        String sql = "SELECT * FROM users WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        User user = null;
        if (rs.next()) {
            user = new User(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("senha"),
                    rs.getString("cidade"),
                    rs.getInt("idade")
            );
        }
        conn.close();
        return user;
    }
    //***************************************************************
    public static User findByEmail(String email) throws Exception {
        Connection conn = DB.connect();
        String sql = "SELECT * FROM users WHERE email = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        User user = null;
        if (rs.next()) {
            user = new User(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("senha"),
                    rs.getString("cidade"),
                    rs.getInt("idade")
            );
        }
        conn.close();
        return user;
    }
    //***************************************************************

    //***************************************************************
    public static void update(User user) throws Exception {
        // Primeiro valida o formato do e-mail
        if (!isValidEmail(user.email)) {
            throw new IllegalArgumentException("Formato de e-mail inválido");
        }

        Connection conn = DB.connect();

        try {
            // Verifica se o novo e-mail já está sendo usado por outro usuário
            String checkSql = "SELECT id FROM users WHERE email = ? AND id != ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, user.email);
            checkStmt.setInt(2, user.id);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                throw new IllegalArgumentException("Este e-mail já está sendo usado por outro usuário");
            }

            // Se passou nas validações, procede com a atualização
            String sql = "UPDATE users SET nome = ?, email = ?, senha = ?, cidade = ?, idade = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.nome);
            stmt.setString(2, user.email);
            stmt.setString(3, user.senha);
            stmt.setString(4, user.cidade);
            stmt.setInt(5, user.idade);
            stmt.setInt(6, user.id);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Falha na atualização - usuário não encontrado");
            }
        } finally {
            conn.close();
        }
    }
//******************************************************************
    public static void delete(int id) throws Exception {
        Connection conn = DB.connect();
        String sql = "DELETE FROM users WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
        conn.close();
}
}