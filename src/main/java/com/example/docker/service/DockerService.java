package com.example.docker.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.PortBinding;
import java.util.ArrayList;
import java.util.Arrays;
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

    return result.stream().sorted().toList();
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

  public InspectContainerResponse createContainer(
      String id, boolean start, List<String> env, List<String> ports) {
    List<PortBinding> portBindings = new ArrayList<>();
    ports.forEach(x -> portBindings.add(PortBinding.parse(x)));

    HostConfig hostConfig = HostConfig.newHostConfig().withPortBindings(portBindings);

    CreateContainerResponse response =
        dockerClient.createContainerCmd(id).withHostConfig(hostConfig).withEnv(env).exec();

    if (start) {
      startContainer(response.getId());
    }

    return dockerClient.inspectContainerCmd(response.getId()).exec();
  }

  public void startContainer(String id) {
    dockerClient.startContainerCmd(id).exec();
  }

  public void stopContainer(String id) {
    dockerClient.stopContainerCmd(id).exec();
  }

  public void killContainer(String id) {
    dockerClient.killContainerCmd(id).exec();
    dockerClient.removeContainerCmd(id).exec();
  }

  public void restartContainer(String id) {
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
