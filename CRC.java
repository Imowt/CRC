import java.util.*;
import java.io.*;

/**
 * @author Valentin Dulong
 * @author Julien Houchard
 */
class CRC {

    /**
     * Methode mettant en place un menu dans la console et
     * attendant une réponse
     * @throws Exception Si jamais un problème survient avec le Scanner
     */
    private static void menu() throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("1 - Verification | 2 - Calculer mot CRC");
        String rep = sc.nextLine();
        switch (rep) {
            case "1":
                verification();
                break;
            case "2":
                calculer();
                break;
            default:
                System.out.println("Invalide input");
                menu();
        }
    }

    /**
     * Methode demandant un mot à l'utilisateur et attendant une réponse
     * @param demande
     *          La demande affichée à l'utilisateur
     * @return le mot binaire saisi
     * @throws Exception Si jamais un problème survient avec le Scanner ou
     *                   si le mot saisi est invalide
     */
    private static String demandeMot(String demande) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println(demande);
        String message = sc.nextLine();
        if (!message.matches("[01]+")) throw new Exception("Mot non binaire");
        return message;
    }

    /**
     * Méthode verifiant si la taille du generateur est valide par rapport
     * au message
     * @param message
     *            Le message binaire
     * @param generateur
     *            Le generateur
     * @throws Exception Si la taille du generateur est pas valide
     */
    private static void verifTaille(String message, String generateur) throws Exception {
        if(message.length() < generateur.length()) throw new Exception("Taille du code generateur impossible avec ce message");
    }

    /**
     * Codage du message a saisir par l'utilisateur par le generateur a saisir aussi
     * @throws Exception Si problème avec un Scanner ou une action invalide
     */
    private static void calculer() throws Exception {
        String generateur = demandeMot("Code generateur : ");
        String message = demandeMot("Message en binaire : ");

        String code = message;

        // On retire les 0 au code generateur s'il y en a au début
        for(int i = 0; i < generateur.length() ; i++) {
            if(generateur.charAt(i) == '0') { generateur = generateur.substring(i+1,generateur.length()); i--; }
            else break;
        }

        int polynome = generateur.length() - 1;
        System.out.println("POLYNOME : " + polynome);

        // On ajoute les '0' correspondant au polynome
        for(int i = 0; i < polynome; i++)
            code += "0";

        System.out.println("Code avec ajout des 0 : " + code );
        code = message + div(code, generateur);
        System.out.println("Code CRC à transmettre : " + code);
    }

    /**
     * Methode verifiant si le message est correct par rapport au generateur
     * @throws Exception Si problème avec un Scanner ou si la taille n'est pas
     * valide
     */
    static void verification() throws Exception {
        String generateur = demandeMot("Code generateur :  ");
        String message = demandeMot("Code du message : ");
        verifTaille(message, generateur);
        // Si le reste de la division est 0, il n'y a pas d'erreur
        if (Integer.parseInt(div(message, generateur)) == 0)
            System.out.println("Le code reçu ne contient pas d'erreur");
        else
            System.out.println("Le code reçu contient une/des erreur(s).");
    }

    /**
     * Methode affichant toute la demarche avec les divisions et retournant
     * le reste
     * @param code
     *          le code binaire
     * @param generateur
     *          le generateur associé au code
     * @return le reste de la division
     */
    private static String div(String code, String generateur) {
        int indice = generateur.length();
        String resultat = code.substring(0, indice);
        String reste = "";

        // Première division
        for (int i = 0; i < generateur.length(); i++) {
            if (resultat.charAt(i) == generateur.charAt(i))
                reste += "0";
            else
                reste += "1";
        }
        System.out.println("Etape division : ");
        System.out.println(resultat);
        System.out.println(generateur);
        System.out.println("-------");
        System.out.println(reste);
        while (indice < code.length()) {
            // Si le premier char du reste est 0, on recupere que la suite
            if (reste.charAt(0) == '0') {
                reste = reste.substring(1, reste.length());
                reste += String.valueOf(code.charAt(indice));
                indice++;
            }
            resultat = reste;
            reste = "";
            for (int i = 0; i < generateur.length(); i++) {
                if (resultat.charAt(i) == generateur.charAt(i))
                    reste += "0";
                else
                    reste += "1";
            }
            System.out.println("Etape division : ");
            System.out.println(resultat);
            System.out.println(generateur);
            System.out.println("-------");
            System.out.println(reste);
        }
        return reste.substring(1, reste.length());
    }

    /**
     * Methode de main
     * @param args
     *          Tableau d'argument
     * @throws Exception Si un probleme survient lors de son execution
     */
    public static void main(String args[]) throws Exception {
        menu();
    }
}