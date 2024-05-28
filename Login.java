package gui;

//GABRIEL LIMA ANDRADE

//importando classes
import com.microsoft.sqlserver.jdbc.SQLServerException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

public class Login extends JFrame implements ActionListener {

    private JTextField user; //campo de digitação do usuario
    private JPasswordField password; //campo de digitação da senha
    private JComboBox<String> cargoBox;
    private JButton btnLogin;
    private JButton btnCadastro;
    private JButton btnCancel;
    private byte tentativas = 0;
    String url = ""; // conexão para bd
    String username = ""; // usuario
    String passwordDB = ""; // senha

    ArrayList<String> userNameArr2 = new ArrayList<>();

    private void LoginPainel() {


        setTitle("Login");//Colocar o titulo
        Container c = getContentPane();//criar um container
        c.setLayout(new GridLayout(4, 2));//fazer uma grid para o layout

        //label para o usuario
        JLabel lblUsuario = new JLabel("Usúario:");//escrever na label
        c.add(lblUsuario);//adiciona a label no container

        //campo para digitar o usuario
        user = new JTextField(); //seta user como um campo de Texto
        c.add(user);//adiciona no layout

        JLabel lblSenha = new JLabel("Senha:");//escrever na label
        c.add(lblSenha);//adiciona no layout

        password = new JPasswordField();//seta senha como um campo de Password

        c.add(password);//adiciona o campo de digitação

        btnLogin = new JButton("Login");//botão de Ok
        btnLogin.addActionListener(new ActionListener() {//quando eu clicar no botão ativa uma funçao
            public void actionPerformed(ActionEvent e) {
                Logar();
            }
        });

        btnCancel = new JButton("Limpar");//botão de cancelar
        btnCancel.addActionListener(new ActionListener() {//quando eu clicar no botão ativa uma funçao
            public void actionPerformed(ActionEvent e) {
                password.setText("");
                user.setText("");
            }
        });


        btnCadastro = new JButton("Cadastro");
        btnCadastro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                setVisible(false);

                Login login = new Login();
                login.Cadastro();
            }
        });


        c.add(btnLogin);//adiciona o botão de ok
        c.add(btnCancel);//adiciona o botao de cancelar
        c.add(btnCadastro);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//fazer o x da janela fechar
        setSize(300, 250);//setar o tamanho da ja
        setLocationRelativeTo(null);//Centralizar a janela na tela
        setVisible(true);//deixar a janela visivel

        try {
            Connection connection = DriverManager.getConnection(url, username, passwordDB); // conectar

            System.out.println("Conectou"); // alegria

            String sqlQuery = "SELECT * FROM userData"; // se eu quiser colocar manualmente no setString ou setInt os valores precisam ser ?, ?

            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(sqlQuery);

            ArrayList<String> arrUser = new ArrayList<>();

            while (rs.next()) { // imprime os dados de coluna especifica
                String x = rs.getString(4); // imprime cargo, por exemplo;

                int id = rs.getInt(1); // imprime cargo, por exemplo;

                String nomeUser = rs.getString(2); // guarda nome, por exemplo;
                if (!userNameArr2.contains(nomeUser)) {
                    userNameArr2.add(nomeUser);
                }

                arrUser.add(x);

            }


            connection.close(); // fechando conexão

        } catch (SQLException e) {
            System.out.println("Não conectou"); // tristeza
            e.printStackTrace();
        }

    }

    private byte usuarioTentativas = 0;
    private byte verificador = 0;
    private byte verificador2 = 0;
    private byte sizeOfArr = 0;

    ArrayList<Integer> userIdArr = new ArrayList<>();
    ArrayList<String> userNameArr = new ArrayList<>();
    ArrayList<String> userSenhaArr = new ArrayList<>();


    ArrayList<Integer> userTentativasArr = new ArrayList<>();

    private void Logar() {
        String usuario = user.getText();
        String senha = new String(password.getPassword());

        if (usuario.equals("") || senha.equals("")) {
            JOptionPane.showMessageDialog(null, "Digite algo nos campos", null, JOptionPane.INFORMATION_MESSAGE);
            password.setText("");
            user.setText("");
            verificador++;
        } else {
            try {

                Connection connection = DriverManager.getConnection(url, username, passwordDB); // conectar

                String sqlQuery = "SELECT * FROM userData";

                Statement statement = connection.createStatement(); // executeQuery não funciona com prepareStatement

                ResultSet rs = statement.executeQuery(sqlQuery); // para poder imprimir os dados

                String nomeUserValor = "";
                String senhaUserValor = "";
                boolean mensagemExibida = false;



                //https://stackoverflow.com/questions/64895557/how-do-i-get-a-result-from-a-query-java-sql
                while (rs.next()) { // imprime os dados de coluna especifica
                    int idUser = rs.getInt(1); // guarda nome, por exemplo;
                    String nomeUser = rs.getString(2); // guarda nome, por exemplo;
                    String senhaUser = rs.getString(3);
                    int bloqueado = rs.getInt(5);


                    nomeUserValor = nomeUser;
                    senhaUserValor = senhaUser;

                        userNameArr = new ArrayList<>();
                        userSenhaArr = new ArrayList<>();

                        ArrayList<String> userAllArr = new ArrayList<>();

                        if (!userIdArr.contains(idUser)) {
                            userIdArr.add(idUser);
                        }
                        userNameArr.add(nomeUser);
                        userSenhaArr.add(senhaUser);

                        userAllArr.add(String.valueOf(idUser));
                        userAllArr.add(nomeUser);
                        userAllArr.add(senhaUser);

                        sizeOfArr++;

                    if (bloqueado != 1) {

                        if (userNameArr.contains(usuario) && userSenhaArr.contains(senha) && bloqueado != 1 && verificador < 1) {
                            JOptionPane.showMessageDialog(null, "Login validado com sucesso!", null, JOptionPane.INFORMATION_MESSAGE);

                            verificador++;
                            verificador2++;
                        } else if (!userNameArr2.contains(usuario) && verificador < 1) {
                            JOptionPane.showMessageDialog(null, "Usuario não encontrado", null, JOptionPane.INFORMATION_MESSAGE);
                            password.setText("");
                            user.setText("");
                            verificador++;
                        }

                         if (usuario.equals(nomeUserValor) && !senha.equals(senhaUserValor) && verificador < 1) {
                            JOptionPane.showMessageDialog(null, "Senha invalida", null, JOptionPane.INFORMATION_MESSAGE);

                            JOptionPane.showMessageDialog(null, usuarioTentativas+1 + " / 3 tentativas de conexão ");
                            userTentativasArr.add(idUser);
                            usuarioTentativas++;
                            verificador2 = 1;

                            //https://stackoverflow.com/questions/47353687/printing-certain-values-using-collections-frequency
                            if (userTentativasArr.size() >= 3) {
                                int idBloquear = Collections.frequency(userTentativasArr, idUser);
                                if (idBloquear == 3) {
                                    JOptionPane.showMessageDialog(null, "O acesso ao usuário " + usuario + " está bloquado por excesso de tentativas");
                                    tentativas = 0;
                                    verificador2 = 1;

                                    String updateSql = "UPDATE userData SET bloqueado = ? WHERE ID = " + "?";

                                    PreparedStatement st = connection.prepareStatement(updateSql);

                                    st.setInt(1, 1);
                                    st.setInt(2, idUser);
                                    st.executeUpdate();

                                }
                            }

                            verificador++;
                            password.setText("");
                            user.setText("");
                        }

                    }else {
                        if (!mensagemExibida && usuario.equals(nomeUser) && senha.equals(senhaUser)) {
                            JOptionPane.showMessageDialog(null, "Usuário bloqueado por excesso de tentativa. Entre em contato com o suporte");
                            mensagemExibida = true;
                            verificador2++;
                        }
                    }
                }
                    verificador = 0;
                    sizeOfArr = 0;


                if (verificador2 == 0) {
                    tentativas++;
                    JOptionPane.showMessageDialog(null, tentativas + "/ 3 tentativas", null, JOptionPane.INFORMATION_MESSAGE);


                }
                verificador = 0;
                verificador2 = 0;

                if (tentativas == 3) {
                    JOptionPane.showMessageDialog(null, "Falha no login... Tente novamente mais tarde");
                    btnLogin.setEnabled(false);
                    btnCadastro.setEnabled(false);
                    btnCancel.setEnabled(false);
                }
                statement.close();
                connection.close(); // fechando conexão
            } catch (SQLException e) {
                System.out.println("Não conectou"); // tristeza
                e.printStackTrace();

            }
        }



    }

    private String usuarioConfirmar = "";
    private String senhaConfirmar = "";
    private String cargoConfirmar = "";
    private byte contador = 0;

    private void Cadastro() {
        String[] cargoAdm = {"Admin"};
        String[] cargoTodos = {"Admin", "Técnico", "Outro"};
        String[] cargo;

        setTitle("Cadastro");//Colocar o titulo
        Container c = getContentPane();//criar um container
        c.setLayout(new GridLayout(4,2));//fazer uma grid para o layout

        //label para o usuario
        JLabel lblUsuario = new JLabel("Usúario:");//escrever na label
        c.add(lblUsuario);//adiciona a label no container

        //campo para digitar o usuario
        user = new JTextField(); //seta user como um campo de Texto
        c.add(user);//adiciona no layout

        JLabel lblSenha = new JLabel("Senha:");//escrever na label
        c.add(lblSenha);//adiciona no layout

        password = new JPasswordField();//seta senha como um campo de Password
        c.add(password);//adiciona o campo de digitação

        JButton btnLogin = new JButton("Ir para login");//botão de Ok
        btnLogin.addActionListener(new ActionListener(){//quando eu clicar no botão ativa uma funçao
            public void actionPerformed(ActionEvent e) {

                dispose();
                setVisible(false);

                Login login = new Login();
                login.LoginPainel();
            }
        });

        JButton btnCancel = new JButton("Limpar");//botão de cancelar
        btnCancel.addActionListener(new ActionListener(){//quando eu clicar no botão ativa uma funçao
            public void actionPerformed(ActionEvent e) {
                password.setText("");
                user.setText("");

            }
        });

        ArrayList<String> arrUser = new ArrayList<>();

        try {
            Connection connection = DriverManager.getConnection(url, username, passwordDB); // conectar

            String sqlQuery = "SELECT * FROM userData"; // read/query

            Statement createStatement = connection.createStatement();

            ResultSet rs = createStatement.executeQuery(sqlQuery); // executar query


            while (rs.next()) { // imprime os dados de coluna especifica
                String x = rs.getString(4); // imprime cargo, por exemplo;
                arrUser.add(x);
            }


            connection.close();
            createStatement.close();
        } catch (SQLException _e) {
            System.out.println("Não conectou"); // tristeza
            _e.printStackTrace();
        }


        Boolean userCargo = arrUser.contains("Admin");


        if (userCargo) {
            cargo = cargoTodos;
            btnLogin.setEnabled(true);
        } else {
            cargo = cargoAdm;
            btnLogin.setEnabled(false);
        }
        cargoBox = new JComboBox<>(cargo);



        JButton btnCadastro = new JButton("Cadastrar");//botão de cancelar
        btnCadastro.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                String usuario = user.getText();
                String senha = new String(password.getPassword());
                String cargoSelecionado = cargoBox.getSelectedItem().toString();


                if(usuario.equals("") || senha.equals("")) {
                    JOptionPane.showMessageDialog(null, "Usuario ou senha inválido");
                    setVisible(true);
                } else {
                    if (contador == 0) {
                        usuarioConfirmar = usuario;
                        senhaConfirmar = senha;
                        cargoConfirmar = cargoSelecionado;
                        JOptionPane.showMessageDialog(null, "Confirme os seus dados");
                        cargoBox.setEnabled(false);
                        password.setText("");
                        user.setText("");
                        contador++;

                        dispose();
                        setVisible(false);

                        Login login = new Login();
                        login.confirmarCadastro(usuarioConfirmar, senhaConfirmar, cargoConfirmar);


                    }
                }
            }
        });

        c.add(btnLogin);//adiciona o botão de ok
        c.add(btnCancel);//adiciona o botao de limpar
        c.add(btnCadastro);
        c.add(cargoBox);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//fazer o x da janela fechar
        setSize(300, 250);//setar o tamanho da ja
        setLocationRelativeTo(null);//Centralizar a janela na tela

        setVisible(true);//deixar a janela visivel

    }

    private void confirmarCadastro(String usuario, String senha, String cargo) {
        setTitle("Cadastro");//Colocar o titulo
        Container c = getContentPane();//criar um container
        c.setLayout(new GridLayout(3,2));//fazer uma grid para o layout

        //label para o usuario
        JLabel lblUsuario = new JLabel("Usúario:");//escrever na label
        c.add(lblUsuario);//adiciona a label no container

        //campo para digitar o usuario
        user = new JTextField(); //seta user como um campo de Texto
        c.add(user);//adiciona no layout

        JLabel lblSenha = new JLabel("Senha:");//escrever na label
        c.add(lblSenha);//adiciona no layout


        password = new JPasswordField();//seta senha como um campo de Password
        c.add(password);//adiciona o campo de digitação

        JButton btnCancel = new JButton("Limpar");//botão de cancelar
        btnCancel.addActionListener(new ActionListener(){//quando eu clicar no botão ativa uma funçao
            public void actionPerformed(ActionEvent e) {
                password.setText("");
                user.setText("");

            }
        });

        JButton btnCadastro = new JButton("Cadastrar");//botão de cancelar
        btnCadastro.addActionListener(new ActionListener(){


            public void actionPerformed(ActionEvent e) {
                String usuarioConfirmarCadastro = user.getText();
                String senhaConfirmarCadastro = new String(password.getPassword());

                if (usuarioConfirmarCadastro.equals("") || senhaConfirmarCadastro.equals("")) {
                    JOptionPane.showMessageDialog(null, "Usuario ou senha inválido");
                    setVisible(true);
                } else {
                    if (senhaConfirmarCadastro.equals(usuario) && senhaConfirmarCadastro.equals(senha)) {
                        try {
                            Connection connection = DriverManager.getConnection(url, username, passwordDB); // conectar

                            System.out.println("Conectou"); // alegria

                            String sql = "INSERT INTO userData (nome, senha, cargo, bloqueado)"
                                    + "VALUES (?, ?, ?, ?)"; // se eu quiser colocar manualmente no setString ou setInt os valores precisam ser ?, ?


                            PreparedStatement statement = connection.prepareStatement(sql);// inserindo os valores linha por linha

                            statement.setString(1, usuario); // colcoar no banco de dados
                            statement.setString(2, senha);
                            statement.setString(3, cargo);
                            statement.setInt(4, 0);

                            statement.executeUpdate(); // manda para o BD // como se fosse o push do git. os outros seriam um commit

                            JOptionPane.showMessageDialog(null, cargo + " adicionado com sucesso", "Cadastro", JOptionPane.INFORMATION_MESSAGE);

                            connection.close(); // fechando conexão
                            dispose();
                            setVisible(false);


                        } catch (SQLServerException q) {
                            JOptionPane.showMessageDialog(null, "Usuário já cadastrado.");
                        } catch (SQLException _e) {
                            System.out.println("Não conectou"); // tristeza
                            _e.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Usuario ou senha incorreto");
                    }
                }
                dispose();
                setVisible(false);

                Login login = new Login();
                login.Cadastro();

            }
        });

        c.add(btnCancel);//adiciona o botao de limpar
        c.add(btnCadastro);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//fazer o x da janela fechar
        setSize(300, 250);//setar o tamanho da ja
        setLocationRelativeTo(null);//Centralizar a janela na tela

        setVisible(true);//deixar a janela visivel


    }


    public static void main(String[] args) {
        Login login = new Login();

        login.Cadastro();

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
