import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class SignLanguagePerceptron extends JFrame {

    // Graphics start
    private int[][] data; // 2D array containing image data
    private BufferedImage image;
    private static final int SCALE = 10; // Scale factor for display

    public SignLanguagePerceptron(int[][] data) {
        this.data = data;
        this.image = createImageFromData(data);
        initUI();
    }

    private void initUI() {
        setTitle("Scaled 28x28 Grayscale Image");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                g2d.drawImage(image, 0, 0, data.length * SCALE, data[0].length * SCALE, this);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(data.length * SCALE, data[0].length * SCALE);
            }
        };

        add(panel);
        pack();
        setLocationRelativeTo(null);
    }

    private BufferedImage createImageFromData(int[][] data) {
        int width = data.length;
        int height = data[0].length;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int brightness = data[x][y];
                int color = (255 << 24) | (brightness << 16) | (brightness << 8) | brightness; // ARGB
                image.setRGB(x, y, color);
            }
        }
        return image;
    }
    public static void main(String[] args) throws IOException {
        List<List<Integer>> data = readCSV("sign_mnist_train.csv");
        int lg = data.size();
        int lg1 = data.get(0).size();
        System.out.println(lg + ", " + lg1);

        int[][] x = new int[lg - 1][lg1];
        for (int i = 0; i < lg - 1; i++) {
            for (int j = 0; j < lg1; j++) {
                x[i][j] = data.get(i + 1).get(j);
            }
        }

        // Print examples of data
        System.out.println(Arrays.toString(x[0]));
        System.out.println(Arrays.toString(x[1]));

        double[][] weights = new double[25][lg1];
        int[] digit = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24};
        String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y"};
        int m = weights[0].length - 1;
        System.out.println(m + ", " + x.length);

        double rate = 0.5;
        int epoch = 50;
        for (int s = 0; s < 25; s++) {
            for (int ii = 0; ii < epoch; ii++) {
                double error = 0.0;
                for (int i = 0; i < x.length; i++) {
                    double y_pred = weights[s][0];
                    for (int k = 0; k < m; k++) {
                        y_pred += weights[s][k + 1] * x[i][k + 1];
                    }

                    double pred = y_pred >= 0.0 ? 1.0 : 0.0;
                    double expect = x[i][0] == digit[s] ? 1.0 : 0.0;
                    double err = pred - expect;
                    error += err * err;

                    weights[s][0] -= rate * err;
                    for (int k = 0; k < m; k++) {
                        weights[s][k + 1] -= rate * err * x[i][k + 1];
                    }
                }
                System.out.println("Letter: " + alphabet[s] + ", Epoch: " + ii + ", Error: " + error);
            }
        }

        // Further logic for testing and visualization can be added here
        List<List<Integer>> test = readCSV("custom_images.csv");
        int lgt = test.size();
        int lg1t = test.get(0).size();
        System.out.println(lgt + ", " + lg1t);

        int[][] xt = new int[lgt - 1][lg1t];
        for (int i = 0; i < lgt - 1; i++) {
            for (int j = 0; j < lg1t; j++) {
                xt[i][j] = test.get(i + 1).get(j);
            }
        }

 //       double[][] ima = new double[28][28];

        // Assuming 'N' and 'weights' are defined elsewhere in your code
 //       int N = 24; // Number of classes
        int M = 10; // Number of test images to show
 //       double[][] weights = new double[N][lg1]; // Dummy initialization


        for (int ii = 0; ii < M; ii++) {
            double pred0 = -100000000;
            int s0 = 0;
            for (int s = 0; s < 25; s++) {
                double y_pred = weights[s][0];
                for (int k = 0; k < lg1t - 1; k++) {
                    y_pred += weights[s][k + 1] * xt[ii][k + 1];
                }
                System.out.println("s=" + s + ", letter=" + alphabet[s] + ", y_pred=" + y_pred);
                if (y_pred > pred0) {
                    pred0 = y_pred;
                    s0 = s;
                }
            }
            System.out.println("prediction test data =" + s0 + " actual label =" + xt[ii][0] + " prediction letter =" + alphabet[s0]);

            int[][][] ima = new int[M][28][28];
            for (int i = 0; i < 28; i++) {
                for (int j = 0; j < 28; j++) {
                        // Adjust the index to match Java's 0-based indexing
                    ima[ii][i][j] = xt[ii][1 + i * 28 + j];
                }
            }

            final int finalIi = ii;  // Declare a final variable
            final int[][] imaCopy = ima[finalIi];  // Create a copy of the im

            SwingUtilities.invokeLater(() -> {
                SignLanguagePerceptron app = new SignLanguagePerceptron(imaCopy);
                app.setVisible(true);
            });
            
        }
    }

    private static List<List<Integer>> readCSV(String fileName) throws IOException {
        List<List<Integer>> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean isFirstLine = true; // To skip the header row
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the header row
                }
                String[] values = line.split(",");
                List<Integer> row = new ArrayList<>();
                for (String val : values) {
                    try {
                        row.add(Integer.parseInt(val.trim()));
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping non-numeric value: " + val);
                        // Handle the non-numeric value, or continue
                    }
                }
                data.add(row);
            }
        }
        return data;
    }
}