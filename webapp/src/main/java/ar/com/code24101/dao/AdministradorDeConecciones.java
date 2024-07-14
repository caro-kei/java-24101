package ar.com.code24101.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import javafx.scene.chart.XYChart.Data;


    //metodo estatico: Clase.metodo(); no es neceario crear un objeto
    //para usar el metodo
  public class AdministradorDeConecciones {

    final private static String TOLOG = "%s - %s %s.";
    private static Connection con = null;
    private static Statement stat = null;
    private static final String URL = "jdbc:mysql://localhost:3306/24101?serverTimeZone=UTC&userSSL=false";
    private static final String USER = "root";//System.getenv("user_mysql");
    private static final String PASSWORD = "";//System.getenv("pass_mysql");
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    public static void conectar() {
        //Connection con = null;
        //System.
        try {

            Class.forName(DRIVER);
            con = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.out.println("Se produjo una falla en conexiÃ³n. " + e);
        }
    }

    public static void desconectar() {
        try {
            if (stat != null) {
                stat.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            System.out.println("Se produjo una falla al cerrar la conexiÃ³n. " + e.getMessage());
        }
    }

    public static <stat> void genericoCrearActualizarBorrar(String sql) throws Exception {
        final String aRealizar = sql.split(" ")[0];

        try {
            conectar();
            stat = con.createStatement();
            boolean ret = stat.execute(sql);
            log((aRealizar.equalsIgnoreCase("DELETE") ? true : ret), aRealizar);

        } catch (Exception e) {
            System.out.println("Se produjo una falla en " + aRealizar + ". " + e);
        } finally {
            desconectar();
        }
    }

    public static ResultSet genericoConsulta(String sql) throws Exception {
        final String aRealizar = sql.split(" ")[0];

        ResultSet resultado = null;

        try {
            conectar();
            stat = con.createStatement();
            resultado = stat.executeQuery(sql);

            log(resultado != null, aRealizar);
            //TODO::: requiere desconectar en el metodo que lo llama
        } catch (Exception e) {
            System.out.println("Se produjo una falla en " + aRealizar + ". " + e);
        }
        return resultado;
    }

    
    private static String getFecha() {
        return new SimpleDateFormat("dd/mm/yyyy - hh:mm:ss").format(new Data());
    }

    private static void log(Boolean verifica, String aRealizar) {
        String fecha = getFecha();
        
        System.out.println(TOLOG.formatted(fecha, aRealizar, (verifica ? "realizado.": "no realizado.")));
     
    }
}
