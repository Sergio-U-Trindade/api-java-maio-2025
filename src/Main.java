import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws Exception {
        UserDAO.init(); //Cria a tabela caso não exista
        //Cria o server
        HttpServer server = HttpServer.create(new InetSocketAddress(8000),0);
        //Cria a rota de contexto (endPoint)
        server.createContext("/users", new UserHandler());
        //Inicializa o servidor
        server.start();
        System.out.println("Servidor rodando em http://localhost:8000");
    }
}
