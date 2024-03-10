package com.example.docker.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DockerService {
  private final DockerClient dockerClient;

  public DockerService(DockerClient dockerClient) {
    this.dockerClient = dockerClient;
  }

  public List<String> images() {
    List<String> result = new ArrayList<>();

    for (Image image : dockerClient.listImagesCmd().exec()) {
      result.addAll(List.of(image.getRepoTags()));
    }

    return result;
  }

  public List<String> containers() {
    List<String> result = new ArrayList<>();

    for (Container container : dockerClient.listContainersCmd().exec()) {
      result.add(container.getImage() + ":" + container.getId());
    }

    return result;
  }

  public Container container(String id) {
    for (Container container : dockerClient.listContainersCmd().exec()) {
      if (container.getId().equals(id)) {
        return container;
      }
    }

    return null;
  }

  public void start(String id) {
    dockerClient.startContainerCmd(id).exec();
  }

  public void stop(String id) {
    dockerClient.stopContainerCmd(id).exec();
  }

  public void kill(String id) {
    dockerClient.killContainerCmd(id).exec();
  }

  public void restart(String id) {
    dockerClient.restartContainerCmd(id).exec();
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
