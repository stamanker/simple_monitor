package maxz.monitor.services.callers;

import lombok.extern.slf4j.Slf4j;
import maxz.monitor.services.callers.exceptions.SetupException;
import org.springframework.http.MediaType;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Slf4j
public abstract class BaseCaller {


    public String processRequest(String url, String fileName2Read, String method) throws IOException {
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        //conn.setRequestProperty("Content-Length", 0+"");//TODO fix
        readFileIntoStream(fileName2Read, conn);
        return readResponse(conn);
    }

    private void readFileIntoStream(String fileName2Read, HttpURLConnection conn) throws IOException {
        if(fileName2Read!=null) {
            String data = readFileFromResources(fileName2Read);
            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                wr.write(data.getBytes());
            }
        }
    }

    protected String readResponse(HttpURLConnection conn) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                content.append(line);
                content.append(System.lineSeparator());
            }
        }
        return content.toString();
    }

    protected String readFileFromResources(String filename) {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(filename).getFile());
            return readStream2String(new FileInputStream(file));
        } catch (Exception e) {
            throw new SetupException(e.getMessage(), e);
        }
    }

    private String readStream2String(InputStream inputStream) throws IOException {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            return br.lines().collect(Collectors.joining("\n"));
        }
    }

}
