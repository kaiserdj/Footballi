package com.kaiserdj.footballi;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author gonfe
 */
class Conexion {

    /**
     * Conexion a la base de datos
     *
     * @param Url Url del hosting de la base de datos
     * @param baseDatos Nombre de la base de datos
     * @param username Usuario de la base de datos
     * @param password Contraseña de la base de datos
     * @return Variable connection de la base de datos MYSQL
     */
    static Connection mySQL(String Url, String baseDatos, String username, String password) {
        java.sql.Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            java.util.Properties props = new java.util.Properties();
            props.put("charSet", "iso-8859-1");
            props.put("user", username);
            props.put("password", password);
            String url = "jdbc:mysql://" + Url + "/" + baseDatos;
            con = (java.sql.Connection) java.sql.DriverManager.getConnection(url, props);
            return (Connection) con;
        } catch (java.sql.SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("Class Not Found Exception: " + e.toString());
            return null;
        }
    }

    /**
     * Lee toda una tabla
     *
     * @param conexion Variable connection de la base de datos MYSQL
     * @param sql Tabla de la que se desea
     * @return Arraylist con el primer valor los nombres de las columnas en un
     * Array de Strings y todos los demas son las lines de la tabla en Arrays de
     * Strings
     */
    static ArrayList leerTabla(Connection conexion, String sql) {
        ArrayList<String[]> base = new ArrayList<String[]>();
        if (conexion != null) {
            try {
                ResultSet rs = null;
                Statement sentencia = (Statement) conexion.createStatement();
                rs = sentencia.executeQuery(sql);

                java.sql.ResultSetMetaData rsMetaData = rs.getMetaData();
                int numberOfColumns = rsMetaData.getColumnCount();
                String[] columnas = new String[numberOfColumns];

                for (int i = 0; i < numberOfColumns; i++) {
                    columnas[i] = rsMetaData.getColumnName(i + 1);
                }

                base.add(columnas);

                while (rs.next()) {
                    String[] datos = new String[columnas.length];

                    for (int i = 0; i < columnas.length; i++) {
                        datos[i] = rs.getString(columnas[i]);
                    }
                    base.add(datos);
                }
                return base;
            } catch (SQLException ex) {
                System.out.println("Error: " + ex);
            }
        }
        return base;
    }

    static int ModificarTabla(Connection conexion, String sql) {
        if (conexion != null) {
            try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
                return stmt.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

}
