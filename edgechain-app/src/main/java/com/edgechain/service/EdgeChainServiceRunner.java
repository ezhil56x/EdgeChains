package com.edgechain.service;

import com.edgechain.lib.configuration.EdgeChainAutoConfiguration;
import com.edgechain.lib.constants.LibConstants;
import com.edgechain.service.constants.ServiceConstants;
import jakarta.annotation.PostConstruct;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.io.File;
import java.io.FileInputStream;

@SpringBootApplication(scanBasePackages = {"com.edgechain.service"})
@Import(EdgeChainAutoConfiguration.class)
public class EdgeChainServiceRunner implements CommandLineRunner {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @PostConstruct
  public void init() throws Exception {
    this.readEmbeddingDoc2VecModel();
  }

  public static void main(String[] args) {

    System.setProperty("server.port", "8002");

    System.setProperty("spring.data.redis.host", "");
    System.setProperty("spring.data.redis.port", "");
    System.setProperty("spring.data.redis.username", "default");
    System.setProperty("spring.data.redis.password", "");
    System.setProperty("spring.data.redis.connect-timeout", "120000");
    System.setProperty("spring.redis.ttl", "3600");

    SpringApplication.run(EdgeChainServiceRunner.class, args);
  }

  private void readEmbeddingDoc2VecModel() throws Exception {

    LibConstants.sentenceModel = this.getClass().getResourceAsStream("/en-sent.zip");

    String modelPath = "R:\\doc_vector.bin";

    File file = new File(modelPath);

    if (!file.exists()) {
      logger.warn(
          "It seems like, you haven't trained the model or correctly specified Doc2Vec model"
              + " path.");
    } else {
      logger.info("Loading...");
      ServiceConstants.embeddingDoc2VecModel =
          WordVectorSerializer.readParagraphVectors(new FileInputStream(modelPath));
      logger.info("Doc2Vec model is successfully loaded...");
    }
  }

  @Override
  public void run(String... args) throws Exception {}
}