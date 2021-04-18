package maxz.monitor;

import lombok.SneakyThrows;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WP {

    @SneakyThrows
    public static void main(String[] args) {
        List<String> strings =
                Arrays.asList(
                Files.readString(Paths.get("src\\main\\resources\\public\\english2000.txt")).split("\n")
        )
        ;
        String result = "";
        for (int i = 0; i < strings.size(); i++) {
            String line = strings.get(i).trim();
            int idx = line.indexOf("]");
            String x = line.substring(0, idx+1).replaceAll("'", "\\\\'");
            String y = line.substring(idx+2);
            line = "['" + x +"', '"+  y + "'],";
            System.out.println("line = " + line);
            result += line + "\n";
        }
        File file = new File("R.html");
        file.delete();
        new FileOutputStream(file).write(result.getBytes(StandardCharsets.UTF_8));
    }
}
