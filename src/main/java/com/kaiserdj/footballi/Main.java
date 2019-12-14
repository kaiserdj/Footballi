/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kaiserdj.footballi;

import com.mysql.jdbc.Connection;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author gonfe
 */
public class Main extends javax.swing.JFrame {

    public static final Connection conexion = Conexion.mySQL("remotemysql.com:3306", "A3zUFW1JZJ", "A3zUFW1JZJ", "l0Q9G1iMRV");
    public static String url = "";
    public static String User = "";
    public static String Avatar = "";
    public static String lang = "es";

    public static void Ligas() {
        Pnl_LigasPopulares.removeAll();
        Pnl_CopasPopulares.removeAll();
        url = "https://" + lang + ".soccerwiki.org/league.php";
        if (Utilidades.getEstadoPagina(url) == 200) {
            Document Html = Utilidades.getHtmlDocument(url);

            /*Ligas populares*/
            Elements General = Html.select(".factfileBorder table");
            Txt_Ligaspopu.setText(Html.select(".factfileBorder h2").get(0).text());
            Elements Equpos_Populares = General.get(0).select("td");
            int position_x = 0;
            int position_y = 0;
            for (Element elem : Equpos_Populares) {
                Elements Html_Bandera = elem.select("span");
                String Bandera = Html_Bandera.attr("class");
                Elements Html_enlc = elem.select("a");
                String Enlace = Utilidades.url(Html_enlc.attr("href"));

                JButton boton = new JButton(elem.text());
                boton.setName("btn" + elem.text());
                boton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Liga(elem.text(), Enlace);
                    }
                });
                boton.setBounds(position_x, position_y, 100, 30);
                boton.setText("");
                boton.setVisible(true);
                boton.setIcon(Utilidades.bandera(Bandera, 30, 30));
                Pnl_LigasPopulares.add(boton);

                JLabel nombre = new JLabel(elem.text());
                nombre.setBounds(position_x, position_y + 35, 100, 15);
                Pnl_LigasPopulares.add(nombre);

