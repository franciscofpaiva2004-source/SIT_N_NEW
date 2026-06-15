package pt.pa.utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilitário para importação de dados de ficheiros CSV e conversão em objetos Java.
 *
 * <p>Esta classe usa a biblioteca OpenCSV para leitura de arquivos CSV e reflexão Java
 * para mapear os dados nas propriedades dos objetos fornecidos.</p>
 *
 * <h3>Funcionalidades:</h3>
 * <ul>
 *     <li>Importa dados de um ficheiro CSV para uma lista de instâncias de uma classe específica.</li>
 *     <li>Realiza a conversão automática de valores CSV para os tipos de dados apropriados.</li>
 *     <li>Suporta convenções de nomenclatura para mapear cabeçalhos CSV para campos da classe alvo.</li>
 * </ul>
 *
 * <p>Os métodos principais incluem:</p>
 * <ul>
 *     <li>{@code importCSV(String file, Class<T> clazz)}: Importa os dados CSV para objetos da classe fornecida.</li>
 *     <li>{@code convertHeaderToField(String header)}: Converte os cabeçalhos do CSV para nomes de campos de classe.</li>
 *     <li>{@code convertValue(Class<?> type, String value)}: Converte valores de string para os tipos de dados corretos.</li>
 * </ul>
 *
 * <p><b>Exemplo de utilização:</b></p>
 * <pre>{@code
 * List<MyClass> objects = DataSetUtil.importCSV("data.csv", MyClass.class);
 * }</pre>
 *
 * @author [Seu Nome]
 */
public class DataSetUtil {

    /**
     * Construtor padrão.
     * Apenas necessário para satisfazer a convenção, já que esta classe é utilitária e não precisa ser instanciada.
     */
    public DataSetUtil() {
    }

    /**
     * Importa os dados de um ficheiro CSV e os converte em objetos de uma classe específica.
     *
     * <p>O método usa os cabeçalhos do CSV para mapear os valores para os campos da classe fornecida.
     * Requer que a classe fornecida tenha um construtor sem argumentos.</p>
     *
     * @param file Caminho do ficheiro CSV.
     * @param clazz Classe para a qual os dados devem ser mapeados.
     * @param <T> Tipo genérico da classe alvo.
     * @return Uma lista de instâncias da classe preenchidas com os dados do ficheiro CSV.
     * @throws IOException Se ocorrer um erro ao ler o ficheiro.
     * @throws CsvValidationException Se o CSV não for válido.
     * @throws NoSuchMethodException Se o construtor padrão da classe não for encontrado.
     * @throws InvocationTargetException Se ocorrer um erro ao invocar o construtor da classe.
     * @throws InstantiationException Se a classe não puder ser instanciada.
     * @throws IllegalAccessException Se não houver acesso aos campos da classe.
     * @throws NoSuchFieldException Se algum campo da classe mapeada não for encontrado.
     */
    public static <T> List<T> importCSV(String file, Class<T> clazz) {
        List<T> instances = new ArrayList<>();
        try {
            CSVReader csvReaders = new CSVReader(new FileReader(file));
            String[] headers = csvReaders.readNext();
            if (headers != null) {
                String[] line;
                while ((line = csvReaders.readNext()) != null) {
                    T instance = clazz.getDeclaredConstructor().newInstance();
                    for (int i = 0; i < headers.length; i++) {
                        String header = headers[i];
                        String value = line[i];
                        if (value == null || value.trim().isEmpty()) {
                            continue;
                        }
                        Field field = clazz.getDeclaredField(convertHeaderToField(header));
                        field.setAccessible(true);
                        field.set(instance, convertValue(field.getType(), value));
                    }
                    instances.add(instance);
                }
            }
        } catch (IOException | CsvValidationException | NoSuchMethodException | InvocationTargetException
                 | InstantiationException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return instances;
    }

    /**
     * Converte um cabeçalho de CSV em um nome de campo da classe.
     *
     * <p>A conversão aplica as seguintes regras:
     * <ul>
     *     <li>Transforma o primeiro caractere em minúscula.</li>
     *     <li>Remove underscores e converte o caractere seguinte para maiúscula.</li>
     * </ul>
     *
     * @param header Cabeçalho do CSV.
     * @return Nome do campo correspondente na classe.
     */
    public static String convertHeaderToField(String header) {
        header = header.substring(0, 1).toLowerCase() + header.substring(1);
        StringBuilder builder = new StringBuilder(header);
        for (int i = 1; i < builder.length(); i++) {
            if (builder.charAt(i) == '_') {
                builder.deleteCharAt(i);
                builder.replace(i, i + 1, String.valueOf(Character.toUpperCase(builder.charAt(i))));
            }
        }
        return builder.toString();
    }

    /**
     * Converte um valor de string para o tipo apropriado.
     *
     * <p>O método suporta os seguintes tipos:
     * <ul>
     *     <li>{@code String}</li>
     *     <li>{@code int} e {@code Integer}</li>
     *     <li>{@code double} e {@code Double}</li>
     *     <li>{@code char} e {@code Character}</li>
     * </ul>
     *
     * @param type O tipo de destino.
     * @param value O valor em formato de string.
     * @return O valor convertido para o tipo de destino, ou {@code null} se o tipo não for suportado.
     */
    public static Object convertValue(Class<?> type, String value) {
        if (type == String.class) {
            return value;
        }
        if (type == int.class || type == Integer.class) {
            return Integer.parseInt(value);
        }
        if (type == double.class || type == Double.class) {
            return Double.parseDouble(value);
        }
        if (type == char.class || type == Character.class) {
            return value.charAt(0);
        }
        return null;
    }
}

