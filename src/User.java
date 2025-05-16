public class User {
    //Atributos (variáveis):
    public int id;
    public int idade;
    public String nome;
    public String email;
    public String senha;
    public String cidade;

    //Construtor da classe vazio.
    public User(){}

    public User(int id, String nome, String email, String senha, String cidade, int idade){
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cidade = cidade;
        this.senha = senha;
        this.idade = idade;
    }
}