                if (position_x < 550) {
                    position_x = position_x + 110;
                } else {
                    position_x = 0;
                    position_y = position_y + 60;
                }
            }
            Pnl_LigasPopulares.updateUI();

            /*Copas populares*/
            position_x = 0;
            position_y = 0;
            Txt_CopasPopu.setText(Html.select(".factfileBorder h2").get(1).text());
            Elements Copas_Populares = General.get(1).select("td:has(a)");
            for (Element elem : Copas_Populares) {
                Elements Html_Bandera = elem.select("span");
                String Bandera = Html_Bandera.attr("class");
                Elements Html_enlc = elem.select("a");
                String Enlace = Utilidades.url(Html_enlc.attr("href"));

                JButton boton = new JButton(elem.text());
                boton.setName("btn" + elem.text());
                boton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Copa(Enlace);
                    }
                });
                boton.setBounds(position_x, position_y, 100, 30);
                boton.setText("");
                boton.setVisible(true);
                boton.setIcon(Utilidades.bandera(Bandera, 30, 30));
                Pnl_CopasPopulares.add(boton);

                JLabel nombre = new JLabel(elem.text());
                nombre.setBounds(position_x, position_y + 35, 100, 15);
                Pnl_CopasPopulares.add(nombre);

                if (position_x < 550) {
                    position_x = position_x + 110;
                } else {
                    position_x = 0;
                    position_y = position_y + 60;
                }
            }
            Pnl_CopasPopulares.updateUI();
        } else {

        }
    }

    public static void Liga(String liga, String url) {
        Pnl_Tablero.removeAll();
        Pnl_Tablero.add(Pnl_Liga);
        Pnl_Tablero.repaint();
        Pnl_Tablero.revalidate();
        Txt_Liga.setText(liga);
        Main.url = url;

        if (Utilidades.getEstadoPagina(url) == 200) {
            Document Html = Utilidades.getHtmlDocument(url);

            /*Logo*/
            Elements Logo = Html.select(".LcontentBox:has(img) img");
            String Logo_img = Logo.attr("src");
            Logo_img = "https:" + Logo_img;
            Txt_Url.setIcon(Utilidades.UrlIcon(Logo_img, 100, 100));

            /*Datos del equipo general*/
            Elements EquipoGeneral = Html.select("#leaguedetailsdiv table");
            Elements TitulosGeneral = EquipoGeneral.select("th");
            Elements DatosGeneral = EquipoGeneral.select("td");
            Txt_Patro.setText(TitulosGeneral.get(0).text());
            Txt_Patro_Res.setText(DatosGeneral.get(0).text());
            Txt_Año.setText(TitulosGeneral.get(1).text());
            Txt_AñoRes.setText(DatosGeneral.get(1).text());
            Txt_Pais.setText(TitulosGeneral.get(2).text());
            Txt_PaisRes.setIcon(Utilidades.bandera(DatosGeneral.get(2).select("span").attr("class"), 30, 30));
            Txt_PaisRes.setText(DatosGeneral.get(2).text());

            /*Tabla Historico*/
            Elements Title = Html.select("#leaguewinnersdiv h2");
            Btn_Historico.setText(Title.text());
            Elements Year = Html.select("#leaguewinnersdiv table th");
            Elements Team = Html.select("#leaguewinnersdiv table td");
            DefaultTableModel dtm = new DefaultTableModel();
            dtm.addColumn("year");
            dtm.addColumn("Team");
            Tbl_Historico.setModel(dtm);
            ArrayList<String[]> Historico_Url = new ArrayList<String[]>();
            for (int i = 0; i < Year.size(); i++) {
                dtm.addRow(new Object[]{Year.get(i).text(), Team.get(i).text()});
                Historico_Url.add(new String[]{Team.get(i).text(), Utilidades.url(Team.get(i).select("a").attr("href"))});
            }
            TableColumn Tbl_Historico_col = Tbl_Historico.getColumnModel().getColumn(1);
            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
            renderer.setForeground(Color.blue);
            Tbl_Historico_col.setCellRenderer(renderer);
            Tbl_Historico.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    int row = Tbl_Historico.rowAtPoint(evt.getPoint());
                    int col = Tbl_Historico.columnAtPoint(evt.getPoint());
                    if (row >= 0 && (col >= 1 && col <= 3)) {
                        String[] Equipo = Historico_Url.get(row);
                        Equipo(Equipo[0], Equipo[1]);
                    }
                }
            });
            Tbl_Historico.setRowHeight(16);
            Tbl_Historico.setDefaultEditor(Object.class, null);

            /*Boton Arbitros*/
            Element Title2 = Html.select(".Border h2").get(1);
            Btn_Arbitros.setText(Title2.text());

            /*Tabla Equipos*/
            Elements Title3 = Html.select(".clearfix:has(table[summary=\"All clubs\"]) .cBox_header");
            Txt_TituloEquipos.setText(Title3.get(0).text());
            Elements Titulos = Html.select(".clearfix table[summary=\"All clubs\"] thead th");
            Elements Equipos = Html.select(".clearfix table[summary=\"All clubs\"] tbody tr");
            DefaultTableModel DatosEquipos = new DefaultTableModel();
            for (int i = 0; i < Titulos.size(); i++) {
                DatosEquipos.addColumn(Titulos.get(i).text());
            }
            ArrayList<String[]> Equipos_Url = new ArrayList<String[]>();
            for (int i = 0; i < Equipos.size(); i++) {
                Elements Equipo = Equipos.get(i).select("td");
                /*Emblema*/
                Elements Emblema = Equipo.get(0).select("img");
                ImageIcon Emblema_img = Utilidades.UrlIcon("https:" + Emblema.attr("src"), 30, 30);
                /*Nombre*/
                String NombreEquipo = Equipo.get(1).text();
                String NombreEquipo_Url = Equipo.get(1).select("a").attr("href");
                NombreEquipo_Url = Utilidades.url(NombreEquipo_Url);
                /*Manager*/
                String Manager = Equipo.get(2).text();
                String Manager_Url = Equipo.get(2).select("a").attr("href");
                Manager_Url = Utilidades.url(Manager_Url);
                /*Estadio*/
                String Estadio_Nombre = Equipo.get(3).text();
                String Estadio_Url = Equipo.get(3).select("a").attr("href");
                Estadio_Url = Utilidades.url(Estadio_Url);
                /*Ubicacion*/
                String Ubicacion = Equipo.get(4).text();
                DatosEquipos.addRow(new Object[]{Emblema_img, NombreEquipo, Manager, Estadio_Nombre, Ubicacion});
                DatosEquipos.setValueAt(Emblema_img, i, 0);
                Equipos_Url.add(new String[]{NombreEquipo, NombreEquipo_Url, Manager, Manager_Url, Estadio_Url});
            }
            Tbl_Equipos.setModel(DatosEquipos);
            Tbl_Equipos.getColumnModel().getColumn(0).setCellRenderer(Tbl_Equipos.getDefaultRenderer(ImageIcon.class));
            Tbl_Equipos.setRowHeight(30);
            Tbl_Equipos.getColumnModel().getColumn(0).setPreferredWidth(5);
            TableColumn Tbl_Equipos_col = Tbl_Equipos.getColumnModel().getColumn(1);
            TableColumn Tbl_Equipos_col2 = Tbl_Equipos.getColumnModel().getColumn(2);
            TableColumn Tbl_Equipos_col3 = Tbl_Equipos.getColumnModel().getColumn(3);
            renderer.setForeground(Color.blue);
            Tbl_Equipos_col.setCellRenderer(renderer);
            Tbl_Equipos_col2.setCellRenderer(renderer);
            Tbl_Equipos_col3.setCellRenderer(renderer);
            Tbl_Equipos.setDefaultEditor(Object.class, null);
            Tbl_Equipos.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    int row = Tbl_Equipos.rowAtPoint(evt.getPoint());
                    int col = Tbl_Equipos.columnAtPoint(evt.getPoint());
                    if (row >= 0 && (col >= 1 && col <= 3)) {
                        String[] Equipo = Equipos_Url.get(row);
                        switch (col) {
                            case 1:
                                Equipo(Equipo[0], Equipo[1]);
                                break;
                            case 2:
                                Entrenador(Equipo[2], Equipo[3]);
                                break;
                            case 3:
                                Campo(Equipo[4]);
                                break;
                        }
                    }
                }
            });
        } else {
            JOptionPane.showMessageDialog(null, "Se a producido un error en la conexion", "Error en la conexión", ERROR);
        }
    }

    public static void Copa(String url) {
        Pnl_Tablero.removeAll();
        Pnl_Tablero.add(Pnl_Copa);
        Pnl_Tablero.repaint();
        Pnl_Tablero.revalidate();
        if (Utilidades.getEstadoPagina(url) == 200) {
            Document Html = Utilidades.getHtmlDocument(url);

            /*Copa*/
            Txt_Copa.setText(Html.select("#content h2").get(0).text());

            /*Logo*/
            Elements Logo = Html.select(".LcontentBox:has(img) img");
            String Logo_img = Logo.attr("src");
            Logo_img = "https:" + Logo_img;
            Txt_LogoCopa.setIcon(Utilidades.UrlIcon(Logo_img, 100, 100));

            /*Datos de la copa*/
            Element DatosCopa = Html.select("#content table").get(0);
            Txt_AñoFund.setText(DatosCopa.select("th").get(0).text());
            Txt_AñoFundRes.setText(DatosCopa.select("td").get(0).text());
            Txt_TipoCopa.setText(DatosCopa.select("th").get(1).text());
            Txt_TipoCopaRes.setText(DatosCopa.select("td").get(1).text());
            Txt_PaisCopa.setText(DatosCopa.select("th").get(2).text());
            Txt_PaisCopaRes.setText(DatosCopa.select("td").get(2).text());
            Txt_PaisCopaRes.setIcon(Utilidades.bandera(DatosCopa.select("td").get(2).select("span").attr("class"), 30, 30));

            /*Datos de años de la copa*/
            Elements AñosCopaAño = Html.select("#content table").get(1).select("th");
            Elements AñosCopaEquipo = Html.select("#content table").get(1).select("td");
            DefaultTableModel Copa = new DefaultTableModel();
            Copa.addColumn("");
            Copa.addColumn("");
            ArrayList<String[]> Equipos_Url = new ArrayList<String[]>();
            for (int i = 0; i < AñosCopaAño.size(); i++) {
                String Año = AñosCopaAño.get(i).text();
                String Equipo = AñosCopaEquipo.get(i).text();
                Equipos_Url.add(new String[]{Equipo, Utilidades.url(AñosCopaEquipo.get(i).select("a").attr("href"))});
                Copa.addRow(new Object[]{Año, Equipo});
            }
            Tbl_Copa.setModel(Copa);
            Tbl_Copa.setDefaultEditor(Object.class, null);
            TableColumn column = Tbl_Copa.getColumnModel().getColumn(1);
            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
            renderer.setForeground(Color.blue);
            column.setCellRenderer(renderer);
            Tbl_Copa.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    int row = Tbl_Copa.rowAtPoint(evt.getPoint());
                    int col = Tbl_Copa.columnAtPoint(evt.getPoint());
                    if (row >= 0 && col == 1) {
                        String[] Equipo = Equipos_Url.get(row);
                        Equipo(Equipo[0], Equipo[1]);
                    }
                }
            });
        }
    }

    public static void Equipo(String nombre, String url) {
        Pnl_Tablero.removeAll();
        Pnl_Tablero.add(Pnl_Equipo);
        Pnl_Tablero.repaint();
        Pnl_Tablero.revalidate();
        Main.url = url;
        if (Utilidades.getEstadoPagina(url) == 200) {
            Document Html = Utilidades.getHtmlDocument(url);

            /*Nombre equipo*/
            Txt_Equipo.setText(nombre);
            /*Logo*/
            Elements Logo = Html.select(".LcontentBox:has(img) img");
            String Logo_img = Logo.attr("src");
            Logo_img = "https:" + Logo_img;
            Txt_Logo.setIcon(Utilidades.UrlIcon(Logo_img, 100, 100));
            /*Datos Generales*/
            Elements TitulosGenerales = Html.select("#clubdetailsdiv table th");
            Elements DatosGenerales = Html.select("#clubdetailsdiv table td");
            Txt_Manager.setText(TitulosGenerales.get(0).text());
            Txt_ManagerRes.setText(DatosGenerales.get(0).text());
            Txt_ManagerRes.setIcon(Utilidades.bandera(DatosGenerales.get(0).select("span").attr("class"), 30, 30));
            Txt_ManagerRes.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    Entrenador(DatosGenerales.get(0).text(), Utilidades.url(DatosGenerales.get(0).select("a").attr("href")));
                }
            });
            Txt_ManagerRes.setForeground(Color.blue);
            Txt_Apodo.setText(TitulosGenerales.get(1).text());
            Txt_ApodoRes.setText("<html>" + DatosGenerales.get(1).text() + "</html>");
            Txt_NombreMedio.setText(TitulosGenerales.get(2).text());
            Txt_NombreMedioRes.setText(DatosGenerales.get(2).text());
            Txt_NombreCorto.setText(TitulosGenerales.get(3).text());
            Txt_NombreCortoRes.setText(DatosGenerales.get(3).text());
            Txt_AñoFun.setText(TitulosGenerales.get(4).text());
            Txt_AñoFunRes.setText(DatosGenerales.get(4).text());
            Txt_NombreEstadio.setText("<html>" + TitulosGenerales.get(5).text() + "</html>");
            Txt_NombreEstadioRes.setText("<html>" + DatosGenerales.get(5).select("a").text() + "<br>" + DatosGenerales.get(5).select("acronym").text() + "</html>");
            Txt_NombreEstadioRes.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    Pnl_Tablero.removeAll();
                    Pnl_Tablero.add(Pnl_Estadio);
                    Pnl_Tablero.repaint();
                    Pnl_Tablero.revalidate();
                    Entrenador(DatosGenerales.get(5).text(), DatosGenerales.get(5).select("a").attr("href"));
                }
            });
            Txt_NombreEstadioRes.setForeground(Color.blue);
            Txt_NomLiga.setText(TitulosGenerales.get(6).text());
            Txt_NomLigaRes.setText(DatosGenerales.get(6).text());
            Txt_NomLigaRes.setIcon(Utilidades.bandera(DatosGenerales.get(6).select("span").attr("class"), 30, 30));
            Txt_NomLigaRes.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    Liga(DatosGenerales.get(6).text(), Utilidades.url(DatosGenerales.get(6).select("a").attr("href")));
                }
            });
            Txt_NomLigaRes.setForeground(Color.blue);
            Txt_Ubicacion.setText(TitulosGenerales.get(7).text());
            Txt_UbicacionRes.setText(DatosGenerales.get(7).text());
            Txt_PaisClub.setText(TitulosGenerales.get(8).text());
            Txt_PaisClubRes.setText(DatosGenerales.get(8).text());
            Txt_PaisClubRes.setIcon(Utilidades.bandera(DatosGenerales.get(8).select("span").attr("class"), 30, 30));

            /*Tabla jugadores*/
            Element Jugadores = Html.select("table:has(acronym)").get(1);
            Elements JugadoresTitulos = Jugadores.select("th");
            Elements JugadoresDatos = Jugadores.select("tr");
            DefaultTableModel DatosJugadores = new DefaultTableModel();
            for (int i = 0; i < JugadoresTitulos.size(); i++) {
                DatosJugadores.addColumn(JugadoresTitulos.get(i).text());
            }
            ArrayList<String[]> Jugadores_Url = new ArrayList<String[]>();
            for (int i = 1; i < JugadoresDatos.size(); i++) {
                Elements JugadorDatos = JugadoresDatos.get(i).select("td");
                String JugadorInfo = JugadorDatos.get(0).text();
                ImageIcon JugadorImg = Utilidades.UrlIcon("https:" + JugadorDatos.get(1).select("img").attr("src"), 50, 50);
                ImageIcon JugadorBandera = Utilidades.bandera(JugadorDatos.get(2).select("span").attr("class"), 30, 30);
                String JugadorNombre = JugadorDatos.get(3).text();
                String JugadorUrl = Utilidades.url(JugadorDatos.get(3).select("a").attr("href"));
                Jugadores_Url.add(new String[]{JugadorNombre, JugadorUrl});
                String JugadorPos = JugadorDatos.get(4).text();
                String JugadorEdad = JugadorDatos.get(5).text();
                String JugadorPun = JugadorDatos.get(6).text();
                DatosJugadores.addRow(new Object[]{JugadorInfo, JugadorImg, JugadorBandera, JugadorNombre, JugadorPos, JugadorEdad, JugadorPun});
                DatosJugadores.setValueAt(JugadorImg, (i - 1), 1);
                DatosJugadores.setValueAt(JugadorBandera, (i - 1), 2);
            }
            Tbl_Equipo.setModel(DatosJugadores);
            Tbl_Equipo.setDefaultEditor(Object.class, null);
            Tbl_Equipo.getColumnModel().getColumn(1).setCellRenderer(Tbl_Equipo.getDefaultRenderer(ImageIcon.class));
            Tbl_Equipo.getColumnModel().getColumn(2).setCellRenderer(Tbl_Equipo.getDefaultRenderer(ImageIcon.class));
            Tbl_Equipo.setRowHeight(50);
            Tbl_Equipo.getColumnModel().getColumn(0).setPreferredWidth(5);
            Tbl_Equipo.getColumnModel().getColumn(1).setPreferredWidth(10);
            Tbl_Equipo.getColumnModel().getColumn(2).setPreferredWidth(10);
            Tbl_Equipo.getColumnModel().getColumn(5).setPreferredWidth(5);
            Tbl_Equipo.getColumnModel().getColumn(6).setPreferredWidth(5);
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            Tbl_Equipo.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            Tbl_Equipo.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
            Tbl_Equipo.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
            TableColumn Tbl_Equipo_col = Tbl_Equipo.getColumnModel().getColumn(3);
            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
            renderer.setForeground(Color.blue);
            Tbl_Equipo_col.setCellRenderer(renderer);
            Tbl_Equipo.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    int row = Tbl_Equipo.rowAtPoint(evt.getPoint());
                    int col = Tbl_Equipo.columnAtPoint(evt.getPoint());
                    if (row >= 0 && col == 3) {
                        String[] Jugador = Jugadores_Url.get(row);
                        Jugador(Jugador[0], Jugador[1]);
                    }
                }
            });
        }
    }

    public static void Entrenador(String nombre, String url) {
        Pnl_Tablero.removeAll();
        Pnl_Tablero.add(Pnl_Entrenador);
        Pnl_Tablero.repaint();
        Pnl_Tablero.revalidate();
        if (Utilidades.getEstadoPagina(url) == 200) {
            Document Html = Utilidades.getHtmlDocument(url);

            /*Nombre entrenador*/
            Txt_Entrenador.setText(nombre);
        }
    }

    public static void Jugador(String nombre, String url) {
        Pnl_Tablero.removeAll();
        Pnl_Tablero.add(Pnl_Jugador);
        Pnl_Tablero.repaint();
        Pnl_Tablero.revalidate();
        if (Utilidades.getEstadoPagina(url) == 200) {
            Document Html = Utilidades.getHtmlDocument(url);

            /*Nombre entrenador*/
            Txt_JugadorTitulo.setText(nombre);
        }
    }

    public static void Campo(String url) {
        Pnl_Tablero.removeAll();
        Pnl_Tablero.add(Pnl_Estadio);
        Pnl_Tablero.repaint();
        Pnl_Tablero.revalidate();
        if (Utilidades.getEstadoPagina(url) == 200) {
            Document Html = Utilidades.getHtmlDocument(url);

        }
    }

    public Main() {
        initComponents();
        inicio();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jOptionPane1 = new javax.swing.JOptionPane();
        Menu = new javax.swing.JPanel();
        Img_User = new javax.swing.JLabel();
        Txt_User = new javax.swing.JLabel();
        Btn_Inicio = new javax.swing.JButton();
        Btn_Ligas = new javax.swing.JButton();
        Btn_Equipos = new javax.swing.JButton();
        Btn_Paises = new javax.swing.JButton();
        Btn_Conf = new javax.swing.JButton();
        Btn_Exit = new javax.swing.JButton();
        Pnl_Tablero = new javax.swing.JPanel();
        Pnl_Inicio = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        Pnl_Ligas = new javax.swing.JPanel();
        Pnl_LigasPopulares = new javax.swing.JPanel();
        Pnl_CopasPopulares = new javax.swing.JPanel();
        Txt_Ligaspopu = new javax.swing.JLabel();
        Txt_CopasPopu = new javax.swing.JLabel();
        Pnl_Liga = new javax.swing.JPanel();
        Txt_Liga = new javax.swing.JLabel();
        Txt_Url = new javax.swing.JLabel();
        Txt_Patro = new javax.swing.JLabel();
        Txt_Año = new javax.swing.JLabel();
        Txt_Pais = new javax.swing.JLabel();
        Txt_PaisRes = new javax.swing.JLabel();
        Txt_AñoRes = new javax.swing.JLabel();
        Txt_Patro_Res = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tbl_Historico = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        Tbl_Equipos = new javax.swing.JTable();
        Txt_TituloEquipos = new javax.swing.JLabel();
        Btn_Historico = new javax.swing.JButton();
        Btn_Arbitros = new javax.swing.JButton();
        Pnl_Equipos = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        Pnl_Equipo = new javax.swing.JPanel();
        Txt_Equipo = new javax.swing.JLabel();
        Txt_Logo = new javax.swing.JLabel();
        Txt_Manager = new javax.swing.JLabel();
        Txt_ManagerRes = new javax.swing.JLabel();
        Txt_Apodo = new javax.swing.JLabel();
        Txt_ApodoRes = new javax.swing.JLabel();
        Txt_NombreMedio = new javax.swing.JLabel();
        Txt_NombreMedioRes = new javax.swing.JLabel();
        Txt_NombreCorto = new javax.swing.JLabel();
        Txt_NombreCortoRes = new javax.swing.JLabel();
        Txt_AñoFun = new javax.swing.JLabel();
        Txt_AñoFunRes = new javax.swing.JLabel();
        Txt_NombreEstadio = new javax.swing.JLabel();
        Txt_NombreEstadioRes = new javax.swing.JLabel();
        Txt_NomLiga = new javax.swing.JLabel();
        Txt_NomLigaRes = new javax.swing.JLabel();
        Txt_Ubicacion = new javax.swing.JLabel();
        Txt_UbicacionRes = new javax.swing.JLabel();
        Txt_PaisClub = new javax.swing.JLabel();
        Txt_PaisClubRes = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        Tbl_Equipo = new javax.swing.JTable();
        Btn_Camisetas = new javax.swing.JButton();
        Btn_Formación = new javax.swing.JButton();
        Btn_Historial = new javax.swing.JButton();
        Pnl_Entrenador = new javax.swing.JPanel();
        Txt_Entrenador = new javax.swing.JLabel();
        Txt_FotoEntrenador = new javax.swing.JLabel();
        Txt_EntClub = new javax.swing.JLabel();
        Txt_EntClubRes = new javax.swing.JLabel();
        Txt_EntRol = new javax.swing.JLabel();
        Txt_EntRolRes = new javax.swing.JLabel();
        Txt_EntInt = new javax.swing.JLabel();
        Txt_EntIntRes = new javax.swing.JLabel();
        Txt_EntFecha = new javax.swing.JLabel();
        Txt_EntFechaRes = new javax.swing.JLabel();
        Txt_EntPais = new javax.swing.JLabel();
        Txt_EntPaisRes = new javax.swing.JLabel();
        Pnl_Jugador = new javax.swing.JPanel();
        Txt_JugadorTitulo = new javax.swing.JLabel();
        Pnl_Estadio = new javax.swing.JPanel();
        Txt_FotoCampo = new javax.swing.JLabel();
        Txt_EstNom = new javax.swing.JLabel();
        Txt_EstNomRes = new javax.swing.JLabel();
        Txt_EstCap = new javax.swing.JLabel();
        Txt_EstCapRes = new javax.swing.JLabel();
        Txt_EstCiudad = new javax.swing.JLabel();
        Txt_EstCiudadRes = new javax.swing.JLabel();
        Txt_EstPais = new javax.swing.JLabel();
        Txt_EstPaisRes = new javax.swing.JLabel();
        Txt_EstEquipo = new javax.swing.JLabel();
        Txt_EstEquipoRes = new javax.swing.JLabel();
        Pnl_Paises = new javax.swing.JPanel();
        Pnl_Copa = new javax.swing.JPanel();
        Txt_LogoCopa = new javax.swing.JLabel();
        Txt_Copa = new javax.swing.JLabel();
        Txt_AñoFund = new javax.swing.JLabel();
        Txt_AñoFundRes = new javax.swing.JLabel();
        Txt_TipoCopa = new javax.swing.JLabel();
        Txt_TipoCopaRes = new javax.swing.JLabel();
        Txt_PaisCopa = new javax.swing.JLabel();
        Txt_PaisCopaRes = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        Tbl_Copa = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        Menu.setBackground(new java.awt.Color(255, 255, 255));

        Img_User.setText("Logo");

        Txt_User.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        Txt_User.setText("jLabel3");

        Btn_Inicio.setText("Home");
        Btn_Inicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_InicioActionPerformed(evt);
            }
        });

        Btn_Ligas.setText("Ligas");
        Btn_Ligas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_LigasActionPerformed(evt);
            }
        });

        Btn_Equipos.setText("Equipos");
        Btn_Equipos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_EquiposActionPerformed(evt);
            }
        });

        Btn_Paises.setText("Paises");
        Btn_Paises.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_PaisesActionPerformed(evt);
            }
        });

        Btn_Conf.setText("Configuration");
        Btn_Conf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_ConfActionPerformed(evt);
            }
        });

        Btn_Exit.setText("Exit");
        Btn_Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_ExitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout MenuLayout = new javax.swing.GroupLayout(Menu);
        Menu.setLayout(MenuLayout);
        MenuLayout.setHorizontalGroup(
            MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuLayout.createSequentialGroup()
                .addGroup(MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MenuLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Btn_Ligas)
                            .addComponent(Btn_Inicio)
                            .addComponent(Btn_Equipos)
                            .addComponent(Btn_Paises)
                            .addComponent(Btn_Conf)
                            .addComponent(Btn_Exit)))
                    .addGroup(MenuLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(Txt_User))
                    .addGroup(MenuLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(Img_User, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        MenuLayout.setVerticalGroup(
            MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(Img_User, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Txt_User)
                .addGap(18, 18, 18)
                .addComponent(Btn_Inicio)
                .addGap(18, 18, 18)
                .addComponent(Btn_Ligas)
                .addGap(18, 18, 18)
                .addComponent(Btn_Equipos)
                .addGap(18, 18, 18)
                .addComponent(Btn_Paises)
                .addGap(18, 18, 18)
                .addComponent(Btn_Conf)
                .addGap(18, 18, 18)
                .addComponent(Btn_Exit)
                .addContainerGap(109, Short.MAX_VALUE))
        );

        Pnl_Tablero.setLayout(new java.awt.CardLayout());

        jLabel1.setText("Inicio");

        javax.swing.GroupLayout Pnl_InicioLayout = new javax.swing.GroupLayout(Pnl_Inicio);
        Pnl_Inicio.setLayout(Pnl_InicioLayout);
        Pnl_InicioLayout.setHorizontalGroup(
            Pnl_InicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_InicioLayout.createSequentialGroup()
                .addGap(302, 302, 302)
                .addComponent(jLabel1)
                .addContainerGap(476, Short.MAX_VALUE))
        );
        Pnl_InicioLayout.setVerticalGroup(
            Pnl_InicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_InicioLayout.createSequentialGroup()
                .addGap(181, 181, 181)
                .addComponent(jLabel1)
                .addContainerGap(316, Short.MAX_VALUE))
        );

        Pnl_Tablero.add(Pnl_Inicio, "card2");

        Pnl_Ligas.setPreferredSize(new java.awt.Dimension(571, 558));

        Pnl_LigasPopulares.setPreferredSize(new java.awt.Dimension(550, 330));

        javax.swing.GroupLayout Pnl_LigasPopularesLayout = new javax.swing.GroupLayout(Pnl_LigasPopulares);
        Pnl_LigasPopulares.setLayout(Pnl_LigasPopularesLayout);
        Pnl_LigasPopularesLayout.setHorizontalGroup(
            Pnl_LigasPopularesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 550, Short.MAX_VALUE)
        );
        Pnl_LigasPopularesLayout.setVerticalGroup(
            Pnl_LigasPopularesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 172, Short.MAX_VALUE)
        );

        Pnl_CopasPopulares.setPreferredSize(new java.awt.Dimension(550, 330));

        javax.swing.GroupLayout Pnl_CopasPopularesLayout = new javax.swing.GroupLayout(Pnl_CopasPopulares);
        Pnl_CopasPopulares.setLayout(Pnl_CopasPopularesLayout);
        Pnl_CopasPopularesLayout.setHorizontalGroup(
            Pnl_CopasPopularesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 550, Short.MAX_VALUE)
        );
        Pnl_CopasPopularesLayout.setVerticalGroup(
            Pnl_CopasPopularesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 182, Short.MAX_VALUE)
        );

        Txt_Ligaspopu.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Txt_Ligaspopu.setText("Ligas");

        Txt_CopasPopu.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Txt_CopasPopu.setText("Copas");

        javax.swing.GroupLayout Pnl_LigasLayout = new javax.swing.GroupLayout(Pnl_Ligas);
        Pnl_Ligas.setLayout(Pnl_LigasLayout);
        Pnl_LigasLayout.setHorizontalGroup(
            Pnl_LigasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_LigasLayout.createSequentialGroup()
                .addGroup(Pnl_LigasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pnl_LigasLayout.createSequentialGroup()
                        .addGap(116, 116, 116)
                        .addComponent(Txt_Ligaspopu))
                    .addGroup(Pnl_LigasLayout.createSequentialGroup()
                        .addGap(89, 89, 89)
                        .addGroup(Pnl_LigasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Pnl_CopasPopulares, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(Pnl_LigasLayout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(Txt_CopasPopu))
                            .addComponent(Pnl_LigasPopulares, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(164, Short.MAX_VALUE))
        );
        Pnl_LigasLayout.setVerticalGroup(
            Pnl_LigasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pnl_LigasLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(Txt_Ligaspopu)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Pnl_LigasPopulares, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addComponent(Txt_CopasPopu)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Pnl_CopasPopulares, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
        );

        Pnl_Tablero.add(Pnl_Ligas, "card3");

        Txt_Liga.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Txt_Liga.setText("Liga");

        Txt_Url.setText("jLabel2");

        Txt_Patro.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        Txt_Patro.setText("jLabel2");

        Txt_Año.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        Txt_Año.setText("jLabel3");

        Txt_Pais.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        Txt_Pais.setText("jLabel4");

        Txt_PaisRes.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        Txt_PaisRes.setText("jLabel5");

        Txt_AñoRes.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        Txt_AñoRes.setText("jLabel6");

        Txt_Patro_Res.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        Txt_Patro_Res.setText("jLabel7");

        Tbl_Historico.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        Tbl_Historico.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(Tbl_Historico);

        Tbl_Equipos.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        Tbl_Equipos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(Tbl_Equipos);

        Txt_TituloEquipos.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Txt_TituloEquipos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Txt_TituloEquipos.setText("jLabel2");

        Btn_Historico.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        Btn_Historico.setText("Historico");
        Btn_Historico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_HistoricoActionPerformed(evt);
            }
        });

        Btn_Arbitros.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        Btn_Arbitros.setText("Arbitros");
        Btn_Arbitros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_ArbitrosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Pnl_LigaLayout = new javax.swing.GroupLayout(Pnl_Liga);
        Pnl_Liga.setLayout(Pnl_LigaLayout);
        Pnl_LigaLayout.setHorizontalGroup(
            Pnl_LigaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_LigaLayout.createSequentialGroup()
                .addGap(184, 184, 184)
                .addComponent(Txt_TituloEquipos)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(Pnl_LigaLayout.createSequentialGroup()
                .addGroup(Pnl_LigaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pnl_LigaLayout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(Pnl_LigaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(Pnl_LigaLayout.createSequentialGroup()
                                .addComponent(Txt_Url, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(52, 52, 52)
                                .addGroup(Pnl_LigaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Txt_Pais)
                                    .addComponent(Txt_Año)
                                    .addComponent(Txt_Patro))
                                .addGap(47, 47, 47)
                                .addGroup(Pnl_LigaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Txt_AñoRes)
                                    .addComponent(Txt_Patro_Res)
                                    .addComponent(Txt_PaisRes)))
                            .addComponent(Txt_Liga))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 192, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(Pnl_LigaLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jScrollPane2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pnl_LigaLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Btn_Historico)
                        .addGap(63, 63, 63)
                        .addComponent(Btn_Arbitros)
                        .addGap(21, 21, 21)))
                .addContainerGap())
        );
        Pnl_LigaLayout.setVerticalGroup(
            Pnl_LigaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_LigaLayout.createSequentialGroup()
                .addGroup(Pnl_LigaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Btn_Historico)
                    .addComponent(Btn_Arbitros))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Pnl_LigaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(Pnl_LigaLayout.createSequentialGroup()
                        .addComponent(Txt_Liga)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(Pnl_LigaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Txt_Url, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(Pnl_LigaLayout.createSequentialGroup()
                                .addComponent(Txt_Patro)
                                .addGap(18, 18, 18)
                                .addComponent(Txt_Año)
                                .addGap(18, 18, 18)
                                .addComponent(Txt_Pais))
                            .addGroup(Pnl_LigaLayout.createSequentialGroup()
                                .addComponent(Txt_Patro_Res)
                                .addGap(18, 18, 18)
                                .addComponent(Txt_AñoRes)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Txt_PaisRes))))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Txt_TituloEquipos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                .addContainerGap())
        );

        Pnl_Tablero.add(Pnl_Liga, "card7");

        jTextField1.setText("jTextField1");

        jButton1.setText("jButton1");

        jLabel2.setText("jLabel2");

        javax.swing.GroupLayout Pnl_EquiposLayout = new javax.swing.GroupLayout(Pnl_Equipos);
        Pnl_Equipos.setLayout(Pnl_EquiposLayout);
        Pnl_EquiposLayout.setHorizontalGroup(
            Pnl_EquiposLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_EquiposLayout.createSequentialGroup()
                .addGap(261, 261, 261)
                .addGroup(Pnl_EquiposLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap(315, Short.MAX_VALUE))
        );
        Pnl_EquiposLayout.setVerticalGroup(
            Pnl_EquiposLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_EquiposLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(Pnl_EquiposLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addContainerGap(435, Short.MAX_VALUE))
        );

        Pnl_Tablero.add(Pnl_Equipos, "card4");

        Txt_Equipo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Txt_Equipo.setText("Equipo");

        Txt_Logo.setText("jLabel2");

        Txt_Manager.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        Txt_Manager.setText("Manager");

        Txt_ManagerRes.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        Txt_ManagerRes.setText("jLabel7");

        Txt_Apodo.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        Txt_Apodo.setText("Apodo");

        Txt_ApodoRes.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        Txt_ApodoRes.setText("jLabel9");

        Txt_NombreMedio.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        Txt_NombreMedio.setText("Medio");

        Txt_NombreMedioRes.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        Txt_NombreMedioRes.setText("jLabel10");

        Txt_NombreCorto.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        Txt_NombreCorto.setText("NomCorto");

        Txt_NombreCortoRes.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        Txt_NombreCortoRes.setText("jLabel11");

        Txt_AñoFun.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        Txt_AñoFun.setText("AñoFun");

        Txt_AñoFunRes.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        Txt_AñoFunRes.setText("jLabel12");

        Txt_NombreEstadio.setText("Estadio");

        Txt_NombreEstadioRes.setText("jLabel17");

        Txt_NomLiga.setText("Liga");

        Txt_NomLigaRes.setText("jLabel18");

        Txt_Ubicacion.setText("Ubicacion");

        Txt_UbicacionRes.setText("jLabel19");

        Txt_PaisClub.setText("Pais");

        Txt_PaisClubRes.setText("jLabel20");

        Tbl_Equipo.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        Tbl_Equipo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(Tbl_Equipo);

        Btn_Camisetas.setText("jButton1");

        Btn_Formación.setText("jButton2");

        Btn_Historial.setText("jButton3");

        javax.swing.GroupLayout Pnl_EquipoLayout = new javax.swing.GroupLayout(Pnl_Equipo);
        Pnl_Equipo.setLayout(Pnl_EquipoLayout);
        Pnl_EquipoLayout.setHorizontalGroup(
            Pnl_EquipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_EquipoLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 715, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
            .addGroup(Pnl_EquipoLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(Pnl_EquipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Txt_Logo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Txt_Equipo))
                .addGap(28, 28, 28)
                .addGroup(Pnl_EquipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Txt_Manager)
                    .addComponent(Txt_Apodo)
                    .addComponent(Txt_NombreMedio)
                    .addComponent(Txt_NombreCorto)
                    .addComponent(Txt_AñoFun))
                .addGap(18, 18, 18)
                .addGroup(Pnl_EquipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Txt_ApodoRes)
                    .addComponent(Txt_NombreMedioRes)
                    .addComponent(Txt_NombreCortoRes)
                    .addComponent(Txt_ManagerRes)
                    .addComponent(Txt_AñoFunRes))
                .addGap(27, 27, 27)
                .addGroup(Pnl_EquipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pnl_EquipoLayout.createSequentialGroup()
                        .addComponent(Txt_Ubicacion)
                        .addGap(18, 18, 18)
                        .addComponent(Txt_UbicacionRes))
                    .addGroup(Pnl_EquipoLayout.createSequentialGroup()
                        .addComponent(Txt_PaisClub)
                        .addGap(18, 18, 18)
                        .addComponent(Txt_PaisClubRes))
                    .addGroup(Pnl_EquipoLayout.createSequentialGroup()
                        .addComponent(Txt_NomLiga)
                        .addGap(18, 18, 18)
                        .addComponent(Txt_NomLigaRes))
                    .addGroup(Pnl_EquipoLayout.createSequentialGroup()
                        .addComponent(Txt_NombreEstadio)
                        .addGap(18, 18, 18)
                        .addComponent(Txt_NombreEstadioRes)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(Pnl_EquipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Btn_Camisetas, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Btn_Formación, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Btn_Historial, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(43, 43, 43))
        );
        Pnl_EquipoLayout.setVerticalGroup(
            Pnl_EquipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_EquipoLayout.createSequentialGroup()
                .addGroup(Pnl_EquipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pnl_EquipoLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(Btn_Camisetas)
                        .addGap(32, 32, 32)
                        .addComponent(Btn_Formación)
                        .addGap(26, 26, 26)
                        .addComponent(Btn_Historial))
                    .addGroup(Pnl_EquipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(Pnl_EquipoLayout.createSequentialGroup()
                            .addGap(6, 6, 6)
                            .addComponent(Txt_Equipo)
                            .addGap(18, 18, 18)
                            .addComponent(Txt_Logo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pnl_EquipoLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(Pnl_EquipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pnl_EquipoLayout.createSequentialGroup()
                                    .addComponent(Txt_ManagerRes)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(Txt_ApodoRes)
                                    .addGap(18, 18, 18)
                                    .addComponent(Txt_NombreMedioRes)
                                    .addGap(18, 18, 18)
                                    .addComponent(Txt_NombreCortoRes)
                                    .addGap(18, 18, 18)
                                    .addComponent(Txt_AñoFunRes))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pnl_EquipoLayout.createSequentialGroup()
                                    .addGap(0, 7, Short.MAX_VALUE)
                                    .addGroup(Pnl_EquipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pnl_EquipoLayout.createSequentialGroup()
                                            .addComponent(Txt_Manager)
                                            .addGap(24, 24, 24)
                                            .addGroup(Pnl_EquipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pnl_EquipoLayout.createSequentialGroup()
                                                    .addComponent(Txt_Apodo)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(Txt_NombreMedio)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(Txt_NombreCorto)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(Txt_AñoFun))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pnl_EquipoLayout.createSequentialGroup()
                                                    .addGroup(Pnl_EquipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(Txt_NomLiga)
                                                        .addComponent(Txt_NomLigaRes))
                                                    .addGap(18, 18, 18)
                                                    .addGroup(Pnl_EquipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(Txt_Ubicacion)
                                                        .addComponent(Txt_UbicacionRes))
                                                    .addGap(18, 18, 18)
                                                    .addGroup(Pnl_EquipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(Txt_PaisClub)
                                                        .addComponent(Txt_PaisClubRes)))))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pnl_EquipoLayout.createSequentialGroup()
                                            .addComponent(Txt_NombreEstadio, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(110, 110, 110))
                                        .addComponent(Txt_NombreEstadioRes, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addGap(27, 27, 27)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                .addContainerGap())
        );

        Pnl_Tablero.add(Pnl_Equipo, "card8");

        Txt_Entrenador.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Txt_Entrenador.setText("Entrenador");

        Txt_FotoEntrenador.setText("jLabel2");

        Txt_EntClub.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Txt_EntClub.setText("jLabel2");

        Txt_EntClubRes.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Txt_EntClubRes.setText("jLabel7");

        Txt_EntRol.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Txt_EntRol.setText("jLabel3");

        Txt_EntRolRes.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Txt_EntRolRes.setText("jLabel8");

        Txt_EntInt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Txt_EntInt.setText("jLabel4");

        Txt_EntIntRes.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Txt_EntIntRes.setText("jLabel9");

        Txt_EntFecha.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Txt_EntFecha.setText("jLabel5");

        Txt_EntFechaRes.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Txt_EntFechaRes.setText("jLabel10");

        Txt_EntPais.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Txt_EntPais.setText("jLabel6");

        Txt_EntPaisRes.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Txt_EntPaisRes.setText("jLabel11");

        javax.swing.GroupLayout Pnl_EntrenadorLayout = new javax.swing.GroupLayout(Pnl_Entrenador);
        Pnl_Entrenador.setLayout(Pnl_EntrenadorLayout);
        Pnl_EntrenadorLayout.setHorizontalGroup(
            Pnl_EntrenadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_EntrenadorLayout.createSequentialGroup()
                .addGroup(Pnl_EntrenadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pnl_EntrenadorLayout.createSequentialGroup()
                        .addGap(130, 130, 130)
                        .addComponent(Txt_FotoEntrenador, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(188, 188, 188)
                        .addGroup(Pnl_EntrenadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(Pnl_EntrenadorLayout.createSequentialGroup()
                                .addComponent(Txt_EntClub)
                                .addGap(18, 18, 18)
                                .addComponent(Txt_EntClubRes))
                            .addGroup(Pnl_EntrenadorLayout.createSequentialGroup()
                                .addComponent(Txt_EntRol)
                                .addGap(18, 18, 18)
                                .addComponent(Txt_EntRolRes))
                            .addGroup(Pnl_EntrenadorLayout.createSequentialGroup()
                                .addComponent(Txt_EntInt)
                                .addGap(18, 18, 18)
                                .addComponent(Txt_EntIntRes))
                            .addGroup(Pnl_EntrenadorLayout.createSequentialGroup()
                                .addComponent(Txt_EntFecha)
                                .addGap(18, 18, 18)
                                .addComponent(Txt_EntFechaRes))
                            .addGroup(Pnl_EntrenadorLayout.createSequentialGroup()
                                .addComponent(Txt_EntPais)
                                .addGap(18, 18, 18)
                                .addComponent(Txt_EntPaisRes))))
                    .addGroup(Pnl_EntrenadorLayout.createSequentialGroup()
                        .addGap(313, 313, 313)
                        .addComponent(Txt_Entrenador)))
                .addContainerGap(243, Short.MAX_VALUE))
        );
        Pnl_EntrenadorLayout.setVerticalGroup(
            Pnl_EntrenadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_EntrenadorLayout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(Txt_Entrenador)
                .addGap(86, 86, 86)
                .addGroup(Pnl_EntrenadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Txt_EntClub)
                    .addComponent(Txt_EntClubRes))
                .addGroup(Pnl_EntrenadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pnl_EntrenadorLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(Pnl_EntrenadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Txt_EntRol)
                            .addComponent(Txt_EntRolRes))
                        .addGap(18, 18, 18)
                        .addGroup(Pnl_EntrenadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Txt_EntInt)
                            .addComponent(Txt_EntIntRes))
                        .addGap(18, 18, 18)
                        .addGroup(Pnl_EntrenadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Txt_EntFecha)
                            .addComponent(Txt_EntFechaRes))
                        .addGroup(Pnl_EntrenadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(Pnl_EntrenadorLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Txt_EntPaisRes))
                            .addGroup(Pnl_EntrenadorLayout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(Txt_EntPais))))
                    .addGroup(Pnl_EntrenadorLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(Txt_FotoEntrenador, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(169, Short.MAX_VALUE))
        );

        Pnl_Tablero.add(Pnl_Entrenador, "card10");

        Txt_JugadorTitulo.setText("jLabel3");

        javax.swing.GroupLayout Pnl_JugadorLayout = new javax.swing.GroupLayout(Pnl_Jugador);
        Pnl_Jugador.setLayout(Pnl_JugadorLayout);
        Pnl_JugadorLayout.setHorizontalGroup(
            Pnl_JugadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_JugadorLayout.createSequentialGroup()
                .addGap(167, 167, 167)
                .addComponent(Txt_JugadorTitulo)
                .addContainerGap(602, Short.MAX_VALUE))
        );
        Pnl_JugadorLayout.setVerticalGroup(
            Pnl_JugadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_JugadorLayout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(Txt_JugadorTitulo)
                .addContainerGap(439, Short.MAX_VALUE))
        );

        Pnl_Tablero.add(Pnl_Jugador, "card12");

        Txt_FotoCampo.setText("jLabel2");

        Txt_EstNom.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Txt_EstNom.setText("jLabel2");

        Txt_EstNomRes.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Txt_EstNomRes.setText("jLabel7");

        Txt_EstCap.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Txt_EstCap.setText("jLabel3");

        Txt_EstCapRes.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Txt_EstCapRes.setText("jLabel8");

        Txt_EstCiudad.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Txt_EstCiudad.setText("jLabel4");

        Txt_EstCiudadRes.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Txt_EstCiudadRes.setText("jLabel9");

        Txt_EstPais.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Txt_EstPais.setText("jLabel5");

        Txt_EstPaisRes.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Txt_EstPaisRes.setText("jLabel10");

        Txt_EstEquipo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Txt_EstEquipo.setText("jLabel6");

        Txt_EstEquipoRes.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Txt_EstEquipoRes.setText("jLabel11");

        javax.swing.GroupLayout Pnl_EstadioLayout = new javax.swing.GroupLayout(Pnl_Estadio);
        Pnl_Estadio.setLayout(Pnl_EstadioLayout);
        Pnl_EstadioLayout.setHorizontalGroup(
            Pnl_EstadioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_EstadioLayout.createSequentialGroup()
                .addGap(137, 137, 137)
                .addComponent(Txt_FotoCampo, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(88, 88, 88)
                .addGroup(Pnl_EstadioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pnl_EstadioLayout.createSequentialGroup()
                        .addComponent(Txt_EstNom)
                        .addGap(18, 18, 18)
                        .addComponent(Txt_EstNomRes))
                    .addGroup(Pnl_EstadioLayout.createSequentialGroup()
                        .addComponent(Txt_EstCap)
                        .addGap(18, 18, 18)
                        .addComponent(Txt_EstCapRes))
                    .addGroup(Pnl_EstadioLayout.createSequentialGroup()
                        .addComponent(Txt_EstCiudad)
                        .addGap(18, 18, 18)
                        .addComponent(Txt_EstCiudadRes))
                    .addGroup(Pnl_EstadioLayout.createSequentialGroup()
                        .addComponent(Txt_EstPais)
                        .addGap(18, 18, 18)
                        .addComponent(Txt_EstPaisRes))
                    .addGroup(Pnl_EstadioLayout.createSequentialGroup()
                        .addComponent(Txt_EstEquipo)
                        .addGap(18, 18, 18)
                        .addComponent(Txt_EstEquipoRes)))
                .addContainerGap(146, Short.MAX_VALUE))
        );
        Pnl_EstadioLayout.setVerticalGroup(
            Pnl_EstadioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_EstadioLayout.createSequentialGroup()
                .addGap(116, 116, 116)
                .addGroup(Pnl_EstadioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Txt_FotoCampo, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(Pnl_EstadioLayout.createSequentialGroup()
                        .addGroup(Pnl_EstadioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Txt_EstNom)
                            .addComponent(Txt_EstNomRes))
                        .addGap(18, 18, 18)
                        .addGroup(Pnl_EstadioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Txt_EstCap)
                            .addComponent(Txt_EstCapRes))
                        .addGap(21, 21, 21)
                        .addGroup(Pnl_EstadioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Txt_EstCiudad)
                            .addComponent(Txt_EstCiudadRes))
                        .addGap(18, 18, 18)
                        .addGroup(Pnl_EstadioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Txt_EstPaisRes)
                            .addComponent(Txt_EstPais))
                        .addGap(18, 18, 18)
                        .addGroup(Pnl_EstadioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Txt_EstEquipoRes)
                            .addComponent(Txt_EstEquipo))))
                .addContainerGap(156, Short.MAX_VALUE))
        );

        Pnl_Tablero.add(Pnl_Estadio, "card11");

        javax.swing.GroupLayout Pnl_PaisesLayout = new javax.swing.GroupLayout(Pnl_Paises);
        Pnl_Paises.setLayout(Pnl_PaisesLayout);
        Pnl_PaisesLayout.setHorizontalGroup(
            Pnl_PaisesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 803, Short.MAX_VALUE)
        );
        Pnl_PaisesLayout.setVerticalGroup(
            Pnl_PaisesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 511, Short.MAX_VALUE)
        );

        Pnl_Tablero.add(Pnl_Paises, "card5");

        Txt_LogoCopa.setText("jLabel2");

        Txt_Copa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Txt_Copa.setText("Copa");

        Txt_AñoFund.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        Txt_AñoFund.setText("Año fun");

        Txt_AñoFundRes.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        Txt_AñoFundRes.setText("jLabel7");

        Txt_TipoCopa.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        Txt_TipoCopa.setText("Tipo");

        Txt_TipoCopaRes.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        Txt_TipoCopaRes.setText("jLabel6");

        Txt_PaisCopa.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        Txt_PaisCopa.setText("Pais");

        Txt_PaisCopaRes.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        Txt_PaisCopaRes.setText("jLabel5");

        Tbl_Copa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(Tbl_Copa);

        javax.swing.GroupLayout Pnl_CopaLayout = new javax.swing.GroupLayout(Pnl_Copa);
        Pnl_Copa.setLayout(Pnl_CopaLayout);
        Pnl_CopaLayout.setHorizontalGroup(
            Pnl_CopaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_CopaLayout.createSequentialGroup()
                .addContainerGap(82, Short.MAX_VALUE)
                .addGroup(Pnl_CopaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pnl_CopaLayout.createSequentialGroup()
                        .addComponent(Txt_LogoCopa, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52)
                        .addGroup(Pnl_CopaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Txt_PaisCopa)
                            .addComponent(Txt_TipoCopa)
                            .addComponent(Txt_AñoFund))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(Pnl_CopaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Txt_TipoCopaRes)
                            .addComponent(Txt_AñoFundRes)
                            .addComponent(Txt_PaisCopaRes)))
                    .addComponent(Txt_Copa))
                .addGap(64, 64, 64)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45))
        );
        Pnl_CopaLayout.setVerticalGroup(
            Pnl_CopaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_CopaLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(Pnl_CopaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                    .addGroup(Pnl_CopaLayout.createSequentialGroup()
                        .addGap(139, 139, 139)
                        .addComponent(Txt_Copa)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(Pnl_CopaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Txt_LogoCopa, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(Pnl_CopaLayout.createSequentialGroup()
                                .addGroup(Pnl_CopaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(Txt_AñoFund)
                                    .addComponent(Txt_AñoFundRes))
                                .addGap(18, 18, 18)
                                .addGroup(Pnl_CopaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(Txt_TipoCopa)
                                    .addComponent(Txt_TipoCopaRes))
                                .addGroup(Pnl_CopaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(Pnl_CopaLayout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(Txt_PaisCopa))
                                    .addGroup(Pnl_CopaLayout.createSequentialGroup()
                                        .addGap(8, 8, 8)
                                        .addComponent(Txt_PaisCopaRes)))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        Pnl_Tablero.add(Pnl_Copa, "card9");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(Menu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Pnl_Tablero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Menu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Pnl_Tablero, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void Btn_InicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_InicioActionPerformed
        Pnl_Tablero.removeAll();
        Pnl_Tablero.add(Pnl_Inicio);
        Pnl_Tablero.repaint();
        Pnl_Tablero.revalidate();
    }//GEN-LAST:event_Btn_InicioActionPerformed

    private void Btn_LigasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_LigasActionPerformed
        Pnl_Tablero.removeAll();
        Pnl_Tablero.add(Pnl_Ligas);
        Pnl_Tablero.repaint();
        Pnl_Tablero.revalidate();
        Ligas();
    }//GEN-LAST:event_Btn_LigasActionPerformed

    private void Btn_EquiposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_EquiposActionPerformed
        Pnl_Tablero.removeAll();
        Pnl_Tablero.add(Pnl_Equipos);
        Pnl_Tablero.repaint();
        Pnl_Tablero.revalidate();
    }//GEN-LAST:event_Btn_EquiposActionPerformed

    private void Btn_PaisesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_PaisesActionPerformed
        Pnl_Tablero.removeAll();
        Pnl_Tablero.add(Pnl_Paises);
        Pnl_Tablero.repaint();
        Pnl_Tablero.revalidate();
    }//GEN-LAST:event_Btn_PaisesActionPerformed

    private void Btn_ConfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_ConfActionPerformed
        String[] lang = {"Español", "English", "Deutsch"};
        String select_lang = (String) JOptionPane.showInputDialog(null, "Select a language", "Configuration", JOptionPane.DEFAULT_OPTION, Utilidades.UrlIcon("https://i.imgur.com/ocO048D.png", 50, 50), lang, lang[0]);
        if (select_lang != null) {
            switch (select_lang) {
                case "Español":
                    Conexion.ModificarTabla(conexion, "UPDATE Users SET lang='1' WHERE username = '" + User + "'");
                    Main.lang = "es";

                    break;
                case "English":
                    Conexion.ModificarTabla(conexion, "UPDATE Users SET lang='2' WHERE username = '" + User + "'");
                    Main.lang = "en";
                    break;
                case "Deutsch":
                    Conexion.ModificarTabla(conexion, "UPDATE Users SET lang='3' WHERE username = '" + User + "'");
                    Main.lang = "de";
                    break;
            }
            Pnl_Tablero.removeAll();
            Pnl_Tablero.add(Pnl_Inicio);
            Pnl_Tablero.repaint();
            Pnl_Tablero.revalidate();
            inicio();
        }
    }//GEN-LAST:event_Btn_ConfActionPerformed

    private void Btn_ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_ExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_Btn_ExitActionPerformed

    private void Btn_ArbitrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_ArbitrosActionPerformed
        /*Tabla Arbitros*/
        Document Html = Utilidades.getHtmlDocument(url);
        Elements Arbitro = Html.select(".Border table table");
        DefaultTableModel DatosArbitros = new DefaultTableModel();
        DatosArbitros.addColumn("");
        DatosArbitros.addColumn(Arbitro.get(0).select("th").get(0).text());
        DatosArbitros.addColumn(Arbitro.get(0).select("th").get(1).text());
        ArrayList<String[]> Arbitros_metadate = new ArrayList<String[]>();
        for (int i = 0; i < Arbitro.size(); i++) {
            Elements Arbitros_foto = Html.select(".Border table img");
            ImageIcon Arbitro_Img = null;
            if (Arbitros_foto.size() > i) {
                String Arbitro_foto = Arbitros_foto.get(i).attr("src");
                Arbitro_Img = Utilidades.UrlIcon("https:" + Arbitro_foto, 60, 60);
            }
            String Arbitro_Nombre = Arbitro.get(i).select("td").get(0).text();
            String Arbitro_url = Arbitro.get(i).select("td").get(0).select("a").attr("href");
            String Arbitro_Nacionalidad = Arbitro.get(i).select("td").get(1).text();
            String Arbitro_Nacionalidad_code = Arbitro.get(i).select("td span").attr("class");
            DatosArbitros.addRow(new Object[]{Arbitro_Img, Arbitro_Nombre, Arbitro_Nacionalidad});
            DatosArbitros.setValueAt(Arbitro_Img, i, 0);
            Arbitros_metadate.add(new String[]{Arbitro_url, Arbitro_Nacionalidad_code});
        }
        Tbl_Historico.setModel(DatosArbitros);
        Tbl_Historico.setDefaultEditor(Object.class, null);
        Tbl_Historico.getColumnModel().getColumn(0).setCellRenderer(Tbl_Historico.getDefaultRenderer(ImageIcon.class));
        Tbl_Historico.setRowHeight(60);
        Tbl_Historico.getColumnModel().getColumn(0).setPreferredWidth(20);
    }//GEN-LAST:event_Btn_ArbitrosActionPerformed

    private void Btn_HistoricoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_HistoricoActionPerformed
        Document Html = Utilidades.getHtmlDocument(url);
        Elements Year = Html.select("#leaguewinnersdiv table th");
        Elements Team = Html.select("#leaguewinnersdiv table td");
        DefaultTableModel dtm = new DefaultTableModel();
        dtm.addColumn("year");
        dtm.addColumn("Team");
        Tbl_Historico.setModel(dtm);
        ArrayList<String[]> Historico_Url = new ArrayList<String[]>();
        for (int i = 0; i < Year.size(); i++) {
            dtm.addRow(new Object[]{Year.get(i).text(), Team.get(i).text()});
            Historico_Url.add(new String[]{Team.get(i).text(), Utilidades.url(Team.get(i).select("a").attr("href"))});
        }
        TableColumn Tbl_Historico_col = Tbl_Historico.getColumnModel().getColumn(1);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setForeground(Color.blue);
        Tbl_Historico_col.setCellRenderer(renderer);
        Tbl_Historico.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = Tbl_Historico.rowAtPoint(evt.getPoint());
                int col = Tbl_Historico.columnAtPoint(evt.getPoint());
                if (row >= 0 && (col >= 1 && col <= 3)) {
                    String[] Equipo = Historico_Url.get(row);
                    Equipo(Equipo[0], Equipo[1]);
                }
            }
        });
        Tbl_Historico.setRowHeight(16);
        Tbl_Historico.setDefaultEditor(Object.class, null);
    }//GEN-LAST:event_Btn_HistoricoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JButton Btn_Arbitros;
    private javax.swing.JButton Btn_Camisetas;
    static javax.swing.JButton Btn_Conf;
    static javax.swing.JButton Btn_Equipos;
    static javax.swing.JButton Btn_Exit;
    private javax.swing.JButton Btn_Formación;
    private javax.swing.JButton Btn_Historial;
    private static javax.swing.JButton Btn_Historico;
    static javax.swing.JButton Btn_Inicio;
    static javax.swing.JButton Btn_Ligas;
    static javax.swing.JButton Btn_Paises;
    static javax.swing.JLabel Img_User;
    private javax.swing.JPanel Menu;
    private static javax.swing.JPanel Pnl_Copa;
    private static javax.swing.JPanel Pnl_CopasPopulares;
    private static javax.swing.JPanel Pnl_Entrenador;
    private static javax.swing.JPanel Pnl_Equipo;
    private static javax.swing.JPanel Pnl_Equipos;
    private static javax.swing.JPanel Pnl_Estadio;
    private static javax.swing.JPanel Pnl_Inicio;
    private static javax.swing.JPanel Pnl_Jugador;
    private static javax.swing.JPanel Pnl_Liga;
    private static javax.swing.JPanel Pnl_Ligas;
    private static javax.swing.JPanel Pnl_LigasPopulares;
    private static javax.swing.JPanel Pnl_Paises;
    private static javax.swing.JPanel Pnl_Tablero;
    private static javax.swing.JTable Tbl_Copa;
    private static javax.swing.JTable Tbl_Equipo;
    private static javax.swing.JTable Tbl_Equipos;
    private static javax.swing.JTable Tbl_Historico;
    private static javax.swing.JLabel Txt_Apodo;
    private static javax.swing.JLabel Txt_ApodoRes;
    private static javax.swing.JLabel Txt_Año;
    private static javax.swing.JLabel Txt_AñoFun;
    private static javax.swing.JLabel Txt_AñoFunRes;
    private static javax.swing.JLabel Txt_AñoFund;
    private static javax.swing.JLabel Txt_AñoFundRes;
    private static javax.swing.JLabel Txt_AñoRes;
    private static javax.swing.JLabel Txt_Copa;
    private static javax.swing.JLabel Txt_CopasPopu;
    private static javax.swing.JLabel Txt_EntClub;
    private static javax.swing.JLabel Txt_EntClubRes;
    private static javax.swing.JLabel Txt_EntFecha;
    private static javax.swing.JLabel Txt_EntFechaRes;
    private static javax.swing.JLabel Txt_EntInt;
    private static javax.swing.JLabel Txt_EntIntRes;
    private static javax.swing.JLabel Txt_EntPais;
    private static javax.swing.JLabel Txt_EntPaisRes;
    private static javax.swing.JLabel Txt_EntRol;
    private static javax.swing.JLabel Txt_EntRolRes;
    private static javax.swing.JLabel Txt_Entrenador;
    private static javax.swing.JLabel Txt_Equipo;
    private static javax.swing.JLabel Txt_EstCap;
    private static javax.swing.JLabel Txt_EstCapRes;
    private static javax.swing.JLabel Txt_EstCiudad;
    private static javax.swing.JLabel Txt_EstCiudadRes;
    private static javax.swing.JLabel Txt_EstEquipo;
    private static javax.swing.JLabel Txt_EstEquipoRes;
    private static javax.swing.JLabel Txt_EstNom;
    private static javax.swing.JLabel Txt_EstNomRes;
    private static javax.swing.JLabel Txt_EstPais;
    private static javax.swing.JLabel Txt_EstPaisRes;
    private static javax.swing.JLabel Txt_FotoCampo;
    private static javax.swing.JLabel Txt_FotoEntrenador;
    private static javax.swing.JLabel Txt_JugadorTitulo;
    private static javax.swing.JLabel Txt_Liga;
    private static javax.swing.JLabel Txt_Ligaspopu;
    private static javax.swing.JLabel Txt_Logo;
    private static javax.swing.JLabel Txt_LogoCopa;
    private static javax.swing.JLabel Txt_Manager;
    private static javax.swing.JLabel Txt_ManagerRes;
    private static javax.swing.JLabel Txt_NomLiga;
    private static javax.swing.JLabel Txt_NomLigaRes;
    private static javax.swing.JLabel Txt_NombreCorto;
    private static javax.swing.JLabel Txt_NombreCortoRes;
    private static javax.swing.JLabel Txt_NombreEstadio;
    private static javax.swing.JLabel Txt_NombreEstadioRes;
    private static javax.swing.JLabel Txt_NombreMedio;
    private static javax.swing.JLabel Txt_NombreMedioRes;
    private static javax.swing.JLabel Txt_Pais;
    private static javax.swing.JLabel Txt_PaisClub;
    private static javax.swing.JLabel Txt_PaisClubRes;
    private static javax.swing.JLabel Txt_PaisCopa;
    private static javax.swing.JLabel Txt_PaisCopaRes;
    private static javax.swing.JLabel Txt_PaisRes;
    private static javax.swing.JLabel Txt_Patro;
    private static javax.swing.JLabel Txt_Patro_Res;
    private static javax.swing.JLabel Txt_TipoCopa;
    private static javax.swing.JLabel Txt_TipoCopaRes;
    private static javax.swing.JLabel Txt_TituloEquipos;
    private static javax.swing.JLabel Txt_Ubicacion;
    private static javax.swing.JLabel Txt_UbicacionRes;
    private static javax.swing.JLabel Txt_Url;
    static javax.swing.JLabel Txt_User;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JOptionPane jOptionPane1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

    public static void inicio() {
        if (Utilidades.getEstadoPagina("https://" + lang + ".soccerwiki.org/league.php") == 200) {
            Document Html = Utilidades.getHtmlDocument("https://" + lang + ".soccerwiki.org");
            Elements Texts = Html.select("#wrapper_menu li");
            Img_User.setText("");
            Img_User.setIcon(Utilidades.UrlIcon(Avatar, 100, 100));
            Txt_User.setText(User);
            Btn_Ligas.setText(Texts.get(3).text());
            Btn_Equipos.setText(Texts.get(4).text());
        }
    }
}
