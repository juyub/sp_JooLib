package naver;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NaverDAO {
	
	private static String NAVER_API_CLIENT_ID;
	private static String NAVER_API_CLIENT_SECRET;

	static {
	    Properties properties = new Properties();
	    try {
	        // 변경된 부분
	        ClassLoader classLoader = NaverDAO.class.getClassLoader();
	        InputStream inputStream = classLoader.getResourceAsStream("config.properties");

	        // 파일을 읽고 속성을 로드
	        properties.load(inputStream);
	        NAVER_API_CLIENT_ID = properties.getProperty("naver.api.clientId");
	        NAVER_API_CLIENT_SECRET = properties.getProperty("naver.api.clientSecret");
	    } catch (IOException e) {
	        // 오류를 로그에 남기거나 예외를 발생시키세요
	        NAVER_API_CLIENT_ID = null;
	        NAVER_API_CLIENT_SECRET = null;
	    }
	}
	
public List<NaverVO> findBooks(String query) {
            	
        try {
        	String encodedQuery = URLEncoder.encode(query, "UTF-8");
        	String urlString = "https://openapi.naver.com/v1/search/book.json?query=" + encodedQuery;
            URI uri = new URI(urlString);

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Naver-Client-Id", NAVER_API_CLIENT_ID);
            headers.set("X-Naver-Client-Secret", NAVER_API_CLIENT_SECRET);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> httpEntity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                String responseBody = responseEntity.getBody();

                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(responseBody);
                JsonNode itemsNode = rootNode.get("items");

                List<NaverVO> newsList = mapper.readValue(itemsNode.toString(), new TypeReference<List<NaverVO>>() {});

                return newsList;
            } else {
                System.out.println("fail : " + responseEntity.getStatusCode());
                return Collections.emptyList();
            }

        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
