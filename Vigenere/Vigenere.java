package vigenere;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileReader;

class Vigenere{
    public static void main(String[] args){
        /* alfabeto = 91
           caracter inicial = 32 */

        String op = "";
        String mensagem = "";
        int tamanhoChave = 0;
        char maisFrequente = ' ';
        String chave = "";
        String chaveEstendida = "";
        String mensagemCifrada = "";
        File arquivo = new File("./vigenere/texto_cripto.txt");
        
        System.out.println("+-------------------------------------------------+");
        System.out.println("|O conteudo do arquivo texto_cripto.txt sera usado|");
        System.out.println("|    como mensagem orignal ou mensagem cifrada.   |");
        System.out.println("+-------------------------------------------------+");
        System.out.print("Deseja Cifrar[C], Decifrar[D] ou Quebrar Chave[Q]? ");
        try {
            op = lerConsole(System.in);
            if (op.equals("c") || op.equals("C")){
                mensagem = lerArquivo(arquivo);
                System.out.print("Digite a chave: ");
                chave = lerConsole(System.in);
                chaveEstendida = expansaoChave(mensagem, chave);
                mensagemCifrada = cifrarMensagem(mensagem, chaveEstendida);
                System.out.println();
                System.out.println("CifraEstendida: " + mensagem);
                System.out.println("CifraEstendida: " + chaveEstendida);
                System.out.println("Mensagem cifrada: " + mensagemCifrada);
                
            }else if(op.equals("d") || op.equals("D")){
                mensagemCifrada = lerArquivo(arquivo);
                System.out.print("Digite a chave: ");
                chave = lerConsole(System.in);
                chaveEstendida = expansaoChave(mensagemCifrada, chave);
                mensagem = decifrarMensagem(mensagemCifrada, chaveEstendida);
                System.out.println();
                System.out.println("Mensagem decifrada: " + mensagem);

            }else if(op.equals("q") || op.equals("Q")){
                mensagem = lerArquivo(arquivo);
                System.out.print("Digite o tamanho da chave: ");
                tamanhoChave = Integer.parseInt(lerConsole(System.in));
                chave = quebrarChave(mensagem, tamanhoChave, maisFrequente);              
                System.out.println();
                System.out.println("Chave: " + chave);

            }else{
                System.out.println("Letra/Sentenca nao permitida.");
            }
        } catch (IOException e) {
            System.out.println("Erro na leitura de dados.");
        }
    }

    private static String lerConsole(InputStream entrada) throws IOException {
        String texto;
        BufferedReader temp = new BufferedReader(new InputStreamReader(entrada));
        texto = temp.readLine();
        return texto;
    }

    private static String lerArquivo(File arquivo) throws IOException {
        String texto = "";
        BufferedReader temp = new BufferedReader(new FileReader(arquivo));
        String tempString = ""; 
        while ((tempString = temp.readLine()) != null){
            if(texto.equals("")){
                texto = tempString;
            }else{
                texto = texto +'\n' + tempString;
            }
        }
        temp.close();
        return texto;
    }

    private static String expansaoChave(String mensagem, String chave){
        String mensagemChave = "";
        for (int contM = 0; contM < mensagem.length(); contM++) {   
            mensagemChave = mensagemChave + chave.charAt(contM % chave.length());
        }
        return mensagemChave;
    }

    private static String cifrarMensagem(String mensagem, String chaveEstendida){
        String mensagemCifrada = "";
        for(int cont = 0; cont < mensagem.length(); cont++){
            char caracter =  (char) (((((int) mensagem.charAt(cont) - 32) + 
                                            ((int) chaveEstendida.charAt(cont) - 32)) % 91) + 32);
            mensagemCifrada = mensagemCifrada + caracter;
        }
        return mensagemCifrada;
    }

    private static String decifrarMensagem(String mensagemCifrada, String chaveEstendida){
        String mensagemDecifrada = "";
        for(int cont = 0; cont < mensagemCifrada.length(); cont++){     
            char caracter =  (char) (((((int) mensagemCifrada.charAt(cont) - 32) - 
                                            ((int) chaveEstendida.charAt(cont) - 32) + 91) % 91) + 32);
            mensagemDecifrada = mensagemDecifrada + caracter;
        }
        return mensagemDecifrada;
    }

    private static int[] descobrirFrequencia(char[] tabelaCaracter, int parteCaracter){
        int[] frequencia = new int[91];
        for(int contC = 0; contC < parteCaracter; contC++){
            frequencia[(int)(tabelaCaracter[contC]) - 32 ]++ ;
        }
        return frequencia;
    }

    private static char analisarFrequencia(int[] frequencia){
        int indexMax = 0;
        int numMax = 0;
        for(int contC = 0; contC < 91; contC++){
            if(frequencia[contC] > numMax){
                numMax = frequencia[contC];
                indexMax = contC;
            }
        }
        return (char) (indexMax);
    }

    private static String quebrarChave(String mensagem, int tamanhoChave, char maisFrequente){
        /* Perceba que o descarte dos caracteres finais (em casos onde a chave 
           não é escrita por completa) é feita pois estamos trabalhando com 
           um texto original muito maior que a chave, logo esses não são 
           significativos. */
        String chave = "";
        int parteCaracter = (int) (mensagem.length()) / tamanhoChave;
        char[][] tabela = new char[tamanhoChave][parteCaracter];
        int temp = 0;

        for(int contL = 0; contL < tamanhoChave; contL++){
            for(int contC = 0; contC < parteCaracter; contC++){
                tabela[contL][contC] = mensagem.charAt(temp + contL) ;
                temp = temp + tamanhoChave;
            }
            temp = 0;
        }

        for(int contL = 0; contL < tamanhoChave; contL++){
            char deslocamento = analisarFrequencia(descobrirFrequencia(tabela[contL], parteCaracter));
            chave = chave + (char) ((((int) deslocamento - 
                                        ((int) maisFrequente -32) + 91) % 91) + 32) ;
        }
        return chave;
    }
}