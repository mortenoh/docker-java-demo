package com.example.docker.web;

import com.example.docker.domain.FindByParams;
import com.example.docker.domain.Status;
import com.example.docker.service.DockerService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/docker")
public class DockerController {

  private DockerService dockerService = null;

  public DockerController(DockerService dockerService) {
    this.dockerService = dockerService;
  }

  @GetMapping({"", "images"})
  public List<String> findBy(FindByParams params) {
    return dockerService.findAllBy(params);
  }

  @GetMapping("ping")
  public Status dockerPing() {
    boolean ping = dockerService.ping();

    if (ping) {
      return new Status("OK");
    }

    return new Status("NOT_OK");
  }
}
