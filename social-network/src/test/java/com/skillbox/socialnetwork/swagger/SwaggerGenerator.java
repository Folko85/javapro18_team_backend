package com.skillbox.socialnetwork.swagger;

import com.skillbox.socialnetwork.AbstractTest;
import com.skillbox.socialnetwork.NetworkApplication;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@SpringBootTest(classes = {NetworkApplication.class})
public class SwaggerGenerator extends AbstractTest {

    @Test
    public void generateSwagger() throws Exception {
        Yaml yaml = new Yaml();
        mockMvc.perform(MockMvcRequestBuilders.get("/v3/api-docs").accept(MediaType.APPLICATION_JSON))
                .andDo((result) -> {
                    JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
                    String forWrite = yaml.dump(yaml.load(jsonObject.toString(4)));
                    Files.writeString(new File("./123.yaml").toPath(), forWrite, StandardCharsets.UTF_8);
                });
    }

    // а здесь мы будем переводить наш json s yml

}
