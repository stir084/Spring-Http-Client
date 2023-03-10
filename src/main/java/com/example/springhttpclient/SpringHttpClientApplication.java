package com.example.springhttpclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class SpringHttpClientApplication {

	@Autowired
	RepositoryService repositoryService;

	@Bean
	ApplicationRunner init() {
		return args -> {
			// RestTemplate
			String link = "https://api.worldbank.org/v2/country/CHN/indicator/SP.DYN.TFRT.IN?format=json";
			RestTemplate restTemplate = new RestTemplate();
			List<Map> list = restTemplate.getForObject(link, List.class);
			printFertilityRate(list, "중국");

			// WebClient
			WebClient client = WebClient.create("https://api.worldbank.org");
			List<Map> list2 = client.get().uri("/v2/country/JPN/indicator/SP.DYN.TFRT.IN?format=json").retrieve().bodyToMono(List.class).block();
			printFertilityRate(list2, "일본");

			// Http Interface ( 가이드문서 in 'https://docs.spring.io/spring-framework/docs/6.0.0/reference/html/integration.html#rest-http-interface' )
			WebClient client2 = WebClient.create("https://api.worldbank.org");
			HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(client2)).build();
			RepositoryService service = factory.createClient(RepositoryService.class);
			List<Map> list3 = service.getFertilityRate("USA");
			printFertilityRate(list3, "미국");

			// Http Interface With Bean
			List<Map> list4 = repositoryService.getFertilityRate("KOR");
			printFertilityRate(list4, "한국");
		};
	}

	interface RepositoryService {
		@GetExchange("/v2/country/{country}/indicator/SP.DYN.TFRT.IN?format=json")
		List getFertilityRate(@PathVariable String country);
	}



	public void printFertilityRate(List<Map> data, String country){
		System.out.println(country + " 출산율");
		List<Map> list = (List<Map>) data.get(1);
		StringBuffer sb= new StringBuffer();
		Double latestedFertilityRate = 0.0;
		Double outDatedFertilityRate = (Double) list.get(list.size()-1).get("value");

		for (Map map:list) {
			if(map.get("value") != null){
				latestedFertilityRate = (Double) map.get("value");
				System.out.println("가장 최근의 출산율 정보는 " + map.get("date") + "년 " + (Math.round(latestedFertilityRate * 100) / 100.0) +"% 입니다.");
				break;
			}
		}
		System.out.println("가장 오래된 출산율 정보는 " + list.get(list.size()-1).get("date") + "년 " + (Math.round(outDatedFertilityRate * 100) / 100.0) + "% 입니다.");
		Double d = outDatedFertilityRate > latestedFertilityRate ? outDatedFertilityRate - latestedFertilityRate : latestedFertilityRate - outDatedFertilityRate;
		System.out.println("두 출산율의 격차는 " + (Math.round(d * 100) / 100.0) + "% 입니다. \n");
	}


	public static void main(String[] args) {
		SpringApplication.run(SpringHttpClientApplication.class, args);
	}

}
