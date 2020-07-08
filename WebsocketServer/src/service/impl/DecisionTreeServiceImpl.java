package service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;

import algorithm.DecisionTree;
import model.ResultDto;
import service.DecisionTreeService;
import weka.core.DenseInstance;
import weka.core.Instance;

public class DecisionTreeServiceImpl implements DecisionTreeService{
	
    private Gson gson = new Gson();

    @Override
    public int water(double temp, double humidity) {
        Instance instance = new DenseInstance(5);
        instance.setDataset(DecisionTree.data);
        double label = 0;

        //get weather from api
        String waether = null;
        double wind = 0;
        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/onecall?lat=20.980230&lon=105.788100&exclude=daily,minutely,hourly&appid=a40a5ab36c986d3838d1dc3801ca07d8");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String response = br.readLine();

            ResultDto resultDto = gson.fromJson(response, ResultDto.class);
            waether = resultDto.getCurrent().getWeather().get(0).getMain();
            wind = resultDto.getCurrent().getWindSpeed();

            conn.disconnect();

            instance.setValue(DecisionTree.data.attribute("temp"), temp);
            instance.setValue(DecisionTree.data.attribute("humidity"), humidity);
            instance.setValue(DecisionTree.data.attribute("weather"), waether);
            instance.setValue(DecisionTree.data.attribute("wind"), wind);
            instance.setClassMissing();

            label = DecisionTree.tree.classifyInstance(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        instance.setClassValue(label);
        return instance.stringValue(4).equals("Y") ? 1 : 0;
    }
}
