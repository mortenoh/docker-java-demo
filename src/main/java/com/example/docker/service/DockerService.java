package com.example.docker.service;

import com.example.docker.domain.FindByParams;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Image;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DockerService {
  private final DockerClient dockerClient;

  public DockerService(DockerClient dockerClient) {
    this.dockerClient = dockerClient;
  }

  public List<String> findAllBy(FindByParams params) {
    List<String> result = new ArrayList<>();

    for(Image image: dockerClient.listImagesCmd().exec()) {
      result.addAll(List.of(image.getRepoTags()));
    }

    return result;
  }

  public boolean ping() {
    try {
      dockerClient.pingCmd().exec();
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
