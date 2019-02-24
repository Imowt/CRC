import java.util.*;
import java.io.*;

class CRC {

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

    private static String demandeMot(String demande) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println(demande);
        String message = sc.nextLine();
        if (!message.matches("[01]+")) throw new Exception("Mot non binaire");
        return message;
    }

    private static void calculer() throws Exception {
        String generateur = demandeMot("Code generateur : ");
        String message = demandeMot("Message en binaire : ");

        String code = message;
        while (code.length() < (message.length() + generateur.length() - 1))
            code = code + "0";
        code = message + div(code, generateur);
        System.out.println("Code CRC à transmettre : " + code);
    }

    static void verification() throws Exception {
        String generateur = demandeMot("Code generateur :  ");
        String recu = demandeMot("Code du message : ");
        if (Integer.parseInt(div(recu, generateur)) == 0)
            System.out.println("Le code reçu ne contient pas d'erreur");
        else
            System.out.println("Le code reçu contient une/des erreur(s).");
    }

    private static String div(String code, String generateur) {
        int pointer = generateur.length();
        String result = code.substring(0, pointer);
        String remainder = "";
        for (int i = 0; i < generateur.length(); i++) {
            if (result.charAt(i) == generateur.charAt(i))
                remainder += "0";
            else
                remainder += "1";
        }
        while (pointer < code.length()) {
            if (remainder.charAt(0) == '0') {
                remainder = remainder.substring(1, remainder.length());
                remainder = remainder + String.valueOf(code.charAt(pointer));
                pointer++;
            }
            result = remainder;
            remainder = "";
            for (int i = 0; i < generateur.length(); i++) {
                if (result.charAt(i) == generateur.charAt(i))
                    remainder += "0";
                else
                    remainder += "1";
            }
        }
        return remainder.substring(1, remainder.length());
    }

    public static void main(String args[]) throws Exception {
        menu();
    }
}