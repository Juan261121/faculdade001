import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ConversorMoedaSimples {

    private static final String API_KEY = "a3c26569a1c0d7c0f8547d6f"; 
    private static final String[] MOEDAS = {"USD", "EUR", "GBP", "BRL", "JPY"};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Conversor de Moeda ===");

        System.out.println("Escolha a moeda de ORIGEM:");
        int origem = exibirMenu(scanner);

        System.out.println("Escolha a moeda de DESTINO:");
        int destino = exibirMenu(scanner);

        System.out.print("Digite o valor: ");
        double valor = scanner.nextDouble();

        double resultado = converter(MOEDAS[origem - 1], MOEDAS[destino - 1], valor);

        System.out.printf("Resultado: %.2f %s = %.2f %s%n",
                valor, MOEDAS[origem - 1], resultado, MOEDAS[destino - 1]);

        scanner.close();
    }

    private static int exibirMenu(Scanner scanner) {
        for (int i = 0; i < MOEDAS.length; i++) {
            System.out.printf("%d - %s%n", i + 1, MOEDAS[i]);
        }

        int opcao;
        do {
            System.out.print("Escolha: ");
            opcao = scanner.nextInt();
        } while (opcao < 1 || opcao > MOEDAS.length);

        return opcao;
    }

    private static double converter(String origem, String destino, double valor) {
        try {
            String urlStr = String.format(
                    "https://v6.exchangerate-api.com/v6/%s/pair/%s/%s",
                    API_KEY, origem, destino
            );

            URL url = new URL(urlStr);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");

            BufferedReader leitor = new BufferedReader(
                    new InputStreamReader(conexao.getInputStream())
            );

            StringBuilder resposta = new StringBuilder();
            String linha;
            while ((linha = leitor.readLine()) != null) {
                resposta.append(linha);
            }
            leitor.close();

            
            String json = resposta.toString();
            String chave = "\"conversion_rate\":";
            int inicio = json.indexOf(chave) + chave.length();
            int fim = json.indexOf(",", inicio);
            if (fim == -1) fim = json.indexOf("}", inicio);

            String valorTaxa = json.substring(inicio, fim).trim();
            double taxa = Double.parseDouble(valorTaxa);

            return valor * taxa;

        } catch (Exception e) {
            System.out.println("Erro na conversão: " + e.getMessage());
            return 0;
        }
    }
}
