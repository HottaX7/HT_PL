import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Task1 {

    public static void main(String[] args) {
        // Создание объекта Scanner для ввода данных с клавиатуры
        Scanner scanner = new Scanner(System.in);

        // Запрос значения n
        System.out.print("Введите значение n (размер кругового массива): ");
        int n = scanner.nextInt();

        // Запрос значения m
        System.out.print("Введите значение m (длина интервала): ");
        int m = scanner.nextInt();

        // Закрытие сканера после использования
        scanner.close();

        // Вызываем метод для нахождения пути и интервалов
        Result result = findPathAndIntervals(n, m);

        // Вывод интервалов
        System.out.println("Интервалы:");
        for (String interval : result.intervals) {
            System.out.println(interval);
        }

        // Вывод результата
        System.out.println("Полученный путь: " + result.path);
    }

    // Метод для нахождения пути и интервалов
    public static Result findPathAndIntervals(int n, int m) {
        StringBuilder path = new StringBuilder();
        List<String> intervals = new ArrayList<>();
        int start = 0;

        do {
            // Формируем интервал
            StringBuilder interval = new StringBuilder();
            for (int i = 0; i < m; i++) {
                interval.append((start + i) % n + 1);
            }

            // Добавляем интервал в список интервалов
            intervals.add(interval.toString());

            // Добавляем начальный элемент интервала в путь
            path.append((start + 1));

            // Переходим к следующему интервалу
            start = (start + m-1) % n;
        } while (start != 0 || path.length() == 0); // Продолжаем до тех пор, пока не вернемся к начальной позиции и не сформируем путь

        return new Result(path.toString(), intervals);
    }

    // Класс для хранения результата
    static class Result {
        String path;
        List<String> intervals;

        Result(String path, List<String> intervals) {
            this.path = path;
            this.intervals = intervals;
        }
    }
}
