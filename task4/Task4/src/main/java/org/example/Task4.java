import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Task4 {

    public static void main(String[] args) {
        // Запрос пути к директории у пользователя
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the path to the directory containing the file: ");
        String directoryPath = scanner.nextLine();
        scanner.close();

        // Проверка, является ли путь директорией
        File directory = new File(directoryPath);
        if (!directory.isDirectory()) {
            System.err.println("Error: The provided path is not a directory.");
            System.exit(1);
        }

        // Фиксированное имя файла
        File file = new File(directory, "file1.txt");

        // Проверка, существует ли файл
        if (!file.exists() || !file.isFile()) {
            System.err.println("Error: The file 'file1.txt' does not exist or is not a file.");
            System.exit(1);
        }

        try {
            // Чтение данных из файла
            List<String> lines = Files.readAllLines(file.toPath());
            int[] nums = lines.stream().mapToInt(Integer::parseInt).toArray();

            // Вычисление минимального количества шагов
            int result = calculateMinSteps(nums);

            // Вывод результата
            System.out.println("Minimum number of moves: " + result);

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing numbers: " + e.getMessage());
        }
    }

    private static int calculateMinSteps(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }

        // Сортировка массива для нахождения медианы
        Arrays.sort(nums);

        // Нахождение медианы
        int median = nums[nums.length / 2];

        // Вычисление количества шагов
        int steps = 0;
        for (int num : nums) {
            steps += Math.abs(num - median);
        }

        return steps;
    }
}
