import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ShamirSecret {
    
    // Function to decode Y values from different bases
    public static int decodeValue(String base, String value) {
        return Integer.parseInt(value, Integer.parseInt(base));
    }

    // Function to perform Lagrange interpolation
    public static double lagrangeInterpolation(List<int[]> points) {
        double c = 0;

        for (int j = 0; j < points.size(); j++) {
            double term = points.get(j)[1];
            int xj = points.get(j)[0];

            for (int i = 0; i < points.size(); i++) {
                if (i != j) {
                    int xi = points.get(i)[0];
                    term *= (0.0 - xi) / (xj - xi);
                }
            }
            c += term;
        }

        return Math.round(c);
    }

    public static void main(String[] args) throws Exception {
        // Read JSON file
        String content = new String(Files.readAllBytes(Paths.get("input.json")));
        JSONObject data = new JSONObject(content);

        int k = data.getJSONObject("keys").getInt("k");
        List<int[]> points = new ArrayList<>();

        for (String key : data.keySet()) {
            if (!key.equals("keys")) {
                int x = Integer.parseInt(key);
                JSONObject valueObj = data.getJSONObject(key);
                int y = decodeValue(valueObj.getString("base"), valueObj.getString("value"));
                points.add(new int[]{x, y});
            }
        }

        // Use only k points
        List<int[]> subsetPoints = points.subList(0, k);
        double secretC = lagrangeInterpolation(subsetPoints);

        System.out.println("Secret (c): " + (int) secretC);
    }
}
