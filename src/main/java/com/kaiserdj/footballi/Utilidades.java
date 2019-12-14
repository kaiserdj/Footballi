/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kaiserdj.footballi;

import static com.kaiserdj.footballi.Main.lang;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author gonfe
 */
public class Utilidades {

    /*Banderas --> https://www.flaticon.es/packs/international-flags-6*/
    public static ImageIcon bandera(String code, int x, int y) {
        switch (code) {
            case "flagESP":
                return UrlIcon("https://i.imgur.com/fp4m2o0.png", x, y);
            case "flagGER":
                return UrlIcon("https://i.imgur.com/AJxeJPl.png", x, y);
            case "flagENG":
                return UrlIcon("https://i.imgur.com/3E9aLvw.png", x, y);
            case "flagARG":
                return UrlIcon("https://i.imgur.com/FlaPzGS.png", x, y);
            case "flagNED":
                return UrlIcon("https://i.imgur.com/Zl8Z1oc.png", x, y);
            case "flagBRA":
                return UrlIcon("https://i.imgur.com/UJvEB5i.png", x, y);
            case "flagGRE":
                return UrlIcon("https://i.imgur.com/ytlyAzr.png", x, y);
            case "flagPOR":
                return UrlIcon("https://i.imgur.com/LWBGsxT.png", x, y);
            case "flagTUR":
                return UrlIcon("https://i.imgur.com/G5a8X5s.png", x, y);
            case "flagITA":
                return UrlIcon("https://i.imgur.com/dlnUhGx.png", x, y);
            case "flagRUS":
                return UrlIcon("https://i.imgur.com/5BoViT8.png", x, y);
            case "flagUKR":
                return UrlIcon("https://i.imgur.com/Jtm4f5T.png", x, y);
            case "flagFRA":
                return UrlIcon("https://i.imgur.com/DTyeq4w.png", x, y);
            case "flagJPN":
                return UrlIcon("https://i.imgur.com/YoFpbzi.png", x, y);
            case "flagSCO":
                return UrlIcon("https://i.imgur.com/zyrISkJ.png", x, y);
            case "flagUSA":
                return UrlIcon("https://i.imgur.com/fkPDtwE.png", x, y);

            default:
                return UrlIcon("https://i.imgur.com/dCszRDS.png", x, y);

        }
    }

    static String url(String url) {
        return "http://" + lang + ".soccerwiki.org/" + url;
    }

    static String urlDetect(String url, String data) {
        if (url.substring(0, 4).equals("http")) {
            if (url.substring(0, 26).equals("http://" + lang + ".soccerwiki.org//")) {
                url = url.substring(26);
            } else if (url.substring(0, 27).equals("https://" + lang + ".soccerwiki.org//")) {
                url = url.substring(27);
            } else if (url.substring(0, 25).equals("http://" + lang + ".soccerwiki.org/")) {
                url = url.substring(25);
            } else if (url.substring(0, 26).equals("https://" + lang + ".soccerwiki.org/")) {
                url = url.substring(26);
            }
        }
        int size = url.length();
        url = url + "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$";
        if (url.substring(0, 20).equals("league.php?leagueid=")) {
            System.out.println("Liga");
        } else if (url.substring(0, 14).equals("cup.php?cupid=")) {
            System.out.println("Copa");
        } else if (url.substring(0, 17).equals("squad.php?clubid=")) {
            System.out.println("Equipo");
        } else if (url.substring(0, 15).equals("player.php?pid=")) {
            System.out.println("Jugador");
        } else if (url.substring(0, 25).equals("football-manager.php?mid=")) {
            System.out.println("Entrenador");
        } else if (url.substring(0, 23).equals("stadium.php?stadiumdid=")) {
            System.out.println("Estadio");
        } else if (url.substring(0, 41).equals("wiki.php?action=countryProfile&countryId=")) {
            System.out.println("Pais");
        } else if (url.substring(0, 40).equals("wiki.php?action=search&searchType=all&q=")) {
            System.out.println("Busqueda generica");
        } else if (url.substring(0, 40).equals("wiki.php?action=search&searchType=all&q=")) {
            System.out.println("Busqueda generica");
        } else if (url.substring(0, 25).equals("wiki.php?action=search&q=")) {
            String url2 = url.substring(25);
            url2 = url2.substring(url2.indexOf("&"));
            if (url2.substring(0, 17).equals("&searchType=clubs")) {
                System.out.println("Busqueda club");
            } else if (url2.substring(0, 19).equals("&searchType=players")) {
                System.out.println("Busqueda jugador");
            } else if (url2.substring(0, 21).equals("&searchType=countries")) {
                System.out.println("Busqueda pais");
            } else if (url2.substring(0, 20).equals("&searchType=managers")) {
                System.out.println("Busqueda entrnador");
            } else if (url2.substring(0, 20).equals("&searchType=stadiums")) {
                System.out.println("Busqueda estadio");
            } else if (url2.substring(0, 19).equals("&searchType=leagues")) {
                System.out.println("Busqueda liga");
            } else if (url2.substring(0, 16).equals("&searchType=cups")) {
                System.out.println("Busqueda copa");
            }
            System.out.println(url2);
        }
        System.out.println(url);
        System.out.println(url.substring(0, size));
        return "http://" + lang + ".soccerwiki.org/" + url;
    }

    /**
     * Carga la imagen de una url para tranformarla en un icono
     *
     * @param Url Url de la imagen
     * @return Un ImageIcon con la imagen
     */
    static ImageIcon UrlIcon(String Url, int x, int y) {
        ImageIcon icon = null;
        try {
            URL url = new URL(Url);
            HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
            httpcon.addRequestProperty("User-Agent", "");
            BufferedImage img = ImageIO.read(httpcon.getInputStream());
            BufferedImage resized = resize(img, x, y);
            icon = new ImageIcon(resized);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return icon;
    }

    //https://memorynotfound.com/java-resize-image-fixed-width-height-example/
    private static BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

    /**
     * Con esta método compruebo el Status code de la respuesta que recibo al
     * hacer la petición EJM: 200 OK 300 Multiple Choices 301 Moved Permanently
     * 305 Use Proxy 400 Bad Request 403 Forbidden 404 Not Found 500 Internal
     * Server Error 502 Bad Gateway 503 Service Unavailable
     *
     * @param url
     * @return Status Code
     */
    public static int getEstadoPagina(String url) {

        Response response = null;

        try {
            response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).ignoreHttpErrors(true).execute();
        } catch (IOException ex) {
            System.out.println("Excepción al obtener el Status Code: " + ex.getMessage());
        }
        return response.statusCode();
    }

    /**
     * Con este método devuelvo un objeto de la clase Document con el contenido
     * del HTML de la web que me permitirá parsearlo con los métodos de la
     * librelia JSoup
     *
     * @param url
     * @return Documento con el HTML
     */
    public static Document getHtmlDocument(String url) {

        Document doc = null;
        try {
            doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();
        } catch (IOException ex) {
            System.out.println("Excepción al obtener el HTML de la página" + ex.getMessage());
        }
        return doc;
    }
}
