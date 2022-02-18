/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Autores: Iván Camilo Narváez - Andrés Hernández
 */
package ieee.pkg754;

import java.text.DecimalFormat;
import javax.swing.JOptionPane;

/**
 *
 * @author Iván
 */
public class IEEE754 {

    /**
     * @param args the command line arguments
     */
    
    private void principal(){
        String opc;
        String menu = "Programa que codifica y decodifica números en el formato IEEE-754 en 32 bits.\n\n" +
                      "MENÚ PRINCIPAL:\n" +
                      "1. Codificar un número\n" +
                      "2. Decodificar un número\n" +
                      "3. Salir\n\n" +
                      "Digite su opción:";
        
        do{
            opc = JOptionPane.showInputDialog(null, menu, "Menú principal", JOptionPane.PLAIN_MESSAGE);
            if(opc == null){
                opc = "3";
            }
            switch (opc){
                case "1":
                    codificar();
                    break;
                case "2":
                    decodificar();
                    break;
                case "3":
                    JOptionPane.showMessageDialog(null, "Hasta luego :)", "Adiós", JOptionPane.INFORMATION_MESSAGE);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida, digite una de las opciones del menú.", "Opción inválida", JOptionPane.WARNING_MESSAGE);
                    break;
            }
        }while(!opc.equals("3"));
    }
    public static void main(String[] args) {
        new IEEE754().principal();
    }

    private void codificar() {
        try{
            long z;
            int exp = 0;
            double t;
            String bz, bd = "", bexp, sign;
            String num = JOptionPane.showInputDialog(null, "Digite un número en decimal a ser codificado", "Codificar", JOptionPane.PLAIN_MESSAGE);
            String o = num;
            if(num.contains("-")){
                sign = "1";
                num = num.substring(1);
            }else{
                sign = "0";
            }        
            if(num.contains(",")){
                num = num.replace(",", ".");
            }
            if(num.contains(".")){
                double d;
                int c = 0;
                String[] n = num.split("\\.");
                z = Long.parseLong(n[0]);
                d = Double.parseDouble("0." + n[1]);

                do{                
                    d = d * 2;
                    n = String.valueOf(d).split("\\.");
                    bd = bd + n[0];
                    d = Double.parseDouble("0." + n[1]);
                    c++;
                }while(d != 0 && c < 32);

                if(z == 0){
                    t = Double.parseDouble("0." + bd);
                    for(exp = 0;t != 1 ;exp--){
                        t = t * 10;
                        bd = bd.substring(1);
                    }
                    if(String.valueOf(t).contains("E")){
                        String tstring = String.valueOf(t);                   
                        exp = Integer.parseInt(tstring.substring(tstring.length() - 2, tstring.length()));
                        bd = tstring.substring(2, tstring.length() - 3) + bd;        
                    }
                }
            }else{
                z = Long.parseLong(num);
            }

            bz = Long.toBinaryString(z);        

            if(z != 0){
                t = Long.parseLong(bz);
                for(exp = 0;String.valueOf(t).charAt(0) != '1' || String.valueOf(t).charAt(1) != '.';exp++){
                    t = t / 10;                
                }
                if(String.valueOf(t).contains("E")){
                    String tstring = String.valueOf(t);
                    exp = Integer.parseInt(tstring.substring(tstring.length() - 1));
                    bd = tstring.substring(2, tstring.length() - 2) + bd;        
                }else{
                    bd = String.valueOf(t).substring(2) + bd;
                }
            }        

            exp = exp + 127;
            bexp = Integer.toBinaryString(exp);       

            if(bd.length() < 23){
                for(int i = bd.length();bd.length() < 23;i++){
                    bd = bd + "0";
                }
            }else if(bd.length() > 23){
                bd = bd.substring(0, 23);
            }

            if(bexp.length() < 8){
                for(int i = bexp.length();bexp.length() < 8;i++){
                    bexp = "0" + bexp;
                }
            }
            String b = sign + bexp + bd;
            String hex = Long.toHexString(Long.valueOf(b, 2)).toUpperCase();
            JOptionPane.showMessageDialog(null, "El número "+ o +" codificado en IEEE-754 es:\n"+ hex, "Resultado", JOptionPane.INFORMATION_MESSAGE);
        }catch(java.lang.NumberFormatException e){
            JOptionPane.showMessageDialog(null, "El valor ingresado no es válido.", "Dato inválido", JOptionPane.WARNING_MESSAGE);
        }catch(java.lang.NullPointerException err){}
    }

    private void decodificar() {
        String num;
        try{
            do{
                num = JOptionPane.showInputDialog(null, "Digite un número en IEEE-754 a ser decodificado", "Deodificar", JOptionPane.PLAIN_MESSAGE);
                if(num.length() != 8){
                    JOptionPane.showMessageDialog(null, "El valor ingresado no está codificado en IEEE-754.", "Dato inválido", JOptionPane.WARNING_MESSAGE);
                }
            }while(num.length() != 8);
            
            String b = Long.toBinaryString(Long.valueOf(num, 16));
            while(b.length() < 32){
                b = "0" + b;
            }
            
            String sign = b.substring(0, 1);
            String bexp = b.substring(1, 9);
            String bd = b.substring(9, b.length());

            int exp = Integer.valueOf(bexp, 2);
            exp = exp - 127;

            double d = 1;
            for(int i = 0;Math.abs(i) < bd.length();i--){
                if(bd.charAt(Math.abs(i)) == '1'){
                    d = d + Math.pow(2, i - 1);
                }
            }
            
            if("1".equals(sign)){
                d = d * (-1);
            }
            
            double n = d * Math.pow(2, exp);            
            JOptionPane.showMessageDialog(null, "El número "+ num.toUpperCase() +" codificado en IEEE-754 en decimal es:\n"+ sinNotacionCientifica(n), "Resultado", JOptionPane.INFORMATION_MESSAGE);
        }catch(java.lang.NumberFormatException e){
            JOptionPane.showMessageDialog(null, "El valor ingresado no es válido.", "Dato inválido", JOptionPane.WARNING_MESSAGE);
        }catch(java.lang.NullPointerException err){}
    }
    
    private static String sinNotacionCientifica(double t){
        return new DecimalFormat("#.################################").format(t);
    }
}
